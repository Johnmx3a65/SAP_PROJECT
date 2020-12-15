package sap.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import sap.webapp.binding.model.SellBindingModel;
import sap.webapp.entity.Order;
import sap.webapp.entity.Product;
import sap.webapp.repository.CategoryRepository;
import sap.webapp.repository.OrderRepository;
import sap.webapp.repository.ProductRepository;
import sap.webapp.repository.UserRepository;

import java.util.Calendar;

@Controller
public class OrderController {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("product/sell/{id}")
    public String sell(Model model, @PathVariable Integer id){

        if(!this.productRepository.existsById(id)){
            return "redirect:/";
        }

        Product product = this.productRepository.getOne(id);

        model.addAttribute("product", product);
        model.addAttribute("view", "product/sell");

        return "base-layout";
    }

    @PostMapping("product/sell/{id}")
    public String sellProcess(SellBindingModel sellBindingModel, @PathVariable Integer id){

        if(!this.productRepository.existsById(id)){
            return "redirect:/";
        }

        Product product = this.productRepository.getOne(id);
        product.setCurrentCount(product.getCurrentCount()-sellBindingModel.getQuantity());

        Order order = new Order(product.getAuthor(), product, sellBindingModel.getQuantity(), Calendar.getInstance(),  sellBindingModel.getDestination(), sellBindingModel.getPhone());

        this.productRepository.saveAndFlush(product);
        this.orderRepository.saveAndFlush(order);

        return "redirect:/product/" + id;
    }
}
