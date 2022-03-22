CREATE TABLE main.folders_by_user (
    user_id text,
    label text,
    color text,
    PRIMARY KEY (user_id, label)
) WITH CLUSTERING ORDER BY (label ASC);
