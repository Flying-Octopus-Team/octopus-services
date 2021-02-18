package pl.flyingoctopus.discord.member.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import pl.flyingoctopus.discord.member.model.Member;

import java.util.UUID;

public interface MemberRepository extends ReactiveCrudRepository<Member, UUID> {
}
