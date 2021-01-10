package sap.webapp.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import sap.webapp.binding.model.UserBindingModel;
import sap.webapp.binding.model.UserEditBindingModel;
import sap.webapp.entity.Role;
import sap.webapp.entity.User;
import sap.webapp.repository.RoleRepository;
import sap.webapp.repository.UserRepository;
import sap.webapp.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("view", "admin/user/register");

        return "base-layout";
    }

    @PostMapping("/register")
    public String registerProcess(UserBindingModel userBindingModel){

        if(!userBindingModel.getPassword().equals(userBindingModel.getConfirmPassword())){
            return "redirect:/admin/users/register";
        }

        userService.createUser(userBindingModel);

        return "redirect:/admin/users/";
    }

    @GetMapping("/")
    public String listUsers(Model model){
        List<User> users = this.userRepository.findAll();

        model.addAttribute("users", users);
        model.addAttribute("view", "admin/user/list");

        return "base-layout";
    }

    @GetMapping("edit/{id}")
    public String edit(Model model, @PathVariable Integer id){
        if(!this.userRepository.existsById(id)){
            return "redirect:/admin/users";
        }

        User user = this.userRepository.getOne(id);
        List<Role> roles = this.roleRepository.findAll();
        roles.remove(this.roleRepository.findByName("ROLE_SUPER"));
        
        model.addAttribute("isNotSuper", !userService.isSuper(user));
        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        model.addAttribute("view", "admin/user/edit");

        return "base-layout";
    }

    @PostMapping("edit/{id}")
    public String editProcess(UserEditBindingModel userBindingModel, @PathVariable Integer id){
        if(!this.userRepository.existsById(id)){
            return "redirect:/admin/users";
        }

        userService.editUser(userBindingModel, id);

        return "redirect:/admin/users/";
    }

    @GetMapping("delete/{id}")
    public String delete(Model model, @PathVariable Integer id){
        if(!this.userRepository.existsById(id)){
            return "redirect:/admin/users/";
        }

        User user = this.userRepository.getOne(id);

        model.addAttribute("user", user);
        model.addAttribute("view", "admin/user/delete");

        return "base-layout";
    }

    @PostMapping("/delete/{id}")
    public String deleteProcess(@PathVariable Integer id){
        if(!this.userRepository.existsById(id)){
            return "redirect:/admin/users/";
        }

        userService.deleteUser(id);

        return "redirect:/admin/users/";
    }
}
