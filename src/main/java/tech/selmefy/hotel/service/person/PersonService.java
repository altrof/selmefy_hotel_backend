package tech.selmefy.hotel.service.person;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tech.selmefy.hotel.controller.room.dto.PersonDTO;
import tech.selmefy.hotel.mapper.PersonMapper;
import tech.selmefy.hotel.repository.person.Person;
import tech.selmefy.hotel.repository.person.PersonRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class PersonService {
    public final PersonRepository personRepository;

    public List<PersonDTO> getAllPeople() {
        List<Person> people = personRepository.findAll();
        List<PersonDTO> personDTOList = new ArrayList<>();
        for (Person person : people) {
            PersonDTO personDTO = PersonMapper.INSTANCE.toDTO(person);
            personDTOList.add(personDTO);
        }
        return personDTOList;
    }
}
