package tech.selmefy.hotel.service.person;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.selmefy.hotel.controller.person.dto.PersonDTO;
import tech.selmefy.hotel.exception.ApiRequestException;
import tech.selmefy.hotel.mapper.PersonMapper;
import tech.selmefy.hotel.mapper.PersonMapperImpl;
import tech.selmefy.hotel.repository.person.Person;
import tech.selmefy.hotel.repository.person.PersonRepository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.reset;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {
    @Mock
    private PersonRepository personRepository;

    @Spy
    private final PersonMapper personMapper = new PersonMapperImpl();

    @InjectMocks
    private PersonService personService;

    @BeforeEach
    public void setUp() {
    }
    @AfterEach
    public void afterEach() {
        reset();
    }

    @Test
    void getPersonById_throwsRunTimeException_WhenProvidedIdDoesNotExist() {
        Exception exception = assertThrows(RuntimeException.class, () -> personService.getPersonById(3L));

        String expectedResponse = "Error: No person with provided id.";
        String actualResponse = exception.getMessage();

        assertTrue(actualResponse.contains(expectedResponse));
    }

    @Test
    void getPersonById_returnPersonDto_WhenProvidedIdExists() {
        PersonDTO personDTO3 = new PersonDTO("3","john","doe",LocalDate.of(2000, 10,5),"Estonia","555");
        Person person3 = new Person(3L,"3","john","doe","Estonia","555",LocalDate.of(2000, 10,5), new Timestamp(System.currentTimeMillis()));
        //given
        given(personRepository.findById(3L)).willReturn(Optional.of(person3));

        //when
        var result = personService.getPersonById(3L);

        //then
        assertEquals(personDTO3,result);
    }

    @Test
    void getPersonByIdentityCode_returnPersonDto_WhenProvidedIndentityCodeExists() {
        PersonDTO personDTO1 = new PersonDTO("1","john","doe",LocalDate.of(2000, 10,5),"Estonia","555");
        Person person1 = new Person(1L,"1","john","doe","Estonia","555",LocalDate.of(2000, 10,5), new Timestamp(System.currentTimeMillis()));
        //given
        given(personRepository.findPersonByIdentityCode("1")).willReturn(Optional.of(person1));

        //when
        var result = personService.getPersonByIdentityCode("1").get();

        //then
        then(personRepository).should().findPersonByIdentityCode("1");

        assertEquals(personDTO1, result);

    }

    @Test
    void updatePerson_throwsApiRequestException_WhenIdDoesNotExist() {
        PersonDTO personDTO1 = new PersonDTO("1","john","doe",LocalDate.of(2000, 10,5),"Estonia","555");
        long personId = 2L;

        Exception exception = assertThrows(ApiRequestException.class, () -> personService.updatePerson(personId, personDTO1));

        String expectedResponse = "Person does not exist with id: " + personId;
        String actualResponse = exception.getMessage();

        assertTrue(actualResponse.contains(expectedResponse));
    }

    @Test
    void updatePerson_throwsApiRequestException_WhenFirstnameOrLastnameDoesNotExist() {
        PersonDTO personDTO1 = new PersonDTO("1",null,null,LocalDate.of(2000, 10,5),"Estonia","555");
        Person person1 = new Person(1L,"1","john","doe","Estonia","555",LocalDate.of(2000, 10,5), new Timestamp(System.currentTimeMillis()));

        given(personRepository.findById(1L)).willReturn(Optional.of(person1));

        Exception exception = assertThrows(ApiRequestException.class, () -> personService.updatePerson(1L, personDTO1));

        String expectedResponse = "Firstname or lastname can not to be null.";
        String actualResponse = exception.getMessage();

        assertTrue(actualResponse.contains(expectedResponse));
    }

    @Test
    void updatePerson_throwsApiRequestException_WhenIdentityCodeDoesNotExist() {
        PersonDTO personDTO1 = new PersonDTO(null,"john","doe",LocalDate.of(2000, 10,5),"Estonia","555");
        Person person1 = new Person(1L,"1","john","doe","Estonia","555",LocalDate.of(2000, 10,5), new Timestamp(System.currentTimeMillis()));

        given(personRepository.findById(1L)).willReturn(Optional.of(person1));

        Exception exception = assertThrows(ApiRequestException.class, () -> personService.updatePerson(1L, personDTO1));

        String expectedResponse = "Identity code is required in request body.";
        String actualResponse = exception.getMessage();

        assertTrue(actualResponse.contains(expectedResponse));
    }

    @Test
    void updatePerson_throwsApiRequestException_WhenDateOfBirthDoesNotExist() {
        PersonDTO personDTO1 = new PersonDTO("1","john","doe",null,"Estonia","555");
        Person person1 = new Person(1L,"1","john","doe","Estonia","555",LocalDate.of(2000, 10,5), new Timestamp(System.currentTimeMillis()));

        given(personRepository.findById(1L)).willReturn(Optional.of(person1));

        Exception exception = assertThrows(ApiRequestException.class, () -> personService.updatePerson(1L, personDTO1));

        String expectedResponse = "Birthday date should be present.";
        String actualResponse = exception.getMessage();

        assertTrue(actualResponse.contains(expectedResponse));
    }

    @Test
    void updatePerson_returnPersonDto_WhenPersonSuccessfullyUpdatedWithNewData() {
        PersonDTO personDTO3 = new PersonDTO("3","john","doe",LocalDate.of(2000, 10,5),"Estonia","555");
        Person person2 = new Person(2L,"2","john","doe","Estonia","555",LocalDate.of(2000, 10,5), new Timestamp(System.currentTimeMillis()));
        //given
        given(personRepository.findById(2L)).willReturn(Optional.of(person2));

        //when
        var result = personService.updatePerson(2L,personDTO3);

        //then
        assertEquals(personDTO3,result);
    }

    @Test
    void createNewPerson_throwsApiRequestException_WhenFirstnameOrLastnameDoesNotExist() {
        PersonDTO personDTO1 = new PersonDTO("1",null,null,LocalDate.of(2000, 10,5),"Estonia","555");

        Exception exception = assertThrows(ApiRequestException.class, () -> personService.createNewPerson(personDTO1));

        String expectedResponse = "Firstname or lastname can not to be null.";
        String actualResponse = exception.getMessage();

        assertTrue(actualResponse.contains(expectedResponse));
    }

    @Test
    void createNewPerson_throwsApiRequestException_WhenIdentityCodeDoesNotExist() {
        PersonDTO personDTO1 = new PersonDTO(null,"john","doe",LocalDate.of(2000, 10,5),"Estonia","555");

        Exception exception = assertThrows(ApiRequestException.class, () -> personService.createNewPerson(personDTO1));

        String expectedResponse = "Identity code is required in request body.";
        String actualResponse = exception.getMessage();

        assertTrue(actualResponse.contains(expectedResponse));
    }

    @Test
    void createNewPerson_throwsApiRequestException_WhenDateOfBirthDoesNotExist() {
        PersonDTO personDTO1 = new PersonDTO("1","john","doe",null,"Estonia","555");

        Exception exception = assertThrows(ApiRequestException.class, () -> personService.createNewPerson(personDTO1));

        String expectedResponse = "Birthday date should be present.";
        String actualResponse = exception.getMessage();

        assertTrue(actualResponse.contains(expectedResponse));
    }

    @Test
    void createNewPerson_returnId_WhenPersonSuccessfullyCreated() {
    }

    @Test
    void getAllPeople_returnsPersonDtoList_WhenRequested() {
        Person person1 = new Person(1L,"1","john","doe","Estonia","555",LocalDate.of(2000, 10,5), new Timestamp(System.currentTimeMillis()));
        Person person2 = new Person(2L,"2","john","doe","Estonia","555",LocalDate.of(2000, 10,5), new Timestamp(System.currentTimeMillis()));
        Person person3 = new Person(3L,"3","john","doe","Estonia","555",LocalDate.of(2000, 10,5), new Timestamp(System.currentTimeMillis()));

        //given
        List<Person> personList = new ArrayList<>();
        List<String> personIdList = Arrays.asList("1", "2", "3");
        personList.add(person1);
        personList.add(person2);
        personList.add(person3);

        given(personRepository.findAll()).willReturn(personList);

        //when

        List<PersonDTO> result = personService.getAllPeople();

        //then

        assertEquals(personList.size(), result.size());

        for (PersonDTO personDTO : result) {
            assertTrue(personIdList.contains(personDTO.getIdentityCode()));
        }
    }
}