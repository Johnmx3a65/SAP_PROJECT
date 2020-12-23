package sap.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import sap.webapp.entity.Category;
import sap.webapp.entity.Product;
import sap.webapp.entity.User;
import sap.webapp.repository.CategoryRepository;
import sap.webapp.repository.UserRepository;

import java.util.*;

@Controller
public class HomeController {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;


    @GetMapping("/")
    public String index(Model model){

        if(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken){
            return "redirect:/login";
        }

        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User entityUser = this.userRepository.findByEmail(principal.getUsername());

        Set<Category> categories = new HashSet<>();

        try{
            Product currentProduct;
            for (Iterator<Product> categoryIterator = entityUser.getProducts().iterator(); (currentProduct = categoryIterator.next()) != null;){
                categories.add(currentProduct.getCategory());
            }
        }catch (NoSuchElementException ignored){}

        model.addAttribute("view", "home/index");
        model.addAttribute("categories", categories);
        return "base-layout";
    }

    @GetMapping("/category/{id}")
    public String listProducts(Model model, @PathVariable Integer id){
        if(!this.categoryRepository.existsById(id)){
            return "redirect:/";
        }

        if(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken){
           return "redirect:/login";
        }

        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User entityUser = this.userRepository.findByEmail(principal.getUsername());

        if(!entityUser.isUser()){
            return "redirect:/error/403";
        }

        Category category = this.categoryRepository.getOne(id);

        Set<Product> products = category.getProductsByCompany(entityUser);

        model.addAttribute("products", products);
        model.addAttribute("category", category);
        model.addAttribute("view", "home/list-products");

        return "base-layout";
    }

    @RequestMapping("/error/403")
    public String accessDenied(Model model){
        model.addAttribute("view", "error/403");

        return "base-layout";
    }
}
