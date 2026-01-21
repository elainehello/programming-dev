from typing import Dict, Any
from fastapi import APIRouter, Request, HTTPException, Depends
import httpx
import logging
import json

from app.middleware.auth import get_current_user


# Instantiate object
router = APIRouter()
logger = logging.getLogger(__name__)

@router.get("/{path:path}")
async def proxy_to_data_service(
    path: str, 
    request: Request, 
    current_user: Dict[str, Any] = Depends(get_current_user)) -> Dict[str, str]:
    """
    Acts as entru point for all v1 data requests.
    """
    # 1. Access the shared client from app.state
    client: httpx.AsyncClient = request.app.state.http_client
    redis = request.app.state.redis

    # 2. Check cache
    # User context
    cache_key = f"v1_cache:{current_user['user_id']}:{path}"
    cached = await redis.get(cache_key)
    if cached:
        cached = json.loads(cached) # Parse JSON string back to dict
        logger.info(f"User {current_user['user_id']} accessed {path} from cache")
        return {"source": "sentinel-cache", "version": "v1", "data": cached}

    # 3. Forward the request
    try:
        # User ID to Forwarded Headers
        headers : Dict[str, Any] = {"X-User-ID": current_user["user_id"]}
        response = await client.get(f"/{path}", params=request.query_params, headers=headers)
        response.raise_for_status()
        logger.info(f"User {current_user['user_id']} accessed {path} from data-service")

        data = response.json()
        # Save to cache
        await redis.setex(cache_key, 60, json.dumps(data))

        return {"source": "data-service", "version": "v1", "data": data}

    except httpx.HTTPStatusError as e:
        raise HTTPException(status_code=e.response.status_code, detail="Remote service error") from e
