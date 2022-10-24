package tech.selmefy.hotel.controller.room.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonDTO {
    private String identityCode;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
}
