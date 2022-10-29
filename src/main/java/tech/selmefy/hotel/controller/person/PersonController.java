package tech.selmefy.hotel.controller.person;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tech.selmefy.hotel.controller.room.dto.PersonDTO;
import tech.selmefy.hotel.repository.person.Person;
import tech.selmefy.hotel.service.person.PersonService;

import java.util.List;

@Api(tags = "Person")
@RestController
@RequiredArgsConstructor
public class PersonController {
    public PersonService personService;
    @GetMapping("/person")
    public List<PersonDTO> getAllPeople() {
        return personService.getAllPeople();
    }

    @PostMapping
    public void createNewEmployee(@RequestBody Person person) {
        PersonService.createNewPerson(person);
    }

}