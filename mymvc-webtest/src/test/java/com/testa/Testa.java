package com.testa;

import com.wanghz.mymvc.exception.GlobalExceptionHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Testa {
    public static void main(String[] args) throws IOException {
        GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(globalExceptionHandler);
        Path file = Paths.get("");
        Files.delete(file);
    }
}
