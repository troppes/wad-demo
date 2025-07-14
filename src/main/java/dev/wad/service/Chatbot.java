package dev.wad.service;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.wad.tools.JVMTool;
import io.quarkiverse.langchain4j.RegisterAiService;

@RegisterAiService(tools = {JVMTool.class})
public interface Chatbot {

    @SystemMessage("""
            You are an AI assistant with access to JVM tools and MCP servers.
            When users ask for JVM statistics or system information, use the available tools directly.
            Only use document context for questions about Kubelab.
            """)
    String chat(@UserMessage String question);
}