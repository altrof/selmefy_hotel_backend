package tech.selmefy.hotel.service.hotelserviceorder;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tech.selmefy.hotel.controller.hotelserviceorder.dto.HotelServiceOrderDTO;
import tech.selmefy.hotel.mapper.HotelServiceOrderMapper;
import tech.selmefy.hotel.repository.hotelserviceorder.HotelServiceOrder;
import tech.selmefy.hotel.repository.hotelserviceorder.HotelServiceOrderRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class HotelServiceOrderService {

    public final HotelServiceOrderRepository hotelServiceOrderRepository;

    public List<HotelServiceOrderDTO> getAllHotelServiceOrders() {
        List<HotelServiceOrder> hotelServiceOrders = hotelServiceOrderRepository.findAll();
        List<HotelServiceOrderDTO> hotelServiceOrderDTOList = new ArrayList<>();
        for (HotelServiceOrder order: hotelServiceOrders) {
            HotelServiceOrderDTO hotelServiceOrderDTO = HotelServiceOrderMapper.INSTANCE.toDTO(order);
            hotelServiceOrderDTOList.add(hotelServiceOrderDTO);
        }
        return hotelServiceOrderDTOList;
    }

    public  HotelServiceOrderDTO getHotelServiceOrderById(Long id) {
        return  hotelServiceOrderRepository.findById(id).map(HotelServiceOrderMapper.INSTANCE::toDTO).orElseThrow();
    }

}
