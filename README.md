В Ollama после первого запуска грузится моделька (500 мб) – нужно wait

\+ В ui пока нет стейта "Thinking", поэтому нужно просто ждать...

```
# DEV
docker compose -f docker-compose.dev.yml up -d

# PROD
docker compose -f docker-compose.prod.yml up -d
```