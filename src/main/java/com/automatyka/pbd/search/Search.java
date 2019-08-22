package com.automatyka.pbd.search;

import com.automatyka.pbd.start_list.AthleteProfileDTO;
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
public class Search {

    private String GET_PEOPLE = "SELECT person.person_id, person.name,person.surname,person.birthday, person.town, teams.name AS team_name FROM person JOIN athletes ON person.person_id=athletes.person_id LEFT JOIN professionals ON athletes.person_id = professionals.professional_id LEFT JOIN teams ON professionals.current_team_id = teams.team_id WHERE person.name = ? AND person.surname = ?";

    private final JdbcTemplate jdbcTemplate;

    List<AthleteProfileDTO> extractData(final ResultSet resultSet) throws SQLException {

        final List<AthleteProfileDTO> people = new LinkedList<>();
        while (resultSet.next()) {
            val id = resultSet.getInt("person_id");
            val name = resultSet.getString("name");
            val surname = resultSet.getString("surname");
            val birthYear = LocalDate.parse(resultSet.getString("birthday")).getYear();
            val town = resultSet.getString("town");
            val teamName = Optional.ofNullable(resultSet.getString("team_name")).orElse("");

            people.add(AthleteProfileDTO.builder().id(id).name(name).surname(surname).birthYear(String.valueOf(birthYear)).town(town).team(teamName).build());
        }
        return people;
    }

    public List<AthleteProfileDTO> getList(String name, String surname) {
        return jdbcTemplate.query(GET_PEOPLE, new String[]{name, surname}, this::extractData);
    }
}