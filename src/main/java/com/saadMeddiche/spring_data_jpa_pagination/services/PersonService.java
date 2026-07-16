package com.saadMeddiche.spring_data_jpa_pagination.services;

import com.saadMeddiche.spring_data_jpa_pagination.entities.Person;
import com.saadMeddiche.spring_data_jpa_pagination.repositories.PersonRepository;
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

    public Page<PersonSummaryResponse> page_(Pageable pageable) {
        return personRepository.page_(pageable);
    }

    public Page<PersonSummaryResponse> page_customCount(Pageable pageable) {
        return personRepository.page_customCount(pageable);
    }

    public Page<PersonSummaryResponse> page_improved(Pageable pageable) {

        List<Long> personIds = personRepository.page_improved(pageable);

        List<PersonSummaryResponse> content = personRepository.findSummaryByIds(personIds);

        long totalPersons = personRepository.simpleCount();

        return new PageImpl<>(content, pageable , totalPersons);

    }

    public Page<PersonSummaryResponse> page_withoutCount(Pageable pageable) {
        return personRepository.page_withoutCount(pageable);
    }

    // ================= Using Slice =================

    public Slice<PersonSummaryResponse> slice(Pageable pageable) {
        return personRepository.slice(pageable);
    }

    public Slice<PersonSummaryResponse> improvedSlice(Pageable pageable) {

        Slice<Long> slicePersonIds = personRepository.improvedSlice(pageable);

        List<PersonSummaryResponse> content = personRepository.findSummaryByIds(slicePersonIds.getContent());

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

        Map<Long, PersonSummaryResponse> countMap = personRepository.findSummaryByIds(personIds).stream()
                .collect(Collectors.toMap(PersonSummaryResponse::id, response -> response));

        return personWindow.map(person ->
                countMap.getOrDefault(person.getId(), new PersonSummaryResponse(-1L, "N/A", "N/A", -1L))
        );

    }

}