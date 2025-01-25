package osiyo.xalqaro.osiyo_xu.bot;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import osiyo.xalqaro.osiyo_xu.entity.Content;
import osiyo.xalqaro.osiyo_xu.entity.enums.MessageType;
import osiyo.xalqaro.osiyo_xu.payload.ApiResponse;
import osiyo.xalqaro.osiyo_xu.service.ContentService;

import java.util.*;

@Service
public class BotMethods {

    private final BotSettings botSettings;
    private final ButtonSettings buttonSettings;
    private final ContentService contentService;

    public BotMethods(@Lazy BotSettings botSettings, ButtonSettings buttonSettings, ContentService contentService) {
        this.botSettings = botSettings;
        this.buttonSettings = buttonSettings;
        this.contentService = contentService;
    }

    private final Map<Long, String> direction = new HashMap<>();
    private final Map<Long, String> choose = new HashMap<>();
    private final Map<Long, String> semester = new HashMap<>();
    private final Map<Long, List<Content>> content = new HashMap<>();
    private final Set<String> semesterSet = new HashSet<>(Semester.ALL_SEMESTERS);


    public void message(Message message) {
        Long chatId = message.getChatId();
        Long userId = message.getFrom().getId();
        if (isAdmin(userId)) adminMessage(message, chatId, userId);
        else userMessage(message, chatId, userId);
    }

    public void adminMessage(Message message, Long chatId, Long userId) {
        if (message.hasText()) {
            SendMessage sm = new SendMessage(chatId.toString(), message.getText());
            String text = message.getText();
            switch (text) {
                case Template.START -> {
                    sm.setReplyMarkup(buttonSettings.getKeyboardButton(Template.ADMIN_START));
                    sendMSG(sm, Template.CHOOSE_DIRECTION);
                    choose.put(userId, "direction");
                }
                // direction
                case Template.DENTISTRY -> {
                    sm.setReplyMarkup(buttonSettings.getKeyboardButton(Semester.TEN_SEMESTERS));
                    sendMSG(sm, Template.CHOOSE_SEMESTER);
                    choose.put(userId, "semester");
                    direction.put(userId, text);
                }
                default -> {
                    if (choose.containsKey(userId))
                        if (choose.get(userId).equals("semester") && semesterSet.contains(text)) {
                            semester.put(userId, text);
                            sm.setReplyMarkup(buttonSettings.getKeyboardButton(getSciences(userId)));
                            sendMSG(sm, Template.CHOOSE_SCIENCE);
                            choose.put(userId, "science");
                        } else if (choose.get(userId).equals("science") && Science.ALL_SCIENCES.contains(text)) {
                            System.out.println("science");
                        }
                }
            }
        } else if (choose.get(userId).equals("addSubjectFile") || choose.get(userId).equals("editSubjectContent")) {
            if (message.hasPhoto())
                addContent(userId, null, message.getCaption(), message.getPhoto().get(0).getFileId(), MessageType.PHOTO);
            else if (message.hasDocument())
                addContent(userId, null, message.getCaption(), message.getDocument().getFileId(), MessageType.DOCUMENT);
            else if (message.hasAudio())
                addContent(userId, null, message.getCaption(), message.getAudio().getFileId(), MessageType.AUDIO);
            else if (message.hasVideo())
                addContent(userId, null, message.getCaption(), message.getVideo().getFileId(), MessageType.VIDEO);
        }
    }

    public void userMessage(Message message, Long chatId, Long userId) {
        if (message.hasText()) {
            SendMessage sm = new SendMessage(chatId.toString(), message.getText());
            String text = message.getText();
            switch (text) {
//                case Template.START -> {
//                    sm.setReplyMarkup(buttonSettings.getKeyboardButton(Template.OFFER));
//                    sendMSG(sm, "Assalomu alaykum botga hush kelibsiz bu bot maqsadi...?");
//                    sm.setReplyMarkup(buttonSettings.getInlineMarkup(scienceService.getSciences()));
//                    sendMSG(sm, "Sizga qiziq fanni tanlang.");
//                    choose.put(userId, "science");
//                }
                case Template.LAVE_AN_OFFER -> {
                    sendMSG(sm, "Taklifingiznyozing");
                    choose.put(userId, "offer");
                }
                default -> {
                    if (choose.get(userId).equals("offer")) {
                        sendMSG(sm, "Habaringiz adminga yuborildi.");
                        sendAdminsMessage(sm, "yangi taklif qoldirildi\n\nFirstName: "
                                + message.getFrom().getFirstName() + "\nMessage: " + message.getText());
                    }
                }
            }
        }
    }

