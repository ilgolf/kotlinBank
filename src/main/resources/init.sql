create table account
(
    account_id       bigint auto_increment not null,
    bank_name        varchar(10)  not null,
    deleted          boolean      not null,
    member_id        bigint,
    account_name     varchar(255),
    number           varchar(30)  not null,
    account_password varchar(200) not null,
    primary key (account_id)
);

create index i_member_id on account (member_id);

create table member
(
    member_id          bigint auto_increment not null,
    created_at         datetime,
    last_modified_date datetime,
    birth              date,
    deleted            boolean      not null,
    email              varchar(255) not null,
    name               varchar(20)  not null,
    nickname           varchar(20)  not null,
    password           varchar(200) not null,
    phone_number       varchar(20)  not null,
    profile_image_url  varchar(100),
    role_type          varchar(255) not null,
    primary key (member_id)
);

create index i_email on member (email);
create index i_nickname on member (nickname)
