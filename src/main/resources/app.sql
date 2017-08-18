create database kingsbury
    with
    owner = postgres
    encoding = 'UTF8'
    connection limit = -1;

create schema task_tracker;

create table  task_tracker.category (
  id serial primary key,
  name varchar(50) not null
);

create table  task_tracker.user_role (
  id serial primary key,
  name varchar(50) not null
);

create table task_tracker.user (
  id serial primary key,
  email varchar(100) not null,
  password varchar(100),
  role_id integer references task_tracker.user_role on delete cascade,
  phone varchar(10),
  first_name varchar(50) not null,
  last_name varchar(50) not null,
  facebook varchar(100),
  recognition_opt_in boolean,
  last_login timestamp,
  deleted boolean,
  invitation_key uuid,
  CONSTRAINT user_email_key UNIQUE (email)
);

create table  task_tracker.user_category (
  user_id integer references task_tracker.user on delete cascade,
  category_id integer references task_tracker.category on delete cascade,
  deleted boolean,
  date_added timestamp,
  user_added integer references task_tracker.user on delete cascade,
  user_modified integer references task_tracker.user on delete cascade,
  date_modified timestamp,
  primary key (user_id, category_id)
);

create table  task_tracker.partner (
  id serial primary key,
  email varchar(100),
  phone varchar(10),
  name varchar(100) not null,
  contact_first_name varchar(50),
  contact_last_name varchar(50),
  facebook varchar(100),
  recognition_opt_in boolean,
  deleted boolean,
  date_added timestamp,
  user_added integer references task_tracker.user on delete cascade,
  user_modified integer references task_tracker.user on delete cascade,
  date_modified timestamp
);

create table  task_tracker.task_status (
  id serial primary key,
  name varchar(20) not null
);

create table  task_tracker.task (
  id serial primary key,
  name varchar(50) not null,
  date timestamp,
  description text,
  status integer references task_tracker.task_status on delete cascade,
  deleted boolean,
  date_added timestamp,
  user_added integer references task_tracker.user on delete cascade,
  user_modified integer references task_tracker.user on delete cascade,
  date_modified timestamp
);

create table  task_tracker.task_category (
  id serial primary key,
  task_id integer references task_tracker.task on delete cascade,
  category_id integer references task_tracker.category on delete cascade,
  deleted boolean,
  date_added timestamp,
  user_added integer references task_tracker.user on delete cascade,
  user_modified integer references task_tracker.user on delete cascade,
  date_modified timestamp
);

create table task_tracker.task_user_status (
    task_user_status_id serial primary key,
    task_user_status_name varchar(50) not null
);

create table  task_tracker.task_user (
  task_id integer references task_tracker.task on delete cascade,
  user_id integer references task_tracker.user on delete cascade,
  status_id integer references task_tracker.task_user_status on delete cascade,
  date_added timestamp,
  user_added integer references task_tracker.user on delete cascade,
  user_modified integer references task_tracker.user on delete cascade,
  date_modified timestamp,
  primary key (task_id, user_id)
);

create table  task_tracker.task_partner (
  task_id integer references task_tracker.task on delete cascade,
  partner_id integer references task_tracker.partner on delete cascade,
  deleted boolean,
  date_added timestamp,
  user_added integer references task_tracker.user on delete cascade,
  user_modified integer references task_tracker.user on delete cascade,
  date_modified timestamp,
  primary key (task_id, partner_id)
);

create view task_tracker.user_role_name  as
  select u.email, ur.name as role_name
  from task_tracker.user u
       join task_tracker.user_role ur on u.role_id = ur.id;

insert into task_tracker.task_status(name) values('Not Started');
insert into task_tracker.task_status(name) values('In Progress');
insert into task_tracker.task_status(name) values('Complete');

insert into task_tracker.task_user_status(task_user_status_name) values('Invited');
insert into task_tracker.task_user_status(task_user_status_name) values('Added');
insert into task_tracker.task_user_status(task_user_status_name) values('Removed');

insert into task_tracker.user_role(name) values('admin');
insert into task_tracker.user_role(name) values('standard');

insert into task_tracker.user(email, password, role_id, phone, first_name, last_name, recognition_opt_in) values ('briancherron@gmail.com', '16d7a4fca7442dda3ad93c9a726597e4', 2, '5178799919', 'Brian', 'Herron', true);


create table task_tracker.audience (
    id serial primary key,
    name varchar(50) not null
);
insert into task_tracker.audience(id, name) values (1, 'Administrators');
insert into task_tracker.audience(id, name) values (2, 'Volunteers');

alter table task_tracker.task add column audience_id integer, add constraint audience_id_fk foreign key (audience_id) references task_tracker.audience(id);

update task_tracker.task set audience_id = 2;

alter table task_tracker.task alter column name type varchar(100);
alter table task_tracker.task add notes text;
update task_tracker.task set notes = '';
update task_tracker.audience set name = 'Volunteers' where id = 2;

create table task_tracker.comment (
	id serial primary key,
	task_id integer references task_tracker.task on delete cascade,
	comment text not null,
	user_added integer references task_tracker.user on delete cascade,
	date_added timestamp,
	date_edited timestamp,
    deleted boolean
);