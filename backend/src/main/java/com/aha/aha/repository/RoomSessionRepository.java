package com.aha.aha.repository;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.aha.aha.entity.RoomSession;

public interface RoomSessionRepository 
        extends CrudRepository<RoomSession, String> {
    Optional<RoomSession> findByRoomSessionId(String roomSessionId);
}