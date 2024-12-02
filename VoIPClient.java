import javax.sound.sampled.*;
import java.io.*;
import java.net.*;

public class VoIPClient {
    private Socket socket;
    private DataOutputStream out;

    // Method to start connection to the server
    public void startConnection(String ip, int port) throws IOException {
        socket = new Socket(ip, port);  // Connect to the server IP and port
        out = new DataOutputStream(socket.getOutputStream());
        captureAndSendAudio();  // Capture and send audio
    }

    // Method to capture and send audio data from microphone to server
    private void captureAndSendAudio() {
        try {
            AudioFormat format = new AudioFormat(44100, 16, 1, true, true);  // Set audio format
            TargetDataLine microphone = AudioSystem.getTargetDataLine(format);
            microphone.open(format);
            microphone.start();  // Start capturing audio

            byte[] buffer = new byte[4096];  // Buffer to hold audio data
            while (microphone.read(buffer, 0, buffer.length) > 0) {
                out.write(buffer);  // Send captured audio to server
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Main method to run the client
    public static void main(String[] args) throws IOException {
        VoIPClient client = new VoIPClient();
        client.startConnection("localhost", 5000); // Connect to the server running on localhost
    }
}
