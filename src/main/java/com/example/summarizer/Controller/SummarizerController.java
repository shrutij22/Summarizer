package com.example.summarizer.Controller;

import com.example.summarizer.service.TogetherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ai")
public class SummarizerController {

    private final TogetherService huggingFaceService;

    @Autowired
    public SummarizerController(TogetherService huggingFaceService) {
        this.huggingFaceService = huggingFaceService;
    }

    // -------- Prompt-based generation --------
    @GetMapping("/generate")
    public Mono<String> generate(@RequestParam String prompt){
        return huggingFaceService.generateScheduleFromThought(prompt);
    }

    @PostMapping("/generate")
    public Mono<String> generateFromBody(@RequestBody PromptRequest request) {
        return huggingFaceService.generateScheduleFromThought(request.getPrompt());
    }

    public static class PromptRequest{
        private String prompt;

        public String getPrompt() {
            return prompt;
        }

        public void setPrompt(String prompt) {
            this.prompt = prompt;
        }
    }

    // -------- Transcript summarization --------
    @PostMapping("/summarize")
    public Mono<String> summarizeTranscript(@RequestParam("file") MultipartFile file) {
        try {
            // Read file content as text
            String transcript = new BufferedReader(new InputStreamReader(file.getInputStream()))
                    .lines()
                    .collect(Collectors.joining("\n"));

            // Send transcript to AI service
            String prompt = "Summarize this transcript into clear bullet points:\n\n" + transcript;
            return huggingFaceService.generateScheduleFromThought(prompt);

        } catch (IOException e) {
            return Mono.error(new RuntimeException("Failed to read file", e));
        }
    }
}
