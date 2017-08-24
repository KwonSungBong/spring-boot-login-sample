package com.example.demo.service;

import com.example.demo.dto.MessageDto;
import com.example.demo.dto.RoomDto;

import java.util.List;

/**
 * Created by whilemouse on 17. 8. 24.
 */
public interface TalkService {

    List<RoomDto.Summary> findRoomSummaryList();

    void createRoom(RoomDto.Create room);

    void updateRoom(RoomDto.Update room);

    void deleteRoom(RoomDto.Delete room);

    void createMessage(MessageDto.Create message);

    void participateRoom(int roomIdx);

    void desertRoom(int roomIdx, int participantIdx);
}
