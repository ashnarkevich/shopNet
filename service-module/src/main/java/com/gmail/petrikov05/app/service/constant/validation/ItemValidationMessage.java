package com.gmail.petrikov05.app.service.constant.validation;

import static com.gmail.petrikov05.app.service.constant.validation.ItemValidationRules.DESCRIPTION_SIZE_MAX;
import static com.gmail.petrikov05.app.service.constant.validation.ItemValidationRules.NAME_SIZE_MAX;
import static com.gmail.petrikov05.app.service.constant.validation.ItemValidationRules.PRICE_MAX_VALUE;
import static com.gmail.petrikov05.app.service.constant.validation.ItemValidationRules.PRICE_MIN_VALUE;

public interface ItemValidationMessage {

    String MESSAGE_NAME_NOT_EMPTY = "Name can not be empty.";
    String MESSAGE_NAME_SIZE_MAX = "Name cannot be more than " + NAME_SIZE_MAX + " characters.";

    String MESSAGE_PRICE_NOT_NULL = "Price can not be empty.";
    String MESSAGE_PRICE_VALUE = "Price must be between " + PRICE_MIN_VALUE + " and " + PRICE_MAX_VALUE + " characters.";

    String MESSAGE_DESCRIPTION_NOT_EMPTY = "Description can not be empty.";
    String MESSAGE_DESCRIPTION_SIZE_MAX = "Name cannot be more than " + DESCRIPTION_SIZE_MAX + " characters.";

}
