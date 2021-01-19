package com.gmail.petrikov05.app.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.gmail.petrikov05.app.repository.ItemRepository;
import com.gmail.petrikov05.app.repository.model.Item;
import com.gmail.petrikov05.app.service.ItemService;
import com.gmail.petrikov05.app.service.model.PaginationWithEntitiesDTO;
import com.gmail.petrikov05.app.service.model.item.ItemPreviewDTO;
import com.gmail.petrikov05.app.service.util.PageUtil;
import com.gmail.petrikov05.app.service.util.converter.ItemConverter;
import org.springframework.stereotype.Service;

import static com.gmail.petrikov05.app.service.constant.PageConstant.COUNT_OF_ITEM_BY_PAGE;
import static com.gmail.petrikov05.app.service.util.PageUtil.getCountOfPage;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    public ItemServiceImpl(ItemRepository itemRepository) {this.itemRepository = itemRepository;}

    @Override
    @Transactional
    public PaginationWithEntitiesDTO<ItemPreviewDTO> getItemsByPage(int page) {
        int startPosition = PageUtil.getStartPosition(page, COUNT_OF_ITEM_BY_PAGE);
        List<Item> items = itemRepository.getItemsByPage(startPosition, COUNT_OF_ITEM_BY_PAGE);
        List<ItemPreviewDTO> itemDTOS = items.stream()
                .map(ItemConverter::convertObjectToPreviewDTO)
                .collect(Collectors.toList());
        int pages = getPages();
        return new PaginationWithEntitiesDTO<>(itemDTOS, pages);
    }

    private int getPages() {
        Long countOfEntities = itemRepository.getCountOfEntities();
        return getCountOfPage(countOfEntities, COUNT_OF_ITEM_BY_PAGE);
    }

}
