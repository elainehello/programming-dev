from datetime import datetime
from typing import Optional, List
from pydantic import BaseModel, Field
from enum import Enum


class ProjectStatus(str, Enum):
    """Project status enumeration."""
    ACTIVE = "active"
    ARCHIVED = "archived"
    COMPLETED = "completed"


class ProjectBase(BaseModel):
    """Base project schema."""
    name: str = Field(..., min_length=1, max_length=255)
    description: Optional[str] = Field(None, max_length=2000)
    status: ProjectStatus = ProjectStatus.ACTIVE


class ProjectCreate(ProjectBase):
    """Schema for creating a project."""
    pass


class ProjectUpdate(BaseModel):
    """Schema for updating a project."""
    name: Optional[str] = Field(None, min_length=1, max_length=255)
    description: Optional[str] = Field(None, max_length=2000)
    status: Optional[ProjectStatus] = None


class ProjectResponse(ProjectBase):
    """Schema for project response."""
    id: int
    created_at: datetime
    updated_at: datetime
    
    class Config:
        from_attributes = True


class ProjectDetailedResponse(ProjectResponse):
    """Detailed project response with tasks."""
    tasks: List['TaskResponse'] = []


# Import TaskResponse for forward reference
from app.domain.schemas.task import TaskResponse  # noqa
ProjectDetailedResponse.model_rebuild()