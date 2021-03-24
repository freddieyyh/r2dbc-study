drop table if exists person;
create table person
(
    id bigint auto_increment,
    firstname varchar(10) not null,
    lastname varchar(50) not null,
    age int not null,
    email varchar(100) null,
    updatedAt datetime default CURRENT_TIMESTAMP not null,
    constraint person_pk
        primary key (id)
);