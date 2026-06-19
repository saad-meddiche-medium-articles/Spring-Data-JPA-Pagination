package com.saadMeddiche.spring_data_jpa_pagination.repositories;

import com.saadMeddiche.spring_data_jpa_pagination.entities.Person;
import com.saadMeddiche.spring_data_jpa_pagination.response.PersonSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PersonRepository extends JpaRepository<Person, Long> {

    @Query("""
           SELECT p.id, p.firstName, p.lastName, COUNT(pp.id)
           FROM Person p LEFT JOIN p.products pp
           GROUP BY p.id, p.firstName, p.lastName order by p.id
           """)
    Page<PersonSummaryResponse> summary(Pageable pageable);

    @Query("""
           SELECT p.id, p.firstName, p.lastName, COUNT(pp.id)
           FROM Person p LEFT JOIN p.products pp
           GROUP BY p.id, p.firstName, p.lastName order by p.id
           """)
    Slice<PersonSummaryResponse> slice(Pageable pageable);

}