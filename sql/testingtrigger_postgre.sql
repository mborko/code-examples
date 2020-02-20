
drop database if exists testingtrigger;
create database testingtrigger;
\c testingtrigger;

drop table if exists test;
create table test (
	id int primary key,
	name varchar(255),
	tobedeleted varchar(1) default 'N'
);

create or replace function refresh_test() returns trigger as $refresh_test$
	begin
		delete from test where tobedeleted='Y';
		return null;
	end;
$refresh_test$ language plpgsql;

drop trigger if exists test_trigger on test;
create trigger test_trigger after insert or delete on test
for each row execute procedure refresh_test();

insert into test values (1,'blabla','N');
insert into test values (2,'blabla','N');
insert into test values (3,'blabla','N');
insert into test values (4,'blabla','N');

select * from test;
update test set tobedeleted = 'Y' where id = 1;
select * from test;
insert into test values (5,'blabla','N');
select * from test;
