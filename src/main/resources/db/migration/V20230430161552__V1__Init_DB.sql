create sequence hibernate_sequence start 2 increment 1;
-- create table keyword
-- (
--     id         int8 not null,
--     keywords   varchar(255),
--     project_id int8,
--     primary key (id)
-- );
create table project
(
    id                       int8 not null,
    name                     varchar(255),
    keywords                 varchar(1000),
    created_at                 timestamp,
    social_media_platform_id int8,
    user_id                  int8,
    primary key (id)
);
create table reddit_data
(
    id                       int8 not null,
    site                     varchar(255),
    sub_body                 varchar(40000),
    sub_date                 timestamp,
    sub_id                   varchar(255),
    sub_title                varchar(300),
    sub_url                  varchar(500),
    social_media_platform_id int8,
    primary key (id)
);
create table sm_platform
(
    id            int8 not null,
    platform_name varchar(255),
    primary key (id)
);
create table twitter_data
(
    id                       int8 not null,
    follower_count           numeric(19, 2),
    friend_count             int4,
    link                     varchar(255),
    listed_count             int4,
    tw_id                    varchar(255),
    tweet                    varchar(500),
    tweeted_at               timestamp,
    username                 varchar(255),
    verification_status      int4,
    social_media_platform_id int8,
    primary key (id)
);
create table user_role
(
    user_id int8 not null,
    roles   varchar(255)
);
create table usr
(
    id       int8 not null,
    email    varchar(255),
    password varchar(255),
    username varchar(255),
    primary key (id)
);
create table yt_data
(
    id                       int8 not null,
    category_id              int4,
    com_id                   varchar(500),
    comment                  varchar(10000),
    hours                    int4,
    likes                    int8,
    minutes                  int4,
    publication_time         varchar(255),
    seconds                  int4,
    sub_count                numeric(19, 2),
    vid_title                varchar(255),
    video_id                 varchar(500),
    view_count               numeric(19, 2),
    social_media_platform_id int8,
    primary key (id)
);
-- alter table if exists keyword
--     add constraint keyword_project_fk foreign key (project_id) references project;
alter table if exists project
    add constraint project_sm_platform_fk foreign key (social_media_platform_id) references sm_platform;
alter table if exists project
    add constraint project_user_fk foreign key (user_id) references usr;
alter table if exists reddit_data
    add constraint reddit_data_sm_platform_fk foreign key (social_media_platform_id) references sm_platform;
alter table if exists twitter_data
    add constraint twitter_data_sm_platform_fk foreign key (social_media_platform_id) references sm_platform;
alter table if exists user_role
    add constraint user_role_user_fk foreign key (user_id) references usr;
alter table if exists yt_data
    add constraint yt_data_sm_platform_fk foreign key (social_media_platform_id) references sm_platform;