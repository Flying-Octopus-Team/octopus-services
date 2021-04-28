package pl.flyingoctopus.discord.member.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import pl.flyingoctopus.discord.member.model.Member;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface MemberRepository extends ReactiveCrudRepository<Member, UUID> {

    Mono<Member> findByDiscordId(String discordId);
}
