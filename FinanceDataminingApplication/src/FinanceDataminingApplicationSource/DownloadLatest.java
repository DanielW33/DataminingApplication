package FinanceDataminingApplicationSource;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

public class DownloadLatest {
	public boolean getLatestDownload() throws IOException {
		File file = new File("C:\\Users\\dwtun\\eclipse-workspace\\Datafiles\\New");
		URL url = new URL("https://www.sec.gov/Archives/edgar/daily-index/xbrl/companyfacts.zip");
		URLConnection connection = url.openConnection();
		
		return true;
	}
}
