package com.example.gomelrwspring.DAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class PhoneNumberDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PhoneNumberDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void savePhoneNumber(String phoneNumber, int id) {
        jdbcTemplate.update("UPDATE data_gomelrw  SET work_tel = ? WHERE id=?", phoneNumber, id);
    }

    public void saveAuthorIP(String phoneNumber, int id, String ip) {
        jdbcTemplate.update("INSERT INTO telupdate (id_data, tel, ip ) VALUES (?,?,?)", id, phoneNumber, ip);
    }

}
