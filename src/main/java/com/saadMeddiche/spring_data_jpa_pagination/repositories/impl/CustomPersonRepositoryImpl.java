package com.saadMeddiche.spring_data_jpa_pagination.repositories.impl;

import com.saadMeddiche.spring_data_jpa_pagination.entities.Person;
import com.saadMeddiche.spring_data_jpa_pagination.entities.Person_;
import com.saadMeddiche.spring_data_jpa_pagination.entities.Product;
import com.saadMeddiche.spring_data_jpa_pagination.entities.Product_;
import com.saadMeddiche.spring_data_jpa_pagination.repositories.CustomPersonRepository;
import com.saadMeddiche.spring_data_jpa_pagination.response.PersonSummaryResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;

import java.util.Collection;
import java.util.List;

public class CustomPersonRepositoryImpl implements CustomPersonRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<PersonSummaryResponse> findSummaryByIds(Collection<Long> personIds) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<PersonSummaryResponse> criteriaQuery = criteriaBuilder.createQuery(PersonSummaryResponse.class);

        Root<Person> personRoot = criteriaQuery.from(Person.class);
        Join<Person, Product> product = personRoot.join(Person_.products, JoinType.LEFT);

        criteriaQuery.select(
                criteriaBuilder.construct(
                        PersonSummaryResponse.class,
                        personRoot.get(Person_.ID),
                        personRoot.get(Person_.FIRST_NAME),
                        personRoot.get(Person_.LAST_NAME),
                        criteriaBuilder.count(product.get(Product_.ID))
                )
        );

        criteriaQuery.where(
                personRoot.get(Person_.ID).in(personIds)
        );

        criteriaQuery.groupBy(
                personRoot.get(Person_.ID),
                personRoot.get(Person_.FIRST_NAME),
                personRoot.get(Person_.LAST_NAME)
        );

        return entityManager.createQuery(criteriaQuery).getResultList();

    }

}