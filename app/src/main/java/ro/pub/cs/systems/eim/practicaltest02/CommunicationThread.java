package ro.pub.cs.systems.eim.practicaltest02;

import android.content.SyncStatusObserver;
import android.util.Log;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

public class CommunicationThread extends Thread {
	
	private ServerThread serverThread;
	private Socket socket;
	
	public CommunicationThread(ServerThread serverThread, Socket socket) {
		this.serverThread = serverThread;
		this.socket       = socket;
	}
	
	@Override
	public void run() {
		if (socket != null) {
			try {
				BufferedReader bufferedReader = Utilities.getReader(socket);
				PrintWriter printWriter    = Utilities.getWriter(socket);
				if (bufferedReader != null && printWriter != null) {
					Log.i(Constants.TAG, "[COMMUNICATION THREAD] Waiting for parameters from client (city / information type)!");
					String word = bufferedReader.readLine();
					HashMap<String, String> data = serverThread.getData();
					String definition = null;
					if (word != null && !word.isEmpty()) {
						if (data.containsKey(word)) {
							Log.i(Constants.TAG, "[COMMUNICATION THREAD] Getting the information from the cache...");
							definition = data.get(word);
						} else {
							Log.i(Constants.TAG, "[COMMUNICATION THREAD] Getting the information from the webservice...");
							HttpClient httpClient = new DefaultHttpClient();
							HttpGet httpGet = new HttpGet(Constants.WEB_SERVICE_ADDRESS
									+ "?" + Constants.QUERY_ATTRIBUTE + "=" + word);
							ResponseHandler<String> responseHandler = new BasicResponseHandler();
							String pageSourceCode = httpClient.execute(httpGet, responseHandler);
							if (pageSourceCode != null) {
//								System.out.println(pageSourceCode);


								Document document = Jsoup.parse(pageSourceCode);
								Element element = document.child(0);
								Elements tags = element.getElementsByTag(Constants.WORD_DEF_TAG);

//								for (Element tag: tags) {
//
//									String tagData = tag.data();
//									definition = tagData;
//									break;
//								}

//
								definition = pageSourceCode;

								serverThread.setData(word, definition);
//								}
							} else {
								Log.e(Constants.TAG, "[COMMUNICATION THREAD] Error getting the information from the webservice!");
							}
						}
						printWriter.println(definition);
						printWriter.flush();
					} else {
						Log.e(Constants.TAG, "[COMMUNICATION THREAD] Error receiving parameters from client (city / information type)!");
					}
				} else {
					Log.e(Constants.TAG, "[COMMUNICATION THREAD] BufferedReader / PrintWriter are null!");
				}
				socket.close();
			} catch (IOException ioException) {
				Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
				if (Constants.DEBUG) {
					ioException.printStackTrace();
				}
			}
		} else {
			Log.e(Constants.TAG, "[COMMUNICATION THREAD] Socket is null!");
		}
	}

}
