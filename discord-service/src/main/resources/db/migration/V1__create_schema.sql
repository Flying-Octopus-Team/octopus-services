create EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS member (
    id uuid NOT NULL PRIMARY KEY DEFAULT uuid_generate_v4 (),
    discord_id varchar,
    trello_id varchar,
    trello_report_card_id varchar
)
