package tech.selmefy.hotel.repository.booking;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;
import tech.selmefy.hotel.controller.booking.dto.BookingDTO;
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

            // Filtering by LocalDate
            if (BookingDTO.class.getDeclaredField(filterBy.get()).getType().equals(LocalDate.class)) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate filterValueAsDate = LocalDate.parse(filterValue.get(), formatter);

                // For now, this filtering option only provides dates from present to the given date.
                // In the future, it would be appropriate to filter a range between two dates.
                bookingCriteriaQuery.select(root).where(cb.between(root.get(filterBy.get()), LocalDate.now(), filterValueAsDate));

            // Filtering by int
            } else if (BookingDTO.class.getDeclaredField(filterBy.get()).getType().equals(int.class)) {

                // For now, this filtering option provides values from 0 to given value.
                bookingCriteriaQuery.select(root).where(cb.between(root.get(filterBy.get()), 0, Integer.parseInt(filterValue.get())));

            // Filtering by boolean
            } else if (BookingDTO.class.getDeclaredField(filterBy.get()).getType().equals(boolean.class)) {
                boolean filterValueAsBoolean = Boolean.parseBoolean(filterValue.get());
                bookingCriteriaQuery.select(root).where(cb.equal(root.get(filterBy.get()), filterValueAsBoolean));

            // Filtering by string
            } else {
                bookingCriteriaQuery.select(root).where(cb.like(cb.lower(root.get(filterBy.get())), filterValue.get().toLowerCase() + '%'));
            }
        }

        return entityManager.createQuery(bookingCriteriaQuery);
    }
}