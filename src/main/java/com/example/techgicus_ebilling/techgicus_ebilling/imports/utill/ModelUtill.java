package com.example.techgicus_ebilling.techgicus_ebilling.imports.utill;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Category;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Company;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Item;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Party;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.ItemType;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.dto.PurchaseItemRow;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.dto.SaleItemRow;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.CategoryRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.ItemRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.PartyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Component
public class ModelUtill {

    private final PartyRepository partyRepository;

    @Autowired
    public ModelUtill(PartyRepository partyRepository) {
        this.partyRepository = partyRepository;
    }

    public  Party findOrCreateParty(String name, String phone, Company company) {
        if (name == null || name.trim().isEmpty()) return null;

        Optional<Party> existing = partyRepository.findByNameAndPhoneNo(
                name.trim(),
                phone != null ? phone.trim() : null
        );

        if (existing.isPresent()) {
            return existing.get();
        }

        // create new
        Party party = new Party();
        party.setName(name.trim());
        party.setCompany(company);
        if (phone != null) party.setPhoneNo(phone.trim());
        return partyRepository.save(party);
    }

    public  Party findOrCreateParty(String name,Company company) {
        if (name == null || name.trim().isEmpty()) return null;

        Optional<Party> existing = partyRepository.findByName(
                name.trim()
                // phone != null ? phone.trim() : null
        );

        if (existing.isPresent()) {
            return existing.get();
        }

        // create new
        Party party = new Party();
        party.setName(name.trim());
        party.setCompany(company);
        // if (phone != null) party.setPhoneNo(phone.trim());
        return partyRepository.save(party);
    }


    public static Item findOrCreateItem(
            SaleItemRow row,
            Company company,
            ItemRepository itemRepository,
            CategoryRepository categoryRepository
    ) {
        Optional<Item> existing = itemRepository.findByItemNameAndCompany(
                row.getItemName(), company);

        if (existing.isPresent()) {
            return existing.get();
        }

        Item newItem = new Item();
        newItem.setItemName(row.getItemName());
        newItem.setItemCode(row.getItemCode());
        newItem.setItemHsn(row.getHsn());
        newItem.setTaxRate(row.getTaxPercentage());
        newItem.setBaseUnit(row.getUnit());
        newItem.setCompany(company);
        newItem.setCreatedAt(LocalDateTime.now());
        newItem.setUpdatedAt(LocalDateTime.now());
        newItem.setItemType(ItemType.PRODUCT);

        if (row.getCategory() != null) {
            Category category = categoryRepository.findByCategoryNameAndCompany(
                    row.getCategory(), company);

            if (category == null) {
                category = new Category();
                category.setCategoryName(row.getCategory());
                category.setCompany(company);
                category.setCraetedAt(LocalDateTime.now());
                category.setUpdatedAt(LocalDateTime.now());
                category = categoryRepository.save(category);
            }

            newItem.setCategories(Set.of(category));
        }

        return itemRepository.save(newItem);
    }


    public static Item findOrCreateItem(
            PurchaseItemRow row,
            Company company,
            ItemRepository itemRepository,
            CategoryRepository categoryRepository
    ) {
        Optional<Item> existing = itemRepository.findByItemNameAndCompany(
                row.getItemName(), company);

        if (existing.isPresent()) {
            return existing.get();
        }

        Item newItem = new Item();
        newItem.setItemName(row.getItemName());
        newItem.setItemCode(row.getItemCode());
        newItem.setItemHsn(row.getHsn());
        newItem.setTaxRate(row.getTaxPercentage());
        newItem.setBaseUnit(row.getUnit());
        newItem.setCompany(company);
        newItem.setCreatedAt(LocalDateTime.now());
        newItem.setUpdatedAt(LocalDateTime.now());
        newItem.setItemType(ItemType.PRODUCT);

        if (row.getCategory() != null) {
            Category category = categoryRepository.findByCategoryNameAndCompany(
                    row.getCategory(), company);

            if (category == null) {
                category = new Category();
                category.setCategoryName(row.getCategory());
                category.setCompany(company);
                category.setCraetedAt(LocalDateTime.now());
                category.setUpdatedAt(LocalDateTime.now());
                category = categoryRepository.save(category);
            }

            newItem.setCategories(Set.of(category));
        }

        return itemRepository.save(newItem);
    }
}
