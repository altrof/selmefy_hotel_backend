package tech.selmefy.hotel.controller.person;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.selmefy.hotel.repository.person.Person;
import tech.selmefy.hotel.service.person.PersonService;
import tech.selmefy.hotel.service.room.RoomService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PersonController {
    public final PersonService personService;
    @GetMapping("/person")
    public List<Person> getAllPeople() {
        return personService.getAllPeople();
    }
}