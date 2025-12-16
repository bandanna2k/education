from flask import Flask, request, send_file
from diffusers import StableDiffusionPipeline
import torch
import io
import os

app = Flask(__name__)

# Force local-only mode
os.environ['HF_HUB_OFFLINE'] = '1'

device = "cpu"
pipe = StableDiffusionPipeline.from_pretrained(
    "runwayml/stable-diffusion-v1-5",
    torch_dtype=torch.float32,
    cache_dir="/app/models",
    local_files_only=True
)
pipe = pipe.to(device)

@app.route('/generate', methods=['POST'])
def generate():
    data = request.json
    prompt = data.get('prompt', '')

    image = pipe(prompt, num_inference_steps=20).images[0]

    img_io = io.BytesIO()
    image.save(img_io, 'PNG')
    img_io.seek(0)

    return send_file(img_io, mimetype='image/png')

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)