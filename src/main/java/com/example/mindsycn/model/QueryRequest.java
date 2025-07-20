package com.example.mindsycn.model;

import lombok.Data;

@Data
public class QueryRequest {


    private String prompt;

    public String getPrompt(){
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }
}
