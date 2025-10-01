package com.example.techgicus_ebilling.techgicus_ebilling.repository;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
}
