package tech.selmefy.hotel.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.selmefy.hotel.controller.booking.dto.BookingDTO;
import tech.selmefy.hotel.repository.booking.Booking;

import java.util.List;

@Mapper
public interface BookingMapper {
    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);
    BookingDTO toDTO(Booking booking);

    List<BookingDTO> toDTOList(List<Booking> bookingList);

    Booking toEntity(BookingDTO bookingDTO);
}
