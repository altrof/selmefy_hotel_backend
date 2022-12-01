package tech.selmefy.hotel.repository.room;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
@RequiredArgsConstructor

public class RoomCriteriaRepository {

    private final EntityManager entityManager;

    public List roomSearch(int pageNumber, int pageSize, String orderBy) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<Room> roomCriteriaQuery = cb.createQuery(Room.class);
        Root<Room> root = roomCriteriaQuery.from(Room.class);
        roomCriteriaQuery.select(root);
        roomCriteriaQuery.orderBy(cb.asc(root.get(orderBy)));
        Query query = entityManager.createQuery(roomCriteriaQuery);
        query.setFirstResult(pageNumber * pageSize);

        return query.getResultList();

    }
}
