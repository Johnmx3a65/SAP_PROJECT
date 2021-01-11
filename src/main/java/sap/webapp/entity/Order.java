package sap.webapp.entity;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@Table(name = "orders")
public class Order {

    private Integer id;

    private User company;

    private Product product;

    private Integer quantity;

    private Calendar orderDate;

    private String destination;

    private String customerPhone;

    public Order() {
    }

    public Order(User company, Product product, Integer quantity, Calendar orderDate, String destination, String customerPhone) {
        this.company =  company;
        this.product = product;
        this.quantity = quantity;
        this.orderDate = orderDate;
        this.destination = destination;
        this.customerPhone = customerPhone;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(nullable = false, name = "companyId")
    public User getCompany() {
        return company;
    }

    public void setCompany(User company) {
        this.company = company;
    }

    @ManyToOne
    @JoinColumn(nullable = false, name = "productId")
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Column(nullable = false)
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    public Calendar getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Calendar orderDate) {
        this.orderDate = orderDate;
    }

    @Column(nullable = false)
    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    @Column(nullable = false)
    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerNumber) {
        this.customerPhone = customerNumber;
    }
}
