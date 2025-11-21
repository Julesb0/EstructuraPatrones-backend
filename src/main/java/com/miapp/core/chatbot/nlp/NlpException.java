package com.miapp.core.chatbot.nlp;

public class NlpException extends Exception {
    public NlpException(String message) {
        super(message);
    }
    
    public NlpException(String message, Throwable cause) {
        super(message, cause);
    }
}