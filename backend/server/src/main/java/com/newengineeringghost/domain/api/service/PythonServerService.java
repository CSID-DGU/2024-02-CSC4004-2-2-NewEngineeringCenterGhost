package com.newengineeringghost.domain.api.service;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;

@Slf4j
@Component
public class PythonServerService {

    private Process pythonServerProcess;

    @Value("${python.script.path.server}")
    private String serverScriptPath;

//    private String serverScriptPath;

    public PythonServerService() {
        log.info("Python Server Started.");
    }

    // SpringApplication 실행 시 자동으로 python server 실행
    public void startPythonServer(String argument) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("python3", serverScriptPath, argument);
        pythonServerProcess = processBuilder.start();
        log.info("Python Server Process: {}", pythonServerProcess);
    }

//    // SpringApplication 실행 시 자동으로 python server 실행
//    public void startPythonServer(String argument) throws IOException {
//        InputStream tempStream = getClass().getClassLoader().getResourceAsStream("core/server.py");
//        log.info("Tempstream: {}", tempStream);
//        if (tempStream == null) {
//            throw new IOException("File not found in classpath");
//        }
//
//        Path tempscriptPath = createTempFileFromStream(tempStream);
//
//        serverScriptPath = tempscriptPath.toAbsolutePath().toString();
//        log.info("Server script path: {}", serverScriptPath);
//
//        ProcessBuilder processBuilder = new ProcessBuilder("python3", serverScriptPath, argument);
//        pythonServerProcess = processBuilder.start();
//        log.info("Python Server Process: {}", pythonServerProcess);
//
//        tempscriptPath.toFile().deleteOnExit();
//    }

//    // 임시 파일 생성 메서드
//    private Path createTempFileFromStream(InputStream inputStream) throws IOException {
//        // 임시 파일 생성
//        Path tempFile = Files.createTempFile("script", ".py");
//
//        // 파일에 내용을 UTF-8로 저장
//        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
//             FileWriter writer = new FileWriter(tempFile.toFile(), StandardCharsets.UTF_8)) {
//
//            String line;
//            while ((line = reader.readLine()) != null) {
//                writer.write(line + System.lineSeparator());
//            }
//        }
//
//        // 파일 권한 설정
//        Files.setPosixFilePermissions(tempFile, PosixFilePermissions.fromString("rwxr-xr-x"));
//        return tempFile;
//    }

    // SpringApplication 종료 시 자동으로 python server 종료 -> 메모리 누수 막기 위함
    @PreDestroy
    public void stopPythonServer() {
        if (pythonServerProcess != null && pythonServerProcess.isAlive()) {
            pythonServerProcess.destroy();
            log.info("Python Server Process with PID {} has been destroyed.", pythonServerProcess.pid());
        } else {
            log.info("Python Server Process is Null!");
        }
    }
}