    public void callbackData(CallbackQuery callbackQuery) {
        Long userId = callbackQuery.getMessage().getChatId();
        String data = callbackQuery.getData();
        SendMessage sm = new SendMessage(userId.toString(), data);
        if (isAdmin(userId))
            adminCallback(userId, data, sm, callbackQuery.getMessage().getMessageId(), callbackQuery.getId());
        else userCallback(userId, data, sm, callbackQuery.getMessage().getMessageId());
    }

    public void adminCallback(Long userId, String data, SendMessage sm, Integer messageId, String callbackId) {
        if (data.equals(Template.BACK))
            switch (choose.get(userId)) {
                case "editScience", "deleteScience" -> sendMessageBtn(sm, Template.ADMIN_SCIENCE);
                case "editSubject" -> sendMessageBtn(sm, Template.ADMIN_SUBJECT);
                case "editSubjectScience", "deleteSubjectChoose" -> sendMessageBtn(sm, Template.ADMIN_SUBJECT_EDIT);
            }
        if (choose.get(userId) == null) sendAnswer(callbackId, "Siz biror amal bajarayotganingiz yuq");
//        else switch (choose.get(userId)) {
//            // science department
//            case "editScience" -> {
//                Science science = scienceService.getScience(data);
//                if (science != null) {
//                    scienceId.put(userId, science.getId());
//                    sm.setReplyMarkup(buttonSettings.getKeyboardButton(Template.BACK_BUTTON));
//                    sendMSG(sm, "yangi nom Kiriting:");
//                    choose.put(userId, "editScienceName");
//                }
//            }
//            case "deleteScience" -> {
//                scienceService.deleteScienceByName(data);
//                sendMSG(sm, "Fan mavzusi o'chirildi.");
//                sendMessageBtn(sm, Template.ADMIN_SCIENCE);
//            }
//            // subject department
//            case "addSubjectScience" -> {
//                Subject saveSubject = subjectService.addSubject(Subject.builder()
//                        .name(subjectName.get(userId))
//                        .science(scienceService.getScience(data))
//                        .build());
//                content.get(userId).forEach(content -> contentService.addContent(Content.builder()
//                        .type(content.getType())
//                        .fileId(content.getFileId())
//                        .message(content.getMessage())
//                        .caption(content.getCaption())
//                        .subject(saveSubject)
//                        .build()));
//                content.remove(userId);
//                sendMSG(sm, "Mavzu qo'shildi.");
//                sendMessageBtn(sm, Template.ADMIN_SUBJECT);
//            }
//            case "editSubject" -> {
//                scienceId.put(userId, scienceService.getScience(data).getId());
//                sm.setReplyMarkup(buttonSettings.getInlineMarkup(subjectService.getSubjectByScience(scienceId.get(userId))));
//                sendMSG(sm, "Qaysi mavzuni o'zgartirmoqchisiz?");
//                choose.put(userId, "editSubjectChoose");
//            }
//            case "editSubjectChoose" -> {
//                subjectName.put(userId, data);
//                sendMessageBtn(sm, Template.ADMIN_SUBJECT_EDIT);
//            }
//            case "editSubjectScience" -> {
//                subjectService.editSubjectScience(subjectName.get(userId), scienceId.get(userId));
//                sm.setReplyMarkup(buttonSettings.getKeyboardButton(Template.ADMIN_SUBJECT_EDIT));
//                sendMSG(sm, "Fan mavzusi o'zgartirildi.");
//            }
//            case "deleteSubject" -> {
//                Long scienceId_ = scienceService.getScience(data).getId();
//                scienceId.put(userId, scienceId_);
//                List<String> subjectByScience = subjectService.getSubjectByScience(scienceId_);
//                subjectByScience.add(Template.BACK);
//                sm.setReplyMarkup(buttonSettings.getInlineMarkup(subjectByScience));
//                sendMSG(sm, "O'chirmoqchi bulgan mavzuni tanlang:");
//                choose.put(userId, "deleteSubjectChoose");
//            }
//            case "deleteSubjectChoose" -> {// TODO o'chiorishni sozlash
//                subjectService.deleteSubject(data);
//                sm.setReplyMarkup(buttonSettings.getKeyboardButton(Template.ADMIN_SUBJECT));
//                sendMSG(sm, "Mavzu o'chirildi.");
//            }
//            case "getSubject" -> {
//                editCallbackQuery(userId, messageId, "Mavzular ro'yhati", buttonSettings.getInlineMarkup(subjectService.getSubjectByScience(scienceService.getScience(data).getId())));
//                sendMessageBtn(sm, Template.ADMIN_SUBJECT);
//                choose.remove(userId);
//            }
//        }
    }

