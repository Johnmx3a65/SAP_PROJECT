package sap.webapp.entity;


import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.IOException;
import java.util.Base64;
import java.util.Set;


@Entity
@Table(name = "products")
public class Product {

    private Integer id;

    private Double price;

    private Integer currentCount;

    private String title;

    private String description;

    private String photoBase64;

    private User author;

    private Category category;

    public Product() {
    }

    public Product(Double price,Integer currentCount, String title, String description, MultipartFile photo, User author, Category category) throws IOException {
        this.price = price;
        this.currentCount = currentCount;
        this.title = title;
        this.description = description;
        this.author = author;
        this.photoBase64 = Base64.getEncoder().encodeToString(photo.getBytes());
        this.category = category;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(nullable = false)
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Column(nullable = false)
    public Integer getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(Integer currentCount) {
        this.currentCount = currentCount;
    }

    @Column(nullable = false)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(nullable = false)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(nullable = false)
    public String getPhotoBase64() {
        return photoBase64;
    }

    public void setPhotoBase64(String photoBase64) {
        this.photoBase64 = photoBase64;
    }

    @ManyToOne
    @JoinColumn(nullable = false, name = "authorId")
    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    @ManyToOne()
    @JoinColumn(nullable = false, name = "categoryId")
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Transient
    public String getSummary(){
        return this.description.substring(0, this.getDescription().length()/2) + "...";
    }
}
