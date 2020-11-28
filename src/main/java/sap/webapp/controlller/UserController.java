package sap.webapp.controlller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import sap.webapp.repository.RoleRepository;
import sap.webapp.repository.UserRepository;

@Controller
public class UserController {
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("view", "user/register");

        return "base-layout";
    }
}
