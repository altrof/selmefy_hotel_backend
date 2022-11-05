package tech.selmefy.hotel.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.selmefy.hotel.controller.room.dto.RoomAvailableHistoryDTO;
import tech.selmefy.hotel.repository.room.RoomAvailableHistory;

@Mapper
public interface RoomAvailableHistoryMapper {

    RoomAvailableHistoryMapper INSTANCE = Mappers.getMapper(RoomAvailableHistoryMapper.class);

    RoomAvailableHistoryDTO toDTO(RoomAvailableHistory roomAvailableHistory);
}
