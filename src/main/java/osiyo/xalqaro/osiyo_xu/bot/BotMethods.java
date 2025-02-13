package osiyo.xalqaro.osiyo_xu.bot;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import osiyo.xalqaro.osiyo_xu.entity.Content;
import osiyo.xalqaro.osiyo_xu.entity.enums.MessageType;
import osiyo.xalqaro.osiyo_xu.service.ContentService;
import osiyo.xalqaro.osiyo_xu.service.MessageService;

import java.util.*;

@Service
public class BotMethods {

    private final BotSettings botSettings;
    private final ButtonSettings buttonSettings;
    private final ContentService contentService;
    private final MessageService messageService;

    public BotMethods(@Lazy BotSettings botSettings, ButtonSettings buttonSettings, ContentService contentService, MessageService messageService) {
        this.botSettings = botSettings;
        this.buttonSettings = buttonSettings;
        this.contentService = contentService;
        this.messageService = messageService;
    }

    private final Map<Long, String> direction = new HashMap<>();
    private final Map<Long, String> semester = new HashMap<>();
    private final Map<Long, String> science = new HashMap<>();
    private final Map<Long, String> subjectName = new HashMap<>();
    private final Map<Long, String> choose = new HashMap<>();
    private final Map<Long, List<osiyo.xalqaro.osiyo_xu.entity.Message>> messageMap = new HashMap<>();
    private final Set<String> semesterSet = new HashSet<>(Semester.ALL_SEMESTERS);


    public void message(Message message) {
        Long chatId = message.getChatId();
        Long userId = message.getFrom().getId();
        if (Template.ADMINS.contains(userId)) adminMessage(message, chatId, userId);
        else userMessage(message, chatId, userId);
    }

    public void adminMessage(Message message, Long chatId, Long userId) {
        if (message.hasText()) {
            SendMessage sm = new SendMessage(chatId.toString(), message.getText());
            String text = message.getText();
            if (text.equals(Template.BACK)) back(userId, sm);
            else switch (text) {
                case Template.START, Template.MENU -> start(userId, sm);
                // direction
                case Template.DENTISTRY -> chooseDentistry(userId, sm, text);
                // subject
                case Template.ADD_SUBJECT -> {
                    sm.setReplyMarkup(buttonSettings.getKeyboardButton(Template.BACK_BUTTON));
                    sendMSG(sm, Template.SUBJECT_NAME);
                    choose.put(userId, "addSubjectName");
                }
                case Template.GET_SUBJECT -> getSubject(userId, sm);
                case Template.DELETE_SUBJECT -> {
                    sm.setReplyMarkup(buttonSettings.getKeyboardButtonCol(getSubjectList(userId)));
                    sendMSG(sm, Template.CHOOSE_SUBJECT);
                    choose.put(userId, "deleteSubject");
                }
                default -> {
                    if (choose.containsKey(userId)) switch (choose.get(userId)) {
                        case "semester" -> getScience(userId, sm, text);
                        case "science" -> {
                            if (Science.ALL_SCIENCES.contains(text)) {
                                science.put(userId, text);
                                sm.setReplyMarkup(buttonSettings.getKeyboardButton(Template.ADMIN_SUBJECT));
                                sendMSG(sm, Template.CHOOSE_DEPARTMENT);
                                choose.put(userId, "subject");
                            }
                        }
                        case "addSubjectName" -> {
                            sm.setReplyMarkup(buttonSettings.getKeyboardButton(Template.BACK_AND_ACCEPT_BUTTON));
                            sendMSG(sm, Template.SUBJECT_CONTENT);
                            choose.put(userId, "addSubjectContent");
                            subjectName.put(userId, text);
                        }
                        // subject
                        case "addSubjectContent" -> {
                            if (text.equals(Template.ACCEPT)) addContent(sm, userId);
                            else addMessage(userId, text, null, null, MessageType.TEXT);
                        }
                        case "getSubject" -> {
                            sendContent(userId, text);
                            sm.setReplyMarkup(buttonSettings.getKeyboardButton(Template.ADMIN_SUBJECT));
                            sendMSG(sm, Template.CHOOSE_DEPARTMENT);
                        }
                        case "deleteSubject" -> deleteContent(userId, text, sm);
                    }
                }
            }
        } else if (choose.get(userId).equals("addSubjectContent")) {
            if (message.hasPhoto())
                addMessage(userId, null, message.getCaption(), message.getPhoto().get(0).getFileId(), MessageType.PHOTO);
            else if (message.hasDocument())
                addMessage(userId, null, message.getCaption(), message.getDocument().getFileId(), MessageType.DOCUMENT);
            else if (message.hasAudio())
                addMessage(userId, null, message.getCaption(), message.getAudio().getFileId(), MessageType.AUDIO);
            else if (message.hasVideo())
                addMessage(userId, null, message.getCaption(), message.getVideo().getFileId(), MessageType.VIDEO);
        }
    }

