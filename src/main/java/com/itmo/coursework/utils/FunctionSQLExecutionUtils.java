package com.itmo.coursework.utils;

import com.google.common.collect.ImmutableMap;
import com.itmo.coursework.dto.*;
import com.itmo.coursework.exception.CourseworkSQLBehaviourException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.io.Serializable;
import java.sql.Array;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class FunctionSQLExecutionUtils {

    public static ValidationResultDTO checkAnimalsForExperiment(NamedParameterJdbcTemplate jdbcTemplate, Integer[] animalIds) {
        String sql = "select * from check_animals_for_experiment(:animalIds)";
        Map<String, Integer[]> queryParams = ImmutableMap.of("animalIds", Objects.requireNonNull(animalIds));
        return jdbcTemplate.query(
                sql,
                queryParams,
                (rs) -> {
                    if (!rs.next()) {
                        throw new CourseworkSQLBehaviourException();
                    }
                    return ValidationResultDTO.builder()
                            .result(rs.getBoolean("result")).message(rs.getString("message")).build();
                });
    }

    public static double calculateMutationProbability(NamedParameterJdbcTemplate jdbcTemplate, Long experimentId) {
        String sql = "select * from mutation_probability_in_experiment(:experimentId)";
        Map<String, Integer> queryParams = ImmutableMap.of("experimentId", Objects.requireNonNull(experimentId).intValue());
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, queryParams, Double.class))
                .orElseThrow(CourseworkSQLBehaviourException::new);
    }

    public static double calculateSuccessProbability(NamedParameterJdbcTemplate jdbcTemplate, Long experimentId) {
        String sql = "select * from success_probability_in_experiment(:experimentId)";
        Map<String, Integer> queryParams = ImmutableMap.of("experimentId", Objects.requireNonNull(experimentId).intValue());
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, queryParams, Double.class))
                .orElseThrow(CourseworkSQLBehaviourException::new);
    }

    public static List<AvailableToChooseCharacteristicDTO> getAvailableToChooseCharacteristics(NamedParameterJdbcTemplate jdbcTemplate, Long experimentId) {
        String sql = "select * from show_available_to_choose_characteristics(:experimentId)";
        Map<String, Integer> queryParams = ImmutableMap.of("experimentId", Objects.requireNonNull(experimentId).intValue());
        return jdbcTemplate.query(
                sql,
                queryParams,
                (rs, rowNum) -> AvailableToChooseCharacteristicDTO.builder()
                        .characteristicId(rs.getLong("characteristic_id"))
                        .characteristicName(rs.getString("characteristic_name"))
                        .build());
    }

    public static List<ConflictedCharacteristicDTO> getConflictedCharacteristics(NamedParameterJdbcTemplate jdbcTemplate, Integer[] characteristicIds) {
        String sql = "select * from show_conflicted_characteristics(:characteristicIds)";
        Map<String, Integer[]> queryParams = ImmutableMap.of("characteristicIds", Objects.requireNonNull(characteristicIds));
        return jdbcTemplate.query(
                sql,
                queryParams,
                (rs, rowNum) -> ConflictedCharacteristicDTO.builder()
                        .firstCharId(rs.getLong("first"))
                        .secondCharId(rs.getLong("second"))
                        .build());
    }

    public static String generateDefaultAnimalName(NamedParameterJdbcTemplate jdbcTemplate, Long experimentId) {
        String sql = "select * from generate_default_animal_name(:experimentId)";
        Map<String, Integer> queryParams = ImmutableMap.of("experimentId", Objects.requireNonNull(experimentId).intValue());
        return jdbcTemplate.queryForObject(sql, queryParams, String.class);
    }

    public static String generateMutatedAnimalName(NamedParameterJdbcTemplate jdbcTemplate, Long experimentId) {
        String sql = "select * from generate_mutated_animal_name(:experimentId)";
        Map<String, Integer> queryParams = ImmutableMap.of("experimentId", Objects.requireNonNull(experimentId).intValue());
        return jdbcTemplate.queryForObject(sql, queryParams, String.class);
    }

    public static List<ReserveClimateForAnimalInfoDTO> getReservesForAnimalByTemp(NamedParameterJdbcTemplate jdbcTemplate, Long animalId) {
        String sql = "select * from get_reserves_for_animal_by_temp(:animalId)";
        Map<String, Integer> queryParams = ImmutableMap.of("animalId", Objects.requireNonNull(animalId).intValue());
        return jdbcTemplate.query(
                sql,
                queryParams,
                (rs, rowNum) -> ReserveClimateForAnimalInfoDTO.builder()
                        .reserveId(rs.getLong("reserve_id"))
                        .reserveName(rs.getString("reserve_name"))
                        .reserveClimateName(rs.getString("reserve_climate_name"))
                        .animalClimateName(rs.getString("animal_climate_name"))
                        .tempMinDiff(rs.getInt("temp_min_diff"))
                        .tempMaxDiff(rs.getInt("temp_max_diff"))
                        .totalTempDiff(rs.getInt("total_temp_diff"))
                        .build()
        );
    }

    public static List<ReserveClimateForAnimalInfoDTO> getReservesForAnimalByName(NamedParameterJdbcTemplate jdbcTemplate, Long animalId) {
        String sql = "select * from get_reserves_for_animal_by_name(:animalId)";
        Map<String, Integer> queryParams = ImmutableMap.of("animalId", Objects.requireNonNull(animalId).intValue());
        return jdbcTemplate.query(
                sql,
                queryParams,
                (rs, rowNum) -> ReserveClimateForAnimalInfoDTO.builder()
                        .reserveId(rs.getLong("reserve_id"))
                        .reserveName(rs.getString("reserve_name"))
                        .reserveClimateName(rs.getString("reserve_climate_name"))
                        .animalClimateName(rs.getString("animal_climate_name"))
                        .build()
        );
    }

    public static List<Integer> generateMutationCharacteristics(NamedParameterJdbcTemplate jdbcTemplate, Long experimentId) {
        try {
            String sql = "select * from get_mutation_characteristics(:experimentId)";
            Map<String, Integer> queryParams = ImmutableMap.of("experimentId", Objects.requireNonNull(experimentId).intValue());
            Array array = jdbcTemplate.queryForObject(sql, queryParams, Array.class);
            return Arrays.asList((Integer[]) Objects.requireNonNull(array).getArray());
        } catch (SQLException | NullPointerException e) {
            throw new CourseworkSQLBehaviourException(e.getMessage());
        }
    }

    public static Integer getReserveAvailableLoad(NamedParameterJdbcTemplate jdbcTemplate, Long reserveId) {
        String sql = "select * from reserve_available_load(:reserveId)";
        Map<String, Integer> queryParams = ImmutableMap.of("reserveId", Objects.requireNonNull(reserveId).intValue());
        return jdbcTemplate.queryForObject(sql, queryParams, Integer.class);
    }

    public static List<AvailableWorkerDTO> getAvailableWorkers(NamedParameterJdbcTemplate jdbcTemplate) {
        String sql = "select * from get_available_workers()";
        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> AvailableWorkerDTO.builder()
                        .userId(rs.getLong("user_id"))
                        .username(rs.getString("username"))
                        .curReserveId(rs.getLong("cur_reserve_id"))
                        .curReserveName(rs.getString("cur_reserve_name"))
                        .reserveAvailableLoad(rs.getInt("reserve_available_load"))
                        .build()
        );
    }

    public static List<BestClimateByReportsDTO> getBestClimatesByReports(NamedParameterJdbcTemplate jdbcTemplate, Long animalId) {
        String sql = "select * from get_best_climates_by_reports(:animalId)";
        Map<String, Integer> queryParams = ImmutableMap.of("animalId", Objects.requireNonNull(animalId).intValue());
        return jdbcTemplate.query(
                sql,
                queryParams,
                (rs, rowNum) -> BestClimateByReportsDTO.builder()
                        .climateId(rs.getLong("climate_id"))
                        .climateName(rs.getString("climate_name"))
                        .reportsNum(rs.getLong("reports_num"))
                        .avgHealthRate(rs.getDouble("avg_health_rate"))
                        .build()
        );
    }

    public static List<ExperimentHistoryDTO> getExperimentHistory(NamedParameterJdbcTemplate jdbcTemplate, Long statusId, LocalDate startDate,
                                                                  LocalDate endDate, Integer[] animalIds, Long experimenterId) {
        String sql = "select * from get_experiment_history(:statusId, :start_date, :end_date, :animalIds, :experimenterId)";
        Map<String, Serializable> queryParams = new HashMap<>();
        queryParams.put("statusId", Optional.ofNullable(statusId).map(Long::intValue).orElse(null));
        queryParams.put("start_date", startDate);
        queryParams.put("end_date", endDate);
        queryParams.put("animalIds", animalIds);
        queryParams.put("experimenterId", Optional.ofNullable(experimenterId).map(Long::intValue).orElse(null));

        return jdbcTemplate.query(
                sql,
                queryParams,
                (rs, rowNum) -> ExperimentHistoryDTO.builder()
                        .experimentId(rs.getLong("experiment_id"))
                        .statusId(rs.getLong("status_id"))
                        .experimentDate(rs.getDate("experiment_date").toLocalDate())
                        .createdAnimalId(rs.getLong("created_animal_id"))
                        .experimenterId(rs.getLong("experimenter_id"))
                        .build()
        );
    }
}
