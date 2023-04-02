drop table if exists account CASCADE;
drop table if exists transfer_history CASCADE;
drop table if exists member CASCADE;

create table account
(
    account_id         bigint auto_increment not null,
    bank_name          varchar(10)           not null,
    deleted            boolean               not null,
    fin_account        varchar(120)          not null,
    register_number    varchar(120)          not null,
    member_id          bigint,
    account_name       varchar(255),
    number             varchar(30)           not null,
    account_password   varchar(200)          not null,
    created_at         datetime,
    last_modified_date datetime,
    created_by         bigint,
    last_modified_by   bigint,

    primary key (account_id)
);

create index i_member_id on account (member_id);

create table member
(
    member_id          bigint auto_increment not null,
    created_at         datetime,
    last_modified_date datetime,
    birth              date,
    deleted            boolean               not null,
    email              varchar(255)          not null,
    name               varchar(20)           not null,
    nickname           varchar(20)           not null,
    password           varchar(200)          not null,
    phone_number       varchar(20)           not null,
    profile_image_url  varchar(100),
    role_type          varchar(255)          not null,
    primary key (member_id)
);

create index i_email on member (email);
create index i_nickname on member (nickname);

create table transfer_history
(
    transfer_history_id bigint auto_increment not null,
    bank_id             bigint                not null,
    client_id           bigint                not null,
    transfer_money      numeric(19, 2)        not null,
    transfer_status     varchar(30)           not null,
    result_message      varchar(255)          not null,
    last_modified_date  datetime,
    created_at          datetime,
    created_by          bigint,
    last_modified_by    bigint,
    primary key (transfer_history_id)
);

create index i_bank_id on transfer_history (bank_id);
create index i_client_id on transfer_history (client_id);