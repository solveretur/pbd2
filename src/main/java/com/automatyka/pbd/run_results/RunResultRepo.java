package com.automatyka.pbd.run_results;

import com.automatyka.pbd.event.EventRank;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class RunResultRepo {
    private static final String GET_RUN_RESULTS_BY_ATHLETE_ID = "SELECT rt.run_type,r.result_time_in_seconds, r.event_id, e.occur_date, e.town, er.event_rank, s.year, st.season_type FROM run_results AS r JOIN events AS e ON r.event_id = e.event_id JOIN event_ranks_dict AS er ON e.event_rank_id = er.event_rank_id JOIN seasons AS s ON e.season_id=s.season_id JOIN season_types_dict AS st ON s.season_type_id=st.season_type_id JOIN run_types_dict AS rt ON r.run_type_id = rt.run_type_id WHERE r.athlete_id=? ORDER BY e.occur_date DESC";
    private static final String GET_BEST_RUN_RESULTS_BY_ATHLETE_ID = "SELECT rt.run_type, t1.result_time_in_seconds, r.event_id, e.occur_date, e.town, er.event_rank, s.year, st.season_type FROM (SELECT r.run_type_id, r.athlete_id, min(r.result_time_in_seconds) AS result_time_in_seconds FROM run_results AS r GROUP BY r.athlete_id, r.run_type_id) AS t1 INNER JOIN run_results AS r ON t1.run_type_id=r.run_type_id AND t1.result_time_in_seconds = r.result_time_in_seconds JOIN events AS e ON r.event_id = e.event_id JOIN event_ranks_dict AS er ON e.event_rank_id = er.event_rank_id JOIN seasons AS s ON e.season_id=s.season_id JOIN season_types_dict AS st ON s.season_type_id=st.season_type_id JOIN run_types_dict AS rt ON r.run_type_id = rt.run_type_id WHERE r.athlete_id=?";
    private final JdbcTemplate jdbcTemplate;


    public List<RunResult> getRunResultsByAthleteId(final long athleteId) {
        return jdbcTemplate.query(GET_RUN_RESULTS_BY_ATHLETE_ID, new String[]{String.valueOf(athleteId)}, this::extractDataForRunResults);
    }

    public List<RunResult> getBestRunResultsByAthleteId(final long athleteId) {
        return jdbcTemplate.query(GET_BEST_RUN_RESULTS_BY_ATHLETE_ID, new String[]{String.valueOf(athleteId)}, this::extractDataForRunResults);
    }

    List<RunResult> extractDataForRunResults(final ResultSet rs) throws SQLException {
        final List<RunResult> results = new LinkedList<>();
        while (rs.next()) {
            val runType = RunType.valueOf(rs.getString("run_type").trim());
            val result_time_in_seconds = rs.getInt("result_time_in_seconds");
            val resultTimeLocalTime = LocalTime.ofSecondOfDay(result_time_in_seconds);
            val town = rs.getString("town");
            val event_id = rs.getInt("event_id");
            val occurDate = LocalDate.parse(rs.getString("occur_date"));
            val eventRank = EventRank.valueOf(rs.getString("event_rank").trim());
            val year = rs.getInt("year");
            val seasonType = SeasonType.valueOf(rs.getString("season_type").trim());
            results.add(RunResult.builder()
                    .runType(runType.toString())
                    .eventId(event_id)
                    .result(resultTimeLocalTime)
                    .occurDate(occurDate)
                    .town(town)
                    .eventRank(eventRank.toString())
                    .season(new Season(year, seasonType))
                    .build()
            );
        }
        return results;
    }
}
