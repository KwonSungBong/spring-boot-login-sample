package com.example.demo.dto;

import lombok.Data;

/**
 * Created by whilemouse on 17. 8. 24.
 */
public class MessageDto {
    @Data
    public static class Detail {
        private long idx;
        private String content;
        private UserDto.Summary createdUser;
    }

    @Data
    public static class Create {
        private String content;
        private RoomDto.Refer room;
    }
}
