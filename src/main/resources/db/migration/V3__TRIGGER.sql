-- Триггер для проверки дубликатов в таблице конфликтующих характеристик
create or replace function check_characteristic_in_conflict_duplicates() returns trigger as $$
declare
    has_duplicate boolean := (select true from characteristic_in_conflict
                                           where first_characteristic_id = new.second_characteristic_id
                                             and second_characteristic_id = new.first_characteristic_id);
begin
    if has_duplicate then
        raise exception 'these characteristics already in conflict, skip creating duplicates...';
    end if;
    return new;
end
$$ language plpgsql;

create trigger check_characteristic_in_conflict_duplicates before insert on characteristic_in_conflict
    for each row execute procedure check_characteristic_in_conflict_duplicates();


-- Триггер для проверки роли экспериментатора в таблице экспериментов (допустимые роли: ADMIN, SCIENTIST)
create or replace function check_experimenter_role() returns trigger as $$
declare
    admin_role_id integer := (select id from role where name = 'ADMIN');
    scientist_role_id integer := (select id from role where name = 'SCIENTIST');
begin
    if not exists(select 1 from user_role where user_id = new.experimenter_id and role_id = admin_role_id) and
       not exists(select 1 from user_role where user_id = new.experimenter_id and role_id = scientist_role_id) then
        raise exception 'experimenter must have role SCIENTIST or ADMIN';
    end if;
    return new;
end
$$ language plpgsql;

create trigger check_experimenter_role before insert on experiment
    for each row execute procedure check_experimenter_role();


-- Триггер для проверки роли сотрудника, заполняющего отчет (допустимые роли: ADMIN, MANAGER)
create or replace function check_reporter_role() returns trigger as $$
declare
    admin_role_id integer := (select id from role where name = 'ADMIN');
    manager_role_id integer := (select id from role where name = 'MANAGER');
begin
    if not exists(select 1 from user_role where user_id = new.reporter_id and role_id = admin_role_id) and
       not exists(select 1 from user_role where user_id = new.reporter_id and role_id = manager_role_id) then
        raise exception 'reporter must have role MANAGER or ADMIN';
    end if;
    return new;
end
$$ language plpgsql;

create trigger check_reporter_role before insert on report
    for each row execute procedure check_reporter_role();


-- Триггер для проверки того, что животное не имеет конфликтующих характеристик
create or replace function check_animal_conflicted_characteristics() returns trigger as $$
begin
    if exists(select 1 from characteristic_in_conflict cic
                                join animal_characteristic ac on ac.animal_id = new.animal_id
              where cic.first_characteristic_id = ac.characteristic_id and cic.second_characteristic_id = new.characteristic_id or
                          cic.first_characteristic_id = new.characteristic_id and cic.second_characteristic_id = ac.characteristic_id) then
        raise exception 'you cannot insert characteristic that conflicts with existing';
    end if;
    return new;
end
$$ language plpgsql;

create trigger check_animal_conflicted_characteristics before insert on animal_characteristic
    for each row execute procedure check_animal_conflicted_characteristics();


-- Триггер считающий хватает ли людей на животных в заповеднике
create or replace function check_available_load_in_reserve() returns trigger as $$
begin
    if (select reserve_available_load(new.reserve_id)) < 1 then
        raise exception 'not enough workers in reserve, add worker and repeat please...';
    end if;
    return new;
end
$$ language plpgsql;

create trigger check_available_load_in_reserve before insert on animal_reserve
    for each row execute procedure check_available_load_in_reserve();


