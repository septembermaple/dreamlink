package com.dreamlink.service;

import com.dreamlink.dto.dream.CreateDreamRequest;
import com.dreamlink.dto.dream.UpdateDreamRequest;
import com.dreamlink.entity.Dream;

import java.util.List;

public interface DreamService {

    Dream createDream(CreateDreamRequest request);

    List<Dream> getUserDreams(Long userId);

    List<Dream> getPublicDreams();

    Dream getDreamDetail(Long dreamId);

    Dream updateDream(Long dreamId, UpdateDreamRequest request);

    void deleteDream(Long dreamId);

}
