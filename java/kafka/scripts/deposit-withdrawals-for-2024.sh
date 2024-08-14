for i in {1..10}
do
  curl -sX POST "http://localhost:8888/deposit?accountId=1&amount=101" | jq
  curl -sX POST "http://localhost:8888/deposit?accountId=1&amount=102" | jq
  curl -sX POST "http://localhost:8888/deposit?accountId=1&amount=103" | jq
  curl -sX POST "http://localhost:8888/deposit?accountId=1&amount=104" | jq
  curl -sX POST "http://localhost:8888/deposit?accountId=1&amount=105" | jq

  curl -sX POST "http://localhost:8888/withdraw?accountId=1&amount=1001" | jq
  curl -sX POST "http://localhost:8888/withdraw?accountId=1&amount=505" | jq
done
