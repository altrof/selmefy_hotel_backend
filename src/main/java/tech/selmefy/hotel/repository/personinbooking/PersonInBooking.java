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
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/** The primary key of this entity is composite.
* See https://www.baeldung.com/jpa-composite-primary-keys
* for implemention details.
 */
@Entity(name = "person_in_booking")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@IdClass(PersonInBookingId.class)
public class PersonInBooking {

/*
    public PersonInBooking(Booking booking, Person person) {
        this.booking = booking;
        this.person = person;
        this.bookingId = booking.getId();
        this.personId = person.getId();
        this.personIdentityCode = person.getIdentityCode();
    }*/

    @ManyToOne
    @JoinColumn(name = "bookingId", referencedColumnName = "id")
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "personId", referencedColumnName = "id")
    @JoinColumn(name = "personIdentityCode", referencedColumnName = "identityCode")
    private Person person;

    @Id
    @Column(nullable = false, insertable = false, updatable = false)
    private Long bookingId;

    @Id
    @Column(nullable = false, insertable = false, updatable = false)
    private Long personId;

    @Column(nullable = false, insertable = false, updatable = false)
    private String personIdentityCode;

}
