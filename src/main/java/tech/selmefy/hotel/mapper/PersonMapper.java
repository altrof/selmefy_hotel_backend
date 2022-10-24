package tech.selmefy.hotel.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.selmefy.hotel.controller.room.dto.PersonDTO;
import tech.selmefy.hotel.repository.person.Person;

@Mapper
public interface PersonMapper {

    PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);
    PersonDTO toDTO(Person person);
}
