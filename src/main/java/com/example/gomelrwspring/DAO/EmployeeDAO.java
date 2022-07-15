package com.example.gomelrwspring.DAO;

import com.example.gomelrwspring.models.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.example.gomelrwspring.utils.StringUtils.convertDateForEmployee;
import static com.example.gomelrwspring.utils.StringUtils.getDateNowHowMonthAndDay;

@Component
public class EmployeeDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public EmployeeDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Employee> getListOfEmployee(String name, String companyName, String divisionName,
                                            String postName, String dateOfBirth) {
        List<String> args = new ArrayList<>();

        String SQL = "SELECT dg.id, firstname as firstName, name, middlename as middleName, dt_birthday as birthday, " +
                "sex, namepost as postName, divisionname as divisionName, tabnum as tabNum, namepred as companyName, " +
                "work_tel as workPhoneNumber, e_mail as email, dg.datasaveinbase FROM people p, data_gomelrw dg, " +
                "post, division d, tabnum, pred where dg.id_people=p.id and dg.id_post=post.id and dg.id_division=d.id " +
                "and dg.id_pred=pred.id and dg.id_tabnum=tabnum.id %s %s %s %s and (firstname like ? or name like ?) " +
                "order by firstname, `name`;";

        String fieldCompany = "";
        String fieldDivision = "";
        String fieldPost = "";
        String fieldBirth = "";

        if (!dateOfBirth.equals("")) {
            fieldBirth = "and ? = DATE_FORMAT(`dt_birthday`, '%m-%d') ";
            args.add(dateOfBirth);
        }

        if (!companyName.equals("")) {
            fieldCompany = "and namepred = ? ";
            args.add(companyName);
        }
        if (!divisionName.equals("")) {
            fieldDivision = "and divisionname = ? ";
            args.add(divisionName);
        }
        if (!postName.equals("")) {
            fieldPost = "and namepost = ? ";
            args.add(postName);
        }
        args.add("%" + name + "%");
        args.add("%" + name + "%");

        SQL = String.format(SQL, fieldBirth, fieldCompany, fieldDivision, fieldPost);
        List<Employee> employeeList = jdbcTemplate.query(SQL, new BeanPropertyRowMapper<>(Employee.class), args.toArray());

        for (Employee employee : employeeList) {
            employee.setBirthday(
                    convertDateForEmployee(employee.getBirthday(), employee.getSex()));
        }
        return employeeList;
    }

    public List<Employee> getListOfBirthdays() {
        String SQL = "SELECT dg.id, firstname as firstName, name, middlename as middleName, dt_birthday as birthday, " +
                "sex, namepost as postName, divisionname as divisionName, tabnum as tabNum, namepred as companyName, " +
                "work_tel as workPhoneNumber, e_mail as email FROM people p, data_gomelrw dg, post, division d, " +
                "tabnum, pred where dg.id_people=p.id and dg.id_post=post.id and dg.id_division=d.id and " +
                "dg.id_pred=pred.id and dg.id_tabnum=tabnum.id and '" + getDateNowHowMonthAndDay() + "'= " +
                "DATE_FORMAT(`dt_birthday`, '%m-%d') order by namepred, firstname;";

        List<Employee> employeeList = jdbcTemplate.query(SQL, new BeanPropertyRowMapper<>(Employee.class));
        for (Employee employee : employeeList) {
            employee.setBirthday(
                    convertDateForEmployee(employee.getBirthday(), employee.getSex()));
        }
        return employeeList;
    }

    public List<Employee> getPersonWithBirthdays(int id) {
        String SQL = "SELECT dg.id, firstname as firstName, name, middlename as middleName, dt_birthday as birthday, " +
                "sex, namepost as postName, divisionname as divisionName, tabnum as tabNum, namepred as companyName, " +
                "work_tel as workPhoneNumber, e_mail as email FROM people p, data_gomelrw dg, post, division d, tabnum, " +
                "pred where dg.id_people=p.id and dg.id_post=post.id and dg.id_division=d.id and dg.id_pred=pred.id and " +
                "dg.id_tabnum=tabnum.id and dg.id = ? order by firstname, name;";
        Object[] objects = new Object[]{id};

        List<Employee> employeeList = jdbcTemplate.query(SQL, new BeanPropertyRowMapper<>(Employee.class), objects);
        for (Employee employee : employeeList) {
            employee.setBirthday(
                    convertDateForEmployee(employee.getBirthday(), employee.getSex()));
        }
        return employeeList;
    }
}