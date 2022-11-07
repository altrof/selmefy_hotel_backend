package tech.selmefy.hotel.repository.room;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity(name = "room_available_status_history")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RoomAvailableHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long roomId;

    private Boolean roomAvailableForBooking;

    private Timestamp createdDtime;
}
