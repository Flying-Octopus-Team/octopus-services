package pl.flyingoctopus.trello.service.impl

import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.client.WebClient
import pl.flyingoctopus.trello.configuration.TrelloProperties
import spock.lang.Specification

class TrelloServiceImplTest extends Specification {

    TrelloProperties trelloProperties = Stub()

    WebClient.Builder webClientBuilder = Stub()

    def trelloService = new TrelloServiceImpl(trelloProperties, webClientBuilder)

    def "method should invite to workspace"() {
        given:
            WebClient webClient = Mock()
            webClientBuilder.build() >> webClient

            trelloProperties.getToken() >> "a"
            trelloProperties.getKey() >> "b"
            trelloProperties.getWorkspaceId() >> "c"

            def email = "example@foo.com"
            def name = "name"

        when:
            def response = trelloService.inviteToWorkspace(email, name)
        then:
            1 * webClient.put() >> WebClient.builder().baseUrl("https://api.trello.com/1").build().put()

            response.block() == HttpStatus.UNAUTHORIZED
    }
}
