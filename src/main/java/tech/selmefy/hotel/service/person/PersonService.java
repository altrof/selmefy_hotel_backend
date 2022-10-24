package tech.selmefy.hotel.service.person;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import tech.selmefy.hotel.controller.room.dto.RoomDTO;
import tech.selmefy.hotel.mapper.RoomMapper;
import tech.selmefy.hotel.repository.person.Person;
import tech.selmefy.hotel.repository.person.PersonRepository;
import tech.selmefy.hotel.repository.room.Room;
import tech.selmefy.hotel.repository.room.RoomRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class PersonService {

    public final PersonRepository personRepository;

    public List<Person> getAllPeople() {
        List<Person> people = personRepository.findAll();
        /*
        List<PersonDTO> personDTOList = new ArrayList<>();
        for (Person person : people) {
            PersonDTO personDTO = PersonMapper.INSTANCE.toDTO(person);
            personDTOList.add(personDTO);
        }*/
        return people;
    }
}
