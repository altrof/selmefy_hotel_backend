package tech.selmefy.hotel.repository.personinbooking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface PersonInBookingRepository extends JpaRepository<PersonInBooking, Long> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO person_in_booking VALUES (:bookingId, :personId, :personIdentityCode)",
    nativeQuery = true)
    void addPersonInBooking(@Param("bookingId") long bookingId,
                            @Param("personId") long personId,
                            @Param("personIdentityCode") String personIdentityCode);
}
