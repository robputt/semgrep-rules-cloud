request_count = 0
current_leader = None
MAX_RETRIES = 3


# ruleid: global-statement-mutation
def record_request():
    global request_count
    request_count += 1


# ruleid: global-statement-mutation
async def elect(node):
    global current_leader
    current_leader = node


# ok: global-statement-mutation
def read_request_count():
    return request_count


# ok: global-statement-mutation
def bump(counter):
    counter["value"] += 1
    return counter
