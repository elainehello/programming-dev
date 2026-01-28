from datetime import datetime
from sqlalchemy import Column, Integer, String, Text, DateTime, Enum as SQLEnum, ForeignKey
from sqlalchemy.orm import relationship
from enum import Enum
from app.db.session import Base


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


class Task(Base):
    """Task domain model."""
    
    __tablename__ = "tasks"
    
    id = Column(Integer, primary_key=True, index=True)
    project_id = Column(Integer, ForeignKey("projects.id"), nullable=False, index=True)
    title = Column(String(255), nullable=False, index=True)
    description = Column(Text, nullable=True)
    status = Column(SQLEnum(TaskStatus), default=TaskStatus.TODO, index=True)
    priority = Column(SQLEnum(TaskPriority), default=TaskPriority.MEDIUM, index=True)
    created_at = Column(DateTime, default=datetime.utcnow, nullable=False)
    updated_at = Column(DateTime, default=datetime.utcnow, onupdate=datetime.utcnow)
    completed_at = Column(DateTime, nullable=True)
    
    # Relationships
    project = relationship("Project", back_populates="tasks")
    
    def __repr__(self) -> str:
        return f"<Task(id={self.id}, title={self.title}, status={self.status})>"
