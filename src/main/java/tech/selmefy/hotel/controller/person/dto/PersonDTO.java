package tech.selmefy.hotel.controller.person.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonDTO {
    private String identityCode;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String country;
    private String phoneNumber;
}
