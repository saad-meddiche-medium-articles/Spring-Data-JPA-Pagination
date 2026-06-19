package com.saadMeddiche.spring_data_jpa_pagination.repositories;

import com.saadMeddiche.spring_data_jpa_pagination.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> { }