    public void userMessage(Message message, Long chatId, Long userId) {
        if (message.hasText()) {
            SendMessage sm = new SendMessage(chatId.toString(), message.getText());
            String text = message.getText();
            if (Template.BACK.equals(text)) backUser(userId, sm);
            else switch (text) {
                case Template.START -> start(userId, sm);
                // direction
                case Template.DENTISTRY -> chooseDentistry(userId, sm, text);
                default -> {
                    if (choose.containsKey(userId)) {
                        if (choose.get(userId).equals("semester")) getScience(userId, sm, text);
                        else if (choose.get(userId).equals("science")) {
                            if (Science.ALL_SCIENCES.contains(text)) {
                                science.put(userId, text);
                                getSubject(userId, sm);
                            }
                        } else if (choose.get(userId).equals("getSubject")) {
                            sendContent(userId, text);
                            getSubject(userId, sm);
                        }
                    }
                }
            }
        }
    }

    //  +=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=  Messages  +=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=
    public void sendMSG(SendMessage sendMessage, String text) {
        try {
            sendMessage.setText(text);
            botSettings.execute(sendMessage);
        } catch (TelegramApiException e) {
            System.out.println("not execute");
        }
    }

    public void sendPIC(Long chatId, String fileId, String caption) {
        try {
            botSettings.execute(SendPhoto.builder().chatId(chatId).photo(new InputFile(fileId)).caption(caption).build());
        } catch (TelegramApiException e) {
            System.err.println("send photo error");
        }
    }

    public void sendVd(Long chatId, String fileId, String caption) {
        try {
            botSettings.execute(SendVideo.builder().chatId(chatId).video(new InputFile(fileId)).caption(caption).build());
        } catch (TelegramApiException e) {
            System.err.println("send video error");
        }
    }

    public void sendDOC(Long chatId, String fileId, String caption) {
        try {
            botSettings.execute(SendDocument.builder().chatId(chatId).document(new InputFile(fileId)).caption(caption).build());
        } catch (TelegramApiException e) {
            System.err.println("send document error");
        }
    }

    public void sendAO(Long chatId, String fileId, String caption) {
        try {
            botSettings.execute(SendAudio.builder().chatId(chatId).audio(new InputFile(fileId)).caption(caption).build());
        } catch (TelegramApiException e) {
            System.err.println("send audio error");
        }
    }

    public void sendContent(Long userId, String subject) {
        Content content = contentService.getContent(direction.get(userId), semester.get(userId), science.get(userId), subject);
        messageService.getMessageByContent(content).forEach(message -> {
            switch (message.getType()) {
                case PHOTO -> sendPIC(userId, message.getFileId(), message.getCaption());
                case VIDEO -> sendVd(userId, message.getFileId(), message.getCaption());
                case AUDIO -> sendAO(userId, message.getFileId(), message.getCaption());
                case DOCUMENT -> sendDOC(userId, message.getFileId(), message.getCaption());
                case TEXT -> sendMSG(new SendMessage(userId.toString(), message.getMessage()), message.getMessage());
            }
        });
    }

    public void addMessage(Long userId, String message, String caption, String fileId, MessageType type) {
        List<osiyo.xalqaro.osiyo_xu.entity.Message> messages = new ArrayList<>();
        if (messageMap.get(userId) != null) messages = messageMap.get(userId);
        messages.add(osiyo.xalqaro.osiyo_xu.entity.Message.builder().fileId(fileId).message(message).caption(caption).type(type).build());
        messageMap.put(userId, messages);
    }

    public void addContent(SendMessage sm, Long userId) {
        Content content = contentService.addContent(Content.builder().direction(direction.get(userId)).semester(semester.get(userId)).science(science.get(userId)).subject(subjectName.get(userId)).build());
        messageMap.get(userId).forEach(message -> {
            message.setContent(content);
            messageService.addMessage(message);
        });
        sm.setReplyMarkup(buttonSettings.getKeyboardButton(Template.ADMIN_SUBJECT));
        sendMSG(sm, Template.SUBJECT_SAVED);
        choose.put(userId, "subject");
        messageMap.remove(userId);
    }

