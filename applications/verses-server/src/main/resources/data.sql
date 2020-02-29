-- impacted users
INSERT INTO impacted (unique_id, name, user_source, display_name, email, picture, locale, roles) VALUES ('google-112955847802201390733', '112955847802201390733', 'google', 'Jeff Black', 'codebeneath@gmail.com', '', 'en', 'ROLE_ADMIN');
INSERT INTO impacted (unique_id, name, user_source, display_name, email, picture, locale, roles) VALUES ('okta-00u27prcsYez7cjum4x6',    '00u27prcsYez7cjum4x6',  'okta',   'J Black',    'jeff@okta.com',  '', '', 'ROLE_USER');
INSERT INTO impacted (unique_id, name, user_source, display_name, email, picture, locale, roles) VALUES ('local-alan',  'alan',  'local', 'Alan Smithe',    'alan@codebeneath.com',     '', '', 'ROLE_USER');
INSERT INTO impacted (unique_id, name, user_source, display_name, email, picture, locale, roles) VALUES ('local-sue',   'sue',   'local', 'Sue Zee',        'sue@codebeneath.com',      '', '', 'ROLE_USER');
INSERT INTO impacted (unique_id, name, user_source, display_name, email, picture, locale, roles) VALUES ('local-dan',   'dan',   'local', 'Dan Zig',        'dan@codebeneath.com',      '', '', 'ROLE_USER');
INSERT INTO impacted (unique_id, name, user_source, display_name, email, picture, locale, roles) VALUES ('local-jenn',  'jenn',  'local', 'Jennifer Doe',   'jennifer@codebeneath.com', '', '', 'ROLE_USER');
INSERT INTO impacted (unique_id, name, user_source, display_name, email, picture, locale, roles) VALUES ('local-chloe', 'chloe', 'local', 'Chloe O''Brian', 'c@c.com',                  '', '', 'ROLE_USER');
INSERT INTO impacted (unique_id, name, user_source, display_name, email, picture, locale, roles) VALUES ('local-tex',   'tex',   'local', 'Tex O''Brian',   'z@z.com',                  '', '', 'ROLE_USER');

-- verses
INSERT INTO verse (text, title, author, reaction, impacted_id) VALUES ('text1', 'title1', 'author', 'reaction', 1);
INSERT INTO verse (text, title, author, reaction, impacted_id) VALUES ('text2', 'title2', 'author', 'reaction', 1);
INSERT INTO verse (text, title, author, reaction, impacted_id) VALUES ('text3', 'title3', 'author', 'reaction', 2);

INSERT INTO verse_tags (verse_id, tags) VALUES (1, 'fear');
INSERT INTO verse_tags (verse_id, tags) VALUES (1, 'joy');
INSERT INTO verse_tags (verse_id, tags) VALUES (3, 'fear');
