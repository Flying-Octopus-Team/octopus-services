package pl.flyingoctopus.discord

import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.containers.PostgreSQLContainer

class EnvironmentInitializer {

    public static final PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres")
            .withDatabaseName("test")
            .withUsername("fobot")
            .withPassword("fobot")

    static {
        postgreSQLContainer.start()
    }

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        void initialize(ConfigurableApplicationContext applicationContext) {
        }
    }
}
