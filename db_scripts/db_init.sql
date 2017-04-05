CREATE TABLE IF NOT EXISTS Users (
    user_id INTEGER NOT NULL AUTO_INCREMENT,
    email text NOT NULL,
    password text NOT NULL,
    display_name varchar(32) NOT NULL,
    PRIMARY KEY(user_id)
);

CREATE TABLE IF NOT EXISTS Projects (
    project_id INTEGER NOT NULL AUTO_INCREMENT,
    title text NOT NULL,
    PRIMARY KEY(project_id)
);

CREATE TABLE IF NOT EXISTS ProjectsMembers (
    projectMember_id INTEGER NOT NULL AUTO_INCREMENT,
    project_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    admin BOOLEAN DEFAULT FALSE,
    PRIMARY KEY(projectMember_id),
    FOREIGN KEY(project_id) references Projects(project_id),
    FOREIGN KEY(user_id) references Users(user_id)
);

CREATE TABLE IF NOT EXISTS Tasks (
    task_id INTEGER NOT NULL AUTO_INCREMENT,
    project_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    description TEXT NOT NULL,
    dateAndTime DATETIME NOT NULL,
    state INTEGER NOT NULL,
    PRIMARY KEY(task_id),
    FOREIGN KEY(project_id) references Projects(project_id),
    FOREIGN KEY(user_id) references Users(user_id)
);