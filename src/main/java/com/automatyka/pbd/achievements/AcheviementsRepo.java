package com.automatyka.pbd.achievements;

import com.automatyka.pbd.event.EventRank;
import com.automatyka.pbd.run_results.RunType;
import com.automatyka.pbd.run_results.Season;
import com.automatyka.pbd.run_results.SeasonType;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class AcheviementsRepo {

    private final JdbcTemplate jdbcTemplate;

    private static final String CHECK_IF_ATHLETHE_HAD_SUCH_RUN = "SELECT COUNT(*) " +
            "FROM run_results AS rr " +
            "JOIN run_types_dict AS rrt " +
            "ON rr.run_type_id = rrt.run_type_id " +
            "WHERE rr.event_id = ? AND rr.athlete_id = ? AND rrt.run_type_id = ?";

    private static final String GET_RESULT_POSITION = "SELECT COUNT(*) - SUM" +
            "(" +
            "CASE WHEN t1.result_time_in_seconds > ? " +
            "THEN 1 ELSE 0 END" +
            ") " +
            "AS runner_postion " +
            "FROM " +
            "( " +
            "SELECT r.result_time_in_seconds " +
            "FROM run_results AS r " +
            "JOIN run_types_dict AS rt ON r.run_type_id = rt.run_type_id " +
            "WHERE r.event_id = ? AND rt.run_type_id = ? " +
            "GROUP BY r.result_time_in_seconds " +
            "ORDER BY r.result_time_in_seconds ASC " +
            ")" +
            "AS t1";


    private static final String GET_ALL_ACHIEVEMENTS_RESULTS = "SELECT " +
            "rt.run_type, " +
            "r.athlete_id, " +
            "r.result_id, " +
            "r.run_type_id, " +
            "r.result_time_in_seconds, " +
            "r.event_id, " +
            "e.occur_date, " +
            "e.town, " +
            "e.district, " +
            "er.event_rank, " +
            "s.year, " +
            "st.season_type " +
            "FROM run_results AS r " +
            "JOIN events AS e ON r.event_id = e.event_id " +
            "JOIN event_ranks_dict AS er ON e.event_rank_id = er.event_rank_id " +
            "JOIN seasons AS s ON e.season_id=s.season_id JOIN season_types_dict AS st ON s.season_type_id=st.season_type_id " +
            "JOIN run_types_dict AS rt ON r.run_type_id = rt.run_type_id " +
            "WHERE r.athlete_id=? AND e.event_rank_id=1 ORDER BY e.occur_date DESC";


    @Transactional
    public List<Achievement> getAchievements(final long athleteId) {
        final List<Achievement> achievementsToDisplay = new LinkedList<>();
        final List<Achievement> achievementsWithoutPosition = jdbcTemplate.query(GET_ALL_ACHIEVEMENTS_RESULTS, new String[]{String.valueOf(athleteId)}, this::extractData);
        if (achievementsWithoutPosition == null) {
            return Collections.emptyList();
        }
        for (final Achievement achievement : achievementsWithoutPosition) {
            val position = getPositionOfAthletheInRace(achievement.withAthleteId(athleteId));
            val achievementToDisplay = achievement
                    .withPosition(position);
            achievementsToDisplay.add(achievementToDisplay);
        }
        return achievementsToDisplay.stream().sorted(Comparator.comparing(Achievement::getOccurDate).reversed()).collect(Collectors.toList());
    }

    public int getPositionOfAthletheInRace(final Achievement achievement) {
        val eventId = achievement.getEventId();
        val runType = achievement.getRunTypeId();
        val athletheId = achievement.getAthleteId();
        final Integer count = jdbcTemplate.queryForObject(
                CHECK_IF_ATHLETHE_HAD_SUCH_RUN,
                new Object[]{eventId, athletheId, runType},
                Integer.class
        );
        if (Optional.ofNullable(count).orElse(0) != 1) {
            throw new RuntimeException("Race for such ahtlethe, runtype, event doesnt exist");
        }
        val position = Optional.ofNullable(
                jdbcTemplate.queryForObject(
                        GET_RESULT_POSITION,
                        new Object[]{achievement.getResultInNumberOfSeconds(), eventId, runType},
                        Integer.class
                )
        ).orElse(0);
        if (position.equals(0)) {
            throw new RuntimeException("Race for such ahtlethe, runtype, event doesnt exist");
        }
        return position;
    }


    public List<Achievement> extractData(final ResultSet rs) throws SQLException {
        final LinkedList<Achievement> achievements = new LinkedList<>();
        while (rs.next()) {
            val runType = RunType.valueOf(rs.getString("run_type").trim());
            val result_time_in_seconds = rs.getInt("result_time_in_seconds");
            val resultTimeLocalTime = LocalTime.ofSecondOfDay(result_time_in_seconds);
            val town = rs.getString("town");
            val runTypeId = rs.getLong("run_type_id");
            val event_id = rs.getLong("event_id");
            val occurDate = LocalDate.parse(rs.getString("occur_date"));
            val eventRank = EventRank.valueOf(rs.getString("event_rank").trim());
            val year = rs.getInt("year");
            val seasonType = SeasonType.valueOf(rs.getString("season_type").trim());
            val district = rs.getString("district");
            achievements.add(
                    Achievement.builder()
                            .runType(runType.toString())
                            .runTypeId(runTypeId)
                            .resultInNumberOfSeconds(result_time_in_seconds)
                            .eventId(event_id)
                            .result(resultTimeLocalTime)
                            .occurDate(occurDate)
                            .result(LocalTime.ofSecondOfDay(result_time_in_seconds))
                            .town(town)
                            .district(district)
                            .eventRank(eventRank.toString())
                            .season(new Season(year, seasonType))
                            .build()
            );
        }
        return achievements;
    }
}
