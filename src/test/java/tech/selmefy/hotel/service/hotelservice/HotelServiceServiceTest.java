package tech.selmefy.hotel.service.hotelservice;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.selmefy.hotel.controller.hotelservice.dto.HotelServiceDTO;
import tech.selmefy.hotel.repository.hotelservice.HotelService;
import tech.selmefy.hotel.repository.hotelservice.HotelServiceRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class HotelServiceServiceTest {

    private List<HotelService> hotelServiceList;
    private HotelService service1 = new HotelService((short) 1, "1");
    private HotelService service2 = new HotelService((short) 2, "2");
    private HotelService service3 = new HotelService((short) 3, "3");
    private HotelService service4 = new HotelService((short) 4, "4");

    private List<String> hotelServiceStringList = Arrays.asList("1", "2", "3", "4");
    @Mock
    private HotelServiceRepository hotelServiceRepository;

    @InjectMocks
    private HotelServiceService hotelServiceService;

    @BeforeEach
    void setUp() {
        hotelServiceList = new ArrayList<>();

    }

    @AfterEach
    void tearDown() {
        hotelServiceList.clear();

    }


    @Test
    void getAllHotelServices_returnsHotelServiceDTO_WhenRequested() {

        // given
        hotelServiceList.add(service1);
        hotelServiceList.add(service2);
        hotelServiceList.add(service3);
        hotelServiceList.add(service4);

        BDDMockito.given(hotelServiceRepository.findAll()).willReturn(hotelServiceList);

        // when

        List<HotelServiceDTO> result = hotelServiceService.getAllHotelServices();
        // then
        assertEquals(hotelServiceList.size(), result.size());

        // Confirming all services are here
        for (HotelServiceDTO hotelServiceDTO : result) {
            assertTrue(hotelServiceStringList.contains(hotelServiceDTO.getHotelServiceName()));
        }
    }

    @Test
    void getHotelServiceById_returnsHotelServiceDTO_WhenRequestedWithId() {
        // given
        hotelServiceList.add(service1);
        hotelServiceList.add(service2);
        hotelServiceList.add(service3);
        hotelServiceList.add(service4);

        BDDMockito.given(hotelServiceRepository.findById((short) 2)).willReturn(Optional.ofNullable(service2));

        // when

        HotelServiceDTO result = hotelServiceService.getHotelServiceById((short) 2);
        // then
        assertEquals(service2.getHotelServiceName(), result.getHotelServiceName());
        assertNotEquals(service1.getHotelServiceName(), result.getHotelServiceName());
    }
}


