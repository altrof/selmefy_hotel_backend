package tech.selmefy.hotel.repository.booking;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;
import tech.selmefy.hotel.controller.booking.dto.BookingDTO;
import tech.selmefy.hotel.controller.person.dto.PersonDTO;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BookingCriteriaRepository {

    private final EntityManager entityManager;

    @SneakyThrows
    public TypedQuery<Booking> bookingSearchQuery(String orderBy, String orderType, Optional<String> filterBy, Optional<String> filterValue) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<Booking> bookingCriteriaQuery = cb.createQuery(Booking.class);
        Root<Booking> root = bookingCriteriaQuery.from(Booking.class);
        bookingCriteriaQuery.select(root);

        if(orderType.equals("DESC")) {
            bookingCriteriaQuery.orderBy(cb.desc(root.get(orderBy)));
        } else {
            bookingCriteriaQuery.orderBy(cb.asc(root.get(orderBy)));
        }

        if (filterBy.isPresent() && filterValue.isPresent() && !filterBy.get().equals("") && !filterValue.get().equals("")) {
            // In case filtering by LocalDate is used.
            if (BookingDTO.class.getDeclaredField(filterBy.get()).getType().equals(LocalDate.class)) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate filterValueAsDate = LocalDate.parse(filterValue.get(), formatter);
                bookingCriteriaQuery.select(root).where(cb.between(root.get(filterBy.get()), filterValueAsDate, LocalDate.now()));

                // All other cases.
            } else {
                bookingCriteriaQuery.select(root).where(cb.like(cb.lower(root.get(filterBy.get())), filterValue.get().toLowerCase() + '%'));
            }
        }

        return entityManager.createQuery(bookingCriteriaQuery);
    }
}