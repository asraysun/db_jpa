package com.example.db_jpa.controller;

import com.example.db_jpa.model.User;
import com.example.db_jpa.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Controller
public class UserController {

    static final String JDBC_DRIVER = "{url}";
    static final String JDBC_DB_URL = "{db_url}";

    static final String JDBC_USER = "{admin}";
    static final String JDBC_PASS = "{password}";
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("users")
    public String findAll(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "user-list";
    }

    @GetMapping("/user-create")
    public String createUserForm(User user) {
        return "user-create";
    }

    @PostMapping("/user-auto-create")
    public String createUser1() {
        try {
            Class.forName(JDBC_DRIVER);
            Connection connObj = DriverManager.getConnection(JDBC_DB_URL, JDBC_USER, JDBC_PASS);
            connObj.setAutoCommit(false);
            Statement stmtObj = connObj.createStatement();
            String correctQuery = "INSERT INTO users (first_name, last_name) VALUES ('autofirstname', 'autolastname'); ";
            stmtObj.executeUpdate(correctQuery);

            connObj.commit();


        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return "redirect:/users";
    }
    @PostMapping("/user-create")
    public String createUser(User user) {
        userService.saveUser(user);
        return "redirect:/users";
    }

    @GetMapping("/user-delete/{id}")
    public String deleteUsers(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return "redirect:/users";
    }

    @GetMapping("user-update/{id}")
    public String updateUserForm(@PathVariable("id") Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        return "/user-update";
    }

    @PostMapping("/user-update")
    public String updateUser(User user) {
        userService.saveUser(user);
        return "redirect:/users";
    }

}
