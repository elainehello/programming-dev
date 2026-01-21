from fastapi import FastAPI
from contextlib import asynccontextmanager
import httpx
import redis.asyncio as redis

from app.config import settings
from app.routes import router as gateway_router # Import your routes

# Handles application lifecycle and "plugs in" the routes with the /v1 prefix
@asynccontextmanager
async def lifespan(app: FastAPI):
    # Startup
    app.state.redis = redis.from_url(settings.REDIS_URL, decode_responses=True)
    app.state.http_client = httpx.AsyncClient(base_url=settings.DATA_SERVICE_URL)
    yield
    # Shutdown
    await app.state.redis.close()
    await app.state.http_client.aclose()

app = FastAPI(title="Sentinel Gateway", lifespan=lifespan)

# Register the routes with a prefix
app.include_router(gateway_router, prefix="/api/v1", tags=["v1"])

@app.get("/health")
async def health_check():
    return {"status": "Sentinel is active"}
