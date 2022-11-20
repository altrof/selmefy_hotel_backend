package tech.selmefy.hotel.service.person;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tech.selmefy.hotel.controller.person.dto.PersonDTO;
import tech.selmefy.hotel.exception.ApiRequestException;
import tech.selmefy.hotel.mapper.PersonMapper;
import tech.selmefy.hotel.repository.person.Person;
import tech.selmefy.hotel.repository.person.PersonCriteriaRepository;
import tech.selmefy.hotel.repository.person.PersonRepository;
import org.springframework.lang.NonNull;
import java.util.List;
import java.util.Optional;

import static tech.selmefy.hotel.validators.ObjectUtilityValidator.isNullOrEmpty;

@Service
@AllArgsConstructor
public class PersonService {
    public final PersonRepository personRepository;
    public final PersonCriteriaRepository personCriteriaRepository;

    public List<PersonDTO> getAllPeople(int pageNumber, int pageSize, String orderBy,
        Optional<String> filterBy, Optional<String> filterValue) {
        List<Person> people = personCriteriaRepository.personSearch(pageNumber, pageSize, orderBy, filterBy, filterValue);
        List<PersonDTO> personDTOList = PersonMapper.INSTANCE.toDTOList(people);
        return personDTOList;
    }
    
    public Long createNewPerson(@NonNull PersonDTO personDTO) {
        if(isNullOrEmpty(personDTO.getFirstName()) || isNullOrEmpty(personDTO.getLastName())) {
            throw new ApiRequestException("Firstname or lastname can not to be null.");
        }

        if(Boolean.TRUE.equals(isNullOrEmpty(personDTO.getIdentityCode()))) {
            throw new ApiRequestException("Identity code is required in request body.");
        }

        if(Boolean.TRUE.equals(isNullOrEmpty(personDTO.getDateOfBirth()))) {
            throw new ApiRequestException("Birthday date should be present.");
        }

        Person person = PersonMapper.INSTANCE.toEntity(personDTO);
        personRepository.save(person);
        return person.getId();
    }

    public PersonDTO getPersonById(Long id) {
        return personRepository.findById(id).map(PersonMapper.INSTANCE::toDTO)
            .orElseThrow(() -> new RuntimeException("Error: No person with provided id."));
    }

    public PersonDTO updatePerson(Long personId, PersonDTO personDTO) {
        Person person = personRepository.findById(personId).orElseThrow(
                () -> new ApiRequestException("Booking does not exist with id: " + personId));

        if(isNullOrEmpty(personDTO.getFirstName()) || isNullOrEmpty(personDTO.getLastName())) {
            throw new ApiRequestException("Firstname or lastname can not to be null.");
        }

        if(Boolean.TRUE.equals(isNullOrEmpty(personDTO.getIdentityCode()))) {
            throw new ApiRequestException("Identity code is required in request body.");
        }

        if(Boolean.TRUE.equals(isNullOrEmpty(personDTO.getDateOfBirth()))) {
            throw new ApiRequestException("Birthday date should be present.");
        }

        person.setDateOfBirth(personDTO.getDateOfBirth());
        person.setLastName(personDTO.getLastName());
        person.setFirstName(personDTO.getFirstName());
        person.setIdentityCode(personDTO.getIdentityCode());

        personRepository.save(person);

        return PersonMapper.INSTANCE.toDTO(person);
    }
}
