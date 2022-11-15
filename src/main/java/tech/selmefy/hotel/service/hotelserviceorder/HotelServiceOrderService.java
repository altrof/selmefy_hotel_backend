package tech.selmefy.hotel.service.hotelserviceorder;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import tech.selmefy.hotel.controller.hotelserviceorder.dto.HotelServiceOrderDTO;
import tech.selmefy.hotel.exception.ApiRequestException;
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

    public void createNewHotelServiceOrder(@NonNull HotelServiceOrderDTO hotelServiceOrderDTO) {
        if (hotelServiceOrderDTO.getPrice() < 0) {
            throw new ApiRequestException("Price can't be negative!");
        }

        if (hotelServiceOrderDTO.getComments().length() > 1000) {
            throw new ApiRequestException("Comment length too long, keep it under 1000 characters");
        }
        HotelServiceOrder hotelServiceOrder = HotelServiceOrderMapper.INSTANCE.toEntity(hotelServiceOrderDTO);
        hotelServiceOrderRepository.save(hotelServiceOrder);
    }
}
