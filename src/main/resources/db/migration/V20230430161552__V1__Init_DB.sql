create sequence hibernate_sequence start 2 increment 1;

create table analyse_data
(
    id                         int8 not null,
    reach_analysis             varchar,
    sentiment_data_chart       varchar,
    sentiment_pie_graph        varchar,
    total_mentions_count_chart varchar,
    word_cloud_generation      varchar,
    primary key (id)
);
create table api_credentials
(
    id                          int8 not null,
    reddit_client_id            varchar(1000),
    reddit_client_secret        varchar(1000),
    twitter_access_token        varchar(1000),
    twitter_access_token_secret varchar(1000),
    twitter_consumer_key        varchar(1000),
    twitter_consumer_secret     varchar(1000),
    yt_api_key                  varchar(1000),
    primary key (id)
);
-- create table api_credentials
-- (
--     id                          int8 not null,
--     reddit_client_id            varchar(1000),
--     reddit_client_secret        varchar(1000),
--     twitter_access_token        varchar(1000),
--     twitter_access_token_secret varchar(1000),
--     twitter_consumer_key        varchar(1000),
--     twitter_consumer_secret     varchar(1000),
--     yt_api_key                  varchar(1000),
--     user_id int8,
--     primary key (id)
-- );
create table comparison
(
    id         int8 not null,
    project_id int8,
    user_id    int8,
    primary key (id)
);
create table project
(
    id                       int8 not null,
    created_at               timestamp,
    keywords                 varchar(1000),
    name                     varchar(255),
    social_media_platform_id int8,
    user_id                  int8,
    primary key (id)
);
-- create table keywords
-- (
--     id                       int8 not null,
--     keywords                 varchar(1000),
--     project_id                  int8,
--     primary key (id)
-- );
create table reddit_data
(
    id                       int8 not null,
    sentiment                varchar(255),
    site                     varchar(1000),
    sub_body                 varchar(5000),
    sub_date                 timestamp,
    sub_id                   varchar(255),
    sub_subscribers          numeric(19, 2),
    sub_title                varchar(1000),
    sub_url                  varchar(1000),
    ups                      int8,
    social_media_platform_id int8,
    primary key (id)
);
create table sm_platform
(
    id              int8 not null,
    platform_name   varchar(255),
    analyse_data_id int8,
    primary key (id)
);
create table twitter_data
(
    id                       int8 not null,
    favorite_count           int8,
    follower_count           numeric(19, 2),
    friend_count             int4,
    link                     varchar(500),
    listed_count             int4,
    retweet_count            int8,
    sentiment                varchar(255),
    tw_id                    varchar(500),
    tweet                    varchar(4001),
    tweeted_at               timestamp,
    username                 varchar(500),
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
    id                 int8 not null,
    email              varchar(255),
    password           varchar(255),
    username           varchar(255),
    api_credentials_id int8,
    primary key (id)
);
-- create table usr
-- (
--     id                 int8 not null,
--     email              varchar(255),
--     password           varchar(255),
--     username           varchar(255),
--     primary key (id)
-- );
create table yt_data
(
    id                       int8 not null,
    category_id              int4,
    com_id                   varchar(1000),
    comment                  varchar(10000),
    hours                    int4,
    likes                    int8,
    minutes                  int4,
    publication_time         varchar(255),
    seconds                  int4,
    sentiment                varchar(255),
    sub_count                numeric(19, 2),
    vid_title                varchar(1000),
    video_id                 varchar(1000),
    view_count               numeric(19, 2),
    view_count_of_video      numeric(19, 2),
    social_media_platform_id int8,
    primary key (id)
);
alter table if exists comparison
    add constraint comparison_project_fk foreign key (project_id) references project;
alter table if exists comparison
    add constraint comparison_user_fk foreign key (user_id) references usr;
alter table if exists project
    add constraint project_sm_platform_fk foreign key (social_media_platform_id) references sm_platform;
alter table if exists project
    add constraint project_user_fk foreign key (user_id) references usr;
alter table if exists reddit_data
    add constraint reddit_data_sm_platform_fk foreign key (social_media_platform_id) references sm_platform;
alter table if exists sm_platform
    add constraint sm_platform_analyse_data_fk foreign key (analyse_data_id) references analyse_data;
alter table if exists twitter_data
    add constraint twitter_data_sm_platform_fk foreign key (social_media_platform_id) references sm_platform;
alter table if exists user_role
    add constraint user_role_usr_fk foreign key (user_id) references usr;
alter table if exists usr
    add constraint user_api_credentials_fk foreign key (api_credentials_id) references api_credentials;
-- alter table if exists api_credentials
--     add constraint api_credentials_user_fk foreign key (user_id) references usr;
alter table if exists yt_data
    add constraint yt_data_sm_platform_fk foreign key (social_media_platform_id) references sm_platform;