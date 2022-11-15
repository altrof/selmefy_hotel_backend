package tech.selmefy.hotel.repository.personinbooking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tech.selmefy.hotel.repository.person.Person;

import java.util.Optional;

public interface PersonInBookingRepository extends JpaRepository<PersonInBooking, Long> {
/*
    // siit edasi https://www.baeldung.com/jpa-query-parameters
    @Query(value = "SELECT * FROM Person WHERE person.identity_code = :identityCode",
            nativeQuery = true)
    Optional<Person> findPersonByIdentityCode(@Param("identityCode") String identityCode);*/
}
