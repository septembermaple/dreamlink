package com.dreamlink.service.impl;

import com.dreamlink.dto.dream.CreateDreamRequest;
import com.dreamlink.dto.dream.UpdateDreamRequest;
import com.dreamlink.entity.Dream;
import com.dreamlink.repository.DreamRepository;
import com.dreamlink.service.DreamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DreamServiceImpl implements DreamService {

    @Autowired
    private DreamRepository dreamRepository;

    @Override
    public Dream createDream(CreateDreamRequest request) {
        Dream dream = new Dream();
        dream.setUserId(request.getUserId());
        dream.setContent(request.getContent());
        dream.setIsPublic(request.isPublic());

        return dreamRepository.save(dream);
    }

    @Override
    public List<Dream> getUserDreams(Long userId) {
        return dreamRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    public List<Dream> getPublicDreams() {
        return dreamRepository.findByIsPublicTrueOrderByCreatedAtDesc();
    }

    @Override
    public Dream getDreamDetail(Long dreamId) {
        return dreamRepository.findById(dreamId)
                .orElseThrow(() -> new RuntimeException("梦境不存在"));
    }

    @Override
    public Dream updateDream(Long dreamId, UpdateDreamRequest request) {
        Dream dream = dreamRepository.findById(dreamId)
                .orElseThrow(() -> new RuntimeException("梦境不存在"));

        dream.setContent(request.getContent());
        dream.setIsPublic(request.isPublic());

        return dreamRepository.save(dream);
    }

    @Override
    public void deleteDream(Long dreamId) {
        if (!dreamRepository.existsById(dreamId)) {
            throw new RuntimeException("梦境不存在");
        }
        dreamRepository.deleteById(dreamId);
    }

}
