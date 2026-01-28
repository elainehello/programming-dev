from datetime import datetime
from typing import Optional
from dataclasses import dataclass
from app.domain.events.base import DomainEvent


@dataclass
class TaskCreatedEvent(DomainEvent):
    """Event fired when a task is created"""
    project_id: Optional[int] = None
    title: Optional[str] = None
    priority: Optional[str] = None

@dataclass
class TaskUpdatedEvent(DomainEvent):
    """Event fire when a task is updated"""
    project_id: Optional[int] = None
    title: Optional[str] = None
    status: Optional[str] = None
    priority: Optional[str] = None

@dataclass
class TaskCompletedEvent(DomainEvent):
    """Event fired when a task is completed"""
    project_id: Optional[int] = None
    title: Optional[str] = None
