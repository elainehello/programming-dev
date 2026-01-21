import logging
from fastapi import FastAPI
from contextlib import asynccontextmanager
from app.routes import router as v1_router
from app.config import settings

import httpx
import redis.asyncio as redis

# Configure Structure Logging
logging.basicConfig(
    level=logging.INFO,
    format="%(levelname)s: [%(name)s] %(message)s"
)
logger = logging.getLogger("sentinel")

# Handles application lifecycle and "plugs in" the routes with the /v1 prefix
@asynccontextmanager
async def lifespan(app: FastAPI):
    # Startup
    try:
        # Connect to Redis
        app.state.redis = redis.from_url(settings.REDIS_URL, decode_responses=True)
        await app.state.redis.ping() # Verify connection
        logger.info("✓ Redis connected")

        # Create HTTP client
        app.state.http_client = httpx.AsyncClient(base_url=settings.DATA_SERVICE_URL)
        logger.info("✓ HTTP client initialised")

        logger.info("Sentinel Gateway starting up...")
    except redis.ConnectionError as e:
        logging.error(f"✗ Redis connection failed {e}")
        raise
    yield

    # Clean Shutdown
    try:
        await app.state.redis.close()
        await app.state.http_client.aclose()
        logger.info("Sentinel Gateway shutting down")
    except Exception as e:
        logger.error(f"✗ Shutdown error: {e}")

app = FastAPI(title="Sentinel", lifespan=lifespan)
app.include_router(v1_router, prefix="/api/v1")

@app.get("/health")
async def health_check():
    """
    Enhanced health check that verifies dependencies
    """
    try:
        # Check Redis
        await app.state.redis.ping()
        redis_status = "healthy"
    except Exception as e:
        logger.warning(f"Redis health check failed: {e}")
        redis_status = "unhealthy"

    # Check data service( -optional- can add later with timeout)
    return {
        "status": "Sentinel is active", 
        "redis": redis_status
    }
