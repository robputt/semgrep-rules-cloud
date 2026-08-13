export class SessionRegistry {
  // ruleid: static-mutable-class-property
  static cache = new Map<string, string>();

  // ruleid: static-mutable-class-property
  private static pending: string[] = [];

  // ruleid: static-mutable-class-property
  protected static overrides = {};

  // ruleid: static-mutable-class-property
  static seen = new Set<string>();

  // ok: static-mutable-class-property
  static readonly RETRY_LIMIT = 3;

  // ok: static-mutable-class-property
  private perInstance = new Map<string, string>();

  constructor(private readonly redis: RedisLike) {}

  // ok: static-mutable-class-property
  async get(key: string): Promise<string | null> {
    const scratch = new Map<string, string>();
    scratch.set(key, key);
    return this.redis.get(`session:${key}`);
  }
}

export interface RedisLike {
  get(key: string): Promise<string | null>;
}
