package ro.pub.cs.systems.eim.Colocviu1_245;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class Colocviu1_245SecondaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent != null && intent.getExtras().containsKey(Constants.ALL_TERMS_SUM)) {
            String all_terms_sum = intent.getStringExtra(Constants.ALL_TERMS_SUM);
            String[] terms = all_terms_sum.split(" \\+ ");

            int sum = 0;
            for (int i = 0; i < terms.length; ++i) {
                sum += Integer.parseInt(terms[i]);
            }

            Intent resultIntent = new Intent();
            resultIntent.putExtra(Constants.SUM_DONE, sum);
            setResult(RESULT_OK, resultIntent);
            finish();
        }

        setResult(RESULT_CANCELED, null);
    }
}
