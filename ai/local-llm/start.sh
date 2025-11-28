

# Add your documents
rm -rf /tmp/local-llm/docs/
mkdir -p /tmp/local-llm/docs/
cp ai/local-llm/resources/*.pdf /tmp/local-llm/docs/
cp ai/local-llm/resources/*.docx /tmp/local-llm/docs/
cp ai/local-llm/resources/*.xlsx /tmp/local-llm/docs/

# Run
source ai/local-llm/stop.sh
docker compose -f ai/local-llm/infra/llm.yaml up -d