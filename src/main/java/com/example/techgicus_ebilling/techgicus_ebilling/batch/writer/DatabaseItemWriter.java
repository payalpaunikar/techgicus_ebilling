package com.example.techgicus_ebilling.techgicus_ebilling.batch.writer;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Sale;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.SaleItem;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.SaleItemRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.SaleRepository;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DatabaseItemWriter implements ItemWriter<Object> {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private SaleItemRepository saleItemRepository;

    @Override
    public void write(Chunk<?> chunk) throws Exception {
        for (Object item : chunk) {
            if (item instanceof Sale) {
                saleRepository.save((Sale) item);
            } else if (item instanceof SaleItem) {
                saleItemRepository.save((SaleItem) item);
            }
            // Add more types later (Purchase, etc.)
        }

        // Add other repos if you import purchases later

    }



}
