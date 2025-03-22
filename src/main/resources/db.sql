create table file(
    id serial primary key,
    name text not null,
    type varchar(10),
    size bigint not null default 0,
    url text not null,
    created_at timestamp not null default current_timestamp,
    updated_at timestamp not null default current_timestamp
);


create database files_db;