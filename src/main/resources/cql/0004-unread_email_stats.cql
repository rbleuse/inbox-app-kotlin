CREATE TABLE main.unread_email_stats (
    user_id text,
    label text,
    unread_count counter,
    PRIMARY KEY (user_id, label)
) WITH CLUSTERING ORDER BY (label ASC);
