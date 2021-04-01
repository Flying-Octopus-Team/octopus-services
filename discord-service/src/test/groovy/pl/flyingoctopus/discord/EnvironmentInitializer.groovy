package pl.flyingoctopus.discord


import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.test.util.TestPropertyValues
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

        private static final Logger log = LoggerFactory.getLogger(Initializer.class);

        @Override
        void initialize(ConfigurableApplicationContext applicationContext) {

            log.info("spring.datasource.url={}", postgreSQLContainer.getJdbcUrl())
            log.info("spring.datasource.username={}", postgreSQLContainer.getUsername())

            TestPropertyValues.of(
                    "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                    "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                    "spring.datasource.password=" + postgreSQLContainer.getPassword(),
                    "discord.enabled=false"
            ).applyTo(applicationContext.getEnvironment())
        }
    }
}
