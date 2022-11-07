package tech.selmefy.hotel.service.room.type;

import lombok.Getter;

@Getter
public enum RoomType {

    ECONOMY("ECONOMY"),
    REGULAR("REGULAR"),
    DELUXE("DELUXE"),
    KING_SIZE("KING_SIZE");

    final String roomTypeText;

    RoomType(String roomTypeText) {
        this.roomTypeText = roomTypeText;
    }
}
