package pl.flyingoctopus.discord;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;


@SpringBootTest
public class DiscordServiceApplicationTest {

    @Test
    public void contextLoads() {
    }

    static class EnvInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertyValues.of("discord.token=abb").applyTo(applicationContext);
        }
    }
}
