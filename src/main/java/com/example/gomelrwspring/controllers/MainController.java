package com.example.gomelrwspring.controllers;

import com.example.gomelrwspring.DAO.CompanyDAO;
import com.example.gomelrwspring.DAO.DivisionDAO;
import com.example.gomelrwspring.DAO.EmployeeDAO;
import com.example.gomelrwspring.DAO.PostDAO;
import com.example.gomelrwspring.models.Division;
import com.example.gomelrwspring.models.Employee;
import com.example.gomelrwspring.models.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MainController {

    private final CompanyDAO companyDAO;
    private final DivisionDAO divisionDAO;
    private final PostDAO postDAO;
    private final EmployeeDAO employeeDAO;

    @Autowired
    public MainController(CompanyDAO companyDAO, DivisionDAO divisionDAO, PostDAO postDAO, EmployeeDAO employeeDAO) {
        this.companyDAO = companyDAO;
        this.divisionDAO = divisionDAO;
        this.postDAO = postDAO;
        this.employeeDAO = employeeDAO;
    }

    @GetMapping
    public String showMainPage(Model model) {
        model.addAttribute("companies", companyDAO.getListOfCompany());
        return "main";
    }

    @RequestMapping(value = "/division", method = RequestMethod.POST)
    public ResponseEntity<List<Division>> getDivisions(@RequestParam("namepred") String nameCompany) {
        List<Division> divisions = divisionDAO.getListOfDivision(nameCompany);
        return new ResponseEntity<>(divisions, HttpStatus.OK);
    }

    @RequestMapping(value = "/post", method = RequestMethod.POST)
    public ResponseEntity<List<Post>> getPosts(@RequestParam("namepred") String nameCompany, @RequestParam("divisionname") String nameDivision) {
        List<Post> posts = postDAO.getListOfPost(nameCompany, nameDivision);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public ResponseEntity<List<Employee>> getEmployee(@RequestParam("firstname") String name,
                                                      @RequestParam("namepred") String nameCompany,
                                                      @RequestParam("divisionname") String nameDivision,
                                                      @RequestParam("namepost") String namePost) {
        List<Employee> employees = employeeDAO.getListOfEmployee(name, nameCompany, nameDivision, namePost);
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }
}
