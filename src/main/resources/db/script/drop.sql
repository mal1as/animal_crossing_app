drop table if exists "user", role, user_role, animal_class, climate, animal, characteristic,
    characteristic_in_conflict, animal_characteristic, status, experiment, animal_in_experiment,
    chosen_characteristic, reserve, reserve_worker, animal_reserve, report cascade;

-- генератор запросов для дропа функций
-- вот это проще для дропа функций использовать, только динамически бы это сделать...
SELECT 'DROP FUNCTION ' || ns.nspname || '.' || proname || '(' || oidvectortypes(proargtypes) || ');'
FROM pg_proc INNER JOIN pg_namespace ns ON (pg_proc.pronamespace = ns.oid)
WHERE ns.nspname in ('s285572', 's284703') order by ns.nspname;