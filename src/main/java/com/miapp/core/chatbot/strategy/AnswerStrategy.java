package com.miapp.core.chatbot.strategy;

public interface AnswerStrategy {
    String generateAnswer(String question);
    boolean canHandle(String question);
    String getStrategyName();
}