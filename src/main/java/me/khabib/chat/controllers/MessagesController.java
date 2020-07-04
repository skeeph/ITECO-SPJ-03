package me.khabib.chat.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MessagesController {
    @GetMapping("/messages")
    public String messages(Model model) {
        return "messages";
    }

    @GetMapping("/admin/page")
    public String admin(Model model) {
        return "admin";
    }
}
