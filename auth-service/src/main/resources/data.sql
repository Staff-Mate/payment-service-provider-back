INSERT INTO role
VALUES (1, 'ROLE_USER');
INSERT INTO role
VALUES (3, 'ROLE_ADMIN');

INSERT INTO administrator
VALUES ('1fba8a11-f059-4bb3-aa6b-891518865033', 'discash.hr@gmail.com','$2a$10$jb2dCfj9odULiIVcL5Dd8eiOV8fP9KSpzLULf4hPLMuwhoPglnbRa',3);

INSERT INTO regular_user
VALUES ('1fba8a11-f059-4bb3-aa6b-89151886503d', 'nick@gmail.com',
        '$2a$10$jb2dCfj9odULiIVcL5Dd8eiOV8fP9KSpzLULf4hPLMuwhoPglnbRa', 1, '1', 'Reno', 'G-P Recruit', 'USA', 'Nick', 'Jones', 'Nevada');

INSERT INTO payment_method
VALUES ('1fba8a11-f059-4bb3-aa6b-89151886503a', 'Card acquiring allows your business to get paid. Use our solution to accept thousands of daily card payments quickly and seamlessly, thanks to our extensive payments rails and licensing network. Then, use our platform to access a full range of banking and payments services, all delivered via a single, developer-first API.', 'https://d18fuqpnk61mcc.cloudfront.net/build/images/prepaid-cards/track-prepaid-card-spend-decentro.svg', 'Bank Card', 'bank-card-service');

INSERT INTO payment_method
VALUES ('1fba8a11-f059-4bb3-aa6b-89151886503b', 'Our platform offers an app-based platform that enables online mobile payments in-store and on the go. App allows smartphones to interact with any other connected screen. Users enter their account details into the app and can make online payments by scanning websites that are set up to work with the app based on QR codes. App is available on iOS and Android devices.', 'https://d18fuqpnk61mcc.cloudfront.net/build/images/payments/static-dynamic-qr-codes-payments-api.svg', 'QR Code', 'qr-code-service');

INSERT INTO payment_method
VALUES ('1fba8a11-f059-4bb3-aa6b-89151886503c', 'PayPal allows its users to easily send money to friends, family, business associates and more. In order to start sending and transferring money via PayPal, you will first need to set up an account.Once you have a PayPal account, you can both send and receive money from anyone else with a PayPal account.', 'https://d18fuqpnk61mcc.cloudfront.net/build/images/neobanks/onboard-users-to-your-neobanking-platform.svg', 'Pay Pal', 'paypal-service');

INSERT INTO payment_method
VALUES ('1fba8a11-f059-4bb3-aa6b-89151886503e', 'Accepting Bitcoin on your website plays into your brand identity, then in some people’s eyes, it’s going to show you’re an innovative, cutting-edge company that’s not afraid to embrace new technology.', 'https://d18fuqpnk61mcc.cloudfront.net/build/images/crypto-exchanges-nft-platforms/banner.svg', 'Bitcoin', 'bitcoin-service');

-- INSERT INTO enabled_payment_method
-- VALUES ('1fba8a11-f059-4bb3-aa6b-89151886503d', CURRENT_TIMESTAMP,'id', '$2a$10$Ranq4UTOwC46aDiPS7gx2eaJgqeLJ73V2ZjBEGsWVll/mm8XJ0RTu', '1fba8a11-f059-4bb3-aa6b-89151886503d');

-- INSERT INTO regular_user_enabled_payment_methods
-- VALUES ('1fba8a11-f059-4bb3-aa6b-89151886503d', '1fba8a11-f059-4bb3-aa6b-89151886503d');
