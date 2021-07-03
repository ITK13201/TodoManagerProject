import java.io.*;
import java.net.*;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Error: Input Port Number");
            System.exit(1);
        }
        int PORT = Integer.parseInt(args[0]);

        InetAddress addr = InetAddress.getByName("localhost");
        System.out.println("addr = " + addr);
        Socket socket = new Socket(addr, PORT);
        try {
            System.out.println("socket = " + socket);
            BufferedReader socket_in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter socket_out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            for (int i = 0; i < 10; i++) {
                socket_out.println("howdy " + i);
                String str = socket_in.readLine();
                System.out.println(str);
            }
            socket_out.println("END");
        } finally {
            System.out.println("closing...");
            socket.close();
        }
    }
}
