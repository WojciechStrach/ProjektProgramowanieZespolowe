DROP TABLE IF EXISTS ProjectsMembers;
DROP TABLE IF EXISTS Tasks;
DROP TABLE IF EXISTS Users;
DROP TABLE IF EXISTS Projects;


CREATE TABLE IF NOT EXISTS Users (
    user_id INTEGER NOT NULL AUTO_INCREMENT,
    email text NOT NULL,
    avatar MEDIUMBLOB,
    password text NOT NULL,
    display_name varchar(32) NOT NULL,
    dateAndTime DATETIME NOT NULL,
    PRIMARY KEY(user_id)
);

CREATE TABLE IF NOT EXISTS Projects (
    project_id INTEGER NOT NULL AUTO_INCREMENT,
    title text NOT NULL,
    dateAndTime DATETIME NOT NULL,
    PRIMARY KEY(project_id)
);

CREATE TABLE IF NOT EXISTS ProjectsMembers (
    projectMember_id INTEGER NOT NULL AUTO_INCREMENT,
    project_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    admin BOOLEAN DEFAULT FALSE,
    dateAndTime DATETIME NOT NULL,
    PRIMARY KEY(projectMember_id),
    FOREIGN KEY(project_id) references Projects(project_id)
);

CREATE TABLE IF NOT EXISTS Tasks (
    task_id INTEGER NOT NULL AUTO_INCREMENT,
    project_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    assigned_user_id INTEGER,
    description TEXT NOT NULL,
    dateAndTime DATETIME NOT NULL,
    state ENUM('TOREVIEW', 'TODO', 'DONE') NOT NULL,
    PRIMARY KEY(task_id),
    FOREIGN KEY(project_id) references Projects(project_id),
    FOREIGN KEY(user_id) references Users(user_id)
);


