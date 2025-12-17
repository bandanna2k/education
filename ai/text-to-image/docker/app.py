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
    seed = data.get('seed', None)
    width = int(data.get('width', 512))
    height = int(data.get('height', 512))
    steps = int(data.get('steps', 20))
    guidance_scale = float(data.get('guidance_scale', 7.5))

    generator = None
    if seed is not None:
        generator = torch.Generator(device=device).manual_seed(int(seed))

    image = pipe(
        prompt=prompt,
        height=height,
        width=width,
        num_inference_steps=steps,
        guidance_scale=guidance_scale,
        generator=generator
    ).images[0]

    img_io = io.BytesIO()
    image.save(img_io, 'PNG')
    img_io.seek(0)

    return send_file(img_io, mimetype='image/png')

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)