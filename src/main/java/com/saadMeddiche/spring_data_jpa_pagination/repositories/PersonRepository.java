package com.saadMeddiche.spring_data_jpa_pagination.repositories;

import com.saadMeddiche.spring_data_jpa_pagination.entities.Person;
import com.saadMeddiche.spring_data_jpa_pagination.response.PersonSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

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

    @Query(value =
            """
            SELECT p.id, p.firstName, p.lastName, COUNT(pp.id)
            FROM Person p LEFT JOIN p.products pp
            GROUP BY p.id, p.firstName, p.lastName order by p.id
            """,
            countQuery = "SELECT COUNT(p) FROM Person p")
    Page<PersonSummaryResponse> summaryCustomCount(Pageable pageable);

    @Query(value =
           """
           SELECT p.id FROM Person p
           """)
    List<Long> improvedSummary(Pageable pageable);

    @Query(value =
            """
            SELECT p.id, p.firstName, p.lastName, COUNT(pp.id)
            FROM Person p LEFT JOIN p.products pp
            WHERE p.id in :personsIds
            GROUP BY p.id, p.firstName, p.lastName
            """)
    List<PersonSummaryResponse> improvedSummary(Collection<Long> personsIds);

    @Query(value = """
            SELECT COUNT(p.id) FROM Person p
            """)
    long simpleCount();

    @Query(value =
            """
            SELECT p.id FROM Person p
            """)
    Slice<Long> improvedSlice(Pageable pageable);

    @Query(value =
            """
            SELECT p.id, p.firstName, p.lastName, COUNT(pp.id)
            FROM Person p LEFT JOIN p.products pp
            WHERE p.id in :personsIds
            GROUP BY p.id, p.firstName, p.lastName
            """)
    List<PersonSummaryResponse> improvedSlice(Collection<Long> personsIds);

}