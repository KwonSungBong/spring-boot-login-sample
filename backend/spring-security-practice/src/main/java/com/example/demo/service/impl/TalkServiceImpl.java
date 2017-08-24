package com.example.demo.service.impl;

import com.example.demo.dto.MessageDto;
import com.example.demo.dto.RoomDto;
import com.example.demo.repository.MessageRepository;
import com.example.demo.repository.ParticipantRepository;
import com.example.demo.repository.RoomRepository;
import com.example.demo.service.TalkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by whilemouse on 17. 8. 24.
 */
@Service("talkService")
public class TalkServiceImpl implements TalkService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public List<RoomDto.Summary> findRoomSummaryList() {
        return null;
    }

    @Override
    public void createRoom(RoomDto.Create room) {

    }

    @Override
    public void updateRoom(RoomDto.Update room) {

    }

    @Override
    public void deleteRoom(RoomDto.Delete room) {

    }

    @Override
    public void createMessage(MessageDto.Create message) {

    }

    @Override
    public void participateRoom(int roomIdx) {

    }

    @Override
    public void desertRoom(int roomIdx, int participantIdx) {

    }
}
