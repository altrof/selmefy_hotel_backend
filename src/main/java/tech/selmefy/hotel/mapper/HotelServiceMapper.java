package tech.selmefy.hotel.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.selmefy.hotel.controller.hotelservice.dto.HotelServiceDTO;
import tech.selmefy.hotel.repository.hotelservice.HotelService;

@Mapper
public interface HotelServiceMapper {

    HotelServiceMapper INSTANCE = Mappers.getMapper(HotelServiceMapper.class);

    HotelServiceDTO toDTO(HotelService hotelService);
}
