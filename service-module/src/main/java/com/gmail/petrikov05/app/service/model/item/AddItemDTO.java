package com.gmail.petrikov05.app.service.model.item;

import java.math.BigDecimal;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.gmail.petrikov05.app.repository.model.Item;
import com.gmail.petrikov05.app.service.constant.validation.ItemValidationMessage;
import com.gmail.petrikov05.app.service.constant.validation.ItemValidationRules;

import static com.gmail.petrikov05.app.service.constant.validation.ItemValidationMessage.MESSAGE_DESCRIPTION_NOT_EMPTY;
import static com.gmail.petrikov05.app.service.constant.validation.ItemValidationMessage.MESSAGE_DESCRIPTION_SIZE_MAX;
import static com.gmail.petrikov05.app.service.constant.validation.ItemValidationMessage.MESSAGE_NAME_NOT_EMPTY;
import static com.gmail.petrikov05.app.service.constant.validation.ItemValidationMessage.MESSAGE_NAME_SIZE_MAX;
import static com.gmail.petrikov05.app.service.constant.validation.ItemValidationMessage.MESSAGE_PRICE_NOT_NULL;
import static com.gmail.petrikov05.app.service.constant.validation.ItemValidationMessage.MESSAGE_PRICE_VALUE;
import static com.gmail.petrikov05.app.service.constant.validation.ItemValidationRules.DESCRIPTION_SIZE_MAX;
import static com.gmail.petrikov05.app.service.constant.validation.ItemValidationRules.NAME_SIZE_MAX;
import static com.gmail.petrikov05.app.service.constant.validation.ItemValidationRules.PRICE_MAX_VALUE;
import static com.gmail.petrikov05.app.service.constant.validation.ItemValidationRules.PRICE_MIN_VALUE;

public class AddItemDTO {

    @NotEmpty(message = MESSAGE_NAME_NOT_EMPTY)
    @Size(max = NAME_SIZE_MAX, message = MESSAGE_NAME_SIZE_MAX)
    private String name;
    @DecimalMin(value = PRICE_MIN_VALUE, message = MESSAGE_PRICE_VALUE)
    @DecimalMax(value = PRICE_MAX_VALUE, message = MESSAGE_PRICE_VALUE)
    @NotNull(message = MESSAGE_PRICE_NOT_NULL)
    private BigDecimal price;
    @NotEmpty(message = MESSAGE_DESCRIPTION_NOT_EMPTY)
    @Size(max = DESCRIPTION_SIZE_MAX, message = MESSAGE_DESCRIPTION_SIZE_MAX)
    private String description;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
