package tech.selmefy.hotel.repository.personinbooking;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.selmefy.hotel.repository.booking.Booking;
import tech.selmefy.hotel.repository.person.Person;
import tech.selmefy.hotel.repository.room.Room;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "person_in_booking")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PersonInBooking {

// composite id https://www.baeldung.com/jpa-composite-primary-keys
//    CONSTRAINT pk_person_in_booking PRIMARY KEY (booking_id, person_id),
    @ManyToOne
    @JoinColumn(name = "bookingId", referencedColumnName = "id")
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "personId", referencedColumnName = "id")
    @JoinColumn(name = "personIdentityCode", referencedColumnName = "identityCode")
    private Person person;

    @Column(nullable = false, insertable = false, updatable = false)
    private Long bookingId;

    @Column(nullable = false, insertable = false, updatable = false)
    private Long personId;

    @Column(nullable = false, insertable = false, updatable = false)
    private String personIdentityCode;

}
