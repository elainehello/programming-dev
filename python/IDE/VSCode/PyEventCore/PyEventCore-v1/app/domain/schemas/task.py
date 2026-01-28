from datetime import datetime
from typing import Optional
from pydantic import BaseModel, Field
from enum import Enum


class TaskStatus(str, Enum):
    """Task status enumeration."""
    TODO = "todo"
    IN_PROGRESS = "in_progress"
    COMPLETED = "completed"
    BLOCKED = "blocked"


class TaskPriority(str, Enum):
    """Task priority enumeration."""
    LOW = "low"
    MEDIUM = "medium"
    HIGH = "high"
    CRITICAL = "critical"


class TaskBase(BaseModel):
    """Base task schema."""
    title: str = Field(..., min_length=1, max_length=255)
    description: Optional[str] = Field(None, max_length=2000)
    status: TaskStatus = TaskStatus.TODO
    priority: TaskPriority = TaskPriority.MEDIUM


class TaskCreate(TaskBase):
    """Schema for creating a task."""
    project_id: int


class TaskUpdate(BaseModel):
    """Schema for updating a task."""
    title: Optional[str] = Field(None, min_length=1, max_length=255)
    description: Optional[str] = Field(None, max_length=2000)
    status: Optional[TaskStatus] = None
    priority: Optional[TaskPriority] = None


class TaskResponse(TaskBase):
    """Schema for task response."""
    id: int
    project_id: int
    created_at: datetime
    updated_at: datetime
    completed_at: Optional[datetime] = None

    class Config:
        from_attributes = True