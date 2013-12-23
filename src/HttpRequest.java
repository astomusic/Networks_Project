
public class HttpRequest {

	public String pasingUrl(String path) {
		String requestUrl = path.substring(path.indexOf(" ")+1, path.lastIndexOf(" "));
		return requestUrl;
	}

	public String pasingType(String path) {
		String requestType = path.substring(0, path.indexOf(" "));
		return requestType;
	}

	public String passingLength(String header) {
		int last =  header.indexOf(" ");
		if(last == -1) {
			last = 0;
		}
		String requestLength = header.substring(0, last);
		return requestLength;
		
	}

	public int passingGetLength(String header) {
		String requestLength = header.substring(header.indexOf(" ")+1);
		return Integer.parseInt(requestLength);
	}

}
