package com.newengineeringghost.domain.api.service;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class PythonServerService {

    private Process pythonServerProcess;

    public PythonServerService() {
        log.info("Python Server Started.");
    }

    // SpringApplication 실행 시 자동으로 python server 실행
    public void startPythonServer(String argument) throws IOException {
        // Todo : 파일경로 ${Path} 등으로 변경
        ProcessBuilder processBuilder = new ProcessBuilder("python3", "/home/testuser/project/backend/server/src/main/resources/core/server.py", argument);
        pythonServerProcess = processBuilder.start();
        log.info("Process: {}", pythonServerProcess);
    }

    // SpringApplication 종료 시 자동으로 python server 종료 -> 메모리 누수 막기 위함
    @PreDestroy
    public void stopPythonServer() {
        if (pythonServerProcess != null && pythonServerProcess.isAlive()) {
            pythonServerProcess.destroy();
            log.info("Python process with PID {} has been destroyed.", pythonServerProcess.pid());
        } else {
            log.info("Python Server Process is Null!");
        }
    }
}
