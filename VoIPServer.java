import java.io.*;
import java.net.*;

public class VoIPServer {
    private ServerSocket serverSocket;

    // Method to start the server and listen for incoming connections
    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Server started on port " + port);
        while (true) {
            new ClientHandler(serverSocket.accept()).start(); // Accept client connections
        }
    }

    // Inner class to handle client connections
    private static class ClientHandler extends Thread {
        private Socket clientSocket;
        private DataInputStream in;

        // Constructor
        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        // Handle incoming client data (audio data)
        public void run() {
            try {
                in = new DataInputStream(clientSocket.getInputStream());
                byte[] buffer = new byte[4096];  // Buffer to hold audio data
                while (in.read(buffer) > 0) {
                    // Process the incoming audio data
                    System.out.println("Received audio data");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();  // Close the connection when done
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Main method to start the server
    public static void main(String[] args) throws IOException {
        VoIPServer server = new VoIPServer();
        server.start(5000); // Start the server on port 5000
    }
}
