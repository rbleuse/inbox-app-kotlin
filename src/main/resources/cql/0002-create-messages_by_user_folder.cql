CREATE TABLE main.messages_by_user_folder (
    user_id text,
    label text,
    created_time_uuid uuid,
    agotimestring text,
    "from" text,
    isread boolean,
    subject text,
    "to" list<text>,
    PRIMARY KEY ((user_id, label), created_time_uuid)
) WITH CLUSTERING ORDER BY (created_time_uuid ASC);
