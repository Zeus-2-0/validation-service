insert into validationdb.rule_category(rule_category_sk, rule_category_id, rule_category_name, rule_category_desc, created_date, updated_date) VALUES ('812162dd-264f-4756-a750-7dddd469c9b1', 'W317DVAVX8XQDMR', 'ACCOUNT', 'All rules associated with the account',sysdate(), sysdate());

insert into validationdb.rule_set(rule_set_sk, rule_category_sk, rule_set_id, rule_set_name, rule_set_desc, active, rule_set_impl_name, created_date, updated_date) VALUES ('ab13ce56-5d0d-4e7f-ac14-46fe4008ae86', '812162dd-264f-4756-a750-7dddd469c9b1','19XK4YKQOU7935V','DEMOGRAPHIC', 'Rules to validate demographics', true, 'demographicRuleSet', sysdate(), sysdate());
insert into validationdb.rule_set(rule_set_sk, rule_category_sk, rule_set_id, rule_set_name, rule_set_desc, active, rule_set_impl_name, created_date, updated_date) VALUES ('2c7fbb58-650b-4399-b5f4-50ac3b0aea98', '812162dd-264f-4756-a750-7dddd469c9b1','CMD1FNG5Y6K90U3','ENROLLMENT_SPAN', 'Rules to validate enrollment spans', true, 'enrollmentSpanRuleSet', sysdate(), sysdate());

insert into validationdb.rule(rule_sk, rule_set_sk, rule_id, rule_name, rule_desc, active, rule_impl_name, created_date, updated_date) VALUES ('12bb9131-db7b-489d-8068-575dce37f4b0', 'ab13ce56-5d0d-4e7f-ac14-46fe4008ae86', 'JL4SPBGRY6O9FJE', 'DATE_OF_BIRTH', 'Rule to validate members date of birth', true, 'dateOfBirthRule', sysdate(), sysdate());
insert into validationdb.rule(rule_sk, rule_set_sk, rule_id, rule_name, rule_desc, active, rule_impl_name, created_date, updated_date) VALUES ('6ffa0310-5d3e-4af9-ad8f-2e8197e59442', 'ab13ce56-5d0d-4e7f-ac14-46fe4008ae86', '6BIWSQZO9T60F99', 'GENDER', 'Rule to validate members gender', true, 'genderRule', sysdate(), sysdate());
insert into validationdb.rule(rule_sk, rule_set_sk, rule_id, rule_name, rule_desc, active, rule_impl_name, created_date, updated_date) VALUES ('8b39191c-a7b3-4562-9904-3dcf18b609c8', '2c7fbb58-650b-4399-b5f4-50ac3b0aea98', 'U2STMQEAIRE1X0Q', 'ES_OVERLAP', 'Rule to validate if enrollment spans overlap', true, 'enrollmentSpanOverlapRule', sysdate(), sysdate());