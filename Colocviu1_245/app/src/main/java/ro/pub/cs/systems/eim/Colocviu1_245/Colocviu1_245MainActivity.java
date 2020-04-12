package ro.pub.cs.systems.eim.Colocviu1_245;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Colocviu1_245MainActivity extends AppCompatActivity {

    private EditText numberText;
    private TextView sumView;
    private Button addButton, computeButton;
    private String lastAllTerms = "";
    private int lastSum = 0;
    private Context context;

    private int serviceStatus = Constants.SERVICE_STOPPED;

    private IntentFilter intentFilter = new IntentFilter();

    private ButtonClickListener buttonClickListener = new ButtonClickListener();
    private class ButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch(view.getId()) {
                case R.id.add:
                    try {
                        int num = Integer.parseInt(numberText.getText().toString());
                        if (sumView.getText().toString().equals(Constants.DEFAULT_SUM_VIEW)) {
                            sumView.setText(Integer.toString(num));
                        } else {
                            String sum = sumView.getText().toString() + Constants.SUM_DELIM + Integer.toString(num);
                            sumView.setText(sum);
                        }
                    } catch (Exception e) { }

                    break;
                case R.id.compute:
                    if (lastAllTerms.equals(sumView.getText().toString())) {
                        Toast.makeText(context, "Sum equals: " + Integer.toString(lastSum), Toast.LENGTH_LONG).show();
                        break;
                    }
                    Intent intent = new Intent(getApplicationContext(), Colocviu1_245SecondaryActivity.class);
                    lastAllTerms = sumView.getText().toString();
                    intent.putExtra(Constants.ALL_TERMS_SUM, lastAllTerms);
                    startActivityForResult(intent, Constants.SECONDARY_ACTIVITY_REQUEST_CODE);
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test01_245_main);

        numberText = (EditText)findViewById(R.id.number);
        sumView = (TextView)findViewById(R.id.sum);
        addButton = (Button)findViewById(R.id.add);
        addButton.setOnClickListener(buttonClickListener);
        computeButton = (Button)findViewById(R.id.compute);
        computeButton.setOnClickListener(buttonClickListener);

        context = getApplicationContext();

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(Constants.ALL_TERMS)) {
                sumView.setText(savedInstanceState.getString(Constants.ALL_TERMS));
            } else {
                sumView.setText("");
            }

            if (savedInstanceState.containsKey(Constants.LAST_ALL_TERMS)) {
                lastAllTerms = savedInstanceState.getString(Constants.LAST_ALL_TERMS);
            } else {
                lastAllTerms = "";
            }

            if (savedInstanceState.containsKey(Constants.LAST_SUM)) {
                lastSum = savedInstanceState.getInt(Constants.LAST_SUM);
            } else {
                lastSum = 0;
            }
        }

        for (int index = 0; index < Constants.actionTypes.length; index++) {
            intentFilter.addAction(Constants.actionTypes[index]);
        }
    }

    private MessageBroadcastReceiver messageBroadcastReceiver = new MessageBroadcastReceiver();
    private class MessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, intent.getStringExtra(Constants.BROADCAST_RECEIVER_EXTRA), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(messageBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        unregisterReceiver(messageBroadcastReceiver);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Intent intent = new Intent(this, Colocviu1_245Service.class);
        stopService(intent);
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if ((requestCode == Constants.SECONDARY_ACTIVITY_REQUEST_CODE) && (intent != null)) {
            int sum = intent.getIntExtra(Constants.SUM_DONE, -1);
            lastSum = sum;
            Toast.makeText(this, "Sum equals: " + Integer.toString(sum), Toast.LENGTH_LONG).show();

            if (lastSum > Constants.SUM_THRESHOLD && serviceStatus == Constants.SERVICE_STOPPED) {
                Intent serviceIntent = new Intent(context, Colocviu1_245Service.class);
                serviceIntent.putExtra(Constants.SERVICE_SUM, lastSum);
                context.startService(serviceIntent);
                serviceStatus = Constants.SERVICE_STARTED;
            }

            if (lastSum > Constants.SUM_THRESHOLD) {
                Intent serviceIntent = new Intent(context, Colocviu1_245Service.class);
                serviceIntent.putExtra(Constants.SERVICE_SUM, lastSum);
                context.startService(serviceIntent);
                serviceStatus = Constants.SERVICE_STARTED;
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(Constants.ALL_TERMS, sumView.getText().toString());
        savedInstanceState.putString(Constants.LAST_ALL_TERMS, lastAllTerms);
        savedInstanceState.putInt(Constants.LAST_SUM, lastSum);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.containsKey(Constants.ALL_TERMS)) {
            sumView.setText(savedInstanceState.getString(Constants.ALL_TERMS));
        } else {
            sumView.setText("");
        }

        if (savedInstanceState.containsKey(Constants.LAST_ALL_TERMS)) {
            lastAllTerms = savedInstanceState.getString(Constants.LAST_ALL_TERMS);
        } else {
            lastAllTerms = "";
        }

        if (savedInstanceState.containsKey(Constants.LAST_SUM)) {
            lastSum = savedInstanceState.getInt(Constants.LAST_SUM);
        } else {
            lastSum = 0;
        }
    }
}
