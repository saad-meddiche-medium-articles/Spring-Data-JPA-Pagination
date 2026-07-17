package com.saadMeddiche.spring_data_jpa_pagination.repositories;

import com.saadMeddiche.spring_data_jpa_pagination.entities.Person;
import com.saadMeddiche.spring_data_jpa_pagination.requests.PersonSummaryFilterRequest;
import com.saadMeddiche.spring_data_jpa_pagination.response.PersonSummaryResponse;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Long>, CustomPersonRepository {

    // ================= Using Page =================

    @Query("""
            SELECT p.id, p.firstName, p.lastName, p.birthDate, COUNT(pp.id)
            FROM Person p LEFT JOIN p.products pp
            WHERE (:#{#filter == null || #filter.fullName == null} = true
                   OR LOWER(CONCAT(p.firstName, ' ', p.lastName)) LIKE LOWER(CONCAT('%', :#{#filter.fullName}, '%')))
              AND (:#{#filter == null || #filter.birthDateFrom == null} = true
                   OR p.birthDate >= :#{#filter.birthDateFrom})
              AND (:#{#filter == null || #filter.birthDateTo == null} = true
                   OR p.birthDate <= :#{#filter.birthDateTo})
            GROUP BY p.id, p.firstName, p.lastName, p.birthDate ORDER BY p.id
            """)
    Page<PersonSummaryResponse> page_(Pageable pageable, @Param("filter") PersonSummaryFilterRequest filter);

    @Query(value =
            """
            SELECT p.id, p.firstName, p.lastName, p.birthDate, COUNT(pp.id)
            FROM Person p LEFT JOIN p.products pp
            WHERE (:#{#filter == null || #filter.fullName == null} = true
                   OR LOWER(CONCAT(p.firstName, ' ', p.lastName)) LIKE LOWER(CONCAT('%', :#{#filter.fullName}, '%')))
              AND (:#{#filter == null || #filter.birthDateFrom == null} = true
                   OR p.birthDate >= :#{#filter.birthDateFrom})
              AND (:#{#filter == null || #filter.birthDateTo == null} = true
                   OR p.birthDate <= :#{#filter.birthDateTo})
            GROUP BY p.id, p.firstName, p.lastName, p.birthDate ORDER BY p.id
            """,
            countQuery = """
                    SELECT COUNT(p)
                    FROM Person p
                    WHERE (:#{#filter == null || #filter.fullName == null} = true
                           OR LOWER(CONCAT(p.firstName, ' ', p.lastName)) LIKE LOWER(CONCAT('%', :#{#filter.fullName}, '%')))
                      AND (:#{#filter == null || #filter.birthDateFrom == null} = true
                           OR p.birthDate >= :#{#filter.birthDateFrom})
                      AND (:#{#filter == null || #filter.birthDateTo == null} = true
                           OR p.birthDate <= :#{#filter.birthDateTo})
                    """)
    Page<PersonSummaryResponse> page_customCount(Pageable pageable, @Param("filter") PersonSummaryFilterRequest filter);

    @Query(value =
           """
           SELECT p.id FROM Person p
           WHERE (:#{#filter == null || #filter.fullName == null} = true
                  OR LOWER(CONCAT(p.firstName, ' ', p.lastName)) LIKE LOWER(CONCAT('%', :#{#filter.fullName}, '%')))
             AND (:#{#filter == null || #filter.birthDateFrom == null} = true
                  OR p.birthDate >= :#{#filter.birthDateFrom})
             AND (:#{#filter == null || #filter.birthDateTo == null} = true
                  OR p.birthDate <= :#{#filter.birthDateTo})
           ORDER BY p.id
           """)
    List<Long> page_improved(Pageable pageable, @Param("filter") PersonSummaryFilterRequest filter);

    @Query(value =
            """
            SELECT p.id, p.firstName, p.lastName, p.birthDate, 0 FROM Person p
            WHERE (:#{#filter == null || #filter.fullName == null} = true
                   OR LOWER(CONCAT(p.firstName, ' ', p.lastName)) LIKE LOWER(CONCAT('%', :#{#filter.fullName}, '%')))
              AND (:#{#filter == null || #filter.birthDateFrom == null} = true
                   OR p.birthDate >= :#{#filter.birthDateFrom})
              AND (:#{#filter == null || #filter.birthDateTo == null} = true
                   OR p.birthDate <= :#{#filter.birthDateTo})
            ORDER BY p.id
            """)
    Page<PersonSummaryResponse> page_withoutCount(Pageable pageable, @Param("filter") PersonSummaryFilterRequest filter);

    @Query(value = """
            SELECT COUNT(p.id) FROM Person p
            WHERE (:#{#filter == null || #filter.fullName == null} = true
                   OR LOWER(CONCAT(p.firstName, ' ', p.lastName)) LIKE LOWER(CONCAT('%', :#{#filter.fullName}, '%')))
              AND (:#{#filter == null || #filter.birthDateFrom == null} = true
                   OR p.birthDate >= :#{#filter.birthDateFrom})
              AND (:#{#filter == null || #filter.birthDateTo == null} = true
                   OR p.birthDate <= :#{#filter.birthDateTo})
            """)
    long simpleCount(@Param("filter") PersonSummaryFilterRequest filter);

    // ================= Using Slice =================

    @Query("""
           SELECT p.id, p.firstName, p.lastName, p.birthDate, COUNT(pp.id)
            FROM Person p LEFT JOIN p.products pp
            WHERE (:#{#filter == null || #filter.fullName == null} = true
                   OR LOWER(CONCAT(p.firstName, ' ', p.lastName)) LIKE LOWER(CONCAT('%', :#{#filter.fullName}, '%')))
              AND (:#{#filter == null || #filter.birthDateFrom == null} = true
                   OR p.birthDate >= :#{#filter.birthDateFrom})
              AND (:#{#filter == null || #filter.birthDateTo == null} = true
                   OR p.birthDate <= :#{#filter.birthDateTo})
            GROUP BY p.id, p.firstName, p.lastName, p.birthDate ORDER BY p.id
           """)
    Slice<PersonSummaryResponse> slice(Pageable pageable, @Param("filter") PersonSummaryFilterRequest filter);

    @Query(value =
            """
            SELECT p.id FROM Person p
            WHERE (:#{#filter == null || #filter.fullName == null} = true
                   OR LOWER(CONCAT(p.firstName, ' ', p.lastName)) LIKE LOWER(CONCAT('%', :#{#filter.fullName}, '%')))
              AND (:#{#filter == null || #filter.birthDateFrom == null} = true
                   OR p.birthDate >= :#{#filter.birthDateFrom})
              AND (:#{#filter == null || #filter.birthDateTo == null} = true
                   OR p.birthDate <= :#{#filter.birthDateTo})
            ORDER BY p.id
            """)
    Slice<Long> slice_improved(Pageable pageable, @Param("filter") PersonSummaryFilterRequest filter);

    @Query("""
           SELECT p.id, p.firstName, p.lastName, p.birthDate, 0 FROM Person p
            WHERE (:#{#filter == null || #filter.fullName == null} = true
                   OR LOWER(CONCAT(p.firstName, ' ', p.lastName)) LIKE LOWER(CONCAT('%', :#{#filter.fullName}, '%')))
              AND (:#{#filter == null || #filter.birthDateFrom == null} = true
                   OR p.birthDate >= :#{#filter.birthDateFrom})
              AND (:#{#filter == null || #filter.birthDateTo == null} = true
                   OR p.birthDate <= :#{#filter.birthDateTo})
            ORDER BY p.id
           """)
    Slice<PersonSummaryResponse> slice_withoutCount(Pageable pageable, @Param("filter") PersonSummaryFilterRequest filter);

    // ================= Using Window =================

    // ================= Auto keyset
    Window<Person> findFirst10ByOrderByIdAsc(KeysetScrollPosition position);

    // ================= Manual keyset
    @Query("""
           SELECT p
           FROM Person p
           WHERE (:lastSeenId IS NULL OR p.id > :lastSeenId)
             AND (:#{#filter == null || #filter.fullName == null} = true
                  OR LOWER(CONCAT(p.firstName, ' ', p.lastName)) LIKE LOWER(CONCAT('%', :#{#filter.fullName}, '%')))
             AND (:#{#filter == null || #filter.birthDateFrom == null} = true
                  OR p.birthDate >= :#{#filter.birthDateFrom})
             AND (:#{#filter == null || #filter.birthDateTo == null} = true
                  OR p.birthDate <= :#{#filter.birthDateTo})
           ORDER BY p.id
           """)
    List<Person> findWindow(@Param("lastSeenId") Long lastSeenId,
                            @Param("filter") PersonSummaryFilterRequest filter,
                            Pageable pageable);

}
