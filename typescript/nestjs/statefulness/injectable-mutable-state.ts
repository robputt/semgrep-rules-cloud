import { Inject, Injectable, Scope } from '@nestjs/common';
import type { Cache } from 'cache-manager';

@Injectable()
export class OrderService {
  // ruleid: injectable-mutable-state
  private readonly seen = new Map<string, string>();

  // ruleid: injectable-mutable-state
  private requestCount = 0;

  // ruleid: injectable-mutable-state
  private pending: string[] = [];
}

@Injectable()
export class StatelessOrderService {
  // ok: injectable-mutable-state
  private readonly ttlSeconds = 300;

  constructor(@Inject('CACHE') private readonly cache: Cache) {}

  // ok: injectable-mutable-state
  async get(id: string): Promise<string | undefined> {
    const scratch = new Map<string, string>();
    scratch.set(id, id);
    return this.cache.get<string>(`order:${id}`);
  }
}

@Injectable({ scope: Scope.REQUEST })
export class RequestScopedService {
  // ok: injectable-mutable-state
  private readonly perRequest = new Map<string, string>();
}
