package com.example.gomelrwspring.DAO;

import com.example.gomelrwspring.models.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PostDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PostDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Post> getListOfPost(String companyName, String divisionName) {
        String SQL = "SELECT namepost as name, count(namepost) as numberOfEmployees FROM division d, data_gomelrw dg, " +
                "pred, post where dg.id_pred=pred.id and dg.id_division=d.id and dg.id_post = post.id and " +
                "pred.namepred=? and d.divisionname=?  group by namepost order by namepost";
        return jdbcTemplate.query(SQL, new Object[]{companyName, divisionName}, new BeanPropertyRowMapper<>(Post.class));
    }
}