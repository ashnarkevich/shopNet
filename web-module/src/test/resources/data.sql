INSERT INTO user (email, role, password)
VALUES ('test@test.test', 'CUSTOMER_USER', '$2y$12$eC2y/VZWJtXOsM5aE1GSqu0hdo5z2c7ntewVBwyvaqS0l2A09KON6');
INSERT INTO user_details (user_id, last_name, first_name, patronymic)
VALUES ((SELECT id FROM user WHERE email = 'test@test.test'), 'TestLastName', 'TestFirstName', 'TestPatronymic');
INSERT INTO user_information (user_id, address, phone)
VALUES ((SELECT id FROM user WHERE email = 'test@test.test'), 'Minsk, st.Esenina 23 - 23', '+123 12 1234567');

INSERT INTO user (email, role, password)
VALUES ('best@best.com', 'SALE_USER', '$2y$12$eC2y/VZWJtXOsM5aE1GSqu0hdo5z2c7ntewVBwyvaqS0l2A09KON6');
INSERT INTO user_details (user_id, last_name, first_name, patronymic)
VALUES ((SELECT id FROM user WHERE email = 'best@best.com'), 'BestLastName', 'BestFirstName', 'BestPatronymic');

INSERT INTO review (text, date_create, is_active, author_id)
VALUES ('This is the review text.', '2020-05-03', 1, 2);

INSERT INTO article (title, date_create, date_publication, text, author_id)
VALUES ('Test article title', '2020-12-15', '2031-02-20 06:30', 'This is the article text.', 2);

INSERT INTO comment (date, text, article_id, author_id)
VALUES ('2020-8-30 21:23:34', 'This is the comment text.', 1, 3);

INSERT INTO orders (number, status, date_create, total_price, item_id, customer_id, item_amount)
VALUES ('5-2021', 'NEW', '2020-08-22 08:25:30', 690.30, 1, 2, 45);

INSERT INTO item (name, number, price)
VALUES ('testItemName', '09354376-13f9-4e05-b550-1abdddca7bd4', 15.34);
INSERT INTO item_details (item_id, description)
VALUES ((SELECT id FROM item WHERE number = '09354376-13f9-4e05-b550-1abdddca7bd4'), 'Test item description')