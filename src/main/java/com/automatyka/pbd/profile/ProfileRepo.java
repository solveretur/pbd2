package com.automatyka.pbd.profile;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProfileRepo {
    private final JdbcTemplate jdbcTemplate;

    private static final String GET_PROFILE_FOR_MY_PROFILE = "SELECT" +
            "  p.username," +
            "  p.email," +
            "  p.name," +
            "  p.surname," +
            "  p.birthday," +
            "  p.town," +
            "  p.sex," +
            "  a.is_professional," +
            "  pr.training_plan_id," +
            "  pr.is_active," +
            "  pr.career_end_date," +
            "  pr.career_start_date," +
            "  pr.licence," +
            "  sc.sport_class," +
            "  t.name AS team_name," +
            "  c.name    AS coach_name," +
            "  c.surname AS coach_surname" +
            "  FROM person AS p" +
            "  JOIN athletes AS a ON a.person_id = p.person_id" +
            "  LEFT JOIN professionals AS pr ON a.professional_id = pr.professional_id" +
            "  LEFT JOIN sport_class_dict AS sc ON sc.sport_class_id = pr.sport_class_id" +
            "  LEFT JOIN person AS c ON pr.current_coach_id = c.person_id" +
            "  LEFT JOIN teams AS t ON pr.current_team_id = t.team_id" +
            "  WHERE p.person_id=?";


    AthleteProfile getProfileForMyProfile(final long athleteId) {
        return jdbcTemplate.query(GET_PROFILE_FOR_MY_PROFILE, new String[]{String.valueOf(athleteId)}, this::extractDataForAthleteProfile);

    }


    AthleteProfile extractDataForAthleteProfile(final ResultSet rs) throws SQLException {
        if (rs.next()) {
            val username = rs.getString("username");
            val email = rs.getString("email");
            val name = rs.getString("name");
            val surname = rs.getString("surname");
            val birthday = LocalDate.parse(rs.getString("birthday"));
            val town = rs.getString("town");
            val sex = rs.getString("sex").equals("M") ? "Mężczyzna" : "Kobieta";
            val isProfessional = rs.getBoolean("is_professional");
            val trainingPlanId = rs.getInt("training_plan_id");
            val isActive = rs.getBoolean("is_active");
            val careerEndDate = Optional.ofNullable(rs.getString("career_end_date")).map(LocalDate::parse).orElse(null);
            val careerStartDate = Optional.ofNullable(rs.getString("career_start_date")).map(LocalDate::parse).orElse(null);
            val licence = rs.getString("licence");
            val sportClass = rs.getString("sport_class");
            val teamName = rs.getString("team_name");
            val coachName = rs.getString("coach_name");
            val coachSurname = rs.getString("coach_surname");
            return AthleteProfile.builder()
                    .username(username)
                    .email(email)
                    .name(name)
                    .surname(surname)
                    .birthday(birthday)
                    .town(town)
                    .sex(sex)
                    .professional(isProfessional)
                    .trainingPlanId(trainingPlanId)
                    .active(isActive)
                    .careerEndDate(careerEndDate)
                    .careerStartDate(careerStartDate)
                    .licence(licence)
                    .teamName(teamName)
                    .sportClass(sportClass)
                    .coachName(coachName)
                    .coachSurname(coachSurname)
                    .build();
        }
        return null;
    }
}
