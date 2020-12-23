package sap.webapp.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

@Entity
@Table(name = "categories")
public class Category {
    private Integer id;

    private String name;

    private Set<Product> products;

    public Category() {
    }

    public Category(String name) {
        this.name = name;
        this.products = new HashSet<>();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(nullable = false, unique = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "category")
    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public Set<Product> getProductsByCompany(User user){
        Set<Product> newSet = new HashSet<>();
        Product current;
        try {
            for(Iterator<Product> productIterator = products.iterator(); (current = productIterator.next()) != null; ){
                if (current.getAuthor().equals(user)){
                    newSet.add(current);
                }
            }
        }catch (NoSuchElementException ignored){ }

        return newSet;
    }
}
