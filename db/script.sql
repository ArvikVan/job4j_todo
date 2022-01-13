create table if not exists items (
    id serial primary key,
    description text,
    created TIMESTAMP,
    done boolean
);
create table if not exists j_role (
    id serial primary key,
    name varchar(2000)
);
create table if not exists j_user (
    id serial primary key,
    name varchar(2000),
    role_id int not null references j_role(id)
);
create table if not exists user_item (
    id serial primary key,
    name varchar(2000),
    item_id int not null references items(id),
    password varchar(200)
);