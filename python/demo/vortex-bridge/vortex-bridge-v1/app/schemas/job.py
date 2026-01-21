from datetime import datetime
from typing import Any, Dict
import uuid, orjson, json
import uuid
from pydantic import BaseModel, Field, field_validator


class Job(BaseModel):
    job_id: str = Field(default_factory=lambda: uuid.uuid4().hex)
    task: str
    data: Dict[str, Any] = Field(default_factory=dict)
    retry_count: int = 0
    max_retries: int = 5
    created_at: datetime = Field(default_factory=datetime.utcnow)

    @field_validator("retry_count", "max_retries")
    @classmethod
    def non_negative(cls, v: int) -> int:
        if v < 0:
            raise ValueError("must be non-negative")
        return v

    def bump_retry(self) -> "Job":
        return self.model_copy(update={"retry_count": self.retry_count + 1})

    def to_bytes(self) -> bytes:
        try:
            return orjson.dumps(self.model_dump())
        except Exception:
            return json.dumps(self.model_dump(), separators=(",", ":")).encode()
