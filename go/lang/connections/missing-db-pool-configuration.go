package store

import (
	"database/sql"
	"os"
	"time"

	_ "github.com/lib/pq"
)

func OpenUnconfigured() (*sql.DB, error) {
	// ruleid: missing-db-pool-configuration
	db, err := sql.Open("postgres", os.Getenv("DATABASE_URL"))
	if err != nil {
		return nil, err
	}
	return db, nil
}

func OpenTuned() (*sql.DB, error) {
	// ok: missing-db-pool-configuration
	db, err := sql.Open("postgres", os.Getenv("DATABASE_URL"))
	if err != nil {
		return nil, err
	}
	db.SetMaxOpenConns(10)
	db.SetMaxIdleConns(10)
	db.SetConnMaxLifetime(5 * time.Minute)
	return db, nil
}