    public void userCallback(Long userId, String data, SendMessage sm, Integer messageId) {
//        switch (choose.get(userId)) {
//            case "science" -> {
//                editCallbackQuery(userId, messageId, "Mavzuni tanlang",
//                        buttonSettings.getInlineMarkup(subjectService.getSubjectByScience(data)));
//                choose.put(userId, "subject");
//            }
//            case "subject" -> {
//                contentService.getContentBySubject(data).forEach(content -> {
//                    switch (content.getType()) {
//                        case TEXT -> sendMSG(sm, content.getMessage());
//                        case PHOTO -> sendPIC(userId, content.getFileId(), content.getCaption());
//                        case VIDEO -> sendVd(userId, content.getFileId(), content.getCaption());
//                        case AUDIO -> sendAO(userId, content.getFileId(), content.getCaption());
//                        case DOCUMENT -> sendDOC(userId, content.getFileId(), content.getCaption());
//                    }
//                });
//                sm.setReplyMarkup(buttonSettings.getInlineMarkup(scienceService.getSciences()));
//                sendMSG(sm, "Sizga qiziq fanni tanlang.");
//                choose.put(userId, "science");
//            }
//        }
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

    public void sendMessageBtn(SendMessage sm, List<String> buttons) {
        sm.setReplyMarkup(buttonSettings.getKeyboardButton(buttons));
        sendMSG(sm, Template.CHOOSE_DEPARTMENT);
//        choose.remove(Long.parseLong(sm.getChatId())); // TODO uylab kurish kerak
    }

    public void sendAdminMessage(SendMessage sm, ApiResponse<?> apiResponse, List<String> buttons) {
        if (!apiResponse.isSuccess()) sendMSG(sm, apiResponse.getMessage());
        else {
            sendMSG(sm, apiResponse.getMessage());
            sendMessageBtn(sm, buttons);
        }
    }

    public void editCallbackQuery(Long chatId, Integer messageId, String text, InlineKeyboardMarkup newInlineKeyboard) {
        try {
            botSettings.execute(EditMessageText.builder()
                    .chatId(chatId.toString())
                    .messageId(messageId)
                    .text(text)
                    .replyMarkup(newInlineKeyboard)
                    .build());
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
    }

    public void sendAnswer(String callbackQueryId, String text) {
        AnswerCallbackQuery acq = new AnswerCallbackQuery();
        acq.setText(text);
        acq.setShowAlert(true);
        acq.setCallbackQueryId(callbackQueryId);
        try {
            botSettings.executeAsync(acq);
        } catch (TelegramApiException e) {
            System.err.println("not answer");
        }
    }

    public void sendPIC(Long chatId, String fileId, String caption) {
        try {
            botSettings.execute(SendPhoto.builder()
                    .chatId(chatId)
                    .photo(new InputFile(fileId))
                    .caption(caption)
                    .build());
        } catch (TelegramApiException e) {
            System.err.println("send photo error");
        }
    }

    public void sendVd(Long chatId, String fileId, String caption) {
        try {
            botSettings.execute(SendVideo.builder()
                    .chatId(chatId)
                    .video(new InputFile(fileId))
                    .caption(caption)
                    .build());
        } catch (TelegramApiException e) {
            System.err.println("send video error");
        }
    }

    public void sendDOC(Long chatId, String fileId, String caption) {
        try {
            botSettings.execute(SendDocument.builder()
                    .chatId(chatId)
                    .document(new InputFile(fileId))
                    .caption(caption)
                    .build());
        } catch (TelegramApiException e) {
            System.err.println("send document error");
        }
    }

    public void sendAO(Long chatId, String fileId, String caption) {
        try {
            botSettings.execute(SendAudio.builder()
                    .chatId(chatId)
                    .audio(new InputFile(fileId))
                    .caption(caption)
                    .build());
        } catch (TelegramApiException e) {
            System.err.println("send audio error");
        }
    }

    public void sendAdminsMessage(SendMessage sm, String text) {
        Template.ADMINS.forEach(admin -> {
            sm.setChatId(admin);
            sendMSG(sm, text);
        });
    }

    public void addContent(Long userId, String message, String caption, String fileId, MessageType type) {
        List<Content> contents = new ArrayList<>();
//        if (content.get(userId) != null) contents = content.get(userId);
//        contents.add(Content.builder()
//                .type(type)
//                .fileId(fileId)
//                .message(message)
//                .caption(caption)
//                .build());
//        content.put(userId, contents);
    }

    public boolean isAdmin(Long userId) {
        for (Long admin : Template.ADMINS) if (admin.equals(userId)) return true;
        return false;
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
}