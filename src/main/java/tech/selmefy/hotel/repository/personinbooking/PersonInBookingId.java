package tech.selmefy.hotel.repository.personinbooking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PersonInBookingId implements Serializable {
    private long bookingId;
    private long personId;
}
