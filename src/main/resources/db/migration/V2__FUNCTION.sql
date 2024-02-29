-- Функция считающая вероятность мутации в эксперименте
create or replace function mutation_probability_in_experiment(experiment int)
    returns float4
as
$$
declare
    success_prob float4 := success_probability_in_experiment(experiment);
    result float4 := 0;
    count int2 := 0;
begin
    count = (select count(animal_id) from animal_in_experiment
            where experiment_id = experiment);
    result := round(((1 - success_prob) / (6 - count))::numeric, 2);
    return result;
/* Вероятность мутации = вероятность провала / коэффициент количества животных в эксперименте
   Меньше провалов, больше мутаций!
   Чем больше животных, тем больше мутаций
   Если выбрать 5 животных, вероятность мутации равна вероятности провала
   Вероятность мутации накладывается отдельно от вероятности успеха/провала
 */
end;
$$ language plpgsql;


-- Функция считающая вероятность успеха/провала эксперимента
create or replace function success_probability_in_experiment(experiment int)
    returns float4
as
$$
declare
    result float4 := 0;
    final_coeff int := 0;
    i int;
    coefficients int[];
begin
    coefficients := (select array (select coefficient from
                                   chosen_characteristic join characteristic c on c.id = chosen_characteristic.characteristic_id
                                   where experiment_id = experiment));
    foreach i in array coefficients loop
        final_coeff := final_coeff + i;
        end loop;
    select case
        when final_coeff < 25 and final_coeff > -25 then 0.5 - final_coeff * 0.02
        when final_coeff >= 25 then 0.01
        when final_coeff <= -25 then 0.99
    end into result;
/* складываем все коэффициенты и прибавляем их вес к базовой вероятности 50%
   при отрицательных коэффициентах (плохие характеристики) вероятность выше
   если сумма коэффициентов выходит слишком большой или слишком маленькой (100 и -100) ставлю худший и лучший случай на 1% и 99%
 */
    return round(result::numeric, 2);
end
$$ language plpgsql;


-- Функция валидирующая список животных для экспериментов (от 2 до 5, и животные одного класса)
-- Только после успешной валидации этой функции будет происходить вставка в animal_in_experiment!!!
create or replace function check_animals_for_experiment(animal_ids integer[])
    returns table
            (
                result  boolean,
                message varchar(128)
            )
as
$$
begin
    select false into result;
    if cardinality(animal_ids) >= 2 and cardinality(animal_ids) <= 5 then
        if (select count(distinct animal_class_id) from animal where id = any (animal_ids)) = 1 then
            select true into result;
            select 'Ok' into message;
        else
            select 'selected animals must be of the same class' into message;
        end if;
    else
        select 'number of selected animals must be from 2 to 5' into message;
    end if;
    return next;
end
$$ language plpgsql;


-- Функция перемещающая сотрудника в заповедник?
-- Функция выдающая все характеристики для выбора для выбранных животных в эксперименте (без дубликатов)
create or replace function show_available_to_choose_characteristics(experiment integer)
    returns table
            (
                characteristic_id   integer,
                characteristic_name varchar(64)
            )
as
$$
declare
    animal_ids integer[] := (select array (select animal_id from animal_in_experiment where experiment_id = experiment));
begin
    return query select distinct c.id,
                                 c.name
                 from animal_characteristic ac
                          join characteristic c on c.id = ac.characteristic_id
                 where ac.animal_id = any (animal_ids)
                 order by c.id;
end
$$ language plpgsql;


-- Функция выдающая пары конфликтующих характеристик среди выбранных
create or replace function show_conflicted_characteristics(characteristic_ids integer[])
    returns table
            (
                first  integer,
                second integer
            )
as
$$
begin
    return query select first_characteristic_id,
                        second_characteristic_id
                 from characteristic_in_conflict
                 where first_characteristic_id = any (characteristic_ids)
                   and second_characteristic_id = any (characteristic_ids);
end
$$ language plpgsql;


-- Функция для генерации имени для нового животного по умолчанию
create or replace function generate_default_animal_name(experiment integer)
    returns varchar(64)
as
$$
declare
    animal_ids integer[] := (select array (select animal_id from animal_in_experiment where experiment_id = experiment));
    result varchar(64);
