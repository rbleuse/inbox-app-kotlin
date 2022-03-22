CREATE TABLE main.messages (
    id uuid PRIMARY KEY,
    body text,
    "from" text,
    subject text,
    "to" list<text>
);
