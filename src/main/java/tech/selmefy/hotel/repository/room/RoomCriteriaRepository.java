package tech.selmefy.hotel.repository.room;


import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;
import tech.selmefy.hotel.controller.room.dto.RoomDTO;
import tech.selmefy.hotel.service.room.type.RoomType;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Optional;

@Repository
@RequiredArgsConstructor

public class RoomCriteriaRepository {

    private final EntityManager entityManager;
    @SneakyThrows
    public TypedQuery<Room> roomSearchQuery(String orderBy, String orderType,
                                 Optional<String> filterBy, Optional<String> filterValue) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<Room> roomCriteriaQuery = cb.createQuery(Room.class);
        Root<Room> root = roomCriteriaQuery.from(Room.class);
        roomCriteriaQuery.select(root);

        if(orderType.equals("DESC")) {
            roomCriteriaQuery.orderBy(cb.desc(root.get(orderBy)));
        } else {
            roomCriteriaQuery.orderBy(cb.asc(root.get(orderBy)));
        }

        if (filterBy.isPresent() && filterValue.isPresent()) {
            // currently if boolean true, then returns all available, if false or anything else will return all unvailable rooms
            if(RoomDTO.class.getDeclaredField(filterBy.get()).getType().equals(Boolean.class)) {
                boolean filterValueAsBoolean = Boolean.parseBoolean(filterValue.get());
                roomCriteriaQuery.select(root).where(cb.equal(root.get(filterBy.get()), filterValueAsBoolean));
            }
            // only works with exact RoomType
            if(RoomDTO.class.getDeclaredField(filterBy.get()).getType().equals(String.class)) {
                RoomType filerValueAsRoomType = RoomType.valueOf(filterValue.get());
                roomCriteriaQuery.select(root).where(cb.equal(cb.upper(root.get(filterBy.get())), filerValueAsRoomType));
            }
            if(RoomDTO.class.getDeclaredField(filterBy.get()).getType().equals(Float.class)) {
                Float filterValueAsFloat = Float.parseFloat(filterValue.get());
                roomCriteriaQuery.select(root).where(cb.lessThan(root.get(filterBy.get()), filterValueAsFloat));
            }
            else if (RoomDTO.class.getDeclaredField(filterBy.get()).getType().equals(int.class)) {
                Integer filterValueAsInt = Integer.parseInt(filterValue.get());
                roomCriteriaQuery.select(root).where(cb.lessThan(root.get(filterBy.get()), filterValueAsInt));
            }
        }

        return entityManager.createQuery(roomCriteriaQuery);

    }
}