begin
    with animal_names as (select row_number() over ()     rownum,
                                 split_part(name, ' ', 1) animal_name
                          from animal
                          where id = any (animal_ids)),

         transform_names as (select case
                                        when rownum = 1
                                            then regexp_replace(animal_name, '[аяоёэеуюыи]+$', '') -- Если первое слово, то просто убираем гласные в конце
                                        when rownum > 1 and rownum < cardinality(animal_ids)
                                            then regexp_replace(lower(animal_name), '[аяоёэеуюыи]+$', '') -- Для середины еще lower
                                        else lower(animal_name) -- а в конце окончание не трогаем
                                        end name
                             from animal_names)

    select string_agg(name, 'о')::varchar(64) into result-- Если длиннее 64 то просто обрежется
    from transform_names;
    -- add experiment id before name
    select concat(experiment::varchar(64), result)::varchar(64) into result;

    return result;
end
$$ language plpgsql;


-- Функция для генерации иимени мутировавшего животного
create or replace function generate_mutated_animal_name(experiment int)
returns varchar(64)
as
$$
declare
    result text := 'Вид_' || experiment::text || '_';
    i text;
begin
    foreach i in array (select array_agg(a.id::varchar(64) || a.name::varchar(1))
                        from animal a
                                 join animal_in_experiment aie on a.id = aie.animal_id and aie.experiment_id = experiment) loop
        result = result || i;
        end loop;
        return result::varchar(64);
end
$$ language plpgsql;


-- Функция для подбора заповедников по температурам климата животного
create or replace function get_reserves_for_animal_by_temp(animal_id integer)
    returns table
            (
                reserve_id integer,
                reserve_name varchar(128),
                reserve_climate_name varchar(64),
                animal_climate_name varchar(64),
                temp_min_diff smallint,
                temp_max_diff smallint,
                total_temp_diff smallint
            )
as
$$
declare
    animal_climate_id integer := (select climate_id from animal where id = animal_id);
begin
    if animal_climate_id is null then
        raise exception 'animal must have climate';
    end if;

    return query select
                     r.id reserve_id,
                     r.name reserve_name,
                     res_c.name reserve_climate_name,
                     c.name animal_climate_name,
                     abs(res_c.temp_min_avg - c.temp_min_avg) temp_min_diff,
                     abs(res_c.temp_max_avg - c.temp_max_avg) temp_max_diff,
                     abs(res_c.temp_min_avg - c.temp_min_avg) + abs(res_c.temp_max_avg - c.temp_max_avg) total_temp_diff
                 from climate c
                          cross join reserve r
                          join climate res_c on res_c.id = r.climate_id
                 where c.id = animal_climate_id
                 order by total_temp_diff;
end
$$ language plpgsql;


-- Функция для подбора заповедников по названию климата животного
create or replace function get_reserves_for_animal_by_name(animal_id integer)
    returns table
            (
                reserve_id integer,
                reserve_name varchar(128),
                reserve_climate_name varchar(64),
                animal_climate_name varchar(64)
            )
as
$$
declare
    animal_climate_id integer := (select climate_id from animal where id = animal_id);
begin
    if animal_climate_id is null then
        raise exception 'animal must have climate';
    end if;

    return query select
                     r.id reserve_id,
                     r.name reserve_name,
                     res_c.name reserve_climate_name,
                     c.name animal_climate_name
                 from climate c
                          cross join reserve r
                          join climate res_c on res_c.id = r.climate_id
                 where c.id = animal_climate_id and
                     (res_c.name ilike '%' || substr(split_part(c.name, ' ', 1), 1, 5) || '%'
                         or c.name ilike '%' || substr(split_part(res_c.name, ' ', 1), 1, 5) || '%');
end
$$ language plpgsql;


-- Функция создающая частично рандомные характеристики при мутации (могут конфликтовать)
create or replace function get_mutation_characteristics(experiment integer)
    returns integer[]
as
$$
declare
    from_selected_part float4 := 0.5;  -- Какая часть из выбранных хар-к останется
    selected_chars integer[] := (select array (select characteristic_id from chosen_characteristic where experiment_id = experiment));
    -- Часть оставшихся хар-к из выбранных
    selected_mutation_chars integer[] := (select array (select id from characteristic
                                                                  where id = any (selected_chars) order by random()
                                                        limit ceil(cardinality(selected_chars) * from_selected_part)));
    -- Часть рандомных хар-к
    random_chars integer[] := (select array (select c.id from characteristic c
                                                         where not(c.id = any(selected_chars)) order by random()
                                            limit ceil(cardinality(selected_chars) * from_selected_part)));
    result_chars integer[] := array_cat(selected_mutation_chars, random_chars);
