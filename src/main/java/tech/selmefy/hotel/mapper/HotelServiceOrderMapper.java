package tech.selmefy.hotel.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.selmefy.hotel.controller.hotelserviceorder.dto.HotelServiceOrderDTO;
import tech.selmefy.hotel.repository.hotelserviceorder.HotelServiceOrder;

@Mapper
public interface HotelServiceOrderMapper {
    HotelServiceOrderMapper INSTANCE = Mappers.getMapper(HotelServiceOrderMapper.class);
    HotelServiceOrderDTO toDTO(HotelServiceOrder hotelServiceOrder);
}
