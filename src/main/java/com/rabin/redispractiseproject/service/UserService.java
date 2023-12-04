package com.rabin.redispractiseproject.service;

import com.rabin.redispractiseproject.dto.UserRequest;

import java.util.List;

public interface UserService {
    String savingTheRecord(UserRequest userRequest);

    List<UserRequest> listOfRecord();

    UserRequest getRecordByName(String name) throws Exception;
}
