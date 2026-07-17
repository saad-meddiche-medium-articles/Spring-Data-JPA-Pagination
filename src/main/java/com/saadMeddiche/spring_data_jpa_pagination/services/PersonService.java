package com.saadMeddiche.spring_data_jpa_pagination.services;

import com.saadMeddiche.spring_data_jpa_pagination.entities.Person;
import com.saadMeddiche.spring_data_jpa_pagination.entities.Person_;
import com.saadMeddiche.spring_data_jpa_pagination.repositories.PersonRepository;
import com.saadMeddiche.spring_data_jpa_pagination.requests.PersonSummaryFilterRequest;
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

    public Page<PersonSummaryResponse> page_(Pageable pageable, PersonSummaryFilterRequest filter) {
        return personRepository.page_(pageable, filter);
    }

    public Page<PersonSummaryResponse> page_customCount(Pageable pageable, PersonSummaryFilterRequest filter) {
        return personRepository.page_customCount(pageable, filter);
    }

    public Page<PersonSummaryResponse> page_improved(Pageable pageable, PersonSummaryFilterRequest filter) {

        List<Long> personIds = personRepository.page_improved(pageable, filter);

        List<PersonSummaryResponse> content = personRepository.findSummaryByIds(personIds);

        long totalPersons = personRepository.simpleCount(filter);

        return new PageImpl<>(content, pageable , totalPersons);

    }

    public Page<PersonSummaryResponse> page_withoutCount(Pageable pageable, PersonSummaryFilterRequest filter) {
        return personRepository.page_withoutCount(pageable, filter);
    }

    // ================= Using Slice =================

    public Slice<PersonSummaryResponse> slice(Pageable pageable, PersonSummaryFilterRequest filter) {
        return personRepository.slice(pageable, filter);
    }

    public Slice<PersonSummaryResponse> slice_improved(Pageable pageable, PersonSummaryFilterRequest filter) {

        Slice<Long> slicePersonIds = personRepository.slice_improved(pageable, filter);

        List<PersonSummaryResponse> content = personRepository.findSummaryByIds(slicePersonIds.getContent());

        return new SliceImpl<>(content, pageable, slicePersonIds.hasNext());

    }

    public Slice<PersonSummaryResponse> slice_withoutCount(Pageable pageable, PersonSummaryFilterRequest filter) {
        return personRepository.slice_withoutCount(pageable, filter);
    }

    // ================= Using Window =================

    // ================= Auto keyset
    public Window<PersonSummaryResponse> window_autoKeyset(Long lastSeenId) {

        KeysetScrollPosition position = ScrollPosition.keyset();

        if (lastSeenId != null) {
            position = ScrollPosition.of(
                    Map.of(Person_.ID, lastSeenId),
                    ScrollPosition.Direction.FORWARD
            );
        }

        Window<Person> personWindow = personRepository.findFirst10ByOrderByIdAsc(position);

        List<Long> personIds = personWindow.getContent().stream().map(Person::getId).toList();

        Map<Long, PersonSummaryResponse> countMap = personRepository.findSummaryByIds(personIds).stream()
                .collect(Collectors.toMap(PersonSummaryResponse::id, response -> response));

        return personWindow.map(person ->
                countMap.getOrDefault(person.getId(), new PersonSummaryResponse(-1L, "N/A", "N/A", null, -1L))
        );

    }

    // ================= Manual keyset
    public Window<PersonSummaryResponse> window_manualKeyset(Long lastSeenId, PersonSummaryFilterRequest filter) {

        // Request one extra row to detect whether there is a next window
        Pageable pageable = PageRequest.of(0, 11);
        List<Person> persons = personRepository.findWindow(lastSeenId, filter, pageable);

        boolean hasNext = persons.size() > 10;
        List<Person> windowContent = hasNext ? persons.subList(0, 10) : persons;

        Window<Person> personWindow = Window.from(
                windowContent,
                index -> ScrollPosition.of(
                        Map.of(Person_.ID, windowContent.get(index).getId()),
                        ScrollPosition.Direction.FORWARD
                ),
                hasNext
        );

        List<Long> personIds = persons.stream().map(Person::getId).toList();

        Map<Long, PersonSummaryResponse> countMap = personRepository.findSummaryByIds(personIds).stream()
                .collect(Collectors.toMap(PersonSummaryResponse::id, response -> response));

        return personWindow.map(person ->
                countMap.getOrDefault(person.getId(), new PersonSummaryResponse(-1L, "N/A", "N/A", null, -1L))
        );

    }

}
