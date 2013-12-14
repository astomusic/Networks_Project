
public class HttpRequest {

	public String pasingUrl(String path) {
		String requestUrl = path.substring(path.indexOf(" ")+1, path.lastIndexOf(" "));
		return requestUrl;
	}

}
