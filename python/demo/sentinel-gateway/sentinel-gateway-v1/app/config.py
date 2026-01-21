from pydantic_settings import BaseSettings, SettingsConfigDict

class Settings(BaseSettings):
    # Attributes automatically filled from the .env file
    REDIS_URL: str | None = None
    DATA_SERVICE_URL: str | None = None
    JWT_SECRET: str | None = None
    ALGORITHM: str = "HS256"

    # Instance pydantic to read from the .env file
    model_config = SettingsConfigDict(env_file=".env")

# Instantiate one global setting object (from base class Settings())
settings = Settings()
