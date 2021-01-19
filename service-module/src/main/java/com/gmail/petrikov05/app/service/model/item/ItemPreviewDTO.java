package com.gmail.petrikov05.app.service.model.item;

import java.math.BigDecimal;

public class ItemPreviewDTO {

    private String name;
    private String number;
    private BigDecimal price;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }

}
