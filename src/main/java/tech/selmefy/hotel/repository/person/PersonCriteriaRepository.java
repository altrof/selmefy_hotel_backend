package tech.selmefy.hotel.repository.person;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PersonCriteriaRepository {

    private final EntityManager entityManager;

    public List<Person> personSearch(int pageNumber, int pageSize, String orderBy,
        Optional<String> filterBy, Optional<String> filterValue) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<Person> personCriteriaQuery = cb.createQuery(Person.class);
        Root<Person> root = personCriteriaQuery.from(Person.class);
        personCriteriaQuery.select(root);
        personCriteriaQuery.orderBy(cb.asc(root.get(orderBy)));
        if (filterBy.isPresent() && filterValue.isPresent()) {
            personCriteriaQuery.select(root).where(cb.like(cb.lower(root.get(filterBy.get())), filterValue.get().toLowerCase() + '%'));
        }

        Query query = entityManager.createQuery(personCriteriaQuery);
        query.setFirstResult(pageNumber * pageSize);
        query.setMaxResults(pageSize);

        return query.getResultList();
    }
}

