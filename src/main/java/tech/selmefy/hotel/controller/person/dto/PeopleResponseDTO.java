package tech.selmefy.hotel.controller.person.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PeopleResponseDTO {
    private List<PersonDTO> people;
    private int totalPeopleLength;
}
