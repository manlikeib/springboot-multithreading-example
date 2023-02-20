package com.ibrahim.executor.api.service;

import com.ibrahim.executor.api.entity.User;
import com.ibrahim.executor.api.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    Logger logger = LoggerFactory.getLogger(UserService.class);

    @Async
    public CompletableFuture<List<User>> saveUsers(MultipartFile file) throws Exception {
        long start = System.currentTimeMillis();
        List<User> users = parseCSVFile(file);
        logger.info("saving list of users of size {}", users.size(), ""+ Thread.currentThread().getName());
        users = repository.saveAll(users);
        long end = System.currentTimeMillis();
        logger.info("Total time {}", (end-start));
        return CompletableFuture.completedFuture(users);
    }

    @Async
    public CompletableFuture<List<User>> findAllusers(){
        logger.info("get list of users by " + Thread.currentThread().getName());
        List<User> users = repository.findAll();
        return CompletableFuture.completedFuture(users);
    }

    private List<User> parseCSVFile(final MultipartFile file) throws Exception {
        final List<User> users = new ArrayList<>();
        try {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            String line;
            while ((line= reader.readLine()) != null) {
                final String[] data = line.split(",");
                final User user = new User();
                user.setName(data[0]);
                user.setEmail(data[1]);
                user.setGender(data[2]);
                users.add(user);
            }
            return users;
        } catch (IOException e) {
            logger.error("Failed to parse CSV file {}",e);
            throw new Exception("Failed to parse CSV file {}",e);
        }
    }
}
