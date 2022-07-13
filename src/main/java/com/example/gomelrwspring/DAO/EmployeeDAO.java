package com.example.gomelrwspring.DAO;

import com.example.gomelrwspring.models.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.example.gomelrwspring.utils.StringUtils.convertDateForEmployee;

@Component
public class EmployeeDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public EmployeeDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Employee> getListOfEmployee(String name, String companyName, String divisionName, String postName) {
        String SQL = "SELECT dg.id, firstname as firstName, name, middlename as middleName, dt_birthday as birthday, " +
                "sex, namepost as postName, divisionname as divisionName, tabnum as tabNum, namepred as companyName, " +
                "work_tel as workPhoneNumber, e_mail as email, dg.datasaveinbase FROM people p, data_gomelrw dg, " +
                "post, division d, tabnum, pred where dg.id_people=p.id and dg.id_post=post.id and dg.id_division=d.id " +
                "and dg.id_pred=pred.id and dg.id_tabnum=tabnum.id and (firstname like ? or name like ?) %s  %s  %s " +
                "order by firstname, name;";
        String fieldCompany = "?";
        String fieldDivision = "?";
        String fieldPost = "?";
        if (!companyName.equals("")) {
            fieldCompany = "and namepred = ?";
        }
        if (!divisionName.equals("")) {
            fieldDivision = "and divisionname = ?";
        }
        if (!postName.equals("")) {
            fieldPost = "and namepost = ?";
        }
        SQL = String.format(SQL, fieldCompany, fieldDivision, fieldPost);
        List<Employee> employeeList = jdbcTemplate.query(SQL, new Object[]{"%" + name + "%", "%" + name + "%", companyName, divisionName,
                postName}, new BeanPropertyRowMapper<>(Employee.class));

        for (int i = 0; i < employeeList.size(); i++) {
            employeeList.get(i).setBirthday(
                    convertDateForEmployee(employeeList.get(i).getBirthday(), employeeList.get(i).getSex()));
        }
        return employeeList;
    }
}
