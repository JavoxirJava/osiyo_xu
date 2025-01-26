package osiyo.xalqaro.osiyo_xu.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class BotSettings extends TelegramLongPollingBot {

    final BotMethods botMethods;

    @Autowired
    public BotSettings(BotMethods botMethods) {
        this.botMethods = botMethods;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) botMethods.message(update.getMessage());
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
