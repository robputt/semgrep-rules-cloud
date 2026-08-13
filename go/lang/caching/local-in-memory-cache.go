package cachelayer

import (
	"context"
	"os"
	"time"

	"github.com/allegro/bigcache/v3"
	lru "github.com/hashicorp/golang-lru/v2"
	"github.com/patrickmn/go-cache"
	"github.com/redis/go-redis/v9"
)

func NewGoCache() *cache.Cache {
	// ruleid: local-in-memory-cache
	return cache.New(5*time.Minute, 10*time.Minute)
}

func NewBig(ctx context.Context) (*bigcache.BigCache, error) {
	// ruleid: local-in-memory-cache
	return bigcache.New(ctx, bigcache.DefaultConfig(10*time.Minute))
}

func NewLRU() (*lru.Cache, error) {
	// ruleid: local-in-memory-cache
	return lru.New(512)
}

func NewShared() *redis.Client {
	// ok: local-in-memory-cache
	return redis.NewClient(&redis.Options{Addr: os.Getenv("REDIS_ADDR")})
}
