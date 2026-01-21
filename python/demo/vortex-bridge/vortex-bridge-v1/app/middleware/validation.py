from app.schemas.job import Job


def parse_job(raw: bytes) -> Job:
    try:
        import orjson
        data = orjson.loads(raw)
    except Exception:
        import json
        data = json.loads(raw.decode())
    return Job.model_validate(data)