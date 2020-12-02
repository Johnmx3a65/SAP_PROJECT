package sap.webapp.bindingModel;

import com.sun.istack.NotNull;
import org.springframework.web.multipart.MultipartFile;

public class ProductBindingModel {
    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    private Integer price;

    @NotNull
    private MultipartFile photo;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public MultipartFile getPhoto() {
        return photo;
    }

    public void setPhoto(MultipartFile photo) {
        this.photo = photo;
    }

}
