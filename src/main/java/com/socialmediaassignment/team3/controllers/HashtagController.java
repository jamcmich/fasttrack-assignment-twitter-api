package com.socialmediaassignment.team3.controllers;

import com.socialmediaassignment.team3.dtos.HashtagResponseDto;
import com.socialmediaassignment.team3.mappers.HashtagMapper;
import com.socialmediaassignment.team3.repositories.HashtagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hashtag")
public class HashtagController {
    private final HashtagRepository hashtagRepository;
    private final HashtagMapper hashtagMapper;

    @GetMapping
    public List<HashtagResponseDto> getAllHashtags() {
        return hashtagMapper.entitiesToDtos(hashtagRepository.findAll());
    }
}
