package sap.webapp.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.util.StringUtils;
import sap.webapp.binding.model.UserBindingModel;
import sap.webapp.binding.model.UserEditBindingModel;
import sap.webapp.db.entity.Product;
import sap.webapp.db.entity.Role;
import sap.webapp.db.entity.User;
import sap.webapp.db.repository.ProductRepository;
import sap.webapp.db.repository.RoleRepository;
import sap.webapp.db.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ProductRepository productRepository;

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

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        User user = new User(userBindingModel.getEmail(),
                userBindingModel.getFullName(),
                userBindingModel.getCompanyName(),
                bCryptPasswordEncoder.encode(userBindingModel.getPassword()));

        Role userRole = this.roleRepository.findByName("ROLE_USER");

        user.addRole(userRole);

        this.userRepository.saveAndFlush(user);

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
        User user = this.userRepository.getOne(id);

        if(!StringUtils.isEmpty(userBindingModel.getPassword()) && !StringUtils.isEmpty(userBindingModel.getConfirmPassword())){
            if(userBindingModel.getPassword().equals(userBindingModel.getConfirmPassword())){
                BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

                user.setPassword(bCryptPasswordEncoder.encode(userBindingModel.getPassword()));
            }
        }
        user.setFullName(userBindingModel.getFullName());
        user.setCompanyName(userBindingModel.getCompanyName());
        user.setEmail(userBindingModel.getEmail());

        Set<Role> roles = new HashSet<>();

        for (Integer roleId : userBindingModel.getRoles()){
            roles.add(this.roleRepository.getOne(roleId));
        }

        user.setRoles(roles);

        this.userRepository.saveAndFlush(user);

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
        User user = this.userRepository.getOne(id);

        for(Product product : user.getProducts()){
            this.productRepository.delete(product);
        }

        this.userRepository.delete(user);

        return "redirect:/admin/users/";
    }
}
