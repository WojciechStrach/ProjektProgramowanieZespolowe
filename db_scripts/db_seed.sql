INSERT INTO Users (email, password, display_name) VALUES ('jankowalski@example.com', '12345678', 'Jan Kowalski');
INSERT INTO Users (email, password, display_name) VALUES ('jerzykrawczyk@example.com', 'zaq12wsx', 'Jerzy Krawczyk');
INSERT INTO Users (email, password, display_name) VALUES ('adamnowak@com.com', 'mko09ijn', 'Adam Nowak');
INSERT INTO Users (email, password, display_name) VALUES ('agatakowalczyk@example.com', '12345678', 'Agata Kowalczyk');
INSERT INTO Users (email, password, display_name) VALUES ('marianowakowska@example.com', 'nji98uhb', 'Maria Nowakowska');

INSERT INTO Projects (title) VALUES ('Pierwszy projekt');
INSERT INTO Projects (title) VALUES ('Drugi projekt');

INSERT INTO Tasks (description, dateAndTime, state, user_id, project_id) VALUES ('Utworzenie GUI', '14.02.2012', '1', '1', '1');
INSERT INTO Tasks (description, dateAndTime, state, user_id, project_id) VALUES ('Utworzenie GUI', '14.02.2012', '1', '2', '1');
INSERT INTO Tasks (description, dateAndTime, state, user_id, project_id) VALUES ('Dodanie funkcjonalnosci', '14.02.2012', '2', '1', '1');
INSERT INTO Tasks (description, dateAndTime, state, user_id, project_id) VALUES ('Dodanie funkcjonalnosci', '14.02.2012', '2', '2', '1');
INSERT INTO Tasks (description, dateAndTime, state, user_id, project_id) VALUES ('Dodanie funkcjonalnocci', '14.02.2012', '1', '3', '1');
INSERT INTO Tasks (description, dateAndTime, state, user_id, project_id) VALUES ('Utworzenie bazy danych', '14.02.2012', '3', '1', '2');
INSERT INTO Tasks (description, dateAndTime, state, user_id, project_id) VALUES ('Utworzenie bazy danych', '14.02.2012', '3', '2', '2');
INSERT INTO Tasks (description, dateAndTime, state, user_id, project_id) VALUES ('Utworzenie bazy danych', '14.02.2012', '2', '3', '2');
INSERT INTO Tasks (description, dateAndTime, state, user_id, project_id) VALUES ('Modyfikacja bazy', '14.02.2012', '4', '1', '2');
INSERT INTO Tasks (description, dateAndTime, state, user_id, project_id) VALUES ('Modyfikacja bazy', '14.02.2012', '4', '2', '2');