package tech.selmefy.hotel.repository.hotelserviceorder;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelServiceOrderRepository extends JpaRepository<HotelServiceOrder, Long> {
}
