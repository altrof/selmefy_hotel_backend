package tech.selmefy.hotel.repository.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BookingCriteriaRepositoryTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    CriteriaBuilder criteriaBuilder;

    @Mock
    Root<Booking> root;

    @Mock
    CriteriaQuery<Booking> bookingCriteriaQuery;

    @InjectMocks
    private BookingCriteriaRepository bookingCriteriaRepository;

    @Test
    void bookingSearchQuery_defaultRunsProperly() {

        // given
        BDDMockito.given(bookingCriteriaQuery.from(Booking.class)).willReturn(root);
        BDDMockito.given(criteriaBuilder.createQuery(Booking.class)).willReturn(bookingCriteriaQuery);
        BDDMockito.given(entityManager.getCriteriaBuilder()).willReturn(criteriaBuilder);


        // then
        assertDoesNotThrow(() -> {
            bookingCriteriaRepository.bookingSearchQuery(
                    "price",
                    "ASC",
                    Optional.empty(),
                    Optional.empty());

        });
    }

    @Test
    void bookingSearchQuery_orderTypeDESCRunsProperly() {


        // given
        BDDMockito.given(bookingCriteriaQuery.from(Booking.class)).willReturn(root);
        BDDMockito.given(criteriaBuilder.createQuery(Booking.class)).willReturn(bookingCriteriaQuery);
        BDDMockito.given(entityManager.getCriteriaBuilder()).willReturn(criteriaBuilder);


        // then
        assertDoesNotThrow(() -> {
            bookingCriteriaRepository.bookingSearchQuery(
                    "price",
                    "DESC",
                    Optional.empty(),
                    Optional.empty());

        });
    }

    @Test
    void bookingSearchQuery_filteringWithLocalDateRunsProperly() {

        // given
        BDDMockito.given(bookingCriteriaQuery.select(root)).willReturn(bookingCriteriaQuery);
        BDDMockito.given(bookingCriteriaQuery.from(Booking.class)).willReturn(root);
        BDDMockito.given(criteriaBuilder.createQuery(Booking.class)).willReturn(bookingCriteriaQuery);
        BDDMockito.given(entityManager.getCriteriaBuilder()).willReturn(criteriaBuilder);

        // then
        assertDoesNotThrow(() -> {
            bookingCriteriaRepository.bookingSearchQuery(
                    "price",
                    "ASC",
                    Optional.of("checkInDate"),
                    Optional.of("01/12/2024"));

        });
    }

    @Test
    void bookingSearchQuery_filteringWithIntRunsProperly() {

        // given
        BDDMockito.given(bookingCriteriaQuery.select(root)).willReturn(bookingCriteriaQuery);
        BDDMockito.given(bookingCriteriaQuery.from(Booking.class)).willReturn(root);
        BDDMockito.given(criteriaBuilder.createQuery(Booking.class)).willReturn(bookingCriteriaQuery);
        BDDMockito.given(entityManager.getCriteriaBuilder()).willReturn(criteriaBuilder);

        // then
        assertDoesNotThrow(() -> {
            bookingCriteriaRepository.bookingSearchQuery(
                    "price",
                    "ASC",
                    Optional.of("price"),
                    Optional.of("50"));

        });
    }

    @Test
    void bookingSearchQuery_filteringWithBooleanRunsProperly() {

        // given
        BDDMockito.given(bookingCriteriaQuery.select(root)).willReturn(bookingCriteriaQuery);
        BDDMockito.given(bookingCriteriaQuery.from(Booking.class)).willReturn(root);
        BDDMockito.given(criteriaBuilder.createQuery(Booking.class)).willReturn(bookingCriteriaQuery);
        BDDMockito.given(entityManager.getCriteriaBuilder()).willReturn(criteriaBuilder);

        // then
        assertDoesNotThrow(() -> {
            bookingCriteriaRepository.bookingSearchQuery(
                    "price",
                    "ASC",
                    Optional.of("lateCheckOut"),
                    Optional.of("false"));

        });
    }

    @Test
    void bookingSearchQuery_filteringWithStringRunsProperly() {

        // given
        BDDMockito.given(bookingCriteriaQuery.select(root)).willReturn(bookingCriteriaQuery);
        BDDMockito.given(bookingCriteriaQuery.from(Booking.class)).willReturn(root);
        BDDMockito.given(criteriaBuilder.createQuery(Booking.class)).willReturn(bookingCriteriaQuery);
        BDDMockito.given(entityManager.getCriteriaBuilder()).willReturn(criteriaBuilder);

        // then
        assertDoesNotThrow(() -> {
            bookingCriteriaRepository.bookingSearchQuery(
                    "price",
                    "ASC",
                    Optional.of("comments"),
                    Optional.of("Hello"));

        });
    }
}