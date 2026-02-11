package com.example.techgicus_ebilling.techgicus_ebilling;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Company;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Item;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.StockTransaction;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.itemDto.CreatedProductItem;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.itemDto.ItemResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.itemDto.UpdateItemRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.exception.ItemAlreadyExitException;
import com.example.techgicus_ebilling.techgicus_ebilling.exception.ResourceNotFoundException;
import com.example.techgicus_ebilling.techgicus_ebilling.mapper.ItemMapper;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.CategoryRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.CompanyRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.ItemRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.StockTransactionRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.service.ItemService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    private static final Logger log = LoggerFactory.getLogger(ItemServiceTest.class);
    @InjectMocks
    private ItemService itemService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ItemMapper itemMapper;

    @Mock
    private StockTransactionRepository stockTransactionRepository;


    @Test
    void createdProductItemInCompany_shouldThrowException_whenCompanyNotFound() {

        CreatedProductItem request = new CreatedProductItem();

        when(companyRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> itemService.createdProductItemInCompany(request, 1L)
        );
    }


    @Test
    void createdProductItemInCompany_shouldThrowException_whenItemAlreadyExists() {

        Company company = new Company();
        CreatedProductItem request = new CreatedProductItem();
        request.setItemName("Laptop");

        when(companyRepository.findById(1L))
                .thenReturn(Optional.of(company));

        when(itemRepository.findByItemNameAndCompany("Laptop", company))
                .thenReturn(Optional.of(new Item()));

        assertThrows(
                ItemAlreadyExitException.class,
                () -> itemService.createdProductItemInCompany(request, 1L)
        );
    }

    @Test
    void createdProductItemInCompany_shouldCreateOpeningStockTransaction() {

        Company company = new Company();
        Item item = new Item();
        ItemResponse response = new ItemResponse();

        CreatedProductItem request = new CreatedProductItem();
        request.setItemName("Mouse");
        request.setStockOpeningQty(10.0);
        request.setStockPricePerQty(100.0);

        when(companyRepository.findById(1L))
                .thenReturn(Optional.of(company));

        when(itemRepository.findByItemNameAndCompany(any(), any()))
                .thenReturn(Optional.empty());

        when(itemMapper.convertCreatedProductItemIntoItem(request))
                .thenReturn(item);

        when(itemRepository.save(any(Item.class)))
                .thenReturn(item);

        when(itemMapper.convertItemIntoItemResponse(item))
                .thenReturn(response);

        ItemResponse result =
                itemService.createdProductItemInCompany(request, 1L);

        assertNotNull(result);
        verify(stockTransactionRepository, times(1))
                .save(any(StockTransaction.class));
    }





}
