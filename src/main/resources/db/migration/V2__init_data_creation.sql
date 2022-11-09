insert into validationdb.rule_category(rule_category_sk, rule_category_id, rule_category_name, rule_category_desc, rule_type, created_date, updated_date) VALUES ('812162dd-264f-4756-a750-7dddd469c9b1', 'W317DVAVX8XQDMR', 'ACCOUNT', 'All rules associated with the account', 'ACCOUNT_RULE', sysdate(), sysdate());
insert into validationdb.rule_category(rule_category_sk, rule_category_id, rule_category_name, rule_category_desc, rule_type, created_date, updated_date) VALUES ('5f5390cb-6e87-4156-8f6d-8fdcbde1f62d', 'EU7MASBTMUBLV62', 'TRANSACTION', 'BUSINESS_RULE' 'All rules associated with the transaction', 'ACCOUNT_RULE', sysdate(), sysdate());

-- Account Rule Sets
insert into validationdb.rule_set(rule_set_sk, rule_category_sk, rule_set_id, rule_set_name, rule_set_desc, active, rule_set_impl_name, created_date, updated_date) VALUES ('ab13ce56-5d0d-4e7f-ac14-46fe4008ae86', '812162dd-264f-4756-a750-7dddd469c9b1','19XK4YKQOU7935V','ACCOUNT DEMOGRAPHIC', 'Rules to validate account demographics', true, 'demographicRuleSet', sysdate(), sysdate());
insert into validationdb.rule_set(rule_set_sk, rule_category_sk, rule_set_id, rule_set_name, rule_set_desc, active, rule_set_impl_name, created_date, updated_date) VALUES ('2c7fbb58-650b-4399-b5f4-50ac3b0aea98', '812162dd-264f-4756-a750-7dddd469c9b1','CMD1FNG5Y6K90U3','ENROLLMENT_SPAN', 'Rules to validate enrollment spans', true, 'enrollmentSpanRuleSet', sysdate(), sysdate());

-- Transaction Rule Sets
insert into validationdb.rule_set(rule_set_sk, rule_category_sk, rule_set_id, rule_set_name, rule_set_desc, active, rule_set_impl_name, created_date, updated_date) VALUES ('097bf0a6-ac07-4573-ae42-8a986de677e3', '5f5390cb-6e87-4156-8f6d-8fdcbde1f62d','NIYHVIJRZBYT9JQ','TRANSACTION DEMOGRAPHIC', 'Rules to validate transaction demographics', true, 'demographicRuleSet', sysdate(), sysdate());
insert into validationdb.rule_set(rule_set_sk, rule_category_sk, rule_set_id, rule_set_name, rule_set_desc, active, rule_set_impl_name, created_date, updated_date) VALUES ('3da9c776-7ed1-473a-a92b-737e5f456dba', '5f5390cb-6e87-4156-8f6d-8fdcbde1f62d','9W0HJ1CCM7B2DDJ','RATE VALIDATION', 'Rules to validate transaction rates', true, 'rateRuleSet', sysdate(), sysdate());

-- Account Rules
-- Demographic Rules
insert into validationdb.rule(rule_sk, rule_set_sk, rule_id, rule_name, rule_desc, active, rule_impl_name, member_level, created_date, updated_date) VALUES ('12bb9131-db7b-489d-8068-575dce37f4b0', 'ab13ce56-5d0d-4e7f-ac14-46fe4008ae86', 'JL4SPBGRY6O9FJE', 'DATE_OF_BIRTH', 'Rule to validate members date of birth', true, 'dateOfBirthRule', true, sysdate(), sysdate());
insert into validationdb.rule(rule_sk, rule_set_sk, rule_id, rule_name, rule_desc, active, rule_impl_name, member_level, created_date, updated_date) VALUES ('6ffa0310-5d3e-4af9-ad8f-2e8197e59442', 'ab13ce56-5d0d-4e7f-ac14-46fe4008ae86', '6BIWSQZO9T60F99', 'GENDER', 'Rule to validate members gender', true, 'genderRule', true, sysdate(), sysdate());
-- Enrollment Span rules
insert into validationdb.rule(rule_sk, rule_set_sk, rule_id, rule_name, rule_desc, active, rule_impl_name, member_level, created_date, updated_date) VALUES ('8b39191c-a7b3-4562-9904-3dcf18b609c8', '2c7fbb58-650b-4399-b5f4-50ac3b0aea98', 'U2STMQEAIRE1X0Q', 'ES_OVERLAP', 'Rule to validate if enrollment spans overlap', true, 'enrollmentSpanOverlapRule', false, sysdate(), sysdate());

