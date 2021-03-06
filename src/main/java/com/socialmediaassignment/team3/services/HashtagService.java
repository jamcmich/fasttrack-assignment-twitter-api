package com.socialmediaassignment.team3.services;

import com.socialmediaassignment.team3.dtos.HashtagResponseDto;
import com.socialmediaassignment.team3.dtos.TweetResponseDto;

import java.util.List;

public interface HashtagService {
    List<HashtagResponseDto> getAllHashtags();

    List<TweetResponseDto> getTweetByTag(String label);
}
