package com.saadMeddiche.spring_data_jpa_pagination.services;

import com.saadMeddiche.spring_data_jpa_pagination.entities.Person;
import com.saadMeddiche.spring_data_jpa_pagination.repositories.PersonRepository;
import com.saadMeddiche.spring_data_jpa_pagination.response.PersonProductCount;
import com.saadMeddiche.spring_data_jpa_pagination.response.PersonSummaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;

    // ================= Using Page =================

    public Page<PersonSummaryResponse> summary(Pageable pageable) {
        return personRepository.summary(pageable);
    }

    public Page<PersonSummaryResponse> summaryCustomCount(Pageable pageable) {
        return personRepository.summaryCustomCount(pageable);
    }

    public Page<PersonSummaryResponse> improvedSummary(Pageable pageable) {

        List<Long> personIds = personRepository.improvedSummary(pageable);

        List<PersonSummaryResponse> content = personRepository.improvedSummary(personIds);

        long totalPersons = personRepository.simpleCount();

        return new PageImpl<>(content, pageable , totalPersons);

    }

    public Page<PersonSummaryResponse> summaryWithoutCount(Pageable pageable) {
        return personRepository.summaryWithoutCount(pageable);
    }

    // ================= Using Slice =================

    public Slice<PersonSummaryResponse> slice(Pageable pageable) {
        return personRepository.slice(pageable);
    }

    public Slice<PersonSummaryResponse> improvedSlice(Pageable pageable) {

        Slice<Long> slicePersonIds = personRepository.improvedSlice(pageable);

        List<PersonSummaryResponse> content = personRepository.improvedSlice(slicePersonIds.getContent());

        return new SliceImpl<>(content, pageable, slicePersonIds.hasNext());

    }

    public Slice<PersonSummaryResponse> sliceWithoutCount(Pageable pageable) {
        return personRepository.sliceWithoutCount(pageable);
    }

    // ================= Using Window =================

    public Window<PersonSummaryResponse> window(Long lastSeenId) {

        KeysetScrollPosition position = ScrollPosition.keyset();

        if (lastSeenId != null) {
            position = ScrollPosition.of(
                    Map.of("id", lastSeenId),
                    ScrollPosition.Direction.FORWARD
            );
        }

        Window<Person> personWindow = personRepository.findFirst10ByOrderByIdAsc(position);

        List<Long> personIds = personWindow.getContent().stream().map(Person::getId).toList();

        Map<Long, Long> countMap = personRepository.findPersonProductCount(personIds).stream()
                .collect(Collectors.toMap(PersonProductCount::personsId, PersonProductCount::productCount));

        return personWindow.map(person ->
                new PersonSummaryResponse(person.getId(), person.getFirstName(), person.getLastName(), countMap.getOrDefault(person.getId(), 0L))
        );

    }

}