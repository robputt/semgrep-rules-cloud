FROM nginx:1.27-alpine

# ruleid: privileged-port-expose
EXPOSE 80

# ruleid: privileged-port-expose
EXPOSE 443

# ruleid: privileged-port-expose
EXPOSE 53/udp

# ok: privileged-port-expose
EXPOSE 8080

# ok: privileged-port-expose
EXPOSE 8443

# ok: privileged-port-expose
EXPOSE 9090/tcp

USER 10001
CMD ["nginx", "-g", "daemon off;"]
