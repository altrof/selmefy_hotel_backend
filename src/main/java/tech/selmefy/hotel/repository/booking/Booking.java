package tech.selmefy.hotel.repository.booking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.selmefy.hotel.repository.person.Person;
import tech.selmefy.hotel.repository.room.Room;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import java.time.LocalDate;

@Entity(name = "booking")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Booking {

    @ManyToOne
    @JoinColumn(name = "roomId", referencedColumnName = "id")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "personId", referencedColumnName = "id")
    @JoinColumn(name = "personIdentityCode", referencedColumnName = "identityCode")
    private Person person;

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, insertable = false, updatable = false)
    private Long roomId;

    @Column(nullable = false, insertable = false, updatable = false)
    private Long personId;

    @Column(nullable = false, insertable = false, updatable = false)
    private String personIdentityCode;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private LocalDate checkInDate;

    @Column(nullable = false)
    private LocalDate checkOutDate;

    @Column(length = 1000)
    private String comments;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean lateCheckOut;

}




