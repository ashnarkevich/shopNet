package com.gmail.petrikov05.app.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.transaction.Transactional;

import com.gmail.petrikov05.app.repository.ItemRepository;
import com.gmail.petrikov05.app.repository.model.Item;
import com.gmail.petrikov05.app.repository.model.ItemDetails;
import com.gmail.petrikov05.app.service.ItemService;
import com.gmail.petrikov05.app.service.exception.ObjectDBException;
import com.gmail.petrikov05.app.service.model.PaginationWithEntitiesDTO;
import com.gmail.petrikov05.app.service.model.item.AddItemDTO;
import com.gmail.petrikov05.app.service.model.item.ItemDTO;
import com.gmail.petrikov05.app.service.model.item.ItemPreviewDTO;
import com.gmail.petrikov05.app.service.util.PageUtil;
import com.gmail.petrikov05.app.service.util.converter.ItemConverter;
import org.springframework.stereotype.Service;

import static com.gmail.petrikov05.app.service.constant.PageConstant.COUNT_OF_ITEM_BY_PAGE;
import static com.gmail.petrikov05.app.service.util.PageUtil.getCountOfPage;
import static com.gmail.petrikov05.app.service.util.converter.ItemConverter.convertAddDTOToObject;
import static com.gmail.petrikov05.app.service.util.converter.ItemConverter.convertObjectToDTO;

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

    @Override
    @Transactional
    public boolean deleteItemByNumber(String number) throws ObjectDBException {
        Optional<Item> optionItem = itemRepository.getItemByNumber(number);
        Item item = optionItem.orElseThrow(() -> new ObjectDBException("Item with " + number + " not found"));
        return itemRepository.delete(item);
    }

    @Override
    @Transactional
    public ItemDTO copyItemByNumber(String number) throws ObjectDBException {
        Optional<Item> optionalItem = itemRepository.getItemByNumber(number);
        Item item = optionalItem.orElseThrow(() -> new ObjectDBException("The item not found"));
        Item copyItem = getCopyItem(item);
        itemRepository.add(copyItem);
        return convertObjectToDTO(copyItem);
    }

    @Override
    @Transactional
    public ItemDTO getItemByNumber(String number) throws ObjectDBException {
        Optional<Item> optionItem = itemRepository.getItemByNumber(number);
        Item item = optionItem.orElseThrow(() -> new ObjectDBException("Item not found"));
        return convertObjectToDTO(item);
    }

    @Override
    @Transactional
    public List<ItemPreviewDTO> getAllItems() {
        List<Item> items = itemRepository.getAllObjects();
        return items.stream()
                .map(ItemConverter::convertObjectToPreviewDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ItemDTO addItem(AddItemDTO addItemDTO) {
        Item addItem = convertAddDTOToObject(addItemDTO);
        String number = UUID.randomUUID().toString();
        addItem.setNumber(number);
        Item addedItem = itemRepository.add(addItem);
        return convertObjectToDTO(addedItem);
    }

    private Item getCopyItem(Item item) {
        Item copyItem = new Item();
        String newName = getNameForCopy(item.getName());
        copyItem.setName(newName);
        UUID uniqueNumber = UUID.randomUUID();
        copyItem.setNumber(uniqueNumber.toString());
        copyItem.setPrice(item.getPrice());
        ItemDetails itemDetails = new ItemDetails();
        itemDetails.setDescription(item.getItemDetails().getDescription());
        copyItem.setItemDetails(itemDetails);
        itemDetails.setItem(copyItem);
        return copyItem;
    }

    private String getNameForCopy(String name) {
        String newName = name + "-copy";
        Long countItemByName = itemRepository.getCountItemByName(newName);
        if (countItemByName == 0) {
            return newName;
        } else {
            return newName + "(" + countItemByName + ")";
        }
    }

    private int getPages() {
        Long countOfEntities = itemRepository.getCountOfEntities();
        return getCountOfPage(countOfEntities, COUNT_OF_ITEM_BY_PAGE);
    }

}
