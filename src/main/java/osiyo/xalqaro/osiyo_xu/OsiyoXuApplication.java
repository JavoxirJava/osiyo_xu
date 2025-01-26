package osiyo.xalqaro.osiyo_xu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import osiyo.xalqaro.osiyo_xu.bot.BotMethods;
import osiyo.xalqaro.osiyo_xu.bot.BotSettings;
import osiyo.xalqaro.osiyo_xu.bot.ButtonSettings;
import osiyo.xalqaro.osiyo_xu.service.ContentService;
import osiyo.xalqaro.osiyo_xu.service.MessageService;

@SpringBootApplication
public class OsiyoXuApplication {
    public static void main(String[] args) throws TelegramApiException {
        ConfigurableApplicationContext run = SpringApplication.run(OsiyoXuApplication.class, args);
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        BotMethods botMethods = new BotMethods(run.getBean(BotSettings.class), run.getBean(ButtonSettings.class),
                run.getBean(ContentService.class), run.getBean(MessageService.class));
        telegramBotsApi.registerBot(new BotSettings(botMethods));
    }
}