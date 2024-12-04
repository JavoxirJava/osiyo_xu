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
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import osiyo.xalqaro.osiyo_xu.payload.ApiResponse;
import osiyo.xalqaro.osiyo_xu.service.ScienceService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BotMethods {

    private final BotSettings botSettings;
    private final ButtonSettings buttonSettings;
    private final ScienceService scienceService;

    public BotMethods(@Lazy BotSettings botSettings, ButtonSettings buttonSettings, ScienceService scienceService) {
        this.botSettings = botSettings;
        this.buttonSettings = buttonSettings;
        this.scienceService = scienceService;
    }

    private final Map<Long, String> chooseCancel = new HashMap<>();
    private final Map<Long, String> choose = new HashMap<>();
    private final Map<Long, String> caption = new HashMap<>();
    private final Map<Long, List<String>> searchImages = new HashMap<>();


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
                case "/start" -> {
                    sm.setReplyMarkup(buttonSettings.getKeyboardButton(Template.ADMIN_START));
                    sendMSG(sm, Template.CHOOSE_DEPARTMENT);
                }
                // science department
                case Template.SCIENCE_DEPARTMENT -> sendMessageBtn(sm, Template.ADMIN_SCIENCE);
                case Template.ADD_SCIENCE -> {
                    sm.setReplyMarkup(buttonSettings.getKeyboardButton(Template.CANSEL_BUTTON));
                    sendMSG(sm, "Fan nomini kiriting:");
                    choose.put(userId, "addScience");
                }
                // subject department
                case Template.SUBJECT_DEPARTMENT -> sendMessageBtn(sm, Template.ADMIN_SUBJECT);
                default -> {
                    // science department
                    switch (choose.get(userId)) {
                        case "addScience" -> {
                            if (text.equals(Template.CANCEL)) sendMessageBtn(sm, Template.ADMIN_SCIENCE);
                            else {
                                ApiResponse<?> apiResponse = scienceService.addScience(text);
                                if (!apiResponse.isSuccess()) sendMSG(sm, apiResponse.getMessage());
                                else {
                                    sendMSG(sm, apiResponse.getMessage());
                                    sendMessageBtn(sm, Template.ADMIN_SCIENCE);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void userMessage(Message message, Long chatId, Long userId) {
        if (message.hasText()) {
            SendMessage sm = new SendMessage(chatId.toString(), message.getText());
            String text = message.getText();
            switch (text) {
                case "/start" -> {
                    sm.setReplyMarkup(buttonSettings.getKeyboardButton(Template.OFFER));
                    sendMSG(sm, "Assalomu alaykum botga hush kelibsiz bu bot maqsadi...?");
                    sm.setReplyMarkup(buttonSettings.getInlineMarkup(scienceService.getSciences()));
                    sendMSG(sm, "Sizga qiziq fanni tanlang.");
                }
                case "Taklif qoldirish" -> {
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
//        choose.remove(Long.parseLong(sm.getChatId())); TODO uylab kurish kerak
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
}