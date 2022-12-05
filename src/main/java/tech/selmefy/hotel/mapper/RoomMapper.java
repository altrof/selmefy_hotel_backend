package tech.selmefy.hotel.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.selmefy.hotel.controller.room.dto.RoomDTO;
import tech.selmefy.hotel.repository.room.Room;

import java.util.List;

@Mapper
public interface RoomMapper {

    RoomMapper INSTANCE = Mappers.getMapper(RoomMapper.class);
    RoomDTO toDTO(Room room);
    List<RoomDTO> toDTOList(List<Room> roomList);
    Room toEntity(RoomDTO roomDTO);
}
