import jwt
from fastapi import Request, HTTPException, Security
from fastapi.security import HTTPBearer, HTTPAuthorizationCredentials
from app.config import settings

# Instantiate object
security = HTTPBearer()

async def get_current_user(auth: HTTPAuthorizationCredentials = Security(security)):
    """
    Decode the JWT. If invalid stops the request here.
    If valid, returns the user data to be injected into the route.
    """
    try:
        payload = jwt.decode(
            auth.credentials,
            settings.JWT_SECRET,
            algorithms=[settings.ALGORITHM]
        )
        user_id: str = payload.get("sub")
        if user_id is None:
            raise HTTPException(status_code=401, detail="Invalid token: missing sub")
        return {"user_id": user_id, "scopes": payload.get("scopes", [])}
    except jwt.PyJWTError:
        raise HTTPException(status_code=401, detail="Could not validate credentials")