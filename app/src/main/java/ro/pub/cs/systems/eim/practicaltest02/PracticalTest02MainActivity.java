package ro.pub.cs.systems.eim.practicaltest02;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PracticalTest02MainActivity extends AppCompatActivity {

    private EditText etServerPort, etAddress, etClientPort, etWord;
    private Button btnStartServer, btnConnect, btnSearch;
    private TextView tvOutput;

    private ServerThread serverThread             = null;
    private ClientThread clientThread             = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);

        etServerPort = (EditText)findViewById(R.id.etServerPort);
        etAddress = (EditText)findViewById(R.id.etAddress);
        etClientPort = (EditText)findViewById(R.id.etClientPort);
        etWord = (EditText)findViewById(R.id.etWord);

        btnStartServer = (Button)findViewById(R.id.btnStartServer);
        btnConnect = (Button)findViewById(R.id.btnConnect);
        btnSearch = (Button)findViewById(R.id.btnSearch);
    }
}
