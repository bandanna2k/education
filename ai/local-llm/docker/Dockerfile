FROM ollama/ollama:0.13.0
RUN ollama serve & sleep 5 && ollama pull llama3.2 && pkill ollama
RUN ollama serve & sleep 5 && ollama pull moondream:1.8b && pkill ollama
