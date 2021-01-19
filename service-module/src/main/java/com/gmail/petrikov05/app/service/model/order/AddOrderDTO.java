package com.gmail.petrikov05.app.service.model.order;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import static com.gmail.petrikov05.app.service.constant.ValidationMessages.MESSAGE_ITEM_AMOUNT_NOT_EMPTY;

public class AddOrderDTO {

    @NotNull(message = MESSAGE_ITEM_AMOUNT_NOT_EMPTY)
    private Integer itemAmount;
    @NotEmpty(message = "Sorry. Try else time.")
    private String itemNumber;

    public int getItemAmount() {
        return itemAmount;
    }

    public void setItemAmount(int itemAmount) {
        this.itemAmount = itemAmount;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

}
