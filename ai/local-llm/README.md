## LLM with GuardRails

Your Java App
↓
User Input → [Llama Guard 3] → Llama 3.2 → [Llama Guard 3] → Output
             (Input Filter)                (Output Filter)
↓
Potentially harmful content blocked before reaching the LLM


llama-guard3:8b - Full featured, multilingual (default)
llama-guard3:1b - Lightweight, on-device inference