package org.launchcode.Wizpic.controllers;

import org.launchcode.Wizpic.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.Date;

@Controller
@RequestMapping("user")
public class UserController {

    String indexMessage;
    String addMessage;
    String keptName;
    String keptEmail;

    public boolean isAlpha(String name) {
        char[] chars = name.toCharArray();

        for (char c: chars) {
            if(!Character.isLetter(c)) {
                return false;
            }
        }

        return true;
    }

    @RequestMapping(value = "")
    public String index(Model model) {
        model.addAttribute("title", "User Page");
        if (indexMessage == null) {
            indexMessage = "Hello!";
        }
        model.addAttribute("message", indexMessage);
        model.addAttribute("users", UserData.getAll()); //UserData.getAll() returns an ArrayList of Users
        return "user/index";
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public String displayEditForm(Model model, @PathVariable int userId) {
        User foundUser = UserData.getById(userId);
        String foundUsername = foundUser.getUsername();
        String foundUserEmail = foundUser.getEmail();
        String foundUserDateString = foundUser.getDateString();
        Date foundUserJoinDate = foundUser.getDate();
        model.addAttribute("username", foundUsername);
        model.addAttribute("email", foundUserEmail);
        model.addAttribute("dateString", foundUserDateString);
        model.addAttribute("date", foundUserJoinDate);
        model.addAttribute("users", UserData.getAll());
        //display the form
        return "user/profile";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String add(Model model) {
        model.addAttribute("title", "Add User");
        if (addMessage == null) {
            addMessage = "Enter User Information";
        }
        else {
            if (keptName != null) {
                model.addAttribute("oldname", keptName);
            }
            if (keptEmail != null) {
                model.addAttribute("oldemail", keptEmail);
            }
        }
        model.addAttribute("message", addMessage);
        model.addAttribute("users", UserData.getAll());
        model.addAttribute(new User());
        return "user/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String add(Model model, @ModelAttribute @Valid User user, Errors errors, String verify) {

        boolean emailExistenceVerified = false;
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            emailExistenceVerified = true;
        }
        if (!emailExistenceVerified) {
            addMessage = "Email address is optional and is missing; must be valid if provided";
        }

        String password = user.getPassword();
        boolean passwordVerified = false;
        if (password == null || password.isEmpty()) {
            addMessage = "Must have password";
        }
        else if (password.length() < 6) {
            addMessage = "Password must be at least 6 characters";
        }
        else if (!password.equals(verify)) {
            addMessage = "Passwords must match";
        }
        else {
            passwordVerified = true;
        }

        boolean usernameVerified = false;
        String username = user.getUsername();
        if (username ==null || username.isEmpty()) {
            addMessage = "Must have username";
        }
        else if (username.length() < 5 || username.length() > 15) {
            addMessage = "username must be between 5 and 15 letters";
        }
        else if (!isAlpha(username)) {
            addMessage = "username must contain only letters";
        }
        else {
            usernameVerified = true;
        }






        if (errors.hasErrors()) {
            model.addAttribute("title", "Add User");
            model.addAttribute("message", addMessage);
            model.addAttribute("oldname", keptName);
            model.addAttribute("oldemail", keptEmail);

            model.addAttribute(user);

            return "user/add";
        }





        if (usernameVerified && passwordVerified) {
            indexMessage = "Hello, "+user.getUsername()+"!";
            keptName = "";
            keptEmail = "";
            UserData.add(user);
            return "redirect:/user";
        }
        else {
            if (usernameVerified) {
                keptName = username;
            }
            else {keptName = "";}
            if (emailExistenceVerified) {
                keptEmail = user.getEmail();
            }
            else {keptEmail = "";}
            model.addAttribute("title", "Add User");
            model.addAttribute("message", addMessage);
            return "user/add";
        }
    }

}


