CREATE DATABASE IF NOT EXISTS task_db;
USE task_db;

CREATE TABLE IF NOT EXISTS user
(
    id         bigint primary key auto_increment,
    email      varchar(256) not null unique,
    first_name varchar(24)  not null,
    last_name  varchar(24)  not null,
    password   varchar(60)  not null
);

CREATE TABLE IF NOT EXISTS task
(
    id          bigint primary key auto_increment,
    title       varchar(50)   not null,
    description varchar(2500) not null,
    status      varchar(20)   not null,
    priority    varchar(20)   not null,
    created_at  timestamp     not null default current_timestamp,
    updated_at  timestamp              default null,
    assigned_at timestamp,
    author_id   bigint        not null references user (id) on delete set null,
    assignee_id bigint        references user (id) on delete set null
);

CREATE TABLE IF NOT EXISTS comment
(
    id         bigint primary key auto_increment,
    text       varchar(400) not null,
    created_at timestamp    not null default current_timestamp,
    author_id  bigint       not null references user (id) on delete cascade,
    task_id    bigint       not null references task (id) on delete cascade
);



