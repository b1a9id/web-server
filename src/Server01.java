import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by uchitate on 2016/04/30.
 */
public class Server01 {

	public static void main(String[] args) throws Exception {

		try (ServerSocket listener = new ServerSocket()) {
			listener.setReuseAddress(true);
			listener.bind(new InetSocketAddress(8002));
			System.out.println("Server listening on port 8002");

			while (true) {
				Socket socket = listener.accept();
				new ServerThread(socket);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}




}
