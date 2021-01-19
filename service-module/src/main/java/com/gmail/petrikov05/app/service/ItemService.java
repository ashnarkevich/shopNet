package com.gmail.petrikov05.app.service;

import com.gmail.petrikov05.app.service.model.PaginationWithEntitiesDTO;
import com.gmail.petrikov05.app.service.model.item.ItemPreviewDTO;

public interface ItemService {

    PaginationWithEntitiesDTO<ItemPreviewDTO> getItemsByPage(int page);

}
