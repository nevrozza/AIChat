В Ollama после первого запуска грузится моделька (500 мб) – нужно wait

\+ В ui пока нет стейта "Thinking", поэтому нужно просто ждать...

(на macos нейронка в докере работает в 13131 раз хуже((()

```
# DEV
docker compose -f docker-compose.dev.yml up -d

# PROD
docker compose -f docker-compose.prod.yml up -d
```