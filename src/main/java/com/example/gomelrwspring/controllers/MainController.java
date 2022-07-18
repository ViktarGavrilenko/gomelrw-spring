package com.example.gomelrwspring.controllers;

import com.example.gomelrwspring.DAO.*;
import com.example.gomelrwspring.models.Division;
import com.example.gomelrwspring.models.Employee;
import com.example.gomelrwspring.models.OldPlaceOfWork;
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

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class MainController {

    private final CompanyDAO companyDAO;
    private final DivisionDAO divisionDAO;
    private final PostDAO postDAO;
    private final EmployeeDAO employeeDAO;
    private final PhoneNumberDAO phoneNumberDAO;
    private final OldPlaceOfWorkDAO oldPlaceOfWorkDAO;

    @Autowired
    public MainController(CompanyDAO companyDAO, DivisionDAO divisionDAO, PostDAO postDAO, EmployeeDAO employeeDAO,
                          PhoneNumberDAO phoneNumberDAO, OldPlaceOfWorkDAO oldPlaceOfWorkDAO) {
        this.companyDAO = companyDAO;
        this.divisionDAO = divisionDAO;
        this.postDAO = postDAO;
        this.employeeDAO = employeeDAO;
        this.phoneNumberDAO = phoneNumberDAO;
        this.oldPlaceOfWorkDAO = oldPlaceOfWorkDAO;
    }

    @GetMapping
    public String showMainPage(Model model) {
        model.addAttribute("companies", companyDAO.getListOfCompany());
        return "main";
    }

    @GetMapping("/former")
    public String showPageWithFormerEmployees(Model model) {
        model.addAttribute("companies", companyDAO.getListOfCompany());
        return "main";
    }

    @RequestMapping(value = "/division", method = RequestMethod.POST)
    public ResponseEntity<List<Division>> getDivisions(@RequestParam("namepred") String nameCompany) {
        List<Division> divisions = divisionDAO.getListOfDivision(nameCompany);
        return new ResponseEntity<>(divisions, HttpStatus.OK);
    }

    @RequestMapping(value = "/post", method = RequestMethod.POST)
    public ResponseEntity<List<Post>> getPosts(@RequestParam("namepred") String nameCompany,
                                               @RequestParam("divisionname") String nameDivision) {
        List<Post> posts = postDAO.getListOfPost(nameCompany, nameDivision);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public ResponseEntity<List<Employee>> getEmployee(@RequestParam(name = "firstname", required = false) String name,
                                                      @RequestParam(name = "namepred", required = false) String nameCompany,
                                                      @RequestParam(name = "divisionname", required = false) String nameDivision,
                                                      @RequestParam(name = "namepost", required = false) String namePost,
                                                      @RequestParam(name = "datebd", required = false, defaultValue = "") String dateOfBirth,
                                                      @RequestParam(name = "id", required = false, defaultValue = "-1") int id) {
        List<Employee> employees;
        if (id == -1) {
            employees = employeeDAO.getListOfEmployee(name, nameCompany, nameDivision, namePost, dateOfBirth);
        } else {
            employees = employeeDAO.getPersonWithBirthdays(id);
        }
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @RequestMapping(value = "/addtel", method = RequestMethod.POST)
    public ResponseEntity addPhoneNumber(@RequestParam("work_tel") String phoneNumber,
                                         @RequestParam("id") int id, HttpServletRequest request) {
        phoneNumberDAO.savePhoneNumber(phoneNumber, id);
        phoneNumberDAO.saveAuthorIP(phoneNumber, id, request.getRemoteAddr());
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/oldPlaceOfWork", method = RequestMethod.POST)
    public ResponseEntity<List<OldPlaceOfWork>> addPhoneNumber(@RequestParam("id") int id) {
        List<OldPlaceOfWork> oldPlaceOfWorks = oldPlaceOfWorkDAO.getOldPlaceOfWork(id);
        return new ResponseEntity<>(oldPlaceOfWorks, HttpStatus.OK);
    }

    @RequestMapping(value = "/birthday", method = RequestMethod.POST)
    public ResponseEntity<List<Employee>> getListOfBirthdays() {
        List<Employee> employees = employeeDAO.getListOfBirthdays();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }
}