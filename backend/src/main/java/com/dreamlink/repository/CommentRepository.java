package com.dreamlink.repository;

import com.dreamlink.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByDreamIdOrderByCreatedAtDesc(Long dreamId);

}
