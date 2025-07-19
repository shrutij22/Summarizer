package com.example.mindsycn.Controller;

import com.example.mindsycn.service.HuggingFaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/ai")
public class MindSyncController {

    private HuggingFaceService huggingFaceService;
    @Autowired

    public MindSyncController(HuggingFaceService huggingFaceService) {
        this.huggingFaceService = huggingFaceService;
    }

    @GetMapping("/generate")
    public Mono<String> generate(@RequestParam String prompt){
        return huggingFaceService.generateText(prompt);
    }

    //This is what your frontend app (like React) will call using a POST request with a JSON body:
//    {
//        "prompt": "Explain Spring Boot"
//    }
    @PostMapping("/generate")
    public Mono<String> generateFromBody(@RequestBody PromptRequest request) {
        return huggingFaceService.generateText(request.getPrompt());
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

}
