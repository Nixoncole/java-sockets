import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;


// No longer need to extednt eh baseclass...
// Instead, have a 'handler class' which extends the base class. Handler will take a socket after SocketServer accepts a new client
// and do al of the IO init like below. 

// Handler class must "implement runnable" for it to be threadable

public class MyServer {

    private int portNum;
    private ServerSocket serverSocket;

    private MyServer(final int portNum) throws IOException {
        if (portNum <= 0 || portNum > 65535) {
            throw new IllegalArgumentException("Portnum must be a positive number below 65535");
        }
        this.portNum = portNum;
        this.serverSocket = new ServerSocket(portNum);
        this.serverSocket.setReuseAddress(true);
    }

    public static void main(String[] args) throws IOException {
        MyServer myServer;
        try {
            myServer = new MyServer(1234);
            myServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void start() {
        System.out.println("TCP Server running at port: " + this.portNum);
        while (true) { 
            // This blocks until a connection is made...
            try {
                // Accept the incoming TCP connection
                Socket socket = serverSocket.accept();
                System.out.println("New client connection accepted!\n\n");
                // Create a handler for the connection
                ConnectionHandler handler = new ConnectionHandler(socket);
                // Let it run until the client disconnects
                new Thread(handler).start(); 
            } catch (IOException e) {
                e.printStackTrace();
            }
            
        }
    }

    private class ConnectionHandler extends SocketBase implements Runnable {

        protected ConnectionHandler(Socket socket) throws IOException {
            super(socket);
            this.inputStreamReader = new InputStreamReader(socket.getInputStream());
            this.outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
            this.readBuffer = new BufferedReader(inputStreamReader);
            this.writeBuffer = new BufferedWriter(outputStreamWriter);
        }

        public void run() {
            String msgFromClient;
            try {
                sendMessage("Hello! How would you like to be called?    (Enter 'exit' to disconnect)");
                msgFromClient = readBuffer.readLine();
                sendMessage("Okay!");
                final String clientName = msgFromClient;
                do {
                    msgFromClient = readBuffer.readLine();
                    System.out.println("Client " +clientName + ": " + msgFromClient);
                    sendMessage("Msg Received...");
                } while (!msgFromClient.equalsIgnoreCase("EXIT"));
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                close();
            }

        }
        
    }
}

