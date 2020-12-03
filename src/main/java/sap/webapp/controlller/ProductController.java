package sap.webapp.controlller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import sap.webapp.bindingModel.ProductBindingModel;
import sap.webapp.entity.Product;
import sap.webapp.entity.User;
import sap.webapp.repository.ProductRepository;
import sap.webapp.repository.UserRepository;

import java.io.IOException;

@Controller
public class ProductController {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/product/create")
    @PreAuthorize("isAuthenticated()")
    public String create(Model model){
        model.addAttribute("view", "product/create");

        return "base-layout";
    }

    @PostMapping("/product/create")
    @PreAuthorize("isAuthenticated()")
    public String createProcess(ProductBindingModel productBindingModel) throws IOException {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User userEntity = this.userRepository.findByEmail(user.getUsername());

        Product productEntity = new Product(productBindingModel.getPrice(), productBindingModel.getTitle(), productBindingModel.getDescription(), productBindingModel.getPhoto(), userEntity);

        this.productRepository.saveAndFlush(productEntity);

        return "redirect:/";
    }

    @GetMapping("/product/{id}")
    public String details(Model model, @PathVariable Integer id){
        if(!this.productRepository.existsById(id)){
            return "redirect:/";
        }

        Product product = this.productRepository.getOne(id);

        model.addAttribute("product", product);
        model.addAttribute("view", "product/details");

        return "base-layout";
    }

}
