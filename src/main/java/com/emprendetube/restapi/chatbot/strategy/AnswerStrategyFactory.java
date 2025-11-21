package com.emprendetube.restapi.chatbot.strategy;

import com.emprendetube.restapi.entity.ChatMessageCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AnswerStrategyFactory {

    private final List<AnswerStrategy> strategies;

    @Autowired
    public AnswerStrategyFactory(List<AnswerStrategy> strategies) {
        this.strategies = strategies;
    }

    public AnswerStrategy getStrategy(ChatMessageCategory category) {
        return strategies.stream()
                .filter(strategy -> strategy.canHandle(category))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No strategy found for category: " + category));
    }
}