package tech.selmefy.hotel.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.selmefy.hotel.controller.person.dto.PersonDTO;
import tech.selmefy.hotel.repository.person.Person;

import java.util.List;

@Mapper
public interface PersonMapper {

    PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);
    PersonDTO toDTO(Person person);

    List<PersonDTO> toDTOList(List<Person> personList);
    Person toEntity(PersonDTO personDTO);
}
