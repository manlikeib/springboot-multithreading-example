package com.ibrahim.executor.api.controller;

import com.ibrahim.executor.api.entity.User;
import com.ibrahim.executor.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/users",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = "application/json")
    public ResponseEntity saveUsers(@RequestParam(value = "files") MultipartFile[] files) throws Exception {
        for (MultipartFile file: files) {
            userService.saveUsers(file);
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(value = "/users", produces = "application/json")
    public CompletableFuture<ResponseEntity> findAllUsers(){
        return userService.findAllusers().thenApply(ResponseEntity::ok);
    }

    @GetMapping(value = "/getUsersByThread", produces = "application/json")
    public ResponseEntity getUsers(){
        CompletableFuture<List<User>> users1 = userService.findAllusers();
        CompletableFuture<List<User>> users2 = userService.findAllusers();
        CompletableFuture<List<User>> users3 = userService.findAllusers();
        CompletableFuture.allOf(users1, users2, users3);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
