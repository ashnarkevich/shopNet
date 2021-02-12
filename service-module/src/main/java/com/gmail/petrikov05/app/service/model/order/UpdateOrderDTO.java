package com.gmail.petrikov05.app.service.model.order;

import javax.validation.constraints.NotNull;

import static com.gmail.petrikov05.app.service.constant.validation.ValidationMessages.MESSAGE_ORDER_STATUS_NOT_NULL;

public class UpdateOrderDTO {

    @NotNull(message = MESSAGE_ORDER_STATUS_NOT_NULL)
    private OrderStatusDTOEnum status;

    public OrderStatusDTOEnum getStatus() {
        return status;
    }

    public void setStatus(OrderStatusDTOEnum status) {
        this.status = status;
    }

}
