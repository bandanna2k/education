FROM nvidia/cuda:11.8.0-cudnn8-runtime-ubuntu22.04

# Set environment variables early (rarely changes)
ENV DEBIAN_FRONTEND=noninteractive \
    PYTHONUNBUFFERED=1 \
    PIP_NO_CACHE_DIR=1

# Install system dependencies (changes infrequently)
RUN apt-get update && apt-get install -y --no-install-recommends \
    wget \
    git \
    python3 \
    python3-pip \
    python3-venv \
    libgl1 \
    libglib2.0-0 \
    libsm6 \
    libxrender1 \
    libxext6 \
    unzip \
    && rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Clone repository (layer cached unless repo changes)
RUN git clone https://github.com/AUTOMATIC1111/stable-diffusion-webui.git /app

# Create Python virtual environment (separate layer for better caching)
RUN python3 -m venv venv

# Upgrade pip (separate layer)
RUN . venv/bin/activate && pip install --upgrade pip

# Install PyTorch (largest dependency, separate layer for caching)
RUN . venv/bin/activate && \
    pip install torch torchvision torchaudio --index-url https://download.pytorch.org/whl/cu118

# Pre-install common dependencies (optional, improves first run)
RUN . venv/bin/activate && \
    pip install transformers diffusers accelerate safetensors huggingface-hub open-clip-torch

# Create directories for models and outputs
RUN mkdir -p models/Stable-diffusion outputs extensions

# Download Stable Diffusion models during build
RUN . venv/bin/activate && \
    python3 -c "from huggingface_hub import hf_hub_download; \
    hf_hub_download(repo_id='runwayml/stable-diffusion-v1-5', \
                    filename='v1-5-pruned-emaonly.safetensors', \
                    local_dir='models/Stable-diffusion', \
                    local_dir_use_symlinks=False)"

# Download VAE model (improves image quality)
RUN . venv/bin/activate && \
    python3 -c "from huggingface_hub import hf_hub_download; \
    hf_hub_download(repo_id='stabilityai/sd-vae-ft-mse-original', \
                    filename='vae-ft-mse-840000-ema-pruned.safetensors', \
                    local_dir='models/VAE', \
                    local_dir_use_symlinks=False)"

# Pre-download CLIP and OpenCLIP models (prevents runtime downloads)
RUN . venv/bin/activate && \
    python3 -c "import torch; from transformers import CLIPTokenizer, CLIPTextModel; \
    CLIPTokenizer.from_pretrained('openai/clip-vit-large-patch14'); \
    CLIPTextModel.from_pretrained('openai/clip-vit-large-patch14')"

RUN . venv/bin/activate && \
    python3 -c "import open_clip; \
    open_clip.create_model_and_transforms('ViT-L-14', pretrained='openai')"

# Pre-download required repositories using wget (avoids git credential issues)
# Download as zip and extract, then remove ALL git metadata to prevent fetch attempts
RUN mkdir -p repositories && \
    wget -O /tmp/sd.zip https://github.com/CompVis/stable-diffusion/archive/refs/heads/main.zip && \
    unzip -q /tmp/sd.zip -d /tmp && mv /tmp/stable-diffusion-main repositories/stable-diffusion-stability-ai && \
    find repositories/stable-diffusion-stability-ai -name ".git*" -exec rm -rf {} + 2>/dev/null || true && rm /tmp/sd.zip && \
    wget -O /tmp/gm.zip https://github.com/Stability-AI/generative-models/archive/refs/heads/main.zip && \
    unzip -q /tmp/gm.zip -d /tmp && mv /tmp/generative-models-main repositories/generative-models && \
    find repositories/generative-models -name ".git*" -exec rm -rf {} + 2>/dev/null || true && rm /tmp/gm.zip && \
    wget -O /tmp/kd.zip https://github.com/crowsonkb/k-diffusion/archive/refs/heads/master.zip && \
    unzip -q /tmp/kd.zip -d /tmp && mv /tmp/k-diffusion-master repositories/k-diffusion && \
    find repositories/k-diffusion -name ".git*" -exec rm -rf {} + 2>/dev/null || true && rm /tmp/kd.zip && \
    wget -O /tmp/cf.zip https://github.com/sczhou/CodeFormer/archive/refs/heads/master.zip && \
    unzip -q /tmp/cf.zip -d /tmp && mv /tmp/CodeFormer-master repositories/CodeFormer && \
    find repositories/CodeFormer -name ".git*" -exec rm -rf {} + 2>/dev/null || true && rm /tmp/cf.zip && \
    wget -O /tmp/blip.zip https://github.com/salesforce/BLIP/archive/refs/heads/main.zip && \
    unzip -q /tmp/blip.zip -d /tmp && mv /tmp/BLIP-main repositories/BLIP && \
    find repositories/BLIP -name ".git*" -exec rm -rf {} + 2>/dev/null || true && rm /tmp/blip.zip

# Install any additional Python dependencies from requirements files
RUN . venv/bin/activate && \
    pip install -r repositories/CodeFormer/requirements.txt 2>/dev/null || true

# Expose port
EXPOSE 7860

# Set the entrypoint (with API enabled)
# Added --skip-torch-cuda-test to handle both GPU and CPU scenarios
# Set COMMANDLINE_ARGS environment variable to skip git checks
ENV GIT_TERMINAL_PROMPT=0
ENV GIT_ASKPASS=/bin/echo
ENV COMMANDLINE_ARGS="--skip-version-check"

ENTRYPOINT ["/bin/bash", "-c", "git config --global credential.helper store && echo 'https://:@github.com' > ~/.git-credentials && source venv/bin/activate && python launch.py --listen --port 7860 --enable-insecure-extension-access --api --skip-torch-cuda-test --skip-version-check"]

# Usage:
# Build: docker build -t automatic1111 .
#
# Run with GPU:
# docker run --gpus all -p 7860:7860 -v $(pwd)/models:/app/models/Stable-diffusion -v $(pwd)/outputs:/app/outputs automatic1111
#
# Run CPU-only (slower, no GPU needed):
# docker run -p 7860:7860 -v $(pwd)/models:/app/models/Stable-diffusion -v $(pwd)/outputs:/app/outputs text-to-image_automatic1111 --skip-torch-cuda-test --no-half --use-cpu all
#
# API docs: http://localhost:7860/docs
#
# Example API call:
# curl -X POST "http://localhost:7860/sdapi/v1/txt2img" \
#   -H "Content-Type: application/json" \
#   -d '{
#     "prompt": "a beautiful sunset over mountains, photorealistic, 4k",
#     "negative_prompt": "blurry, low quality, distorted",
#     "steps": 20,
#     "width": 512,
#     "height": 512,
#     "cfg_scale": 7,
#     "sampler_name": "Euler a",
#     "batch_size": 1
#   }' | jq -r '.images[0]' | base64 -d > output.png