package com.example.techgicus_ebilling.techgicus_ebilling.mapper;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Item;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.itemDto.CreatedProductItem;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.itemDto.CreatedServiceItem;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.itemDto.ItemResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.itemDto.UpdateItemRequest;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {

Item convertCreatedProductItemIntoItem(CreatedProductItem createdProductItem);

    Item convertCreatedServiceItemIntoItem(CreatedServiceItem createdServiceItem);

    ItemResponse convertItemIntoItemResponse(Item item);

    List<ItemResponse> convertItemListIntoItemResponseList(List<Item> items);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateItemFromDto(UpdateItemRequest updateItemRequest, @MappingTarget Item item);
}
