#  AI Summarizer

## Overview
Summarizer AI is a **full-stack application** that allows users to generate AI-powered meeting summaries. Users can either input a custom prompt or upload a transcript (meeting notes, call transcript) to generate a structured summary. The output is **editable**, and users can share it via email (optional extension).

---

## Features
- **Custom Prompt Generation**: Users type any instruction like “Highlight action items” or “Summarize in bullet points”.
- **Transcript Summarization**: Users upload a `.txt` file to summarize meeting notes automatically.
- **Editable Output**: AI-generated summaries appear in an editable box for tweaks.
- **Email Sharing (Optional)**: Summaries can be shared via email by entering recipient addresses.

---

## Tech Stack
- **Backend**: Java Spring Boot  
  - WebFlux for reactive REST API calls  
  - Together API for AI text generation  
- **Frontend**: Basic HTML, CSS, and JavaScript  
- **Build Tool**: Maven  
- **Version Control**: Git + GitHub  
- **Deployment**: Render or Railway (supports Spring Boot)  
- **Other Libraries**:  
  - Jackson Databind for JSON parsing  

---

## How It Works
1. **User Interaction**:
   - Type a custom prompt **or** upload a transcript file.
   - Click **Generate** or **Summarize**.
2. **Backend**:
   - Receives prompt or transcript.
   - Calls **Together API** to generate structured summary.
3. **Frontend**:
   - Displays AI-generated summary in an editable box.
   - Users can tweak the text.
4. **(Optional)**: Users can share summary via email.

---

## Getting Started (Local Setup)
1. **Clone the repository**:
   ```bash
   git clone https://github.com/shrutij22/Summarizer.git
   cd Summarizer
