from typing import Optional
from dotenv import load_dotenv
from pydantic import field_validator
from pydantic_settings import BaseSettings, SettingsConfigDict
import redis

# Load .env file if present (root project)
load_dotenv()

class Settings(BaseSettings):
    model_config = SettingsConfigDict(
        env_file = ".env",
        env_file_encoding="utf-8",
        extra="ignore",
    )

    REDIS_URL: str | None
    JOBS_QUEUE_NAME: str | None
    FAILED_QUEUE_NAME: str | None

    @field_validator("REDIS_URL")
    @classmethod #metaprogramming
    def validate_redis_url(cls, v: str) -> str:
        if not (
            v.startswith("redis://" or 
            v.startswith("rediss://") or
            v.startswith("unix://"))
        ):
            raise ValueError("REDIS_URL must start with \
                            redis://, rediss:// or unix://")
        return v

# Instantiate object
settings = Settings()

_pool: Optional[redis.ConnectionPool] = None

def get_redis() -> redis.Redis:
    global _pool
    if _pool is None:
        _pool = redis.ConnectionPool.from_url(settings.REDIS_URL, decode_responses=False)
    return redis.Redis(connection_pool=_pool)
