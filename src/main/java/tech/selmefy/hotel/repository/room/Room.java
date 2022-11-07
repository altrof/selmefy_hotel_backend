package tech.selmefy.hotel.repository.room;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.selmefy.hotel.service.room.type.RoomType;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "room")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Float size;

    private int floorId;

    private int roomNumber;

    private int numberOfBeds;

    @Enumerated(EnumType.STRING)
    private RoomType roomType;

    private Boolean roomAvailableForBooking;

}
