package sap.webapp.bindingModel;

import com.sun.istack.NotNull;

public class SellBindingModel {
    @NotNull
    private Integer sellCount;
    @NotNull
    private String destination;

    public Integer getSellCount() {
        return sellCount;
    }

    public void setSellCount(Integer sellCount) {
        this.sellCount = sellCount;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
