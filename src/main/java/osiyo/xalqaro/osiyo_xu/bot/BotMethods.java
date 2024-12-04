package osiyo.xalqaro.osiyo_xu.bot;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import osiyo.xalqaro.osiyo_xu.entity.Content;
import osiyo.xalqaro.osiyo_xu.entity.Science;
import osiyo.xalqaro.osiyo_xu.entity.Subject;
import osiyo.xalqaro.osiyo_xu.entity.enums.MessageType;
import osiyo.xalqaro.osiyo_xu.payload.ApiResponse;
import osiyo.xalqaro.osiyo_xu.service.ContentService;
import osiyo.xalqaro.osiyo_xu.service.ScienceService;
import osiyo.xalqaro.osiyo_xu.service.SubjectService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BotMethods {

    private final BotSettings botSettings;
    private final ButtonSettings buttonSettings;
    private final ScienceService scienceService;
    private final SubjectService subjectService;
    private final ContentService contentService;

    public BotMethods(@Lazy BotSettings botSettings, ButtonSettings buttonSettings, ScienceService scienceService, SubjectService subjectService, ContentService contentService) {
        this.botSettings = botSettings;
        this.buttonSettings = buttonSettings;
        this.scienceService = scienceService;
        this.subjectService = subjectService;
        this.contentService = contentService;
    }

    private final Map<Long, Long> scienceId = new HashMap<>();
    private final Map<Long, String> choose = new HashMap<>();
    private final Map<Long, String> subjectName = new HashMap<>();
    private final Map<Long, String> photoMediaGroupId = new HashMap<>();
    private final Map<Long, List<Content>> content = new HashMap<>();


    public void message(Message message) {
        Long chatId = message.getChatId();
        Long userId = message.getFrom().getId();
        if (Template.CREATOR_ID.equals(userId.toString()))
            adminMessage(message, chatId, userId);
        else userMessage(message, chatId, userId);
    }

    public void adminMessage(Message message, Long chatId, Long userId) {
        if (message.hasText()) {
            SendMessage sm = new SendMessage(chatId.toString(), message.getText());
            String text = message.getText();
            switch (text) {
                case Template.START -> {
                    sm.setReplyMarkup(buttonSettings.getKeyboardButton(Template.ADMIN_START));
                    sendMSG(sm, Template.CHOOSE_DEPARTMENT);
                }
                // science department
                case Template.SCIENCE_DEPARTMENT -> sendMessageBtn(sm, Template.ADMIN_SCIENCE);
                case Template.ADD_SCIENCE -> {
                    sm.setReplyMarkup(buttonSettings.getKeyboardButton(Template.BACK_BUTTON));
                    sendMSG(sm, "Fan nomini kiriting:");
                    choose.put(userId, "addScience");
                }
                case Template.EDIT_SCIENCE -> {
                    sm.setReplyMarkup(buttonSettings.getInlineMarkup(getSubjects()));
                    sendMSG(sm, "O'zgartirmoqchi bulgan fanni tanlang");
                    choose.put(userId, "editScience");
                }
                case Template.DELETE_SCIENCE -> {
                    sm.setReplyMarkup(buttonSettings.getInlineMarkup(getSubjects()));
                    sendMSG(sm, "O'chirmoqchi bulgan fanni tanlang");
                    choose.put(userId, "deleteScience");
                }
                case Template.GET_SCIENCE -> {
                    sm.setReplyMarkup(buttonSettings.getInlineMarkup(scienceService.getSciences()));
                    sendMSG(sm, "Fanlar ro'yhati");
                    sendMessageBtn(sm, Template.ADMIN_SCIENCE);
                }
                case Template.MENU -> sendMessageBtn(sm, Template.ADMIN_START);
                // subject department
                case Template.SUBJECT_DEPARTMENT -> sendMessageBtn(sm, Template.ADMIN_SUBJECT);
                case Template.ADD_SUBJECT -> {
                    sm.setReplyMarkup(buttonSettings.getKeyboardButton(Template.BACK_BUTTON));
                    sendMSG(sm, "Mavzu nomini kiriting:");
                    choose.put(userId, "addSubject");
                }
                case Template.ACCEPT -> {
                    if (choose.get(userId).equals("editSubjectContent")) { // TODO contentni o'zgartirish tuliq ishlamayapdi!
                        Subject subject = subjectService.getSubject(subjectName.get(userId));
                        contentService.deleteContentBySubject(subject.getId());
                        content.get(userId).forEach(content -> contentService.addContent(Content.builder()
                                .type(content.getType())
                                .fileId(content.getFileId())
                                .messageId(content.getMessageId())
                                .subject(subject)
                                .build()));

                        content.remove(userId);
                        sendMSG(sm, "Mavzu contenti o'zgartirildi");
                        sendMessageBtn(sm, Template.ADMIN_SUBJECT_EDIT);
                    } else {
                        sm.setReplyMarkup(buttonSettings.getInlineMarkup(scienceService.getSciences()));
                        sendMSG(sm, "Bu mavzu qaysi fanga tegishli?");
                        choose.put(userId, "addSubjectScience");
                    }
                }
                case Template.EDIT_SUBJECT -> {
                    sm.setReplyMarkup(buttonSettings.getInlineMarkup(getSubjects()));
                    sendMSG(sm, "Qaysi fandagi mavzuni o'zgartirmoqchisiz?");
                    choose.put(userId, "editSubject");
                }
                case Template.EDIT_SUBJECT_SCIENCE -> {
                    sm.setReplyMarkup(buttonSettings.getInlineMarkup(getSubjects()));
                    sendMSG(sm, "Mavzuni qaysi fanga tug'irlamoqchisiz?");
                    choose.put(userId, "editSubjectScience");
                }
                case Template.EDIT_NAME -> {
                    sm.setReplyMarkup(buttonSettings.getKeyboardButton(Template.BACK_BUTTON));
                    sendMSG(sm, "Mavzu nomini kiriting:");
                    choose.put(userId, "editSubjectName");
                }
                case Template.EDIT_CONTENT -> {
                    sm.setReplyMarkup(buttonSettings.getKeyboardButton(Template.BACK_AND_ACCEPT_BUTTON));
                    sendMSG(sm, "Mavzu matnini kiriting:");
                    choose.put(userId, "editSubjectContent");
                }
                default -> {
                    switch (choose.get(userId)) {
                        // science department
                        case "addScience" -> {
                            if (!Template.BACK.equals(text))
                                sendAdminMessage(sm, scienceService.addScience(text), Template.ADMIN_SCIENCE);
                            else sendMessageBtn(sm, Template.ADMIN_SCIENCE);
                        }
                        case "editScienceName" -> {
                            if (!Template.BACK.equals(text))
                                sendAdminMessage(sm, scienceService.editScience(scienceId.get(userId), text), Template.ADMIN_SCIENCE);
                            else sendMessageBtn(sm, Template.ADMIN_SCIENCE);
                        }
                        // subject department
                        case "addSubject" -> {
                            if (!Template.BACK.equals(text)) {
                                if (!subjectService.isExist(text)) {
                                    sm.setReplyMarkup(buttonSettings.getKeyboardButton(Template.BACK_AND_ACCEPT_BUTTON));
                                    sendMSG(sm, "Kerakli fayillarni kiriting:");
                                    choose.put(userId, "addSubjectFile");
                                    subjectName.put(userId, text);
                                } else sendMSG(sm, "Bunday mavzu nomi mavjud");
                            } else sendMessageBtn(sm, Template.ADMIN_SUBJECT);
                        }
                        case "addSubjectFile" -> {
                            if (!Template.BACK.equals(text))
                                addContent(userId, message.getMessageId(), null, MessageType.TEXT);
                            else sendMessageBtn(sm, Template.ADMIN_SUBJECT);
                        }
                        case "editSubjectName" -> {
                            if (!Template.BACK.equals(text))
                                sendAdminMessage(sm, subjectService.editSubjectName(subjectName.get(userId), text), Template.ADMIN_SUBJECT_EDIT);
                            else sendMessageBtn(sm, Template.ADMIN_SUBJECT_EDIT);
                            subjectName.put(userId, text);
                        }
                    }
                }
            }
        } else if (choose.get(userId).equals("addSubjectFile") || choose.get(userId).equals("editSubjectContent")) {
            if (message.hasPhoto())
                addContent(userId, null, message.getPhoto().get(0).getFileId(), MessageType.PHOTO);
            else if (message.hasDocument())
                addContent(userId, null, message.getDocument().getFileId(), MessageType.DOCUMENT);
            else if (message.hasAudio())
                addContent(userId, null, message.getAudio().getFileId(), MessageType.AUDIO);
            else if (message.hasVideo())
                addContent(userId, null, message.getVideo().getFileId(), MessageType.VIDEO);
        }
    }

    public void userMessage(Message message, Long chatId, Long userId) {
        if (message.hasText()) {
            SendMessage sm = new SendMessage(chatId.toString(), message.getText());
            String text = message.getText();
            switch (text) {
                case Template.START -> {
                    sm.setReplyMarkup(buttonSettings.getKeyboardButton(Template.OFFER));
                    sendMSG(sm, "Assalomu alaykum botga hush kelibsiz bu bot maqsadi...?");
                    sm.setReplyMarkup(buttonSettings.getInlineMarkup(scienceService.getSciences()));
                    sendMSG(sm, "Sizga qiziq fanni tanlang.");
                }
                case Template.LAVE_AN_OFFER -> {
                    sendMSG(sm, "Taklifingiznyozing");
                    choose.put(userId, "offer");
                }
                default -> {
                    if (choose.get(userId).equals("offer")) {
                        sendMSG(sm, "Habaringiz adminga yuborildi.");
                        sm.setChatId(Template.CREATOR_ID);
                        sendMSG(sm, "yangi taklif qoldirildi\n\nFirstName: " + message.getFrom().getFirstName()
                                + "\nMessage: " + message.getText());
                    }
                }
            }
        }
    }

    public void callbackData(CallbackQuery callbackQuery) {
        Long userId = callbackQuery.getMessage().getChatId();
        String data = callbackQuery.getData();
        SendMessage sm = new SendMessage(userId.toString(), data);
        SendPhoto sp = new SendPhoto();
        if (Template.CREATOR_ID.equals(userId.toString()))
            adminCallback(userId, data, sm);
        else userCallback(userId);
    }

    public void adminCallback(Long userId, String data, SendMessage sm) {
        if (data.equals(Template.BACK))
            switch (choose.get(userId)) {
                case "editScience", "deleteScience" -> sendMessageBtn(sm, Template.ADMIN_SCIENCE);
                case "editSubject" -> sendMessageBtn(sm, Template.ADMIN_SUBJECT);
                case "editSubjectScience" -> sendMessageBtn(sm, Template.ADMIN_SUBJECT_EDIT);
            }
        switch (choose.get(userId)) {
            // science department
            case "editScience" -> {
                Science science = scienceService.getScience(data);
                if (science != null) {
                    scienceId.put(userId, science.getId());
                    sm.setReplyMarkup(buttonSettings.getKeyboardButton(Template.BACK_BUTTON));
                    sendMSG(sm, "yangi nom Kiriting:");
                    choose.put(userId, "editScienceName");
                }
            }
            case "deleteScience" -> {
                scienceService.deleteScienceByName(data);
                sendMSG(sm, "Fan mavzusi o'chirildi.");
                sendMessageBtn(sm, Template.ADMIN_SCIENCE);
            }
            // subject department
            case "addSubjectScience" -> {
                Subject saveSubject = subjectService.addSubject(Subject.builder()
                        .name(subjectName.get(userId))
                        .science(scienceService.getScience(data))
                        .build());
                content.get(userId).forEach(content -> contentService.addContent(Content.builder()
                        .type(content.getType())
                        .fileId(content.getFileId())
                        .messageId(content.getMessageId())
                        .subject(saveSubject)
                        .build()));
                content.remove(userId);
                sendMSG(sm, "Mavzu qo'shildi.");
                sendMessageBtn(sm, Template.ADMIN_SUBJECT);
            }
            case "editSubject" -> {
                scienceId.put(userId, scienceService.getScience(data).getId());
                sm.setReplyMarkup(buttonSettings.getInlineMarkup(subjectService.getSubjectByScience(scienceId.get(userId))));
                sendMSG(sm, "Qaysi mavzuni o'zgartirmoqchisiz?");
                choose.put(userId, "editSubjectChoose");
            }
            case "editSubjectChoose" -> {
                subjectName.put(userId, data);
                sendMessageBtn(sm, Template.ADMIN_SUBJECT_EDIT);
            }
            case "editSubjectScience" -> {
                subjectService.editSubjectScience(subjectName.get(userId), scienceId.get(userId));
                sm.setReplyMarkup(buttonSettings.getKeyboardButton(Template.ADMIN_SUBJECT_EDIT));
                sendMSG(sm, "Fan mavzusi o'zgartirildi.");
            }
        }
    }

    public void userCallback(Long userId) {

    }

    //  +=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=  Messages  +=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+=
    public Message sendMSG(SendMessage sendMessage, String text) {
        try {
            sendMessage.setText(text);
            return botSettings.execute(sendMessage);
        } catch (TelegramApiException e) {
            System.out.println("not execute");
        }
        return null;
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

    public void editCallbackQuery(CallbackQuery callbackQuery, InlineKeyboardMarkup newInlineKeyboard) {
        try {
            botSettings.execute(new EditMessageReplyMarkup(
                    callbackQuery.getMessage().getChatId().toString(),
                    callbackQuery.getMessage().getMessageId(),
                    null,
                    newInlineKeyboard
            ));
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

    public void deleteMSG(Integer messageId, Long chatId) {
        try {
            botSettings.execute(new DeleteMessage(String.valueOf(chatId), messageId));
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
    }

    public void sendPIC(SendPhoto sp) {
        try {
            botSettings.execute(sp);
        } catch (TelegramApiException e) {
            System.err.println("send photo error");
        }
    }

    public void sendVd(SendVideo sv) {
        try {
            botSettings.execute(sv);
        } catch (TelegramApiException e) {
            System.err.println("send video error");
        }
    }

    public void addContent(Long userId, Integer messageId, String fileId, MessageType type) {
        List<Content> contents = new ArrayList<>();
        if (content.get(userId) != null) contents = content.get(userId);
        contents.add(Content.builder()
                .type(type)
                .fileId(fileId)
                .messageId(messageId)
                .build());
        content.put(userId, contents);
    }

    public List<String> getSubjects() {
        List<String> sciencesList = scienceService.getSciences();
        sciencesList.add(Template.BACK);
        return sciencesList;
    }
}