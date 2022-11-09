package tech.selmefy.hotel.controller.hotelservice;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.selmefy.hotel.controller.hotelservice.dto.HotelServiceDTO;
import tech.selmefy.hotel.service.hotelservice.HotelServiceService;

import java.util.List;

@Api(tags = "HotelService")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "hotel_services")
public class HotelServiceController {
    public final HotelServiceService hotelServiceService;

    @GetMapping
    public List<HotelServiceDTO> getAllHotelServices() {
        return hotelServiceService.getAllHotelServices();
    }

    @GetMapping("/{serviceId}")
    public HotelServiceDTO getHotelServiceById(@PathVariable short serviceId) {
        return hotelServiceService.getHotelServiceById(serviceId);
    }
}
