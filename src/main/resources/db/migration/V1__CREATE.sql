create table "user"
(
    id            serial primary key,
    username      varchar(64) not null unique,
    password_hash varchar(64) not null
);

create table role
(
    id   serial primary key,
    name varchar(64) not null unique
);

create table user_role
(
    user_id integer references "user" (id) on delete cascade,
    role_id integer references role (id) on delete cascade,
    primary key (user_id, role_id)
);

create table animal_class
(
    id   serial primary key,
    name varchar(64) not null unique
);

create table climate
(
    id           serial primary key,
    name         varchar(64) not null,
    description  text,
    temp_min_avg smallint    not null default -10, -- Minimum of month average in year
    temp_max_avg smallint    not null default 10   -- Maximum of month average in year
);

create table animal
(
    id              serial primary key,
    name            varchar(64) not null unique,
    is_artificial   bool        not null default false, -- Natural animal by default
    climate_id      integer references climate (id) on delete restrict,
    animal_class_id integer references animal_class (id) on delete restrict
);

create table characteristic
(
    id          serial primary key,
    name        varchar(64) not null unique,
    description text,
    coefficient smallint    not null default 0 check (coefficient >= -10 and coefficient <= 10) -- By default characteristic neutral
);

create table characteristic_in_conflict
(
    first_characteristic_id  integer references characteristic (id) on delete cascade,
    second_characteristic_id integer references characteristic (id) on delete cascade check (first_characteristic_id != second_characteristic_id),
    primary key (first_characteristic_id, second_characteristic_id)
);

create table animal_characteristic
(
    id                serial primary key,
    animal_id         integer references animal (id) on delete cascade,
    characteristic_id integer references characteristic (id) on delete cascade
);

create table status
(
    id   serial primary key,
    name varchar(64) not null unique
);

create table experiment
(
    id                serial primary key,
    status_id         integer references status (id) on delete restrict,
    start_date        date           not null default current_date,
    created_animal_id integer unique references animal (id) on delete set null, -- if delete then as 'fail' status
    experimenter_id   integer references "user" (id) on delete restrict
);

create table animal_in_experiment
(
    experiment_id integer references experiment (id) on delete cascade,
    animal_id     integer references animal (id) on delete cascade,
    primary key (experiment_id, animal_id)
);

create table chosen_characteristic
(
    experiment_id     integer references experiment (id) on delete cascade,
    characteristic_id integer references characteristic (id) on delete cascade,
    primary key (experiment_id, characteristic_id)
);

create table reserve
(
    id          serial primary key,
    name        varchar(128) not null unique,
    description text,
    climate_id  integer references climate (id) on delete restrict
);

create table reserve_worker
(
    reserve_id     integer references reserve (id) on delete cascade,
    worker_id      integer unique references "user" (id) on delete cascade,
    available_load smallint not null default 3 check (available_load >= 1 and available_load <= 10), -- Maximum 10 animals to one manager
    primary key (reserve_id, worker_id)
);

create table animal_reserve
(
    id         serial primary key,
    animal_id  integer references animal (id) on delete restrict,
    reserve_id integer references reserve (id) on delete cascade
);

create table report
(
    id          serial primary key,
    animal_id   integer references animal (id) on delete restrict,
    reserve_id  integer references reserve (id) on delete restrict,
    health_rate smallint not null default 5 check (health_rate >= 1 and health_rate <= 5),
    comment     text,
    report_date date     not null default current_date,
    reporter_id integer references "user" (id) on delete restrict,
    unique (animal_id, reserve_id, report_date)
);