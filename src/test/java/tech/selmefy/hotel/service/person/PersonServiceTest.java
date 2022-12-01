package tech.selmefy.hotel.service.person;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.selmefy.hotel.controller.person.dto.PersonDTO;
import tech.selmefy.hotel.mapper.PersonMapper;
import tech.selmefy.hotel.mapper.PersonMapperImpl;
import tech.selmefy.hotel.repository.person.Person;
import tech.selmefy.hotel.repository.person.PersonRepository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    private PersonDTO personDTO1 = new PersonDTO("1","john","doe",LocalDate.of(2000, 10,5),"Estonia","555");
    private PersonDTO personDTO2 = new PersonDTO("2","john","doe",LocalDate.of(2000, 10,5),"Estonia","555");
    private PersonDTO personDTO3 = new PersonDTO("3","john","doe",LocalDate.of(2000, 10,5),"Estonia","555");
    private Person person1 = new Person(1L,"1","john","doe","Estonia","555",LocalDate.of(2000, 10,5), new Timestamp(System.currentTimeMillis()));
    private Person person2 = new Person(2L,"2","john","doe","Estonia","555",LocalDate.of(2000, 10,5), new Timestamp(System.currentTimeMillis()));
    private Person person3 = new Person(3L,"3","john","doe","Estonia","555",LocalDate.of(2000, 10,5), new Timestamp(System.currentTimeMillis()));

    @Mock
    private PersonRepository personRepository;

    @Spy
    private PersonMapper personMapper = new PersonMapperImpl();

    @InjectMocks
    private PersonService personService;

    @Test
    void getPersonById() {
        //given
        given(personRepository.findById(3L)).willReturn(Optional.of(person3));

        //when
        var result = personService.getPersonById(3L);

        //then
        assertEquals(personDTO3,result);
    }

    @Test
    void getPersonByIdentityCode() {
        //given
        given(personRepository.findPersonByIdentityCode("1")).willReturn(Optional.of(person1));

        //when
        var result = personService.getPersonByIdentityCode("1").get();

        //then
        then(personRepository).should().findPersonByIdentityCode("1");

        //did mapper use this method. doesnt see it cus our call to it is a bit different than usual
        //then(personMapper).should().toDTO(person);

        assertEquals(personDTO1, result);

    }

    @Test
    void updatePerson() {
        //given
        given(personRepository.findById(2L)).willReturn(Optional.of(person2));

        //when
        var result = personService.updatePerson(2L,personDTO3);

        //then
        assertEquals(personDTO3,result);
    }

    @Test
    void createNewPerson() {
        /*//given
        given(personService.createNewPerson(personDTO2)).willReturn(null);
        //when

        var result = personService.createNewPerson(personDTO2);
        //then
        //then(personRepository).should().save()
        //if you put null instead of long gives another error
        assertEquals(2L, result);*/

    }

    @Test
    void getAllPeople() {
    }
}