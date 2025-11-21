package com.miapp.core.chatbot.nlp;

public interface NlpClient {
    String ask(String prompt) throws NlpException;
    String getClientInfo();
}