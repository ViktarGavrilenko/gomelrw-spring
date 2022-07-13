package com.example.gomelrwspring.DAO;

import com.example.gomelrwspring.models.Division;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DivisionDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DivisionDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Division> getListOfDivision(String companyName) {
        String SQL = "SELECT divisionname as name, count(divisionname) as numberOfEmployees FROM division d, " +
                "data_gomelrw dg, pred where dg.id_pred=pred.id and dg.id_division=d.id and pred.namepred=?  " +
                "group by divisionname order by divisionname";
        return jdbcTemplate.query(SQL, new Object[]{companyName}, new BeanPropertyRowMapper<>(Division.class));
    }
}