# VoIP-communication-system-project

## Objective
The primary objective of this VoIP Communication System project is to develop a system that enables real-time voice communication over the Internet using packet-switched IP networks. This project simulates a real-world Voice-over-IP (VoIP) setup and aims to:

- Enable real-time voice communication between users using packet-switched IP networks.
- Understand the technical requirements for setting up a VoIP infrastructure.
- Learn how audio data is encoded, transmitted, and decoded over a network.
- Explore VoIP’s role in enhancing communication efficiency, reducing costs, and increasing scalability.

## Background and Motivation

### Background
Traditional voice communication systems rely on circuit-switched networks (e.g., PSTN) that reserve a dedicated communication path for each call, which is often inefficient. VoIP, in contrast, uses packet-switched networks to send voice data as digital packets over the Internet. VoIP protocols like SIP (Session Initiation Protocol) and RTP (Real-time Transport Protocol) facilitate call setup and data transport.

### Motivation
VoIP has become essential for modern communication due to its low cost, flexibility, and efficiency. Businesses and individuals save on traditional phone costs by using IP-based voice communication, especially for long-distance and international calls. This project aims to implement, simulate, and understand VoIP systems that are integral to telecommunications.

## Working of the Project

### 1. Practical Setup in Cisco Packet Tracer

#### Topology Configuration
1. Set up a network topology with IP phones, a router (configured as a VoIP server), a switch, and a computer acting as a DHCP and TFTP server.
2. Configure the router as a telephony server using specific commands for IP phone registration and call management.

#### VoIP Router Configuration
Use commands to set up the router as a VoIP server:
```plaintext
enable
configure terminal
telephony-service
max-ephones 5         # Maximum number of IP phones
max-dn 5              # Maximum directory numbers
ip source-address [Router_IP] port 2000
auto assign 1 to 5    # Automatically assigns extensions
exit
IP Phone Configuration
Assign extension numbers for each IP phone:

plaintext
Copy code
ephone-dn 1
number 1001
ephone 1
mac-address [MAC of IP Phone 1]
button 1:1
Repeat for additional phones, incrementing the ephone and ephone-dn numbers.

Testing
Test by dialing the assigned extension numbers between IP phones to establish communication and verify voice quality.

2. Technical Details for Java Implementation
In this section, we implement the client-server architecture using Java.

Server Code
The server listens on a designated port, accepting incoming connections and handling audio data:

java
Copy code
import java.io.*;
import java.net.*;

public class VoIPServer {
    private ServerSocket serverSocket;

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Server started on port " + port);
        while (true) {
            new ClientHandler(serverSocket.accept()).start();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket clientSocket;
        private DataInputStream in;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try {
                in = new DataInputStream(clientSocket.getInputStream());
                byte[] buffer = new byte[4096];
                while (in.read(buffer) > 0) {
                    System.out.println("Received audio data");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        VoIPServer server = new VoIPServer();
        server.start(5000);
    }
}
Client Code
The client captures audio from the microphone and sends it to the server:

java
Copy code
import javax.sound.sampled.*;
import java.io.*;
import java.net.*;

public class VoIPClient {
    private Socket socket;
    private DataOutputStream out;

    public void startConnection(String ip, int port) throws IOException {
        socket = new Socket(ip, port);
        out = new DataOutputStream(socket.getOutputStream());
        captureAndSendAudio();
    }

    private void captureAndSendAudio() {
        try {
            AudioFormat format = new AudioFormat(44100, 16, 1, true, true);
            TargetDataLine microphone = AudioSystem.getTargetDataLine(format);
            microphone.open(format);
            microphone.start();

            byte[] buffer = new byte[4096];
            while (microphone.read(buffer, 0, buffer.length) > 0) {
                out.write(buffer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        VoIPClient client = new VoIPClient();
        client.startConnection("localhost", 5000); // Connect to server
    }
}
Results
1. Packet Tracer Simulation
IP phones communicate by dialing each other’s extensions, simulating real-time VoIP communication.
2. Java Implementation
Clients capture and send audio data to the server, which processes incoming audio data.
Applications of the Project
This VoIP Communication System project has significant applications in various fields:

Telecommunications: Reduces reliance on costly PSTN systems, enabling seamless global communication.
Business Communication: Widely used for internal/external communications, conferencing, and remote work.
Education: Supports online classes, webinars, and remote tutoring.
Customer Service: VoIP-based call centers for handling queries efficiently.
Summary
This project demonstrates the implementation of a VoIP system, encompassing network setup (Packet Tracer) and client-server voice communication (Java). It highlights how VoIP enables cost-effective, scalable communication across diverse sectors. This project provides a comprehensive understanding of VoIP technologies, packet switching, and real-time data transmission.

Instructions for Usage
Packet Tracer Setup: Follow the setup instructions to configure the VoIP network and devices within Cisco Packet Tracer.
Java Code: Run the provided Java server and client code to simulate voice communication. The server listens for incoming audio data, while the client captures and transmits audio to the server.
Requirements
Cisco Packet Tracer for network simulation.
Java Development Kit (JDK) to run the Java code.
Java Sound API for capturing and sending audio data.
