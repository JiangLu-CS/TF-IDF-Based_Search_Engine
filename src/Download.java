package search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Download {
	private static long count = 1;

	private boolean checkURL(URL url) {
		String s = url.toString();
		if (s.endsWith(".zip") || s.endsWith(".gz") || s.endsWith(".rar")
				|| s.endsWith(".exe") || s.endsWith(".exe")
				|| s.endsWith(".jpg") || s.endsWith(".png")
				|| s.endsWith(".tar") || s.endsWith(".chm")
				|| s.endsWith(".iso") || s.endsWith(".gif")
				|| s.endsWith(".csv") || s.endsWith(".pdf")
				|| s.endsWith(".doc"))
			return false;
		else
			return true;
	}

	public String downloadHttp(URL url) {
		boolean isOK = checkURL(url);
		if (!isOK)
			return null;
		StringBuffer content = new StringBuffer();
		try {
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("User-agent","SCUTNIR2021S201830590093");
			int responseCode = connection.getResponseCode();
			System.out.println("return code is :"
					+ responseCode);
			if (responseCode == 404
					|| responseCode == 403)
				return null;
			int i = 1;
			BufferedReader reader  = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				//System.out.println(line);
				content.append(line + "\n");
			}
			return content.toString();
		} catch (IOException e) {
			return null;
		}
	}

}
