package com.example.techgicus_ebilling.techgicus_ebilling.repository;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Feature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FeatureRepository extends JpaRepository<Feature,Long> {
    Optional<Feature> findByCode(String code);
}
