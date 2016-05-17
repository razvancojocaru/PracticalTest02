package ro.pub.cs.systems.eim.practicaltest02;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class PracticalTest02MainActivity extends AppCompatActivity {

    private EditText etServerPort, etAddress, etClientPort, etWord;
    private Button btnStartServer, btnSearch;
    private TextView tvOutput;

    private ServerThread serverThread             = null;
    private ClientThread clientThread             = null;

    private ConnectButtonClickListener connectButtonClickListener = new ConnectButtonClickListener();
    private class ConnectButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String serverPort = etServerPort.getText().toString();
            if (serverPort == null || serverPort.isEmpty()) {
                Toast.makeText(
                        getApplicationContext(),
                        "Server port should be filled!",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            serverThread = new ServerThread(Integer.parseInt(serverPort));
            if (serverThread.getServerSocket() != null) {
                serverThread.start();
            } else {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not creat server thread!");
            }

        }
    }

    private GetDefButtonClickListener getDefButtonClickListener = new GetDefButtonClickListener();
    private class GetDefButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String clientAddress = etAddress.getText().toString();
            String clientPort    = etClientPort.getText().toString();
            if (clientAddress == null || clientAddress.isEmpty() ||
                    clientPort == null || clientPort.isEmpty()) {
                Toast.makeText(
                        getApplicationContext(),
                        "Client connection parameters should be filled!",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

			if (serverThread == null || !serverThread.isAlive()) {
				Log.e(Constants.TAG, "[MAIN ACTIVITY] There is no server to connect to!");
				return;
			}

            String word = etWord.getText().toString();
            if (word == null || word.isEmpty()) {
                Toast.makeText(
                        getApplicationContext(),
                        "Parameters from client (city / information type) should be filled!",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            tvOutput.setText(Constants.EMPTY_STRING);

            clientThread = new ClientThread(
                    clientAddress,
                    Integer.parseInt(clientPort),
                    word,
                    tvOutput);
            clientThread.start();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);

        etServerPort = (EditText)findViewById(R.id.etServerPort);
        etAddress = (EditText)findViewById(R.id.etAddress);
        etClientPort = (EditText)findViewById(R.id.etClientPort);
        etWord = (EditText)findViewById(R.id.etWord);

        tvOutput = (TextView)findViewById(R.id.tvOutput);

        btnStartServer = (Button)findViewById(R.id.btnStartServer);
        btnSearch = (Button)findViewById(R.id.btnSearch);

        btnStartServer.setOnClickListener(connectButtonClickListener);
        btnSearch.setOnClickListener(getDefButtonClickListener);
    }

    @Override
    protected void onDestroy() {
        if (serverThread != null) {
            serverThread.stopThread();
        }
        super.onDestroy();
    }
}
