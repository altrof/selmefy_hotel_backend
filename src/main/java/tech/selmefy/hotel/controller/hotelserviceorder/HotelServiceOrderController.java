package tech.selmefy.hotel.controller.hotelserviceorder;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.selmefy.hotel.controller.hotelserviceorder.dto.HotelServiceOrderDTO;
import tech.selmefy.hotel.service.hotelserviceorder.HotelServiceOrderService;

import java.util.List;

@Api(tags = "HotelServiceOrder")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "hotel_service_order")
public class HotelServiceOrderController {
    public final HotelServiceOrderService hotelServiceOrderService;

    @GetMapping
    public List<HotelServiceOrderDTO> getAllgetAllHotelServiceOrders() {
        return hotelServiceOrderService.getAllHotelServiceOrders();
    }

    @GetMapping("/{serviceOrderId}")
    public  HotelServiceOrderDTO getHotelServiceOrderById(@PathVariable Long serviceOrderId) {
        return hotelServiceOrderService.getHotelServiceOrderById(serviceOrderId);
    }

    @PostMapping
    public void createNewHotelServiceOrder(@RequestBody HotelServiceOrderDTO hotelServiceOrderDTO) {
        hotelServiceOrderService.createNewHotelServiceOrder(hotelServiceOrderDTO);
    }

    @PutMapping("/{serviceOrderId}")
    public ResponseEntity<HotelServiceOrderDTO> updateHotelServiceOrder(@PathVariable Long serviceOrderId,
                                                                        @RequestBody HotelServiceOrderDTO hotelServiceOrderDTO) {
        return ResponseEntity.ok(hotelServiceOrderService.updateHotelServiceOrder(serviceOrderId, hotelServiceOrderDTO));
    }
}
