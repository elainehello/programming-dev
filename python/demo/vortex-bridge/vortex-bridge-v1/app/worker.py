import logging
from app.broker.connection import get_redis, settings
from app.middleware.validation import parse_job
from app.middleware.retry import retry_or_fail
from app.schemas.job import Job

logging.basicConfig(
    level=logging.INFO,
    format="%(asctime)s %(levelname)s %(message)s"
)
log = logging.getLogger("worker")

def handle_job(job: Job) -> None:
    task = job.task
    if task == "echo":
        msg = job.data.get("message", "")
        log.info(f"Echo {msg}")
    elif task == "sum":
        a = job.data.get("a", 0)
        b = job.data.get("b", 0)
        total = a + b
        log.info(f"Sum: {total}")
    elif task == "fail":
        raise RuntimeError("forced failure")
    else:
        log.info(f"No-op task {task}")

def main() -> None:
    r = get_redis()
    log.info(f"Worker connected to {settings.REDIS_URL}")
    while True:
        item = r.brpop(settings.JOBS_QUEUE_NAME, timeout=0)
        if not item:
            continue
        _, raw = item
        try:
            job = parse_job(raw)
            handle_job(job)
            log.info(f"Processed job {job.job_id}")
        except Exception as e:
            try:
                job = parse_job(raw)
            except Exception:
                log.exception("Invalid job payload")
                continue
            retry_or_fail(r, job, e)

if __name__ == "__main__":
    main()