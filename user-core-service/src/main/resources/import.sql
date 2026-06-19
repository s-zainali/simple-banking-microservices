-- Seed accounts: account number is the login username.
INSERT INTO user_entities (account_number, name, balance, phone_number, password, role)
VALUES ('1000000001', 'System Admin', 0.00, '+920000000001', 'admin123', 'ADMIN');

INSERT INTO user_entities (account_number, name, balance, phone_number, password, role)
VALUES ('1000000002', 'Bank Teller', 0.00, '+920000000002', 'teller123', 'TELLER');

INSERT INTO user_entities (account_number, name, balance, phone_number, password, role)
VALUES ('1000000003', 'Ali Farhan', 50000.00, '+923324444555', 'customer123', 'CUSTOMER');

INSERT INTO user_entities (account_number, name, balance, phone_number, password, role)
VALUES ('1000000004', 'Sara Khan', 25000.00, '+923331112223', 'customer123', 'CUSTOMER');
