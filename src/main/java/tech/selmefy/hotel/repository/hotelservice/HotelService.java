package tech.selmefy.hotel.repository.hotelservice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "hotel_service")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class HotelService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short id;

    @Column(nullable = false, unique = true, length = 100)
    private String hotelServiceName;

}
