package tech.selmefy.hotel.controller.hotelservice;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.selmefy.hotel.controller.hotelservice.dto.HotelServiceDTO;
import tech.selmefy.hotel.service.hotelservice.HotelServiceService;

import java.util.List;

@Api(tags = "HotelService")
@RestController
@RequiredArgsConstructor
public class HotelServiceController {
    public final HotelServiceService hotelServiceService;

    @GetMapping("/hotel_services")
    public List<HotelServiceDTO> getAllHotelServices() {
        return hotelServiceService.getAllHotelServices();
    }
}
