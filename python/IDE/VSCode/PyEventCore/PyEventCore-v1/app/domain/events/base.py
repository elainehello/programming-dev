from datetime import datetime
from typing import Any, Dict
from dataclasses import dataclass, asdict
import json

@dataclass
class DomainEvent:
    """Base class for all domain events."""
    event_id: str
    timestamp: datetime
    aggregate_id: int

    def to_dict(self) -> Dict[str, Any]:
        """Convert event to dictionary"""
        return asdict(self)

    def to_json(self) -> str:
        """Convert event to JSON string"""
        data = self.to_dict()
        data['timestamp'] = data['timestamp'].isoformat()
        return json.dumps(data)

    @classmethod
    def from_dict(cls, data: Dict[str, Any]) -> 'DomainEvent':
        """Create event from dictionary"""
        return cls(**data)
