# Usage:

```shell
docker build -t text-to-image_automatic1111 .
```


# Run with GPU:
```shell
docker run --gpus all -p 7860:7860 -v $(pwd)/models:/app/models/Stable-diffusion -v $(pwd)/outputs:/app/outputs automatic1111
```

# Run CPU-only (slower, no GPU needed):
```shell
docker run -p 7860:7860 -v $(pwd)/models:/app/models/Stable-diffusion -v $(pwd)/outputs:/app/outputs text-to-image_automatic1111 --skip-torch-cuda-test --no-half --use-cpu all
```

# API docs: http://localhost:7860/docs

# Example API call:
```shell
curl -X POST "http://localhost:7860/sdapi/v1/txt2img" \
  -H "Content-Type: application/json" \
  -d '{
    "prompt": "a beautiful sunset over mountains, photorealistic, 4k",
    "steps": 20,
    "width": 1024,
    "height": 768,
    "cfg_scale": 7,
    "sampler_name": "Euler a",
    "batch_size": 1
  }' | jq -r '.images[0]' | base64 -d > output.png
```

#     "negative_prompt": "blurry, low quality, distorted",
