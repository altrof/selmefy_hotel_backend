package tech.selmefy.hotel.service.person;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.selmefy.hotel.controller.room.dto.PersonDTO;
import tech.selmefy.hotel.exception.ApiRequestException;
import tech.selmefy.hotel.mapper.PersonMapper;
import tech.selmefy.hotel.repository.person.Person;
import tech.selmefy.hotel.repository.person.PersonRepository;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;

import static tech.selmefy.hotel.validators.ObjectUtilityValidator.IsNullOrEmpty;

@Service
@AllArgsConstructor
public class PersonService {
    public static PersonRepository personRepository;

    public List<PersonDTO> getAllPeople() {
        List<Person> people = personRepository.findAll();
        List<PersonDTO> personDTOList = new ArrayList<>();
        for (Person person : people) {
            PersonDTO personDTO = PersonMapper.INSTANCE.toDTO(person);
            personDTOList.add(personDTO);
        }
        return personDTOList;
    }

    public static void createNewPerson(@NonNull Person person) {
        if(IsNullOrEmpty(person.getFirstName()) || IsNullOrEmpty(person.getLastName())) {
            throw new ApiRequestException("Firstname or lastname can not to be null.");
        }

        if(IsNullOrEmpty(person.getIdentityCode())) {
            throw new ApiRequestException("Identity code is required in request body.");
        }

        if(IsNullOrEmpty(person.getDateOfBirth())) {
            throw new ApiRequestException("Birthday date should be present.");
        }

        if(IsNullOrEmpty(person.getTimeOfRegistration())) {
            throw new ApiRequestException("Birthday date should be present.");
        }

        personRepository.save(person);
    }

}
