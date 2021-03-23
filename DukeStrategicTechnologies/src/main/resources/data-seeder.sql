insert into authority(id, name) values (1, 'ROLE_ADMIN');
insert into authority(id, name) values (2, 'ROLE_END_ENTITY_USER');
insert into authority(id, name) values (2, 'ROLE_CA');

insert into users values ('23', 0, 'Novi Sad', 'user1', 'user1@gmail.com', 'user11', false, 'org23', 'orgunit23', 'Serbia', 'userovic');
insert into users values ('24', 0, 'Novi Sad', 'user2', 'user2@gmail.com', 'user12', false, 'org24', 'orgunit24', 'Serbia', 'userovic');
insert into users values ('25', 0, 'Novi Sad', 'user3', 'user31@gmail.com', 'user13', false, 'org25', 'orgunit25', 'Serbia', 'userovic');
insert into users values ('26', 0, 'Novi Sad', 'user4', 'user41@gmail.com', 'user14', false, 'org26', 'orgunit26', 'Serbia', 'userovic');

insert into accounts(id, email, password, role) VALUES (100, 'rootic@gmail.com', '$2y$12$x/JI8Vrzn3IWCPZpbLn4qOpqObD0npAA6DV6EcHv7GojslHdL1n4K', 'Admin');
insert into accounts(id, email, password, role) VALUES (101, 'user1@gmail.com', '$2y$12$x/JI8Vrzn3IWCPZpbLn4qOpqObD0npAA6DV6EcHv7GojslHdL1n4K', 'User');
insert into accounts(id, email, password, role) VALUES (102, 'user2@gmail.com', '$2y$12$x/JI8Vrzn3IWCPZpbLn4qOpqObD0npAA6DV6EcHv7GojslHdL1n4K', 'User');
insert into accounts(id, email, password, role) VALUES (103, 'user3@gmail.com', '$2y$12$x/JI8Vrzn3IWCPZpbLn4qOpqObD0npAA6DV6EcHv7GojslHdL1n4K', 'User');
insert into accounts(id, email, password, role) VALUES (104, 'user4@gmail.com', '$2y$12$x/JI8Vrzn3IWCPZpbLn4qOpqObD0npAA6DV6EcHv7GojslHdL1n4K', 'User');

insert into user_authority(user_id, authority_id) values (100, 1);
insert into user_authority(user_id, authority_id) values (101, 2);
insert into user_authority(user_id, authority_id) values (102, 2);
insert into user_authority(user_id, authority_id) values (103, 2);
insert into user_authority(user_id, authority_id) values (104, 2);