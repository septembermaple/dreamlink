package com.dreamlink.controller;

import com.dreamlink.dto.ApiResponse;
import com.dreamlink.dto.dream.CreateDreamRequest;
import com.dreamlink.dto.dream.UpdateDreamRequest;
import com.dreamlink.entity.Dream;
import com.dreamlink.service.DreamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dream")
public class DreamController {

    @Autowired
    private DreamService dreamService;

    @PostMapping
    public ApiResponse<?> createDream(@RequestBody CreateDreamRequest request) {
        try {
            Dream dream = dreamService.createDream(request);
            return ApiResponse.success(dream);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<?> getUserDreams(@PathVariable Long userId) {
        try {
            List<Dream> dreams = dreamService.getUserDreams(userId);
            return ApiResponse.success(dreams);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/public")
    public ApiResponse<?> getPublicDreams() {
        try {
            List<Dream> dreams = dreamService.getPublicDreams();
            return ApiResponse.success(dreams);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/{dreamId}")
    public ApiResponse<?> getDreamDetail(@PathVariable Long dreamId) {
        try {
            Dream dream = dreamService.getDreamDetail(dreamId);
            return ApiResponse.success(dream);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @PutMapping("/{dreamId}")
    public ApiResponse<?> updateDream(@PathVariable Long dreamId, @RequestBody UpdateDreamRequest request) {
        try {
            Dream dream = dreamService.updateDream(dreamId, request);
            return ApiResponse.success(dream);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @DeleteMapping("/{dreamId}")
    public ApiResponse<?> deleteDream(@PathVariable Long dreamId) {
        try {
            dreamService.deleteDream(dreamId);
            return ApiResponse.success(null, "删除成功");
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

}
