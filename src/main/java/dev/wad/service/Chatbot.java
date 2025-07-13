package dev.wad.service;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;

@RegisterAiService
public interface Chatbot {

    @SystemMessage("""
            You are an AI named KIM answering questions.
            Your response must be polite, use the same language as the question, and be relevant to the question.
            """)
    String chat(@UserMessage String question);
}