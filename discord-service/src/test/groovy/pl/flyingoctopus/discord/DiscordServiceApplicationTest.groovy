package pl.flyingoctopus.discord


import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.spock.Testcontainers
import spock.lang.Specification

@Testcontainers
@SpringBootTest
@ContextConfiguration(initializers = EnvironmentInitializer.Initializer.class)
class DiscordServiceApplicationTest extends Specification {


    def "context should load"() {
    }
}
