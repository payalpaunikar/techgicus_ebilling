package com.example.techgicus_ebilling.techgicus_ebilling.repository;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Category;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
    List<Category> findAllByCompany(Company company);

    @Query(value = "SELECT * From category where category_id IN(:ids)",nativeQuery = true)
    List<Category> findCategoriesByIds(@Param("ids")List<Long> ids);
}
