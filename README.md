#File Transfer Server/Client using UDP Sockets
####A trivial implementation of a File Transfer System over UDP
Written in Java

## How to compile?
(Client and Server folder)
- javac Init.java

## How to run?
(Client)
- java Init [ServerIP] [ServerPort] [DestinationFolder]

(Server)
- java Init [Port] [SourceFolder]

## Commands supported by the Client:
- shutdown
- list
- download [filename]
