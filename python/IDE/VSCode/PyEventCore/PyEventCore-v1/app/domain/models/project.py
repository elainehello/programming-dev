from datetime import datetime
from sqlalchemy import Column, Integer, String, Text, DateTime, Enum as SQLEnum
from sqlalchemy.orm import relationship
from enum import Enum
from app.db.session import Base


class ProjectStatus(str, Enum):
    """ Project status enumeration """
    ACTIVE = "active"
    ARCHIVED = "archived"
    COMPLETED = "completed"

class Project(Base):
    """ Project domain model """

    __tablename__ = "projects"

    id = Column(Integer, primary_key=True, index=True)
    name = Column(String(255), nullable=False, index=True)
    description = Column(Text, nullable=True)
    status = Column(SQLEnum(ProjectStatus), default = ProjectStatus.ACTIVE, index=True)
    created_at = Column(DateTime, default=datetime.now, nullable=False)
    updated_at = Column(DateTime, default=datetime.now, onupdate=datetime.now)

    # Relationships
    tasks = relationship("Task", back_populates="project", cascade="all, delete-orphan")

    def __repr__(self) -> str:
        return f"<Project(id={self.id}, name={self.name}, status={self.status})>"
