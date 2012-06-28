alter table period add column cancelled_due_to_debt_clearance boolean not null default false;
alter table period add column interest_amount numeric(19, 2) not null default 0.00;
alter table period add column interest_outstanding numeric(19, 2) not null default 0.00;
alter table period add column interest_paid boolean not null default false;

alter table loan_type add column interest_processor varchar(255) not null default '';
alter table loan_type add column period_generator_processor varchar(255) not null default '';
alter table loan_type add column period_processor varchar(255) not null default '';

update loan_type set interest_processor = processor where interest_processor = '';
update loan_type set period_generator_processor = processor where period_generator_processor = '';
update loan_type set period_processor = processor where period_processor = '';

INSERT INTO public.loan_type (id, name, processor, interest_rate, max_interest_rate, must_keep_advanced_interest, number_of_period, interest_processor, period_processor, period_generator_processor, version, cooperative_share)
VALUES (nextval('hibernate_sequence'), 'เงินกู้ด่วน (ปรับปรุงใหม่)', 'Effective', 12.00, 18.00, false, 3, 'Effective', 'ExpressCash01', 'ExpressCash01', 0, 0.00);

alter table contract add column interest_processor varchar(255) not null default '';
alter table contract add column period_generator_processor varchar(255) not null default '';
alter table contract  add column period_processor varchar(255) not null default '';

update contract set interest_processor = processor where interest_processor = '';
update contract set period_generator_processor = processor where period_generator_processor = '';
update contract set period_processor = processor where period_processor = '';

alter table member add column credit_union_member_id bigint;
alter table member add column credit_union_member_no character varying(255);
alter table member add column member_no character varying(255)  not null default '';