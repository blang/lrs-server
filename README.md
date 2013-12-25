LRS Server
==========

Aggregates log messages from lrs clients and provides a simple REST and socket.io interface.

Usage
-----
Build jar using

    mvn assembly:single

Execute jar to startup the server

    java -jar lrsserver.jar

Server is listening on following TCP ports

* 8001: HTTP server
* 8002: Socket.io server
* 8003: LRS Server using protobufs
