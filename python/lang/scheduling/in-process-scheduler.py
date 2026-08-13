import threading

import schedule
from apscheduler.schedulers.background import BackgroundScheduler
from celery import Celery


def start_reconciler(reconcile):
    # ruleid: in-process-scheduler
    scheduler = BackgroundScheduler()
    scheduler.add_job(reconcile, "interval", minutes=5)
    scheduler.start()


def register_nightly(rollup):
    # ruleid: in-process-scheduler
    schedule.every().day.at("02:00").do(rollup)


def delayed_retry(fn):
    # ruleid: in-process-scheduler
    threading.Timer(30.0, fn).start()


app = Celery("tasks")

# ok: in-process-scheduler
app.conf.beat_schedule = {
    "nightly-rollup": {"task": "tasks.rollup", "schedule": 86400.0},
}


# ok: in-process-scheduler
@app.task
def rollup():
    return True
