package sap.webapp.controlller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import sap.webapp.entity.Product;
import sap.webapp.repository.ProductRepository;

import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private ProductRepository productRepository;


    @GetMapping("/")
    public String index(Model model){
        List<Product> products = this.productRepository.findAll();

        model.addAttribute("view", "home/index");
        model.addAttribute("products", products);
        return "base-layout";
    }

    @RequestMapping("/error/403")
    public String accessDenied(Model model){
        model.addAttribute("view", "error/403");

        return "base-layout";
    }
}
