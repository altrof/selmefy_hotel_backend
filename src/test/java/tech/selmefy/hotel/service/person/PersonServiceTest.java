package tech.selmefy.hotel.service.person;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.selmefy.hotel.mapper.PersonMapper;
import tech.selmefy.hotel.mapper.PersonMapperImpl;
import tech.selmefy.hotel.repository.person.Person;
import tech.selmefy.hotel.repository.person.PersonRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;
    @Spy
    private PersonMapper personMapper = new PersonMapperImpl();

    @InjectMocks
    private PersonService personService;
    @Test
    void createNewPerson() {
    }

    @Test
    void getAllPeople() {
    }

    @Test
    void getPersonById() {

        //given
        Person person = Person
                .builder()
                .id(1L)
                .identityCode("1234")
                .firstName("john")
                .lastName("doe")
                .dateOfBirth(LocalDate.of(2000, 10,5))
                .country("Estonia")
                .phoneNumber("555555555")
                .build();
        given(personRepository.findPersonByIdentityCode("1234")).willReturn(Optional.of(person));
        //when
        System.out.println(personRepository.findAll());
        System.out.println(personRepository.findPersonByIdentityCode("1234").get().getId());
        System.out.println(personRepository.findAll());
        //var result = personService.getPersonById(personRepository.findPersonByIdentityCode("1234").get().getId());
        //then
        //then(personMapper).should().toDTO(person);
        then(personRepository).should().findPersonByIdentityCode("1234");
        /*assertEquals(PersonDTO.builder()
                .identityCode("1234")
                .firstName("john")
                .lastName("doe")
                .dateOfBirth(LocalDate.of(2000, 10,5))
                .country("Estonia")
                .phoneNumber("555555555")
                .build(), result);*/
    }

    @Test
    void updatePerson() {
    }
}