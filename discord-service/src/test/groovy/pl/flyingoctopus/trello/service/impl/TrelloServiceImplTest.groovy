package pl.flyingoctopus.trello.service.impl

import org.springframework.http.HttpStatus
import pl.flyingoctopus.trello.configuration.TrelloProperties
import spock.lang.Specification

class TrelloServiceImplTest extends Specification {

    TrelloProperties trelloProperties = Mock()
    TrelloServiceImpl trelloService = new TrelloServiceImpl(trelloProperties)

    def "inviteToWorkspace should return HttpStatus"() {
        given:
            def email = "a"
            def name = "b"
        when:
            def response = trelloService.inviteToWorkspace(email, name)
        then:
            response.block().getClass() == HttpStatus
    }
}
