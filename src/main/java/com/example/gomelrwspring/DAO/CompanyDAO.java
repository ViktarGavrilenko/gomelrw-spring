package com.example.gomelrwspring.DAO;

import com.example.gomelrwspring.models.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CompanyDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CompanyDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Company> getListOfCompany() {
        String SQL = "SELECT pred.id, namepred as name, count(namepred) as numberOfEmployees FROM data_gomelrw dg, pred where dg.id_pred=pred.id group by namepred order by namepred";
        return jdbcTemplate.query(SQL, new BeanPropertyRowMapper<>(Company.class));
    }
}