    public void deleteContent(Long userId, String subject, SendMessage sm) {
        Content content = contentService.getContent(direction.get(userId), semester.get(userId), science.get(userId), subject);
        messageService.deleteMessageByContent(content);
        contentService.deleteContent(content.getId());
        sm.setReplyMarkup(buttonSettings.getKeyboardButton(Template.ADMIN_SUBJECT));
        sendMSG(sm, Template.SUBJECT_DELETED);
        choose.put(userId, "subject");
    }

    public List<String> getSubjectList(Long userId) {
        List<String> subjectList = new ArrayList<>(contentService.getContents(direction.get(userId), semester.get(userId), science.get(userId)).stream().map(Content::getSubject).toList());
        subjectList.add(Template.BACK);
        return subjectList;
    }

    public List<String> getSciences(Long userId) {
        switch (direction.get(userId)) {
            case Template.DENTISTRY -> {
                switch (semester.get(userId)) {
                    case Semester.ONE_SEMESTER -> {
                        return Science.ONE_SEMESTER_DENTISTRY;
                    }
                    case Semester.TWO_SEMESTER -> {
                        return Science.TWO_SEMESTER_DENTISTRY;
                    }
                    case Semester.THREE_SEMESTER -> {
                        return Science.THREE_SEMESTER_DENTISTRY;
                    }
                    case Semester.FOUR_SEMESTER -> {
                        return Science.FOUR_SEMESTER_DENTISTRY;
                    }
                    case Semester.FIVE_SEMESTER -> {
                        return Science.FIVE_SEMESTER_DENTISTRY;
                    }
                }
            }
            case Template.TREATMENT_WORK -> {
                /// TODO TREATMENT_WORK code...
            }
        }
        return null;
    }

    public void getScience(Long userId, SendMessage sm, String text) {
        List<String> getSciences = getSciences(userId);
        if (semesterSet.contains(text) && !getSciences.isEmpty()) {
            semester.put(userId, text);
            sm.setReplyMarkup(buttonSettings.getKeyboardButtonCol(getSciences));
            sendMSG(sm, Template.CHOOSE_SCIENCE);
            choose.put(userId, "science");
        }
    }

    public void getSubject(Long userId, SendMessage sm) {
        sm.setReplyMarkup(buttonSettings.getKeyboardButtonCol(getSubjectList(userId)));
        sendMSG(sm, Template.CHOOSE_SUBJECT);
        choose.put(userId, "getSubject");
    }

    //  +=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=  User  +=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=
    public void start(Long userId, SendMessage sm) {
        sm.setReplyMarkup(buttonSettings.getKeyboardButton(Template.ADMIN_START));
        sendMSG(sm, Template.CHOOSE_DIRECTION);
        choose.put(userId, "direction");
    }

    public void chooseDentistry(Long userId, SendMessage sm, String text) {
        sm.setReplyMarkup(buttonSettings.getKeyboardButton(Semester.TEN_SEMESTERS));
        sendMSG(sm, Template.CHOOSE_SEMESTER);
        choose.put(userId, "semester");
        direction.put(userId, text);
    }

    public void back(Long userId, SendMessage sm) {
        switch (choose.get(userId)) {
            case "semester" -> start(userId, sm);
            case "science" -> chooseDentistry(userId, sm, direction.get(userId));
            case "subject" -> getScience(userId, sm, semester.get(userId));
            case "deleteSubject", "getSubject", "addSubjectContent", "addSubjectName" -> {
                sm.setReplyMarkup(buttonSettings.getKeyboardButton(Template.ADMIN_SUBJECT));
                sendMSG(sm, Template.CHOOSE_DEPARTMENT);
                choose.put(userId, "subject");
            }
        }
        // yunalish -> ADMIN_START -> direction
        // semestr -> *-semester -> semester
        // fan     -> science -> science
        // mavzu   -> *-subject -> subject
    }

    public void backUser(Long userId, SendMessage sm) {
        if (choose.containsKey(userId)) switch (choose.get(userId)) {
            case "semester" -> start(userId, sm);
            case "science" -> chooseDentistry(userId, sm, direction.get(userId));
            case "getSubject" -> getScience(userId, sm, semester.get(userId));
        }
    }
}