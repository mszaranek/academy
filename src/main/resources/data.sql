INSERT INTO public.app_role (id, role_name) VALUES (1, 'ROLE_ADMIN');
INSERT INTO public.app_role (id, role_name) VALUES (4, 'ROLE_USER');
INSERT INTO public.app_role (id, role_name) VALUES (5, 'ROLE_MANAGER');
INSERT INTO public."user" (id, username, password, first_name, last_name, email) VALUES (2, 'user', 'user', 'user', 'user', 'user@autorun.solutions');
INSERT INTO public."user" (id, username, password, first_name, last_name, email) VALUES (3, 'manager', '$2a$10$7QtfbG6Owz.Za.aU9cUMeebL658luNiml6SyM6MZ.hP.ByiwSlS5G', 'manager', 'manager', 'manager@autorun.solutions');
INSERT INTO public."user" (id, username, password, first_name, last_name, email) VALUES (1, 'admin', '$2a$10$jYI81dDxnrkh3TndGsz9IO3iiCDoAwzs0nQqcpzjJwQHXju4K1Doe', 'admin', 'admin', 'admin@autorun.solutions');
INSERT INTO public.project (id, name, monthly_cost, status) VALUES (1, 'project1', 50000, 'in progress');
INSERT INTO public.project (id, name, monthly_cost, status) VALUES (2, 'project2', 100000, 'in progress');
INSERT INTO public.project (id, name, monthly_cost, status) VALUES (3, 'project3', 200000, 'in progress');
INSERT INTO public.invoice (id, amount, user_id, paid, date, validation_status) VALUES (1, 1000, 1, false, '2018-11-06', 'pending');
INSERT INTO public.invoice (id, amount, user_id, paid, date, validation_status) VALUES (2, 2000, 2, false, '2018-11-06', 'pending');
INSERT INTO public.invoice (id, amount, user_id, paid, date, validation_status) VALUES (3, 3000, 3, false, '2018-11-06', 'pending');
INSERT INTO public.invoice (id, amount, user_id, paid, date, validation_status) VALUES (4, 20000, 1, false, '2018-11-07', 'pending');
INSERT INTO public.invoice (id, amount, user_id, paid, date, validation_status) VALUES (5, 18000, 2, false, '2018-11-07', 'pending');
INSERT INTO public.invoice (id, amount, user_id, paid, date, validation_status) VALUES (6, 12000, 3, false, '2018-11-07', 'pending');
INSERT INTO public.invoice (id, amount, user_id, paid, date, validation_status) VALUES (7, 14000, 1, false, '2018-11-07', 'pending');
INSERT INTO public.invoice (id, amount, user_id, paid, date, validation_status) VALUES (8, 6000, 2, false, '2018-11-07', 'pending');
INSERT INTO public.invoice (id, amount, user_id, paid, date, validation_status) VALUES (9, 7500, 3, false, '2018-11-07', 'pending');
INSERT INTO public.system (id, name) VALUES (1, 'system1');
INSERT INTO public.system (id, name) VALUES (2, 'system2');
INSERT INTO public.system (id, name) VALUES (3, 'system3');
INSERT INTO public.task (id, number, user_id, estimate, start_date, finish_date, status, type, sprint_id, system_id) VALUES (1, 1000, 1, 4, '2018-11-13', '2018-11-13', 'done', 'task', null, 1);
INSERT INTO public.task (id, number, user_id, estimate, start_date, finish_date, status, type, sprint_id, system_id) VALUES (2, 2000, 2, 7, '2018-11-13', '2018-11-13', 'done', 'task', null, 1);
INSERT INTO public.task (id, number, user_id, estimate, start_date, finish_date, status, type, sprint_id, system_id) VALUES (3, 3000, 3, 9, '2018-11-13', '2018-11-13', 'done', 'task', null, 1);
INSERT INTO public.project_invoice (project_id, invoice_id) VALUES (1, 1);
INSERT INTO public.project_invoice (project_id, invoice_id) VALUES (2, 2);
INSERT INTO public.project_invoice (project_id, invoice_id) VALUES (3, 3);
INSERT INTO public.project_invoice (project_id, invoice_id) VALUES (1, 5);
INSERT INTO public.project_invoice (project_id, invoice_id) VALUES (3, 8);
INSERT INTO public.project_invoice (project_id, invoice_id) VALUES (2, 4);
INSERT INTO public.project_invoice (project_id, invoice_id) VALUES (3, 7);
INSERT INTO public.project_invoice (project_id, invoice_id) VALUES (1, 6);
INSERT INTO public.project_invoice (project_id, invoice_id) VALUES (2, 9);
INSERT INTO public.project_system (project_id, system_id) VALUES (1, 1);
INSERT INTO public.project_system (project_id, system_id) VALUES (2, 2);
INSERT INTO public.project_system (project_id, system_id) VALUES (3, 3);
INSERT INTO public.project_user (project_id, user_id) VALUES (2, 2);
INSERT INTO public.project_user (project_id, user_id) VALUES (1, 2);
INSERT INTO public.project_user (project_id, user_id) VALUES (3, 2);
INSERT INTO public.user_app_role (user_id, app_role_id) VALUES (2, 4);
INSERT INTO public.user_app_role (user_id, app_role_id) VALUES (1, 1);
INSERT INTO public.user_app_role (user_id, app_role_id) VALUES (3, 5);