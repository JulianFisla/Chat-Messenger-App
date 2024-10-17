# Chat Messenger App

This project is a prototype for a multi-client chat application built in Java, designed to demonstrate the fundamentals of network programming. The application utilizes client-server architecture, enabling real-time communication between connected users. By leveraging Java's `Socket` and I/O libraries, the app facilitates message exchange over TCP/IP, simulating a basic messaging service.

## Features

- **Real-time messaging:** Supports real-time text-based communication between users.
- **Basic networking:** Implements networking functionality to establish communication between server and clients.

## Java Libraries Used

- **java.net.Socket:** Used for establishing a connection between client and server.
- **java.io.InputStream/OutputStream:** Handles the sending and receiving of messages between users.
- **java.util.Scanner:** Reads user input for communication in the chat system.

## How to Use

1. Clone the repository:
   
   ```bash
   git clone https://github.com/JulianFisla/Chat-Messenger-App.git
   ```
   
3. Run the server and client files in separate terminals.
   
5. Start chatting by entering your messages in the console.

## Installation

1. Ensure you have the Java Development Kit (JDK) installed.
   
2. Compile the code using the command:

   ```bash
   javac *.java
   ```
   
3. Run the server:

   ```bash
   java Server
   ```

4. Run the Client

   ```bash
   java Server
   ```

## Known Issues:

1. Text display may behave differently on various machines due to system compatibility.
