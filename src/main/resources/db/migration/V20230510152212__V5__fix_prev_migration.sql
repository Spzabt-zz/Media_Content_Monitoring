alter table public.reddit_data
    alter column sub_subscribers type numeric(19, 2) using sub_subscribers::numeric(19, 2);