package sap.webapp.binding.model;

import javax.validation.constraints.NotNull;

public class SellBindingModel {
    @NotNull
    private Integer quantity;
    @NotNull
    private String destination;
    @NotNull
    private String phone;

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
