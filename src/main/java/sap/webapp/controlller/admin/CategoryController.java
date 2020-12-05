package sap.webapp.controlller.admin;

import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.util.StringUtils;
import sap.webapp.bindingModel.CategoryBindingModel;
import sap.webapp.entity.Category;
import sap.webapp.entity.Product;
import sap.webapp.repository.CategoryRepository;
import sap.webapp.repository.ProductRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/categories")
public class CategoryController {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;
    @GetMapping("/")
    public String list(Model model){
        List<Category> categories = this.categoryRepository.findAll();

        categories = categories.stream().sorted(Comparator.comparingInt(Category::getId)).collect(Collectors.toList());

        model.addAttribute("categories", categories);
        model.addAttribute("view", "admin/category/list");

        return "base-layout";
    }

    @GetMapping("/create")
    public String create(Model model){
        model.addAttribute("view", "admin/category/create");

        return "base-layout";
    }

    @PostMapping("/create")
    public String createProcess(CategoryBindingModel categoryBindingModel){
        if(StringUtils.isEmpty(categoryBindingModel.getName())){
            return "redirect:/admin/categories/create";
        }
        Category category = new Category(categoryBindingModel.getName());
        this.categoryRepository.saveAndFlush(category);
        return "redirect:/admin/categories/";
    }

    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable Integer id){
        if(!this.categoryRepository.existsById(id)){
            return "redirect:/admin/categories";
        }

        Category category = this.categoryRepository.getOne(id);

        model.addAttribute("category", category);
        model.addAttribute("view", "admin/category/edit");

        return "base-layout";
    }

    public String editProcess(CategoryBindingModel categoryBindingModel, @PathVariable Integer id){
        if(!this.categoryRepository.existsById(id)){
            return "redirect:/admin/categories/";
        }
        Category category = this.categoryRepository.getOne(id);

        category.setName(categoryBindingModel.getName());

        this.categoryRepository.saveAndFlush(category);

        return "redirect:/admin/categories/";
    }

    @GetMapping("delete/{id}")
    public String delete(Model model, @PathVariable Integer id){
        if(!this.categoryRepository.existsById(id)){
            return "redirect:/admin/categories/";
        }

        Category category = this.categoryRepository.getOne(id);

        model.addAttribute("category", category);
        model.addAttribute("view", "admin/category/delete");

        return "base-layout";
    }

    @PostMapping("/delete/{id}")
    public String deleteProcess(@PathVariable Integer id){
        if(!this.categoryRepository.existsById(id)){
            return "redirect:/admin/categories/";
        }

        Category category = this.categoryRepository.getOne(id);

        for(Product product : category.getProducts()){
            this.productRepository.delete(product);
        }

        this.categoryRepository.delete(category);

        return "redirect:/admin/categories/";
    }
}
