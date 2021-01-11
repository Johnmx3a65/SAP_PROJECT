package sap.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import sap.webapp.binding.model.SellBindingModel;
import sap.webapp.entity.Order;
import sap.webapp.entity.Product;
import sap.webapp.entity.User;
import sap.webapp.repository.CategoryRepository;
import sap.webapp.repository.OrderRepository;
import sap.webapp.repository.ProductRepository;
import sap.webapp.repository.UserRepository;
import sap.webapp.service.MailSendler;
import sap.webapp.service.OrderService;

import java.util.Calendar;

@Controller
public class OrderController {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderService orderService;


    @GetMapping("product/sell/{id}")
    public String sell(Model model, @PathVariable Integer id){

        if(!this.productRepository.existsById(id)){
            return "redirect:/";
        }

        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User entityUser = this.userRepository.findByEmail(principal.getUsername());

        Product product = this.productRepository.getOne(id);

        if(!entityUser.getProducts().contains(product)){
            return "redirect:/error/403";
        }

        if(product.getCurrentCount() <= 0){
            return "redirect:/";
        }

        model.addAttribute("product", product);
        model.addAttribute("view", "product/sell");

        return "base-layout";
    }

    @PostMapping("product/sell/{id}")
    public String sellProcess(SellBindingModel sellBindingModel, @PathVariable Integer id){

        if(!this.productRepository.existsById(id)){
            return "redirect:/";
        }

        orderService.sellProduct(sellBindingModel, id);

        return "redirect:/product/" + id;
    }
}
