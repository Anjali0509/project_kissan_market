package com.app.repository;
import com.app.model.FarmerRegisDetails;
import com.app.model.UserDtls;
import org.springframework.boot.sql.init.dependency.DependsOnDatabaseInitialization;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.util.List;
import javax.sql.DataSource;

@DependsOnDatabaseInitialization
@Repository
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public UserRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void save(UserDtls user) {
        String sql = "INSERT INTO users (name, phoneNumber, password, role) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, user.getName(), user.getPhoneNumber(), user.getPassword(), user.getRole());
    }


    public UserDtls findByPhoneNumber(String phoneNumber) {
        String sql = "SELECT * FROM users WHERE phoneNumber = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            UserDtls user = new UserDtls();
            user.setId(rs.getInt("id"));
            user.setName(rs.getString("name"));
            user.setPhoneNumber(rs.getString("phoneNumber"));
            user.setPassword(rs.getString("password"));
            user.setRole(rs.getString("role"));
            return user;
        }, new Object[]{phoneNumber});
    }

    public void saveFarmerDetail(FarmerRegisDetails farmer) {
        String sql = "insert into farmer_details (id,  farmerType, state, city,area,pincode) values(?,?,?,?,?,?)";
        jdbcTemplate.update(sql,farmer.getId(),farmer.getFarmerType(),farmer.getState(),farmer.getCity(),farmer.getArea(),farmer.getPincode());
    }

    @SuppressWarnings("unused")
    private RowMapper<UserDtls> userRowMapper() {
        return (rs, rowNum) -> {
            UserDtls user = new UserDtls();
            user.setId(rs.getInt("id"));
            user.setName(rs.getString("name"));
            user.setPhoneNumber(rs.getString("phone_number"));
            user.setRole(rs.getString("role"));
            return user;
        };
    }

    public List<String> findFarmerNamesByCropCode(String cropCode) {
        String sql = "SELECT u.name FROM users u " +
                     "JOIN crops c ON u.id = c.farmer_id WHERE c.cropCode = ?";
        return jdbcTemplate.queryForList(sql, String.class, new Object[]{cropCode});
    }
    public List<UserDtls> getAllBuyers() {
        String sql = "SELECT id, name, phoneNumber FROM users WHERE role = 'BY'";

        RowMapper<UserDtls> rowMapper = (rs, rowNum) -> {
            UserDtls user = new UserDtls();
            user.setId(rs.getInt("id"));
            user.setName(rs.getString("name"));
            user.setPhoneNumber(rs.getString("phoneNumber"));
            return user;
        };

        return jdbcTemplate.query(sql, rowMapper);
    }
}

