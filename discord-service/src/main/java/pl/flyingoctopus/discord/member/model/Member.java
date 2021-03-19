package pl.flyingoctopus.discord.member.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("member")
public class Member {

    @Id
    @Column("id")
    private UUID id;

    @Column("discord_name")
    private String discordName;

    @Column("discord_id")
    private String discordId;

    @Column("trello_id")
    private String trelloId;

    @Column("trello_report_card_id")
    private String trelloReportCardId;

    @Column("trello_email")
    private String trelloEmail;

    @Column("wiki_email")
    private String wikiEmail;

}
