import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class SocketBase implements AutoCloseable {

    protected Socket socket;
    protected InputStreamReader inputStreamReader;
    protected OutputStreamWriter outputStreamWriter;
    protected BufferedReader readBuffer;
    protected BufferedWriter writeBuffer;

    protected SocketBase(Socket socket) throws IOException {
        this.socket = socket;
        this.inputStreamReader = new InputStreamReader(socket.getInputStream());
        this.outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
        this.readBuffer = new BufferedReader(inputStreamReader);
        this.writeBuffer = new BufferedWriter(outputStreamWriter);
    }

    public void close() {
        try {
            if (this.socket != null) {
                this.socket.close();
            }
            if (this.inputStreamReader != null) {
                this.inputStreamReader.close();
            }
            if (this.outputStreamWriter != null) {
                this.outputStreamWriter.close();
            }
            if (this.readBuffer != null) {
                this.readBuffer.close();
            }
            if (this.writeBuffer != null) {
                this.writeBuffer.close();
            }   
        } catch (IOException e) {
            System.out.println(this.getClass().getSimpleName() + ".shutdown(): Encountered an error while shutting down:");
            e.printStackTrace();
        }
    }

    protected void sendMessage(final String msg) throws IOException {
        this.writeBuffer.write(msg);
        this.writeBuffer.newLine(); 
        this.writeBuffer.flush();
    }

}
