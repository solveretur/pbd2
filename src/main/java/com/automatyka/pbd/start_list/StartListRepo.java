package com.automatyka.pbd.start_list;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
class StartListRepo {

    private static final String GET_START_LIST_FOR_EVENT = "SELECT p.name,p.surname,p.town,p.birthday,ec.is_valid_for_run,ec.run_number,t.name AS team_name FROM event_contestants ec LEFT JOIN person AS p ON ec.athlete_id = p.person_id LEFT JOIN athletes AS a ON ec.athlete_id = a.person_id LEFT JOIN professionals AS pr ON a.professional_id = pr.professional_id LEFT JOIN teams AS t ON t.team_id = pr.current_team_id WHERE ec.event_id=?";

    private final JdbcTemplate jdbcTemplate;

    public List<AthleteProfileDTO> getStartListForEvent(final long eventId) {
        return jdbcTemplate.query(GET_START_LIST_FOR_EVENT, new String[]{String.valueOf(eventId)}, this::extractDataForStartList);
    }

    List<AthleteProfileDTO> extractDataForStartList(final ResultSet rs) throws SQLException {
        final List<AthleteProfileDTO> athletes = new LinkedList<>();
        while (rs.next()) {
            val name = rs.getString("name");
            val surname = rs.getString("surname");
            val town = rs.getString("town");
            val birthYear = LocalDate.parse(rs.getString("birthday")).getYear();
            val isValidForRun = rs.getBoolean("is_valid_for_run") ? "tak" : "nie";
            val runNumber = Optional.ofNullable(rs.getString("run_number")).orElse("");
            val teamName = Optional.ofNullable(rs.getString("team_name")).orElse("");
            athletes.add(AthleteProfileDTO.builder().name(name).surname(surname).town(town).birthYear(String.valueOf(birthYear)).isPaid(isValidForRun).runNumber(runNumber).team(teamName).build());
        }
        return athletes;
    }
}
