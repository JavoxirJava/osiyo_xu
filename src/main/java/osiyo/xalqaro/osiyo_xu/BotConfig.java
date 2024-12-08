package osiyo.xalqaro.osiyo_xu;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.DefaultBotOptions;

@Configuration
public class BotConfig {

    @Bean
    public DefaultBotOptions defaultBotOptions() {
        // Qo'shimcha sozlashlar kerak bo'lsa, shu yerda amalga oshiring
        return new DefaultBotOptions();
    }
}
