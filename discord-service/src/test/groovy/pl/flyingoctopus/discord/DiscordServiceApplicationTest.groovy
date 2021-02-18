package pl.flyingoctopus.discord

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.spock.Testcontainers
import pl.flyingoctopus.discord.member.repository.MemberRepository
import spock.lang.Specification

@Testcontainers
@SpringBootTest
@ContextConfiguration(initializers = EnvironmentInitializer.Initializer.class)
class DiscordServiceApplicationTest extends Specification {

    @Autowired
    MemberRepository memberRepository

    def "context should load"() {
        expect:
            memberRepository != null
    }
}
