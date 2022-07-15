package com.example.gomelrwspring.DAO;

import com.example.gomelrwspring.models.Employee;
import com.example.gomelrwspring.models.OldPlaceOfWork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OldPlaceOfWorkDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public OldPlaceOfWorkDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Employee> getIDEmployee(int idData) {
        String SQL = "SELECT id_people as id FROM data_gomelrw WHERE id=?";
        return jdbcTemplate.query(SQL, new BeanPropertyRowMapper<>(Employee.class), new Object[]{idData});
    }

    public List<OldPlaceOfWork> getOldPlaceOfWork(int idData) {
        String SQL = "SELECT namepred as companyName, divisionname as divisionName, namepost as postName, " +
                "work_tel as phoneNumber FROM data_gomelrw_old dg, people p, post, division d, pred  WHERE " +
                "dg.id_people=p.id and dg.id_people=? and dg.id_pred=pred.id and dg.id_division=d.id " +
                "and dg.id_post=post.id ORDER BY dg.datasaveinbase DESC";
        return jdbcTemplate.query(SQL, new BeanPropertyRowMapper<>(OldPlaceOfWork.class),
                new Object[]{getIDEmployee(idData).get(0).getId()});
    }
}