import uuid
from datetime import datetime
from typing import List, Optional
from sqlalchemy.orm import Session
from app.domain.models.project import Project, ProjectStatus
from app.domain.schemas.project import ProjectCreate, ProjectUpdate
from app.domain.events.project_events import (
    ProjectCreatedEvent,
    ProjectUpdatedEvent,
    ProjectDeletedEvent
)
from app.services.event_publisher import event_publisher


class ProjectService:
    """Service for project business logic."""
    
    # (visibility modifier) 'static' method limits its scope
    @staticmethod
    def create_project(db: Session, project_data: ProjectCreate) -> Project:
        """Create a new project and publish event."""
        project = Project(
            name=project_data.name,
            description=project_data.description,
            status=project_data.status
        )
        db.add(project)
        db.commit()
        db.refresh(project)
        
        # Publish event
        event = ProjectCreatedEvent(
            event_id=str(uuid.uuid4()),
            timestamp=datetime.utcnow(),
            aggregate_id=project.id,
            name=project.name,
            description=project.description
        )
        event_publisher.publish(event)
        
        return project
    
    @staticmethod
    def get_project(db: Session, project_id: int) -> Optional[Project]:
        """Retrieve a project by ID."""
        return db.query(Project).filter(Project.id == project_id).first()
    
    @staticmethod
    def list_projects(db: Session, skip: int = 0, limit: int = 10) -> List[Project]:
        """List projects with pagination."""
        return db.query(Project).offset(skip).limit(limit).all()
    
    @staticmethod
    def update_project(db: Session, project_id: int, project_data: ProjectUpdate) -> Optional[Project]:
        """Update a project and publish event."""
        project = ProjectService.get_project(db, project_id)
        if not project:
            return None
        
        update_data = project_data.model_dump(exclude_unset=True)
        for field, value in update_data.items():
            setattr(project, field, value)
        
        db.commit()
        db.refresh(project)
        
        # Publish event
        event = ProjectUpdatedEvent(
            event_id=str(uuid.uuid4()),
            timestamp=datetime.utcnow(),
            aggregate_id=project.id,
            name=update_data.get("name"),
            description=update_data.get("description"),
            status=str(update_data.get("status"))
        )
        event_publisher.publish(event)
        
        return project
    
    @staticmethod
    def delete_project(db: Session, project_id: int) -> bool:
        """Delete a project and publish event."""
        project = ProjectService.get_project(db, project_id)
        if not project:
            return False
        
        db.delete(project)
        db.commit()
        
        # Publish event
        event = ProjectDeletedEvent(
            event_id=str(uuid.uuid4()),
            timestamp=datetime.utcnow(),
            aggregate_id=project_id,
            name=project.name
        )
        event_publisher.publish(event)
        
        return True


