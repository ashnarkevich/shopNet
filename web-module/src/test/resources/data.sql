INSERT INTO user (email, role, password)
VALUES ('test@test.test', 'ADMINISTRATOR', '$2y$12$eC2y/VZWJtXOsM5aE1GSqu0hdo5z2c7ntewVBwyvaqS0l2A09KON6');
INSERT INTO user_details (user_id, last_name, first_name, patronymic)
VALUES ((SELECT id FROM user WHERE email = 'test@test.test'), 'TestLastName', 'TestFirstName', 'TestPatronymic');

INSERT INTO user (email, role, password)
VALUES ('best@best.com', 'SALE_USER', '$2y$12$eC2y/VZWJtXOsM5aE1GSqu0hdo5z2c7ntewVBwyvaqS0l2A09KON6');
INSERT INTO user_details (user_id, last_name, first_name, patronymic)
VALUES ((SELECT id FROM user WHERE email = 'best@best.com'), 'BestLastName', 'BestFirstName', 'BestPatronymic');

INSERT INTO review (text, date_create, is_active, author_id)
VALUES ('This is the review text.', '2020-05-03', 1, '2');