package sap.webapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sap.webapp.binding.model.SellBindingModel;
import sap.webapp.entity.Order;
import sap.webapp.entity.Product;

import java.util.Calendar;

@Service
public class OrderService extends ShopSuperService{
    @Autowired
    private MailSendler mailSendler;

    public void sellProduct(SellBindingModel sellBindingModel, Integer id){

        Product product = this.productRepository.getOne(id);
        product.setCurrentCount(product.getCurrentCount()-sellBindingModel.getQuantity());

        if(product.getCurrentCount() <= product.getWarnCount()){
            sendWarnMessage(product);
        }

        Order order = new Order(product.getAuthor(), product, sellBindingModel.getQuantity(), Calendar.getInstance(),  sellBindingModel.getDestination(), sellBindingModel.getPhone());

        this.productRepository.saveAndFlush(product);
        this.orderRepository.saveAndFlush(order);
    }

    private void sendWarnMessage(Product product){
        String message = "Dear " + product.getAuthor().getFullName().toUpperCase() + "!\n " +
                "Unfortunately, we want to inform you that a product called " + product.getTitle().toUpperCase() + " has reached a critical amount. We ask you to replenish the product stock";
        mailSendler.send(product.getAuthor().getEmail(), "Critical Amount", message);
    }

    public void editOrder(SellBindingModel sellBindingModel, Integer id) {

        Order order = this.orderRepository.getOne(id);

        Integer quantityDiff = order.getQuantity() - sellBindingModel.getQuantity();
        order.getProduct().setCurrentCount(order.getProduct().getCurrentCount() + quantityDiff);

        if(order.getProduct().getCurrentCount() <= order.getProduct().getWarnCount()){
            sendWarnMessage(order.getProduct());
        }

        order.setQuantity(sellBindingModel.getQuantity());
        order.setDestination(sellBindingModel.getDestination());
        order.setCustomerPhone(sellBindingModel.getPhone());

        this.orderRepository.saveAndFlush(order);
    }

    public void deleteOrder(Integer id) {

        Order order = this.orderRepository.getOne(id);
        order.getProduct().setCurrentCount(order.getProduct().getCurrentCount() + order.getQuantity());

        this.orderRepository.delete(order);
    }
}
