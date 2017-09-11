INSERT INTO Users (email, password, display_name) VALUES ('jankowalski@example.com', '12345678', 'Jan Kowalski','2017-04-16 19:22:34');
INSERT INTO Users (email, password, display_name) VALUES ('jerzykrawczyk@example.com', 'zaq12wsx', 'Jerzy Krawczyk','2017-04-16 19:22:34');
INSERT INTO Users (email, password, display_name) VALUES ('adamnowak@com.com', 'mko09ijn', 'Adam Nowak','2017-04-16 19:22:34');
INSERT INTO Users (email, password, display_name) VALUES ('agatakowalczyk@example.com', '12345678', 'Agata Kowalczyk','2017-04-16 19:22:34');
INSERT INTO Users (email, password, display_name) VALUES ('marianowakowska@example.com', 'nji98uhb', 'Maria Nowakowska','2017-04-16 19:22:34');

INSERT INTO Projects (title) VALUES ('Pierwszy projekt','2017-04-16 19:22:34');
INSERT INTO Projects (title) VALUES ('Drugi projekt','2017-04-16 19:22:34');

INSERT INTO projectsmembers(project_id, user_id, admin) VALUES ('1','1','TRUE','2017-04-16 19:22:34');
INSERT INTO projectsmembers(project_id, user_id, admin) VALUES ('1','2','FALSE','2017-04-16 19:22:34');
INSERT INTO projectsmembers(project_id, user_id, admin) VALUES ('1','3','FALSE','2017-04-16 19:22:34');
INSERT INTO projectsmembers(project_id, user_id, admin) VALUES ('2','4','TRUE','2017-04-16 19:22:34');
INSERT INTO projectsmembers(project_id, user_id, admin) VALUES ('2','5','FALSE','2017-04-16 19:22:34');

INSERT INTO Tasks (project_id, user_id, description, dateandtime, state) VALUES ('1', '1', 'Utworzenie GUI', '2017-04-16 19:22:34', '1');
INSERT INTO Tasks (project_id, user_id, description, dateandtime, state) VALUES ('1', '2', 'Utworzenie GUI', '2017-04-16 20:12:45', '2');
INSERT INTO Tasks (project_id, user_id, description, dateandtime, state) VALUES ('1', '2', 'Dodanie funkcjonalnosci', '2017-04-16 20:14:27', '1');
INSERT INTO Tasks (project_id, user_id, description, dateandtime, state) VALUES ('1', '1', 'Dodanie funkcjonalnosci', '2017-04-16 20:38:20', '2');
INSERT INTO Tasks (project_id, user_id, description, dateandtime, state) VALUES ('1', '1', 'Dodanie funkcjonalnosci', '2017-04-16 22:10:45', '3');
INSERT INTO Tasks (project_id, user_id, description, dateandtime, state) VALUES ('2', '3', 'Utworzenie bazy danych', '2017-04-17 16:12:45', '1');
INSERT INTO Tasks (project_id, user_id, description, dateandtime, state) VALUES ('2', '4', 'Utworzenie bazy danych', '2017-04-17 16:42:29', '2');
INSERT INTO Tasks (project_id, user_id, description, dateandtime, state) VALUES ('2', '5', 'Utworzenie bazy danych', '2017-04-17 16:59:11', '3');
INSERT INTO Tasks (project_id, user_id, description, dateandtime, state) VALUES ('2', '3', 'Modyfikacja bazy', '2017-04-17 17:18:20', '1');
INSERT INTO Tasks (project_id, user_id, description, dateandtime, state) VALUES ('2', '4', 'Modyfikacja bazy', '2017-04-17 17:49:45', '2');
