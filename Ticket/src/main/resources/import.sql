--INSERT INTO ticket (title, description, status, priority, progress, estimate, type, project_id, assigned_to_id, created_by_id, created_At) VALUES ('Ticket 1', 'Descrição do Ticket 1', 'NEW', 'LOW', 0, 10, 'OTHER', 1, 1, 1, CURRENT_TIMESTAMP);

--tem os 2 projectos
INSERT INTO ticket (title, description, status, priority, progress, estimate, type, project_id, people_id, created_At) VALUES ('Ticket 1', 'Descrição do Ticket 1', 'NEW', 'LOW', 10, 10, 'OTHER', 1, 1, CURRENT_TIMESTAMP);
INSERT INTO ticket (title, description, status, priority, progress, estimate, type, project_id, people_id, created_At) VALUES ('Ticket 2', 'Descrição do Ticket 2', 'NEW', 'LOW', 20, 10, 'OTHER', 2, 1, CURRENT_TIMESTAMP);
INSERT INTO ticket (title, description, status, priority, progress, estimate, type, project_id, people_id, created_At) VALUES ('Ticket 3', 'Descrição do Ticket 3', 'NEW', 'LOW', 30, 10, 'OTHER', 3, 1, CURRENT_TIMESTAMP);
INSERT INTO ticket (title, description, status, priority, progress, estimate, type, project_id, people_id, created_At) VALUES ('Ticket 4', 'Descrição do Ticket 4', 'NEW', 'LOW', 50, 10, 'OTHER', 3, 1, CURRENT_TIMESTAMP);

--tem so o projecto
--INSERT INTO ticket (title, description, status, priority, progress, estimate, type, project_id, created_At) VALUES ('Ticket 1', 'Descrição do Ticket 1', 'NEW', 'LOW', 0, 10, 'OTHER', 1, CURRENT_TIMESTAMP);

--tem so o people
--INSERT INTO ticket (title, description, status, priority, progress, estimate, type, people_id, created_At) VALUES ('Ticket 1', 'Descrição do Ticket 1', 'NEW', 'LOW', 0, 10, 'OTHER', 1, CURRENT_TIMESTAMP);