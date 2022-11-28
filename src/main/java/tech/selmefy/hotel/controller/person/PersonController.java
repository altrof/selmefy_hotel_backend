package tech.selmefy.hotel.controller.person;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.selmefy.hotel.controller.person.dto.PersonDTO;
import tech.selmefy.hotel.exception.ApiRequestException;
import tech.selmefy.hotel.service.person.PersonService;

import java.util.List;
import java.util.Optional;

@Api(tags = "Person")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "person")
public class PersonController {
    public final PersonService personService;

    @GetMapping
    public List<PersonDTO> getAllPeople(
            @RequestParam(name="pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(name="pageSize", defaultValue = "10") int pageSize,
            @RequestParam(name="orderBy", defaultValue = "lastName") String orderBy,
            @RequestParam(name = "filterBy") Optional<String> filterBy,
            @RequestParam(name = "filterValue") Optional<String> filterValue) {

        // Ensures that only PersonDTO fields are used for sorting/searching.
        try {
            PersonDTO.class.getDeclaredField(orderBy);
        } catch (NoSuchFieldException e) {
            throw new ApiRequestException("Cannot sort by " + orderBy);
        }

        if (filterBy.isPresent()) {
            try {
                PersonDTO.class.getDeclaredField(filterBy.get());
            } catch (NoSuchFieldException e) {
                throw new ApiRequestException("Cannot filter by " + filterBy);
            }
        }

        // This prevents requesting pages that are too big.
        int maxPageSize = 200;
        if (pageSize > maxPageSize) {
            pageSize = maxPageSize;
        }
        return personService.getAllPeople(pageNumber, pageSize, orderBy, filterBy, filterValue);
    }

    @GetMapping("/public/{personId}")
    public PersonDTO getPersonById(@PathVariable Long personId) {
        return personService.getPersonById(personId);
    }

    @PostMapping("/public/")
    public void createNewPerson(@RequestBody PersonDTO personDTO) {
        personService.createNewPerson(personDTO);
    }

    @PutMapping("/{personId}")
    public ResponseEntity<PersonDTO> updateBooking(@PathVariable Long personId, @RequestBody PersonDTO personDTO) {
        return ResponseEntity.ok(personService.updatePerson(personId, personDTO));
    }
}