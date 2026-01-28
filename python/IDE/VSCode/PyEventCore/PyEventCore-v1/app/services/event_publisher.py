import uuid
from datetime import datetime
from typing import Any, Callable, Dict, List
from app.domain.events.base import DomainEvent


class EventPublisher:
    """Publisher for domain events"""

    def __init__(self):
        """Initialize event publisher"""
        self._subscribers: Dict[str, List[Callable[[DomainEvent], None]]] = {}

    def subscribe(self, event_type: str, callback: Callable[[DomainEvent], None]):
        """Subcribe to an event type"""
        if event_type not in self._subscribers:
            self._subscribers[event_type] = []
        self._subscribers[event_type].append(callback) 

    def publish(self, event: DomainEvent):
        """Publish an event to all subscribers"""
        event_type = event.__class__.__name__

        if event_type in self._subscribers:
            for callback in self._subscribers[event_type]:
                callback(event)

# Global event publisher instance
event_publisher = EventPublisher()
