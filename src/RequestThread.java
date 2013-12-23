import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class RequestThread extends Thread {
	public static LogHandler logger = LogHandler.getHandler();
	public static final String NEWLINE = System.getProperty("line.separator");
	public static final String DEFAULT_WEBAPPS_DIR = "./webapps";

	Socket connection;

	public RequestThread(Socket connection) {
		this.connection = connection;
	}

	public void run() {
		logger.info("Request Thread Start");
		InputStream is;
		OutputStream os;
		try {
			is = connection.getInputStream();
			os = connection.getOutputStream();

			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			HttpRequest request = new HttpRequest();

			String header = br.readLine();
			String path = header;

			String requestType = request.pasingType(path);
			System.out.println(requestType);
			System.out.println(header);

			if (requestType.equals("GET")) {
				while (!"".equals(header) && header != null) {
					header = br.readLine();
					System.out.println(header);
				}
			} else {
				int value;
				int length = 0;
				char c;
				String len; 
				while (!"".equals(header) && header != null) {
					header = br.readLine();
					len = request.passingLength(header);
					if (len.equals("Content-Length:")) {
						length = request.passingGetLength(header);
					}
					System.out.println(header);
				}
				for(int i=0; i<length ; i++) {
					value= br.read();
					c=(char)value;
					//System.out.print(value);
					System.out.print(c);
				}
			}

			String requestUrl = request.pasingUrl(path);
			File requestFile = new File(DEFAULT_WEBAPPS_DIR + requestUrl);
			System.out.println(requestFile);
			// String str = request.getParameter("content");
			DataOutputStream dos = new DataOutputStream(os);
			FileInputStream fis = new FileInputStream(requestFile);

			responseHTML(dos, requestFile.length());

			int data = fis.read();
			while (data != -1) {
				dos.write(data);
				//System.out.print((char)data);
				data = fis.read();
			}

			fis.close();
			dos.close();
			connection.close();

		} catch (IOException e) {
			logger.warning(e.getMessage());
		}

	}

	private void responseHTML(DataOutputStream dos, long contentSize)
			throws IOException {
		response(dos, contentSize, "text/html");
	}

	private void response(DataOutputStream dos, long contentSize,
			String contentType) throws IOException {
		dos.writeBytes("HTTP/1.0 200 Document Follows " + NEWLINE);
		dos.writeBytes("Content-Type: " + contentType + ";charset=utf=8"
				+ NEWLINE);
		dos.writeBytes("Content-Length: " + contentSize + NEWLINE);
		dos.writeBytes(NEWLINE);
	}

}
