from datetime import datetime
from typing import Optional
from dataclasses import dataclass
from app.domain.events.base import DomainEvent


@dataclass # metaprogramming notation
class ProjectCreateEvent(DomainEvent):
    """Event fired when a project is created"""
    name: Optional[str] = None
    description: Optional[str] = None

@dataclass
class ProjectUpdatedEvent(DomainEvent):
    """Event fired when a project is updated"""
    name: Optional[str] = None
    description: Optional[str] = None
    status: Optional[str] = None

@dataclass
class ProjectDeletedEvent(DomainEvent):
    """Event fired when a project is deleted"""
    name: Optional[str] = None
