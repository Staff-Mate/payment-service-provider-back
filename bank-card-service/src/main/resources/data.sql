INSERT INTO payment_request (id, merchant_id, merchant_password, amount, merchant_order_id, merchant_timestamp,
                             success_url, failed_url, error_url, active)
VALUES ('e0d102b4-a320-40da-86f7-2bccc7990190', 'id', '$2a$10$jb2dCfj9odULiIVcL5Dd8eiOV8fP9KSpzLULf4hPLMuwhoPglnbRa', 1000, 'e0d102b4-a320-40da-86f7-2bccc7990191', '2023-01-11 10:00:46.375', NULL, NULL, NULL, false);

INSERT INTO payment_response (id, payment_id, transaction_status, merchant_id, acquirer_order_id, acquirer_timestamp,
                              payment_request_id)
VALUES ('d10fc115-a13b-4cc0-afa0-fabedfbd0dbe', 'e0d102b4-a320-40da-86f7-2bccc7990192', 'SUCCESS', 'id', 'e0c373fb-f866-494f-b972-645aa6cdead7', '2023-01-11 10:05:46.375', 'e0d102b4-a320-40da-86f7-2bccc7990190');

INSERT INTO payment_request (id, merchant_id, merchant_password, amount, merchant_order_id, merchant_timestamp,
                             success_url, failed_url, error_url, active)
VALUES ('e0d102b4-a320-40da-86f7-2bccc7990195', 'id', '$2a$10$jb2dCfj9odULiIVcL5Dd8eiOV8fP9KSpzLULf4hPLMuwhoPglnbRa', 1000, 'e0d102b4-a320-40da-86f7-2bccc7990196', '2023-01-11 13:00:46.375', NULL, NULL, NULL,true);
