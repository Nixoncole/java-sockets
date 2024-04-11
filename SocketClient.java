import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class SocketClient extends SocketBase { 

    private SocketClient(final int portNum, final String serverLocation) throws IOException, IllegalArgumentException {
        super(new Socket(serverLocation, portNum));
        this.inputStreamReader = new InputStreamReader(this.socket.getInputStream());
        this.outputStreamWriter = new OutputStreamWriter(this.socket.getOutputStream());
        this.readBuffer = new BufferedReader(inputStreamReader);
        this.writeBuffer = new BufferedWriter(outputStreamWriter);
    }

    public static void main(String[] args) {
        String serverLocation = "localhost";
        int portNum = 1234;

        try (final SocketClient client = new SocketClient(portNum, serverLocation);) {
            System.out.println("Ready to send + receive messages with server found at " + serverLocation + ":" + portNum);
            client.start();
            System.out.println("Client disconnecting from server... 左様なら");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void start() {
        // Accept user input
        final Scanner scanner = new Scanner(System.in);
        try {
            while (true) {
                System.out.println("\n\n . . .");

                // Works because we are based off of the same socket
                System.out.println("Server Says: " + readBuffer.readLine());

                final String msgToSend = scanner.nextLine();
                sendMessage(msgToSend);
                if (msgToSend.equalsIgnoreCase("EXIT")) {
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }   
    }
}