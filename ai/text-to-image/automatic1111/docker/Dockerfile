FROM python:3.10-slim

# Install system dependencies
RUN apt-get update && apt-get install -y \
    git \
    wget \
    libgl1 \
    libglib2.0-0 \
    libgomp1 \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Clone Automatic1111 first (so we have the correct directory structure)
RUN git clone https://github.com/AUTOMATIC1111/stable-diffusion-webui.git .

# Clone repositories (changes rarely)
RUN mkdir -p repositories && \
    cd repositories && \
    git clone https://github.com/CompVis/stable-diffusion.git stable-diffusion-stability-ai && \
    cd stable-diffusion-stability-ai && git checkout 69ae4b35e0a0f6ee1af8bb9a5d0016ccb27e36dc && cd .. && \
    git clone https://github.com/CompVis/taming-transformers.git && \
    git clone https://github.com/crowsonkb/k-diffusion.git && \
    git clone https://github.com/sczhou/CodeFormer.git && \
    git clone https://github.com/salesforce/BLIP.git && \
    git clone https://github.com/Stability-AI/generative-models.git

# Install torch first (before k-diffusion can pull in a different version)
RUN pip install --no-cache-dir \
    torch==2.0.1 \
    torchvision==0.15.2 \
    torchaudio==2.0.2 \
    --index-url https://download.pytorch.org/whl/cpu

# Install k-diffusion dependencies manually (to control torch version)
RUN pip install --no-cache-dir \
    scipy \
    tqdm \
    jsonmerge \
    resize-right \
    dctorch \
    clean-fid \
    clip-anytorch \
    matplotlib \
    Pillow \
    wandb

# Install k-diffusion WITHOUT dependencies (to avoid torch upgrade)
RUN pip install --no-cache-dir --no-deps -e repositories/k-diffusion

# Install taming-transformers WITHOUT dependencies
RUN pip install --no-cache-dir --no-deps -e repositories/taming-transformers

# Download model (large, but changes rarely - cache this layer)
RUN wget -O models/Stable-diffusion/v1-5-pruned-emaonly.safetensors \
    https://huggingface.co/runwayml/stable-diffusion-v1-5/resolve/main/v1-5-pruned-emaonly.safetensors

# Install requirements from file if it exists, otherwise install manually
RUN if [ -f requirements_versions.txt ]; then \
        pip install --no-cache-dir -r requirements_versions.txt; \
    else \
        pip install --no-cache-dir \
            transformers==4.30.2 \
            accelerate==0.20.3 \
            basicsr==1.4.2 \
            gfpgan==1.3.8 \
            gradio==3.41.2 \
            numpy==1.23.5 \
            Pillow==9.5.0 \
            realesrgan==0.3.0 \
            opencv-python==4.8.0.74 \
            requests==2.31.0 \
            piexif==1.1.3 \
            einops==0.4.1 \
            jsonmerge==1.8.0 \
            clean-fid==0.1.35 \
            resize-right==0.0.2 \
            torchdiffeq==0.2.3 \
            kornia==0.6.7 \
            lark==1.1.2 \
            inflection==0.5.1 \
            GitPython==3.1.32 \
            torchsde==0.2.6 \
            safetensors==0.3.1 \
            psutil==5.9.5 \
            rich==13.3.5 \
            open-clip-torch==2.20.0 \
            omegaconf==2.2.3 \
            pytorch-lightning==1.9.0 \
            scikit-image==0.21.0 \
            timm==0.6.13 \
            blendmodes==2022 \
            httpcore==0.15; \
    fi

# Install clip and ftfy (required by stable-diffusion repo)
RUN pip install --no-cache-dir ftfy clip-anytorch

# Disable MiDaS by commenting out the import
RUN sed -i 's/^import ldm.modules.midas as midas/# import ldm.modules.midas as midas/' modules/sd_models.py && \
    sed -i 's/midas\.MidasDetector/None/' modules/sd_models.py

# Fix ATTENTION_MODES compatibility issue
RUN sed -i 's/ldm.modules.attention.BasicTransformerBlock.ATTENTION_MODES\["softmax-xformers"\]/# ldm.modules.attention.BasicTransformerBlock.ATTENTION_MODES["softmax-xformers"]/' modules/sd_hijack.py || true

# Disable MiDaS imports in processing.py
RUN sed -i 's/^from ldm.data.util import AddMiDaS/# from ldm.data.util import AddMiDaS/' modules/processing.py && \
    sed -i 's/AddMiDaS/None/' modules/processing.py

# Create necessary runtime directories and files (lightweight, last)
RUN mkdir -p outputs embeddings extensions && \
    touch styles.csv

# Install CodeFormer requirements if they exist
RUN if [ -f repositories/CodeFormer/requirements.txt ]; then \
        pip install --no-cache-dir -r repositories/CodeFormer/requirements.txt; \
    fi

EXPOSE 7860

CMD ["python3", "webui.py", "--listen", "--api", "--skip-torch-cuda-test", "--no-half", "--port", "7860", "--skip-install", "--skip-version-check"]