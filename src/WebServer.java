import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class WebServer {
	public static LogHandler logger = LogHandler.getHandler();
	public static void main(String[] args) throws IOException {
		ServerSocket server =  new ServerSocket(8080);
		logger.info("WebServer Start");
		
		Socket connection = null;
		while(true) {
			connection = server.accept();
			RequestThread thread = new RequestThread(connection);
			thread.start();
			//스레드를 여러개 만들고 (채팅서버에 접속한 사람 모두) 스레드를 어레이리스트로 저장해서 클라이언트에서 데이터가 오면 어레이리스트 전체에 뿌려줌
		}

	}

}
