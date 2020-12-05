package sap.webapp.controlller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import sap.webapp.bindingModel.ProductBindingModel;
import sap.webapp.entity.Category;
import sap.webapp.entity.Product;
import sap.webapp.entity.User;
import sap.webapp.repository.CategoryRepository;
import sap.webapp.repository.ProductRepository;
import sap.webapp.repository.UserRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Controller
public class ProductController {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/product/create")
    @PreAuthorize("isAuthenticated()")
    public String create(Model model){
        List<Category> categories = this.categoryRepository.findAll();
        List<User> users = new ArrayList<>();

        for(User user : this.userRepository.findAll()){
            if(!user.getCompanyName().equals("admin")){
                users.add(user);
            }
        }

        model.addAttribute("users", users);
        model.addAttribute("categories", categories);
        model.addAttribute("view", "product/create");

        return "base-layout";
    }

    @PostMapping("/product/create")
    @PreAuthorize("isAuthenticated()")
    public String createProcess(ProductBindingModel productBindingModel) throws IOException {

        User userEntity = this.userRepository.getOne(productBindingModel.getUserId());
        Category category = this.categoryRepository.getOne(productBindingModel.getCategoryId());

        Product productEntity = new Product(productBindingModel.getPrice(), productBindingModel.getTitle(), productBindingModel.getDescription(), productBindingModel.getPhoto(), userEntity, category);

        this.productRepository.saveAndFlush(productEntity);

        return "redirect:/";
    }

    @GetMapping("/product/{id}")
    public String details(Model model, @PathVariable Integer id){
        if(!this.productRepository.existsById(id)){
            return "redirect:/";
        }

        if(!(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)){
            UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User entityUser = this.userRepository.findByEmail(principal.getUsername());

            model.addAttribute("user", entityUser);
        }

        Product product = this.productRepository.getOne(id);

        model.addAttribute("product", product);
        model.addAttribute("view", "product/details");

        return "base-layout";
    }

    @GetMapping("product/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String edit(Model model, @PathVariable Integer id){
        if(!this.productRepository.existsById(id)){
            return "redirect:/";
        }
        Product product = this.productRepository.getOne(id);

        if(!isUserAuthorOrAdmin(product)){
            return "redirect:/product/" + id;
        }

        List<Category> categories = this.categoryRepository.findAll();

        model.addAttribute("categories", categories);
        model.addAttribute("product", product);
        model.addAttribute("view", "product/edit");

        return "base-layout";
    }

    @PostMapping("product/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String editProcess(ProductBindingModel productBindingModel, @PathVariable Integer id) throws IOException {
        if(!this.productRepository.existsById(id)){
            return "redirect:/";
        }

        Product product = this.productRepository.getOne(id);
        Category category = this.categoryRepository.getOne(productBindingModel.getCategoryId());


        if(!isUserAuthorOrAdmin(product)){
            return "redirect:/product/" + id;
        }

        product.setTitle(productBindingModel.getTitle());
        product.setPrice(productBindingModel.getPrice());
        if (!productBindingModel.getPhoto().isEmpty()){
            product.setPhoto(productBindingModel.getPhoto().getBytes());
            product.setPhotoBase64(Base64.getEncoder().encodeToString(productBindingModel.getPhoto().getBytes()));
        }
        product.setDescription(productBindingModel.getDescription());
        product.setCategory(category);

        this.productRepository.saveAndFlush(product);

        return "redirect:/product/" + product.getId();
    }

    @GetMapping("product/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String delete(Model model, @PathVariable Integer id){

        if(!this.productRepository.existsById(id)){
            return "redirect:/";
        }

        Product product = this.productRepository.getOne(id);

        if(!isUserAuthorOrAdmin(product)){
            return "redirect:/product/" + id;
        }

        model.addAttribute("product", product);
        model.addAttribute("view", "product/delete");

        return "base-layout";
    }

    @PostMapping("product/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String deleteProcess(@PathVariable Integer id){

        if(!this.productRepository.existsById(id)){
            return "redirect:/";
        }

        Product product = this.productRepository.getOne(id);

        if(!isUserAuthorOrAdmin(product)){
            return "redirect:/product/" + id;
        }

        this.productRepository.delete(product);

        return "redirect:/";
    }

    private boolean isUserAuthorOrAdmin(Product product){
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User userEntity = this.userRepository.findByEmail(user.getUsername());

        return userEntity.isAdmin() || userEntity.isAuthor(product);
    }

}
