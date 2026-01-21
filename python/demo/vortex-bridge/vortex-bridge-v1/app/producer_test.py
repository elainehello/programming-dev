from app.broker.connection import get_redis, settings
from app.schemas.job import Job


def push(job: Job) -> None:
    r = get_redis()
    r.lpush(settings.JOBS_QUEUE_NAME, job.to_bytes())

def main() -> None:
    push(Job(task="echo", data={"message": "Hello Vortex"}))
    push(Job(task="sum", data={"a": 2, "b": 3}))
    push(Job(task="fail", data={"reason": "demo"}))
    print("Published 3 jobs")

if __name__ == "__main__":
    main()