package com.dreamlink.repository;

import com.dreamlink.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<Message> findByUserIdAndOtherUserIdOrUserIdAndOtherUserIdOrderByCreatedAtAsc(
            Long userId1, Long userId2, Long userId2Again, Long userId1Again);

}
