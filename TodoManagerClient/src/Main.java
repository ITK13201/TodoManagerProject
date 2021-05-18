import java.io.*;
import java.net.*;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: JabberServer Port Number");
            System.exit(1);
        }
        int PORT = Integer.parseInt(args[0]);

        InetAddress addr = InetAddress.getByName("localhost"); // IP アドレスへの変換
        System.out.println("addr = " + addr);
        Socket socket = new Socket(addr, PORT); // ソケットの生成
        try {
            System.out.println("socket = " + socket);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // データ受信用バッファの設定
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true); // 送信バッファ設定
            for (int i = 0; i < 10; i++) {
                out.println("howdy " + i); // データ送信
                String str = in.readLine(); // データ受信
                System.out.println(str);
            }
            out.println("END");
        } finally {
            System.out.println("closing...");
            socket.close();
        }
    }
}
