package com.automatyka.pbd.security;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
@RequiredArgsConstructor
public class AppUserRepo {
    private static final String FIND_BY_USERNAME = "SELECT p.person_id, p.username, p.password_hash, ar.access_role, pt.profile_type FROM person AS p JOIN access_roles_dict AS ar ON p.access_role_id=ar.access_role_id JOIN profile_types_dict AS pt ON p.profile_type_id=pt.profile_type_id WHERE p.username=?";

    private final JdbcTemplate jdbcTemplate;

    public AppUser findByUsername(final String username) {
        return jdbcTemplate.query(FIND_BY_USERNAME, new String[]{username}, this::extractDataForAppUser);
    }

    AppUser extractDataForAppUser(final ResultSet rs) throws SQLException {
        if (rs.next()) {
            val id = rs.getInt("person_id");
            val accessRole = AccessRole.valueOf(rs.getString("access_role").trim());
            val profileType = ProfileType.valueOf(rs.getString("profile_type").trim());
            val passwordHash = rs.getString("password_hash").trim();
            val username = rs.getString("username").trim();
            return AppUser.builder()
                    .id((long) id)
                    .accessRole(accessRole)
                    .profileType(profileType)
                    .passwordHash(passwordHash)
                    .username(username)
                    .build();
        }
        return null;
    }
}
