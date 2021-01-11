package sap.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import sap.webapp.binding.model.ProductBindingModel;
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

import java.io.IOException;
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
        model.addAttribute("view", "orders/sell");

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

    @GetMapping("order/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String edit(Model model, @PathVariable Integer id){
        if(!this.orderRepository.existsById(id)){
            return "redirect:/profile";
        }

        Order order = this.orderRepository.getOne(id);

        model.addAttribute("order", order);
        model.addAttribute("view", "orders/edit");

        return "base-layout";
    }

    @PostMapping("order/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String editProcess(SellBindingModel sellBindingModel, @PathVariable Integer id) throws IOException {

        if(this.orderRepository.existsById(id)){
            orderService.editOrder(sellBindingModel, id);
        }

        return "redirect:/profile";
    }

    @GetMapping("order/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String delete(Model model, @PathVariable Integer id){
        if(!this.orderRepository.existsById(id)){
            return "redirect:/profile";
        }

        Order order = this.orderRepository.getOne(id);

        model.addAttribute("order", order);
        model.addAttribute("view", "orders/delete");

        return "base-layout";
    }

    @PostMapping("order/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String deleteProcess(@PathVariable Integer id){

        if(this.orderRepository.existsById(id)){
            orderService.deleteOrder(id);
        }

        return "redirect:/profile";
    }


}
