package tech.selmefy.hotel.repository.personinbooking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tech.selmefy.hotel.repository.person.Person;

import javax.transaction.Transactional;
import java.util.Optional;

public interface PersonInBookingRepository extends JpaRepository<PersonInBooking, Long> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO person_in_booking VALUES (:bookingId, :personId, :personIdentityCode)",
    nativeQuery = true)
    void addPersonInBooking(@Param("bookingId") long bookingId,
                            @Param("personId") long personId,
                            @Param("personIdentityCode") String personIdentityCode);
}
