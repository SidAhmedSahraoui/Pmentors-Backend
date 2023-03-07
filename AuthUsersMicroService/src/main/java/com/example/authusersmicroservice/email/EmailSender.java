package com.example.authusersmicroservice.email;

public interface EmailSender {
    void send(String to, String email);
}
