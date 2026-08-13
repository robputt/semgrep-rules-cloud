package server

import "sync"

// ruleid: package-level-mutable-state
var sessions = map[string]string{}

// ruleid: package-level-mutable-state
var pending []string

// ruleid: package-level-mutable-state
var seen = make(map[string]struct{}, 128)

// ruleid: package-level-mutable-state
var work = make(chan string, 64)

// ok: package-level-mutable-state
var DefaultHeaders = map[string]string{"content-type": "application/json"}

var mu sync.Mutex

func Handle(id string) map[string]string {
	// ok: package-level-mutable-state
	var local = map[string]string{}
	// ok: package-level-mutable-state
	var buffer []string
	buffer = append(buffer, id)
	local[id] = id
	return local
}

type Aggregator struct{}

func (a *Aggregator) Reduce(rows []string) map[string]int {
	// ok: package-level-mutable-state
	var counts = map[string]int{}
	for _, row := range rows {
		counts[row]++
	}
	return counts
}
