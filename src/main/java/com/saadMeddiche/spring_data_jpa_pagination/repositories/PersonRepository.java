package com.saadMeddiche.spring_data_jpa_pagination.repositories;

import com.saadMeddiche.spring_data_jpa_pagination.entities.Person;
import com.saadMeddiche.spring_data_jpa_pagination.response.PersonSummaryResponse;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Long>, CustomPersonRepository {

    // ================= Using Page =================

    @Query("""
           SELECT p.id, p.firstName, p.lastName, COUNT(pp.id)
           FROM Person p LEFT JOIN p.products pp
           GROUP BY p.id, p.firstName, p.lastName order by p.id
           """)
    Page<PersonSummaryResponse> page_(Pageable pageable);

    @Query(value =
            """
            SELECT p.id, p.firstName, p.lastName, COUNT(pp.id)
            FROM Person p LEFT JOIN p.products pp
            GROUP BY p.id, p.firstName, p.lastName order by p.id
            """,
            countQuery = "SELECT COUNT(p) FROM Person p")
    Page<PersonSummaryResponse> page_customCount(Pageable pageable);

    @Query(value =
           """
           SELECT p.id FROM Person p order by p.id
           """)
    List<Long> page_improved(Pageable pageable);

    @Query(value =
            """
            SELECT p.id, p.firstName, p.lastName, 0 FROM Person p
            """)
    Page<PersonSummaryResponse> page_withoutCount(Pageable pageable);

    @Query(value = """
            SELECT COUNT(p.id) FROM Person p
            """)
    long simpleCount();

    // ================= Using Slice =================

    @Query("""
           SELECT p.id, p.firstName, p.lastName, COUNT(pp.id)
           FROM Person p LEFT JOIN p.products pp
           GROUP BY p.id, p.firstName, p.lastName order by p.id
           """)
    Slice<PersonSummaryResponse> slice(Pageable pageable);

    @Query(value =
            """
            SELECT p.id FROM Person p
            """)
    Slice<Long> improvedSlice(Pageable pageable);

    @Query("""
           SELECT p.id, p.firstName, p.lastName, 0 FROM Person p
           """)
    Slice<PersonSummaryResponse> sliceWithoutCount(Pageable pageable);

    // ================= Using Window =================

    Window<Person> findFirst10ByOrderByIdAsc(KeysetScrollPosition position);

}