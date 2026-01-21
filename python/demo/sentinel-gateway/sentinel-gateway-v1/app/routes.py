import httpx
import logging
import json
from typing import Dict, Any
from fastapi import APIRouter, Request, HTTPException, Depends
from app.middleware.auth import get_current_user

# Instantiate object
router = APIRouter()
logger = logging.getLogger("sentinel")

@router.get("/{path:path}")
async def proxy_to_data_service(
    path: str, 
    request: Request, 
    current_user: Dict[str, Any] = Depends(get_current_user)
    ) -> Dict[str, str]:
    """
    Acts as entru point for all v1 data requests.
    """
    # --- 1. Validate path (prevent traversal attacks) ---
    if ".." in path or path.startswith("/"):
        logger.warning(f"Invalid path attemted: {path}")
        raise HTTPException(status_code=400, detail="Invalid path format")

    # 2. Get clients from app state
    client: httpx.AsyncClient = request.app.state.http_client
    redis = request.app.state.redis

    # 3. Build cache key (include query params)
    query_string = str(dict(request.query_params)) if request.query_params else ""
    cache_key = f"v1_cache:{current_user['user_id']}:{path}:{query_string}"

    # 4. Check cache (with error handling)
    try:
        cached_data = await redis.get(cache_key)
        if cache_key:
            parsed = json.loads(cached_data)
            logger.info(f"User {current_user['user_id']} accessed {path} from cache")
            return {
                "source": "sentinel-cache",
                "version": "v1",
                "data": parsed
            }
    except Exception as e:
        logger.warning(f"Cache retrieval failed: {e}. Continuing without cache.")

    # 5. Forward the request (with error handling)
    try:
        headers: Dict[str, Any] = {
            "X-User-ID": current_user["user_id"]
        }
        response = await client.get(f"/{path}", params=request.query_params, headers=headers)
        response.raise_for_status()

        # Parse to Json format
        data = response.json()
        
        # 6. Cache response
        try:
            await redis.setex(cache_key, 60, json.dumps(data))
        except Exception as e:
            logger.warning(f"Cache storage failed: {e}")
        
        logger.info(f"User {current_user['user_id']} accessed {path} from data-service")
        return {
            "source": "data-service",
            "version": "v1",
            "data" : data
        }
    
    except httpx.HTTPStatusError as e:
        logger.error(f"Rempte service error: {e.response.status_code} for path {path}")
        raise HTTPException(status_code=e.response.status_code, detail="Remote service error")
    except httpx.RequestError as e:
        logger.error(f"Network error: {str(e)}")
        raise HTTPException(status_code=503, detail="Service unavailable")