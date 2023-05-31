alter table public.api_credentials
    add reddit_username varchar(1000);
alter table public.api_credentials
    add reddit_password varchar(1000);
alter table public.api_credentials
    add reddit_user_agent varchar(1000);