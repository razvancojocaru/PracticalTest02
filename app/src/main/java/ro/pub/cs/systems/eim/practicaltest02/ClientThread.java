package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread {
	
	private String address;
	private int      port;
	private String word;
	private TextView tvOutput;
	
	private Socket socket;
	
	public ClientThread(
			String address,
			int port,
			String word,
			TextView tvOutput) {
		this.address                 = address;
		this.port                    = port;
		this.word                    = word;
		this.tvOutput = tvOutput;
	}
	
	@Override
	public void run() {
		try {
			socket = new Socket(address, port);
			if (socket == null) {
				Log.e(Constants.TAG, "[CLIENT THREAD] Could not create socket!");
			}
			
			BufferedReader bufferedReader = Utilities.getReader(socket);
			PrintWriter printWriter    = Utilities.getWriter(socket);
			if (bufferedReader != null && printWriter != null) {
				printWriter.println(word);
				printWriter.flush();
				String definition;
				while ((definition = bufferedReader.readLine()) != null) {
					final String finalizeddefinition = definition;
					tvOutput.post(new Runnable() {
						@Override
						public void run() {
							tvOutput.append(finalizeddefinition + "\n");
						}
					});
				}
			} else {
				Log.e(Constants.TAG, "[CLIENT THREAD] BufferedReader / PrintWriter are null!");
			}
			socket.close();
		} catch (IOException ioException) {
			Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
			if (Constants.DEBUG) {
				ioException.printStackTrace();
			}
		}
	}

}
