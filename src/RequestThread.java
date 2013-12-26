import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class RequestThread extends Thread {
	public static LogHandler logger = LogHandler.getHandler();
	public static final String NEWLINE = System.getProperty("line.separator");
	public static final String DEFAULT_WEBAPPS_DIR = "./webapps";
	
	private static ArrayList<String> myArrayList;

	Socket connection;

	public RequestThread(Socket connection) {
		this.connection = connection;
	}

	public void run() {
		logger.info("Request Thread Start");
		InputStream is;
		OutputStream os;
		String jsonData="";
		DataOutputStream dos;
		FileInputStream fis = null;
		File requestFile;
		FileWriter fw = null;
		
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
				myArrayList = new ArrayList<String>();
				String line = "";
				for(int i=0; i<length ; i++) {
					value= br.read();
					c=(char)value;
					if(!(value == 10 || value==13)) {
						line += c;
					}
					if(value == 10) {
						myArrayList.add(line);
						System.out.println(line);
						line = "";
					}
					//System.out.print(value);
					//System.out.print(c);
					
				}
				System.out.print(myArrayList.get(3));
				String str = myArrayList.get(3);
				jsonData = "{\"content\":\"" + str + "\"}";
			}
			

			String requestUrl = request.pasingUrl(path);
			System.out.println(requestUrl);
			
			if(requestUrl.equals("/reponse.json")) {
				requestFile = new File(DEFAULT_WEBAPPS_DIR + requestUrl);
				fw = new FileWriter(requestFile);
				fw.write(jsonData);
				fw.close();
			} else {
				requestFile = new File(DEFAULT_WEBAPPS_DIR + requestUrl);	
			}
			
			dos = new DataOutputStream(os);
			fis = new FileInputStream(requestFile);

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
