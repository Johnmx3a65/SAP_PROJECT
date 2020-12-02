package sap.webapp.bindingModel;

import com.sun.istack.NotNull;

public class ProductBindingModel {
    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    private Integer price;

    @NotNull
    private byte[] photo;

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

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

}
