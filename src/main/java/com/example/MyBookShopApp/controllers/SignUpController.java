package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.ContactConfirmationPayload;
import com.example.MyBookShopApp.data.ContactConfirmationResponse;
import com.example.MyBookShopApp.data.RegistrationForm;
import com.example.MyBookShopApp.security.BookStoreUserRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
public class SignUpController {

    private final BookStoreUserRegister bookStoreUserRegister;

    @Autowired
    public SignUpController(BookStoreUserRegister bookStoreUserRegister) {
        this.bookStoreUserRegister = bookStoreUserRegister;
    }

    @GetMapping("/signup")
    public String signupPage(Model model){
        model.addAttribute("regForm", new RegistrationForm());
        return "/signup";
    }

    @PostMapping("/requestContactConfirmation")
    @ResponseBody
    public ContactConfirmationResponse requestContactConfirmation(@RequestBody ContactConfirmationPayload contactConfirmationPayload){
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult("true");
        return response;
    }

    @PostMapping("/approveContact")
    @ResponseBody
    public ContactConfirmationResponse approveContact(@RequestBody ContactConfirmationPayload contactConfirmationPayload){
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult("true");
        return response;
    }

    @PostMapping("/reg")
    public String userRegistration(RegistrationForm form, Model model){
        bookStoreUserRegister.registerNewUser(form);
        model.addAttribute("regOk", true);
        return "/signin";
    }

    @PostMapping("/login")
    @ResponseBody
    public ContactConfirmationResponse login(@RequestBody ContactConfirmationPayload contactConfirmationPayload,
                                             HttpServletResponse httpServletResponse){
        ContactConfirmationResponse loginResponse = bookStoreUserRegister.jwtLogin(contactConfirmationPayload);
        Cookie cookie = new Cookie("token", loginResponse.getResult());
        httpServletResponse.addCookie(cookie);
        return loginResponse;
    }

    @GetMapping("/my")
    public String my(Model model){
        model.addAttribute("curUsr", bookStoreUserRegister.getCurrentUser());
        return "/my";
    }

    @GetMapping("/profile")
    public String handleProfile(Model model) {
        model.addAttribute("curUsr", bookStoreUserRegister.getCurrentUser());
        return "profile";
    }

}



