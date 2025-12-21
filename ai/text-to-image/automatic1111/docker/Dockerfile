FROM python:3.10-slim

# Install system dependencies
RUN apt-get update && apt-get install -y \
    git \
    wget \
    libgl1 \
    libglib2.0-0 \
    && rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /stable-diffusion-webui

# Clone Automatic1111
RUN git clone https://github.com/AUTOMATIC1111/stable-diffusion-webui.git . && \
    git checkout v1.7.0

# Install Python dependencies for CPU
RUN pip install --no-cache-dir torch torchvision torchaudio --index-url https://download.pytorch.org/whl/cpu

# Install all required dependencies
RUN pip install --no-cache-dir -r requirements_versions.txt

# Install additional required packages
RUN pip install --no-cache-dir \
    open-clip-torch \
    clip-anytorch

# Clone all required repositories
RUN mkdir -p repositories && \
    cd repositories && \
    git clone https://github.com/CompVis/stable-diffusion.git stable-diffusion-stability-ai && \
    cd stable-diffusion-stability-ai && \
    git checkout 69ae4b35e0a0f6ee1af8bb9a5d0016ccb27e36dc && \
    cd .. && \
    git clone https://github.com/Stability-AI/generative-models.git generative-models --depth 1 && \
    git clone https://github.com/crowsonkb/k-diffusion.git k-diffusion --depth 1 && \
    git clone https://github.com/sczhou/CodeFormer.git CodeFormer --depth 1 && \
    git clone https://github.com/salesforce/BLIP.git BLIP --depth 1 && \
    git clone https://github.com/CompVis/taming-transformers.git taming-transformers --depth 1

# Install k-diffusion and taming-transformers dependencies
RUN cd repositories/k-diffusion && pip install --no-cache-dir -e . && \
    cd ../taming-transformers && pip install --no-cache-dir -e .

# Download Stable Diffusion v1.5 model during build
RUN mkdir -p models/Stable-diffusion && \
    wget -q --show-progress -O models/Stable-diffusion/v1-5-pruned-emaonly.safetensors \
    https://huggingface.co/runwayml/stable-diffusion-v1-5/resolve/main/v1-5-pruned-emaonly.safetensors

# Expose port
EXPOSE 7860

# Run with minimal flags - let it handle dependencies on first run
CMD ["python", "webui.py", "--listen", "--api", "--skip-torch-cuda-test", "--no-half", "--port", "7860"]