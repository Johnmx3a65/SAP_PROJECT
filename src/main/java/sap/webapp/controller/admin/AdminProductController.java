package sap.webapp.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import sap.webapp.binding.model.ProductBindingModel;
import sap.webapp.entity.Category;
import sap.webapp.entity.Product;
import sap.webapp.entity.User;
import sap.webapp.repository.CategoryRepository;
import sap.webapp.repository.ProductRepository;
import sap.webapp.repository.RoleRepository;
import sap.webapp.repository.UserRepository;
import sap.webapp.service.ShopService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ShopService shopService;

    @GetMapping("/")
    public String list(Model model){
        List<Product> products = this.productRepository.findAll();

        products = products.stream().sorted(Comparator.comparingInt(Product::getId)).collect(Collectors.toList());

        model.addAttribute("products", products);
        model.addAttribute("view", "admin/product/list");

        return "base-layout";
    }

    @GetMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String create(Model model){

        List<Category> categories = this.categoryRepository.findAll();
        List<User> users = new ArrayList<>();

        for(User user : this.userRepository.findAll()){
            if(!shopService.userIsAdmin(user)){
                users.add(user);
            }
        }

        model.addAttribute("users", users);
        model.addAttribute("categories", categories);
        model.addAttribute("view", "admin/product/create");

        return "base-layout";
    }

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public String createProcess(ProductBindingModel productBindingModel) throws IOException {

        User userEntity = this.userRepository.getOne(productBindingModel.getUserId());
        Category category = this.categoryRepository.getOne(productBindingModel.getCategoryId());

        Product productEntity = new Product(productBindingModel.getPrice(), productBindingModel.getCurrentCount(), productBindingModel.getWarnCount(), productBindingModel.getTitle(), productBindingModel.getDescription(), productBindingModel.getPhoto(), userEntity, category);

        this.productRepository.saveAndFlush(productEntity);

        return "redirect:/admin/products/";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String edit(Model model, @PathVariable Integer id){
        if(!this.productRepository.existsById(id)){
            return "redirect:/admin/products/";
        }
        Product product = this.productRepository.getOne(id);

        List<Category> categories = this.categoryRepository.findAll();

        List<User> users = new ArrayList<>();

        for(User user : this.userRepository.findAll()){
            if(!user.getCompanyName().equals("admin")){
                users.add(user);
            }
        }

        model.addAttribute("users", users);
        model.addAttribute("categories", categories);
        model.addAttribute("product", product);
        model.addAttribute("view", "admin/product/edit");

        return "base-layout";
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String editProcess(ProductBindingModel productBindingModel, @PathVariable Integer id) throws IOException {
        if(!this.productRepository.existsById(id)){
            return "redirect:/admin/products/";
        }

        Product product = this.productRepository.getOne(id);
        Category category = this.categoryRepository.getOne(productBindingModel.getCategoryId());
        User user = this.userRepository.getOne(productBindingModel.getUserId());


        product.setTitle(productBindingModel.getTitle());
        product.setPrice(productBindingModel.getPrice());
        if (!productBindingModel.getPhoto().isEmpty()){
            product.setPhotoBase64(Base64.getEncoder().encodeToString(productBindingModel.getPhoto().getBytes()));
        }
        product.setAuthor(user);
        product.setDescription(productBindingModel.getDescription());
        product.setCategory(category);
        product.setCurrentCount(productBindingModel.getCurrentCount());
        product.setWarnCount(productBindingModel.getWarnCount());

        this.productRepository.saveAndFlush(product);

        return "redirect:/admin/products/";
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String delete(Model model, @PathVariable Integer id){

        if(!this.productRepository.existsById(id)){
            return "redirect:/admin/products/";
        }

        Product product = this.productRepository.getOne(id);

        model.addAttribute("product", product);
        model.addAttribute("view", "admin/product/delete");

        return "base-layout";
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String deleteProcess(@PathVariable Integer id){

        if(!this.productRepository.existsById(id)){
            return "redirect:/admin/products/";
        }

        Product product = this.productRepository.getOne(id);

        this.productRepository.delete(product);

        return "redirect:/admin/products/";
    }
}
