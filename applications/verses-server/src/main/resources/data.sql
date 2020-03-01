-- verses
INSERT INTO verse (text, title, author, reaction, impacted_id) VALUES ('text1', 'title1', 'author', 'reaction', 1);
INSERT INTO verse (text, title, author, reaction, impacted_id) VALUES ('text2', 'title2', 'author', 'reaction', 1);
INSERT INTO verse (text, title, author, reaction, impacted_id) VALUES ('text3', 'title3', 'author', 'reaction', 2);

INSERT INTO verse_tags (verse_id, tags) VALUES (1, 'fear');
INSERT INTO verse_tags (verse_id, tags) VALUES (1, 'joy');
INSERT INTO verse_tags (verse_id, tags) VALUES (3, 'fear');
