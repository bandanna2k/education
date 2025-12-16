FROM python:3.10-slim

WORKDIR /app

# Layer 1: System dependencies
RUN apt-get update && apt-get install -y --no-install-recommends \
    build-essential \
    && rm -rf /var/lib/apt/lists/*

# Layer 2: Core ML dependencies
RUN pip install --no-cache-dir \
    torch==2.1.0 \
    torchvision==0.16.0

# Layer 3: HuggingFace ecosystem
RUN pip install --no-cache-dir \
    transformers==4.35.0 \
    diffusers==0.21.0 \
    huggingface_hub==0.17.0

# Layer 4: Utility dependencies
RUN pip install --no-cache-dir \
    pillow==10.1.0 \
    flask==3.0.0

COPY app.py .

EXPOSE 5000

CMD ["python", "app.py"]