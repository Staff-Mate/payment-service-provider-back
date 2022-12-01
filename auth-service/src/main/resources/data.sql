INSERT INTO role
VALUES (1, 'ROLE_USER');
INSERT INTO role
VALUES (3, 'ROLE_ADMIN');

INSERT INTO regular_user
VALUES ('1fba8a11-f059-4bb3-aa6b-89151886503d', 'nick@gmail.com',
        '$2a$10$jb2dCfj9odULiIVcL5Dd8eiOV8fP9KSpzLULf4hPLMuwhoPglnbRa', 1, '1', '', '', '', '', '', '');

INSERT INTO payment_method
VALUES ('1fba8a11-f059-4bb3-aa6b-89151886503d', '', '', 'Bank Card', 'bank-card-service');

INSERT INTO enabled_payment_method
VALUES ('1fba8a11-f059-4bb3-aa6b-89151886503d', 'id', 'secret', '1fba8a11-f059-4bb3-aa6b-89151886503d');

INSERT INTO regular_user_enabled_payment_methods
VALUES ('1fba8a11-f059-4bb3-aa6b-89151886503d', '1fba8a11-f059-4bb3-aa6b-89151886503d');
