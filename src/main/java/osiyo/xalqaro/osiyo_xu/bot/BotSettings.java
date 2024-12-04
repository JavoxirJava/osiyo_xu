package osiyo.xalqaro.osiyo_xu.bot;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class BotSettings extends TelegramLongPollingBot {

    final BotMethods botMethods;

    public BotSettings(BotMethods botMethods) {
        this.botMethods = botMethods;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) botMethods.message(update.getMessage());
        else if (update.hasCallbackQuery()) botMethods.callbackData(update.getCallbackQuery());
    }

    @Override
    public String getBotUsername() {
        return Template.BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return Template.BOT_TOKEN;
    }
}