-- Transaction Rules
-- Demographic Rules
insert into validationdb.rule(rule_sk, rule_set_sk, rule_id, rule_name, rule_desc, active, rule_impl_name, member_level, created_date, updated_date) VALUES ('7a54f033-c17b-44cc-833a-9c619a9fdb9b', '097bf0a6-ac07-4573-ae42-8a986de677e3', 'AU3IUZ2FTSAFAV7', 'DATE_OF_BIRTH', 'Rule to validate members date of birth', true, 'dateOfBirthRule', true, sysdate(), sysdate());
insert into validationdb.rule(rule_sk, rule_set_sk, rule_id, rule_name, rule_desc, active, rule_impl_name, member_level, created_date, updated_date) VALUES ('d7ce973f-65b6-483c-96ae-dc01a9436eef', '097bf0a6-ac07-4573-ae42-8a986de677e3', '84WNR9KAS23XKP3', 'GENDER', 'Rule to validate members gender', true, 'genderRule', true, sysdate(), sysdate());

-- Rate Rules
insert into validationdb.rule(rule_sk, rule_set_sk, rule_id, rule_name, rule_desc, active, rule_impl_name, member_level, created_date, updated_date) VALUES ('a2d3c4b0-5d1a-47cf-abc1-fc5f73f9f18f', '3da9c776-7ed1-473a-a92b-737e5f456dba', '84MBZPRAOZC1NU6', 'PREM_TOT_VALIDATION', 'Rule to validate the premium amount total', true, 'premiumTotalRule', true, sysdate(), sysdate());


-- Rule - Transaction type mapping
insert into validationdb.rule_transaction(rule_transaction_sk, rule_sk, transaction_type_code, created_date, updated_date) VALUES ('3922e842-4163-41e3-bb12-83bb48ca114c', '7a54f033-c17b-44cc-833a-9c619a9fdb9b', 'ADD', sysdate(), sysdate());
insert into validationdb.rule_transaction(rule_transaction_sk, rule_sk, transaction_type_code, created_date, updated_date) VALUES ('a5db7f54-354b-437f-80b6-4129eb41e1f2', '7a54f033-c17b-44cc-833a-9c619a9fdb9b', 'CHANGE', sysdate(), sysdate());

insert into validationdb.rule_transaction(rule_transaction_sk, rule_sk, transaction_type_code, created_date, updated_date) VALUES ('b702cae3-6e19-4493-9e56-91b471abc84a', 'd7ce973f-65b6-483c-96ae-dc01a9436eef', 'ADD', sysdate(), sysdate());
insert into validationdb.rule_transaction(rule_transaction_sk, rule_sk, transaction_type_code, created_date, updated_date) VALUES ('69f3f42f-50bc-42c6-93a0-28d1e983811a', 'd7ce973f-65b6-483c-96ae-dc01a9436eef', 'CHANGE', sysdate(), sysdate());

insert into validationdb.rule_transaction(rule_transaction_sk, rule_sk, transaction_type_code, created_date, updated_date) VALUES ('4d0f2e16-bf16-4af1-90b2-6d0e95eec390', 'a2d3c4b0-5d1a-47cf-abc1-fc5f73f9f18f', 'ADD', sysdate(), sysdate());
insert into validationdb.rule_transaction(rule_transaction_sk, rule_sk, transaction_type_code, created_date, updated_date) VALUES ('e06f038a-7f47-418e-aecb-da150560ff60', 'a2d3c4b0-5d1a-47cf-abc1-fc5f73f9f18f', 'CHANGE', sysdate(), sysdate());
