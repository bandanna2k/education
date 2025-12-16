FROM python:3.10-slim

WORKDIR /app

# Layer 1: System dependencies
RUN apt-get update && apt-get install -y --no-install-recommends \
    build-essential \
    && rm -rf /var/lib/apt/lists/*

# Layer 2: PyTorch
RUN pip install --no-cache-dir torch==2.1.0

# Layer 3: Transformers
RUN pip install --no-cache-dir transformers==4.35.0

# Layer 4: Diffusers
RUN pip install --no-cache-dir diffusers==0.21.0

# Layer 5: HuggingFace Hub
RUN pip install --no-cache-dir huggingface_hub==0.17.0

# Layer 6: Utilities
RUN pip install --no-cache-dir numpy==1.24.0 pillow==10.1.0 flask==3.0.0
# RUN pip install --no-cache-dir numpy==1.24.0 scipy==1.11.0 pillow==10.1.0 flask==3.0.0 safetensors==0.4.0 requests==2.31.0

COPY app.py .

RUN python -c "from diffusers import StableDiffusionPipeline; StableDiffusionPipeline.from_pretrained('runwayml/stable-diffusion-v1-5', torch_dtype=__import__('torch').float32, cache_dir='/app/models')"

EXPOSE 5000

CMD ["python", "app.py"]