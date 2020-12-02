package sap.webapp.entity;


import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;


@Entity
@Table(name = "products")
public class Product {

    private Integer id;

    private Integer price;

    private String title;

    private String description;

    private byte[] photo;

    private String photoBase64;

    private User author;

    public Product() {
    }

    public Product(Integer price, String title, String description, MultipartFile photo, User author) throws IOException {
        this.price = price;
        this.title = title;
        this.description = description;
        this.photo = photo.getBytes();
        this.author = author;
        this.photoBase64 = Base64.getEncoder().encodeToString(this.photo);
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
    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
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
    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
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

    @Transient
    public String getSummary(){
        return this.description.substring(0, this.getDescription().length()/2) + "...";
    }
}