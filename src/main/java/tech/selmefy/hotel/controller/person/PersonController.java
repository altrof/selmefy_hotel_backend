package tech.selmefy.hotel.controller.person;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.selmefy.hotel.controller.room.dto.PersonDTO;
import tech.selmefy.hotel.service.person.PersonService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PersonController {
    public final PersonService personService;
    @GetMapping("/person")
    public List<PersonDTO> getAllPeople() {
        return personService.getAllPeople();
    }
}