from fastapi import FastAPI, Request
from typing import Any, Dict
import socket
import time


app = FastAPI()
INSTANCE_ID = socket.gethostname()


@app.get("/health")
def health():
    return {
        "status": "ok",
        "instance": INSTANCE_ID
    }

@app.get("/headers")
def headers(request: Request):
    return dict(request.headers)

@app.get("/slow")
def slow(ms: int = 1000) -> Dict[Any, Any]:
    time.sleep(ms / 1000)
    return {
        "slept_ms": ms,
        "instace": INSTANCE_ID
    }

@app.get("/")
def root():
    return {
        "message": "Hello from API",
        "instance": INSTANCE_ID
    }
