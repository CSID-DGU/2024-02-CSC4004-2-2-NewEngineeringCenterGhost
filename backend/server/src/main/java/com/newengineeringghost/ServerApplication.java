package com.newengineeringghost;

import com.newengineeringghost.domain.api.service.PythonServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class ServerApplication {

    @Autowired
    PythonServerService pythonServerService;

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initialize(ApplicationReadyEvent event) throws Exception {
        pythonServerService.startPythonServer("127.0.0.1");
    }
}
