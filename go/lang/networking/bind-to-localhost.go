package server

import (
	"net"
	"net/http"
	"os"
)

func ServeLoopback(handler http.Handler) error {
	// ruleid: bind-to-localhost
	return http.ListenAndServe("127.0.0.1:8080", handler)
}

func ListenLoopback() (net.Listener, error) {
	// ruleid: bind-to-localhost
	return net.Listen("tcp", "localhost:9000")
}

func LoopbackServer(handler http.Handler) *http.Server {
	// ruleid: bind-to-localhost
	return &http.Server{Addr: "127.0.0.1:8080", Handler: handler}
}

func ServeAllInterfaces(handler http.Handler) error {
	// ok: bind-to-localhost
	return http.ListenAndServe(":"+os.Getenv("PORT"), handler)
}

func GoodServer(handler http.Handler) *http.Server {
	// ok: bind-to-localhost
	return &http.Server{Addr: "0.0.0.0:8080", Handler: handler}
}
