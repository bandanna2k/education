```shell
docker run -p 5000:5000 text-to-image
```


```shell
curl -X POST http://localhost:5000/generate \
  -H "Content-Type: application/json" \
  -d '{"prompt": "a cat"}' \
  --output ai-cat.png
```

```shell
curl -X POST http://localhost:5000/generate \
  -H "Content-Type: application/json" \
  -d '{"prompt": "a deck of cards on grass"}' \
  --output ai-cards.png
```