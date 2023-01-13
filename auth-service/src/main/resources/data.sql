INSERT INTO role
VALUES (1, 'ROLE_USER');
INSERT INTO role
VALUES (3, 'ROLE_ADMIN');

INSERT INTO bank (id, name, bank_url)
VALUES ('3890539b-06cd-4be1-9ab8-b2e9516b3b5a', 'UniCredit Bank', 'http://localhost:8080');

INSERT INTO bank (id, name, bank_url)
VALUES ('3890539b-06cd-4be1-9ab8-b2e9516b3b51', 'Erste Bank', 'http://localhost:8088');

INSERT INTO administrator
VALUES ('1fba8a11-f059-4bb3-aa6b-891518865033', 'discash.hr@gmail.com','$2a$10$jb2dCfj9odULiIVcL5Dd8eiOV8fP9KSpzLULf4hPLMuwhoPglnbRa',3);

INSERT INTO regular_user (id, email, password, role_id, company_name, first_name, last_name, country, state, city,
                          api_key, bank_id, success_url, failed_url, error_url)
VALUES ('1fba8a11-f059-4bb3-aa6b-89151886503d', 'nick@gmail.com', '$2a$10$jb2dCfj9odULiIVcL5Dd8eiOV8fP9KSpzLULf4hPLMuwhoPglnbRa', 1, 'StaffMate', 'Nick', 'Jones', 'USA', 'Nevada', 'Reno', '1',
        '3890539b-06cd-4be1-9ab8-b2e9516b3b5a', 'http://localhost:4200/payment/success', 'http://localhost:4200/payment/failed', 'http://localhost:4200/payment/error');

INSERT INTO payment_method
VALUES ('1fba8a11-f059-4bb3-aa6b-89151886503d', 'Description', 'https://d18fuqpnk61mcc.cloudfront.net/build/images/prepaid-cards/track-prepaid-card-spend-decentro.svg', 'Bank Card', 'bank-card-service');

-- INSERT INTO enabled_payment_method
-- VALUES ('1fba8a11-f059-4bb3-aa6b-89151886503d', CURRENT_TIMESTAMP,'id', '$2a$10$Ranq4UTOwC46aDiPS7gx2eaJgqeLJ73V2ZjBEGsWVll/mm8XJ0RTu', '1fba8a11-f059-4bb3-aa6b-89151886503d');

-- INSERT INTO regular_user_enabled_payment_methods
-- VALUES ('1fba8a11-f059-4bb3-aa6b-89151886503d', '1fba8a11-f059-4bb3-aa6b-89151886503d');
