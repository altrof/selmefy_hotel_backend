package tech.selmefy.hotel.repository.room;


import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;
import tech.selmefy.hotel.controller.room.dto.RoomDTO;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor

public class RoomCriteriaRepository {

    private final EntityManager entityManager;
    @SneakyThrows
    public List<Room> roomSearch(int pageNumber, int pageSize, String orderBy,
                           Optional<String> filterBy, Optional<String> filterValue) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<Room> roomCriteriaQuery = cb.createQuery(Room.class);
        Root<Room> root = roomCriteriaQuery.from(Room.class);
        roomCriteriaQuery.select(root);
        roomCriteriaQuery.orderBy(cb.asc(root.get(orderBy)));
        if (filterBy.isPresent() && filterValue.isPresent()) {
            System.out.println("this is declared field: " + RoomDTO.class.getDeclaredField(filterBy.get()).getType());
            System.out.println("COMPARE is: " + cb.lower(root.get(filterBy.get())).toString());
            // currently if boolean true, then returns all available, if false or anything else will return all unvailable rooms
            if(RoomDTO.class.getDeclaredField(filterBy.get()).getType().equals(Boolean.class)) {
                boolean filterValueAsBoolean = Boolean.parseBoolean(filterValue.get());
                System.out.println("BOOLEAN is :" + filterValueAsBoolean);
                roomCriteriaQuery.select(root).where(cb.equal(root.get(filterBy.get()), filterValueAsBoolean));
            }
            if(RoomDTO.class.getDeclaredField(filterBy.get()).getType().equals(String.class)) {
                String filterValueAsString = filterValue.get().toUpperCase();
                System.out.println("String is : " + filterValueAsString);
//                roomCriteriaQuery.select(root).where(cb.like(cb.lower(root.get(filterBy.get())), filterValue.get().toLowerCase() + '%'));
                roomCriteriaQuery.select(root).where(cb.like(cb.upper(root.get(filterBy.get())), filterValueAsString));
            }
            if(RoomDTO.class.getDeclaredField(filterBy.get()).getType().equals(Float.class)) {
                Float filterValueAsFloat = Float.parseFloat(filterValue.get());
                roomCriteriaQuery.select(root).where(cb.lessThan(root.get(filterBy.get()), filterValueAsFloat));
            }
            else if (RoomDTO.class.getDeclaredField(filterBy.get()).getType().equals(int.class)) {
                Integer filterValueAsInt = Integer.parseInt(filterValue.get());
                System.out.println("ELSE is :" + filterValueAsInt);
                System.out.println("this is declared field: " + RoomDTO.class.getDeclaredField(filterBy.get()));
                roomCriteriaQuery.select(root).where(cb.lessThan(root.get(filterBy.get()), filterValueAsInt));
            }

            //TODO juhul kui filterby tüübid
            //     private Long id; ok less than int
            //    private Float size; OK
            //    private int floorId; ok less than int
            //    private int roomNumber; OK less than int
            //    private int numberOfBeds; OK less than int
            //    private String roomType;
            //    private Boolean roomAvailableForBooking; ok
        }
        Query query = entityManager.createQuery(roomCriteriaQuery);
        query.setFirstResult(pageNumber * pageSize);
        query.setMaxResults(pageSize);

        return query.getResultList();

    }
}
