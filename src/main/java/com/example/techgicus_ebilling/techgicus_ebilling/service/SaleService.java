package com.example.techgicus_ebilling.techgicus_ebilling.service;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Sale;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleDto.SaleRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.mapper.SaleMapper;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.SaleItemRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SaleService {

       private SaleRepository saleRepository;
       private SaleItemRepository saleItemRepository;
       private SaleMapper saleMapper;

    @Autowired
    public SaleService(SaleRepository saleRepository, SaleItemRepository saleItemRepository, SaleMapper saleMapper) {
        this.saleRepository = saleRepository;
        this.saleItemRepository = saleItemRepository;
        this.saleMapper = saleMapper;
    }

    public void createdSale(SaleRequest saleRequest){
        Sale sale = saleMapper.convertSaleRequestIntoSale(saleRequest);
        saleRepository.save(sale);
    }

}
