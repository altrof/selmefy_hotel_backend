package tech.selmefy.hotel.service.hotelservice;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tech.selmefy.hotel.controller.hotelservice.dto.HotelServiceDTO;
import tech.selmefy.hotel.mapper.HotelServiceMapper;
import tech.selmefy.hotel.repository.hotelservice.HotelServiceRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class HotelServiceService {

    public final HotelServiceRepository hotelServiceRepository;

    public List<HotelServiceDTO> getAllHotelServices() {
        return hotelServiceRepository.findAll()
                .stream()
                .map(HotelServiceMapper.INSTANCE::toDTO)
                .toList();
    }
}
