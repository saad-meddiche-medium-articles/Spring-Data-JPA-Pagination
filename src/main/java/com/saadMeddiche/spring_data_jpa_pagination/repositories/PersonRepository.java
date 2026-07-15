package com.saadMeddiche.spring_data_jpa_pagination.repositories;

import com.saadMeddiche.spring_data_jpa_pagination.entities.Person;
import com.saadMeddiche.spring_data_jpa_pagination.response.PersonProductCount;
import com.saadMeddiche.spring_data_jpa_pagination.response.PersonSummaryResponse;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Long> {

    // ================= Using Page =================

    @Query("""
           SELECT p.id, p.firstName, p.lastName, COUNT(pp.id)
           FROM Person p LEFT JOIN p.products pp
           GROUP BY p.id, p.firstName, p.lastName order by p.id
           """)
    Page<PersonSummaryResponse> summary(Pageable pageable);

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

    @Query(value =
            """
            SELECT p.id, p.firstName, p.lastName, 0 FROM Person p
            """)
    Page<PersonSummaryResponse> summaryWithoutCount(Pageable pageable);

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

    @Query(value =
            """
            SELECT p.id, p.firstName, p.lastName, COUNT(pp.id)
            FROM Person p LEFT JOIN p.products pp
            WHERE p.id in :personsIds
            GROUP BY p.id, p.firstName, p.lastName
            """)
    List<PersonSummaryResponse> improvedSlice(Collection<Long> personsIds);

    @Query("""
           SELECT p.id, p.firstName, p.lastName, 0 FROM Person p
           """)
    Slice<PersonSummaryResponse> sliceWithoutCount(Pageable pageable);

    // ================= Using Window =================

    Window<Person> findFirst10ByOrderByIdAsc(KeysetScrollPosition position);

    @Query(value =
            """
            SELECT p.id, COUNT(pp.id)
            FROM Person p LEFT JOIN p.products pp
            WHERE p.id in :personsIds
            GROUP BY p.id
            """)
    List<PersonProductCount> findPersonProductCount(List<Long> personsIds);

}