
drop database if exists testingtrigger;
create database testingtrigger;
use testingtrigger;

create table test (
	id int primary key,
	name varchar(255),
	tobedeleted varchar(1) default 'N'
);

delimiter $$

create trigger test_trigger before insert on test for each row
begin
delete from test where tobedeleted = 'Y';
end;$$

delimiter ;

insert into test values (1,'blabla','N');

select * from test;
