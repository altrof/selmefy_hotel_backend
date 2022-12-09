package tech.selmefy.hotel.controller.hotelservice;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.selmefy.hotel.controller.hotelservice.dto.HotelServiceDTO;
import tech.selmefy.hotel.service.hotelservice.HotelServiceService;


import java.util.ArrayList;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;



@ExtendWith(MockitoExtension.class)
class HotelServiceControllerTest {

    private List<HotelServiceDTO> hotelServiceDTOList = new ArrayList<>();
    private HotelServiceDTO service1 = new HotelServiceDTO("1");
    private HotelServiceDTO service2 = new HotelServiceDTO("2");
    private HotelServiceDTO service3 = new HotelServiceDTO("3");
    private HotelServiceDTO service4 = new HotelServiceDTO("4");

    @Mock
    private HotelServiceService hotelServiceService;;

    @InjectMocks
    private HotelServiceController hotelServiceController;

    @AfterEach
    public void tearDown() {
        hotelServiceDTOList.clear();
    }


    @Test
    void getAllHotelServices_returnsHotelServiceDTO_WhenRequested() {

        // given
        hotelServiceDTOList.add(service1);
        hotelServiceDTOList.add(service2);
        hotelServiceDTOList.add(service3);
        hotelServiceDTOList.add(service4);
        BDDMockito.given(hotelServiceService.getAllHotelServices()).willReturn(hotelServiceDTOList);


        // when
        List<HotelServiceDTO> result = hotelServiceController.getAllHotelServices();

        // then
        assertEquals(hotelServiceDTOList.size(), result.size());
        assertEquals("2", result.get(1).getHotelServiceName());
        assertEquals("4", result.get(3).getHotelServiceName());

    }

    @Test
    void getHotelServiceById_returnsHotelServiceDTO_WhenRequestedWithI() {

        // given
        hotelServiceDTOList.add(service1);
        hotelServiceDTOList.add(service2);
        hotelServiceDTOList.add(service3);
        hotelServiceDTOList.add(service4);
        BDDMockito.given(hotelServiceService.getHotelServiceById((short) 2)).willReturn(service2);

        // when
        HotelServiceDTO result = hotelServiceController.getHotelServiceById((short) 2);

        // then

        assertEquals(service2.getHotelServiceName(), result.getHotelServiceName());
        assertNotEquals(service1.getHotelServiceName(), result.getHotelServiceName());
    }
}