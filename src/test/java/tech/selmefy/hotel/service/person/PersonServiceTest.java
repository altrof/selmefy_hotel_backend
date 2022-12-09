package tech.selmefy.hotel.service.person;

import liquibase.pro.packaged.M;
import org.hibernate.query.internal.QueryImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.selmefy.hotel.controller.person.dto.PeopleResponseDTO;
import tech.selmefy.hotel.controller.person.dto.PersonDTO;
import tech.selmefy.hotel.exception.ApiRequestException;
import tech.selmefy.hotel.mapper.PersonMapper;
import tech.selmefy.hotel.mapper.PersonMapperImpl;
import tech.selmefy.hotel.repository.person.Person;
import tech.selmefy.hotel.repository.person.PersonCriteriaRepository;
import tech.selmefy.hotel.repository.person.PersonRepository;

import javax.management.Query;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {
    @Mock
    private TypedQuery<Person> typedQuery;

    @Mock
    private EntityManager entityManager;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private PersonCriteriaRepository personCriteriaRepository;

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

/*
    @Test
    void createNewPerson_returnId_WhenPersonSuccessfullyCreated() {
        PersonDTO personDTO = PersonDTO.builder()
                .identityCode("12345678")
                .dateOfBirth (LocalDate.of(1996, 8, 17))
                .firstName("Aleksandr")
                .lastName("Trofimov")
                .country("Estonia")
                .phoneNumber ("+37255555555")
                .build();


        Person person = Person.builder()
                .id(1L)
                .identityCode("12345678")
                .dateOfBirth (LocalDate.of(1996, 8, 17))
                .firstName("Aleksandr")
                .lastName("Trofimov")
                .country("Estonia")
                .phoneNumber("+37255555555")
                .build();

        when(personMapper.toEntity(personDTO)).thenReturn(person);
        var actualResult = personService.createNewPerson(personDTO);
        assertEquals(1L, actualResult);
    }*/


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

    @Test
    void getAllPeopleWithParams_returnsPeopleResponseDto_WhenRequested() {
        Person person1 = new Person(1L,"1","john3","doe3","Estonia","555",LocalDate.of(2000, 10,5), new Timestamp(System.currentTimeMillis()));
        Person person2 = new Person(2L,"2","john2","doe2","Estonia","555",LocalDate.of(2000, 10,5), new Timestamp(System.currentTimeMillis()));
        Person person3 = new Person(3L,"3","john1","doe1","Estonia","555",LocalDate.of(2000, 10,5), new Timestamp(System.currentTimeMillis()));

        //given
        List<Person> personList = new ArrayList<>();
        List<String> personIdList = Arrays.asList("1", "2", "3");
        personList.add(person1);
        personList.add(person2);
        personList.add(person3);
        //Optional.of("lastname")
        given(typedQuery.getResultList()).willReturn(personList);
        //given(personRepository.findAll()).willReturn(personList);
        given(personCriteriaRepository
            .personSearchQuery("lastName","ASC", Optional.of("lastname"),
            Optional.<String>empty()))
            .willReturn(typedQuery);
        PeopleResponseDTO result = personService.getAllPeopleWithParams(0,10,"lastName","ASC",Optional.of("lastname"), Optional.<String>empty());
        System.out.println();

        then(personCriteriaRepository).should().personSearchQuery("lastName","ASC", Optional.of("lastname"), Optional.<String>empty());
        System.out.println(result.getPeople());
        assertEquals(personList.size(), result.getTotalPeopleLength());

        for (PersonDTO personDTO : result.getPeople()) {
            assertTrue(personIdList.contains(personDTO.getIdentityCode()));
        }
    }

    @Test
    void getAllPeopleWithParams_returnsPeopleResponseDto_WhenRequested_WithoutFilter() {
        Person person1 = new Person(1L,"1","john3","doe3","Estonia","555",LocalDate.of(2000, 10,5), new Timestamp(System.currentTimeMillis()));
        Person person2 = new Person(2L,"2","john2","doe2","Estonia","555",LocalDate.of(2000, 10,5), new Timestamp(System.currentTimeMillis()));
        Person person3 = new Person(3L,"3","john1","doe1","Estonia","555",LocalDate.of(2000, 10,5), new Timestamp(System.currentTimeMillis()));

        //given
        List<Person> personList = new ArrayList<>();
        List<String> personIdList = Arrays.asList("1", "2", "3");
        personList.add(person1);
        personList.add(person2);
        personList.add(person3);
        given(typedQuery.getResultList()).willReturn(personList);
        given(personRepository.findAll()).willReturn(personList);
        given(personCriteriaRepository
                .personSearchQuery("lastName","ASC", Optional.empty(),
                        Optional.<String>empty()))
                .willReturn(typedQuery);

        PeopleResponseDTO result = personService.getAllPeopleWithParams(0,10,"lastName","ASC",Optional.empty(), Optional.empty());
        System.out.println();

        System.out.println(result.getPeople());
        assertEquals(personList.size(), result.getTotalPeopleLength());

        for (PersonDTO personDTO : result.getPeople()) {
            assertTrue(personIdList.contains(personDTO.getIdentityCode()));
        }
    }

}