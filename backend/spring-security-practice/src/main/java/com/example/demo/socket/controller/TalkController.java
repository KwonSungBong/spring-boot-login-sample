package com.example.demo.socket.controller;

import com.example.demo.dto.MessageDto;
import com.example.demo.dto.RoomDto;
import com.example.demo.service.TalkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * Created by whilemouse on 17. 8. 24.
 */
@Controller
public class TalkController {

    @Autowired
    private TalkService talkServiceImpl;

    @SubscribeMapping("/talk/room.find")
    @SendTo("/talk/room.list")
    public List<RoomDto.Summary> findRoom() {
        return talkServiceImpl.findRoomSummaryList();
    }

    @SubscribeMapping("/talk/room.insert")
    @SendTo("/talk/room.list")
    public List<RoomDto.Summary> insertRoom(RoomDto.Create room) {
        talkServiceImpl.createRoom(room);
        return talkServiceImpl.findRoomSummaryList();
    }

    @SubscribeMapping("/talk/room.update")
    @SendTo("/talk/room.list")
    public List<RoomDto.Summary> updateRoom(RoomDto.Update room) {
        talkServiceImpl.updateRoom(room);
        return talkServiceImpl.findRoomSummaryList();
    }

    @SubscribeMapping("/talk/room.delete")
    @SendTo("/talk/room.list")
    public List<RoomDto.Summary> deleteRoom(RoomDto.Delete room) {
        talkServiceImpl.deleteRoom(room);
        return talkServiceImpl.findRoomSummaryList();
    }

    @SubscribeMapping("/talk/room.participate/{room_idx}")
    @SendTo("/talk/room.list")
    public List<RoomDto.Summary> participateRoom(@DestinationVariable int roomIdx) {
        talkServiceImpl.participateRoom(roomIdx);
        return talkServiceImpl.findRoomSummaryList();
    }

    @SubscribeMapping("/talk/room.desert/{room_idx}/participant/{participant_idx}")
    @SendTo("/talk/room.list")
    public List<RoomDto.Summary> desertRoom(@DestinationVariable int roomIdx, @DestinationVariable int participantIdx) {
        talkServiceImpl.desertRoom(roomIdx, participantIdx);
        return talkServiceImpl.findRoomSummaryList();
    }

    @SubscribeMapping("/talk/room.message/{room_idx}")
    @SendTo("/talk/room.list/{room_idx}")
    public List<RoomDto.Summary> message(@DestinationVariable int roomIdx, MessageDto.Create message) {
        talkServiceImpl.createMessage(message);

        return talkServiceImpl.findRoomSummaryList();
    }
}
