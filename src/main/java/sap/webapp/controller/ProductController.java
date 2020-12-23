package sap.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import sap.webapp.binding.model.ProductBindingModel;
import sap.webapp.binding.model.SellBindingModel;
import sap.webapp.entity.Category;
import sap.webapp.entity.Order;
import sap.webapp.entity.Product;
import sap.webapp.entity.User;
import sap.webapp.repository.CategoryRepository;
import sap.webapp.repository.OrderRepository;
import sap.webapp.repository.ProductRepository;
import sap.webapp.repository.UserRepository;

import java.io.IOException;
import java.util.*;

@Controller
public class ProductController {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/product/{id}")
    public String details(Model model, @PathVariable Integer id){
        if(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken){
            return "redirect:/login";
        }

        if(!this.productRepository.existsById(id)){
            return "redirect:/";
        }

        Product product = this.productRepository.getOne(id);
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User entityUser = this.userRepository.findByEmail(principal.getUsername());

        boolean isAuthor = entityUser.getId().equals(product.getAuthor().getId());

        if (!isAuthor){
            return "redirect:/error/403";
        }

        model.addAttribute("product", product);
        model.addAttribute("view", "product/details");

        return "base-layout";
    }
}