begin
    return result_chars;
end
$$ language plpgsql;


-- Функция считающая коэффициент незагруженности заповедника (сколько еще животных можно добавить)
create or replace function reserve_available_load(reserve integer)
    returns integer
as
$$
declare
    available_reserve_load integer := (select sum(available_load) from reserve_worker where reserve_id = reserve);
    reserve_animal_count integer := (select count(animal_id) from animal_reserve where reserve_id = reserve);
begin
    return available_reserve_load - reserve_animal_count;
end
$$ language plpgsql;


-- Функция для подбора свободного сотрудника (или из не сильно нагруженного заповелника)
-- Здесь ищутся только сотрудники с ролями MANAGER и RESERVE_WORKER, сортируются по загруженности, сначала идут свободные
create or replace function get_available_workers()
    returns table
            (
                user_id integer,
                username varchar(64),
                cur_reserve_id integer,
                cur_reserve_name varchar(128),
                reserve_available_load integer
            )
as
$$
declare
    manager_role_id integer := (select id from role where name = 'MANAGER');
    reserve_worker_role_id integer := (select id from role where name = 'RESERVE_WORKER');
begin
    return query select distinct
                     u.id,
                     u.username,
                     r.id,
                     r.name,
                     case when r.id is not null then reserve_available_load(r.id)
                     else 2147483647 end reserve_available_load  -- для свободных сотрудников MAX_INT
                 from "user" u
                          left join reserve_worker rw on u.id = rw.worker_id
                          join user_role ur on u.id = ur.user_id
                          left join reserve r on r.id = rw.reserve_id
                 where ur.role_id = manager_role_id or ur.role_id = reserve_worker_role_id
                 order by reserve_available_load desc;
end
$$ language plpgsql;


-- Функция считающая наиболее удобные климатические условия для животного на основе отчетов
-- если ничего не возвращает, то данных пока недостаточно
create or replace function get_best_climates_by_reports(animal integer)
    returns table
        (
            climate_id integer,
            climate_name varchar(64),
            reports_num bigint,
            avg_health_rate numeric
        )
as
$$
declare
    min_reports_num integer := 5; -- Минимум по климату должно быть столько отчетов
begin
    return query select
                     c.id,
                     c.name,
                     count(r.health_rate) reports_num,
                     round(avg(r.health_rate), 2) avg_health_rate
                 from report r
                          join reserve res on res.id = r.reserve_id
                          join climate c on res.climate_id = c.id
                 where r.animal_id = animal
                 group by c.id
                 having count(r.health_rate) >= min_reports_num
                 order by avg_health_rate desc; -- Сортируем по наивысшей средней оценке
end
$$ language plpgsql;


-- Функция, которая показывает историю экспериментов по фильтрам и ставит дефолтные параметры если что
create or replace function get_experiment_history(status_id_param integer,
                                                  start_date date,
                                                  end_date date,
                                                  animal_ids_param integer[],
                                                  experimenter_id_param integer)
    returns table
            (
                experiment_id integer,
                status_id integer,
                experiment_date date,
                created_animal_id integer, -- в дальнейшем на бэке еще будут добираться хар-ки, это чтобы просто видно было
                experimenter_id integer
            )
as
$$
declare
    start_date_param date := (select coalesce(start_date, '1900-01-01'));
    end_date_param date := (select coalesce(end_date, '2100-01-01'));
begin
    return query select e.id, e.status_id, e.start_date, e.created_animal_id, e.experimenter_id from experiment e
                            join animal_in_experiment aie on e.id = aie.experiment_id and
                                                (animal_ids_param is null or cardinality(animal_ids_param) = 0 or aie.animal_id = any (animal_ids_param))
                 where (status_id_param is null or e.status_id = status_id_param) and
                     (e.start_date >= start_date_param and e.start_date <= end_date_param) and
                     (experimenter_id_param is null or e.experimenter_id = experimenter_id_param)
                 group by e.id;
end
$$ language plpgsql;