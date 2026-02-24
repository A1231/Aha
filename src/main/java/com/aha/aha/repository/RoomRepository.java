package com.aha.aha.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.aha.aha.entity.Room;

public interface RoomRepository extends CrudRepository<Room, Long> {
    Optional<Room> findByRoomId(Long roomId);
}
