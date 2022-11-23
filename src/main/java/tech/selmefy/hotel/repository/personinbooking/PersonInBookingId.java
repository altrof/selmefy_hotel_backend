package tech.selmefy.hotel.repository.personinbooking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PersonInBookingId implements Serializable {
    private Long bookingId;
    private Long personId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonInBookingId personInBookingId = (PersonInBookingId) o;
        return bookingId.equals(personInBookingId.bookingId) &&
                personId.equals(personInBookingId.personId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookingId, personId);
    }
}
