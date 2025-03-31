package com.example.chatbot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

@Controller
public class ChatbotController {

    @Autowired
    private OllamaService ollamaService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/chat")
    @ResponseBody
    public String chat(@RequestParam String message) {
       
      //  String[] BLOCKED_WORDS = {"badword1", "badword2"};

        // Check for blocked words
    /** /    for (String word : BLOCKED_WORDS) {
            if (message.toLowerCase().contains(word)) {
                return " Message contains blocked words.";
            }
        }
        // Get response from Ollama Service **/
        String response = ollamaService.getOllamaResponse(message);
        return response;
    }
}
