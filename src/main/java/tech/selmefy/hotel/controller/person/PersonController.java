package tech.selmefy.hotel.controller.person;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.selmefy.hotel.controller.person.dto.PersonDTO;
import tech.selmefy.hotel.service.person.PersonService;

import java.util.List;

@Api(tags = "Person")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "person")
public class PersonController {
    public final PersonService personService;

    @GetMapping
    public List<PersonDTO> getAllPeople() {
        return personService.getAllPeople();
    }

    @GetMapping("/{personId}")
    public PersonDTO getPersonById(@PathVariable Long personId) {
        return personService.getPersonById(personId);
    }

    @PostMapping
    public void createNewPerson(@RequestBody PersonDTO personDTO) {
        personService.createNewPerson(personDTO);
    }

    @PutMapping("/{personId}")
    public ResponseEntity<PersonDTO> updateBooking(@PathVariable Long personId, @RequestBody PersonDTO personDTO) {
        return ResponseEntity.ok(personService.updatePerson(personId, personDTO));
    }
}