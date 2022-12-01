package tech.selmefy.hotel.repository.person;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;
import tech.selmefy.hotel.controller.person.dto.PersonDTO;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PersonCriteriaRepository {

    private final EntityManager entityManager;

    @SneakyThrows
    public TypedQuery<Person> personSearchQuery(String orderBy, String orderType, Optional<String> filterBy, Optional<String> filterValue) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<Person> personCriteriaQuery = cb.createQuery(Person.class);
        Root<Person> root = personCriteriaQuery.from(Person.class);
        personCriteriaQuery.select(root);

        if(orderType.equals("DESC")) {
            personCriteriaQuery.orderBy(cb.desc(root.get(orderBy)));
        } else {
            personCriteriaQuery.orderBy(cb.asc(root.get(orderBy)));
        }

        if (filterBy.isPresent() && filterValue.isPresent() && !filterBy.get().equals("") && !filterValue.get().equals("")) {
            // In case filtering by date of birth is used.
            if (PersonDTO.class.getDeclaredField(filterBy.get()).getType().equals(LocalDate.class)) {
                LocalDate filterValueAsDate = LocalDate.parse(filterValue.get());
                personCriteriaQuery.select(root).where(cb.between(root.get(filterBy.get()), filterValueAsDate, LocalDate.now()));

            // All other cases.
            } else {
                personCriteriaQuery.select(root).where(cb.like(cb.lower(root.get(filterBy.get())), filterValue.get().toLowerCase() + '%'));
            }
        }

        return entityManager.createQuery(personCriteriaQuery);
    }
}