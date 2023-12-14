package  android.example.vkinfo;

import static android.example.vkinfo.utils.NetworkUtils.generateURL;
import static android.example.vkinfo.utils.NetworkUtils.getResponseFromURL;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText searchField;
    private Button searchButton;
    private TextView result;
    private TextView errorMessage;
    private ProgressBar loadingIndicator;

    private void showResultTextView() {
        result.setVisibility(View.VISIBLE);
        errorMessage.setVisibility(View.INVISIBLE);
    }

    private void showErrorTextView() {
        result.setVisibility(View.INVISIBLE);
        errorMessage.setVisibility(View.VISIBLE);
    }

    class VKQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            loadingIndicator.setVisibility(View.VISIBLE);
        }
        @Override
        protected String doInBackground(URL... urls) {
            String response = null;
            try {
                response = getResponseFromURL(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            String activeUser = null;
            String firstName = null;
            String lastName = null;

            if (response != null && !response.equals("")) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = jsonResponse.getJSONArray("response");

                    String resultingString = "";
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject userInfo = jsonArray.getJSONObject(i);

                        activeUser = userInfo.getString("deactivated");
                        firstName = userInfo.getString("first_name");
                        lastName = userInfo.getString("last_name");

                        resultingString += "Активный: " + activeUser + "\n" + "Имя: " + firstName + "\n" + "Фамилия: " + lastName
                                + "\n\n";
                    }
                    result.setText(resultingString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                showResultTextView();
            } else {
                showErrorTextView();
            }

            loadingIndicator.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchField = findViewById(R.id.et_search_filed);
        searchButton = findViewById(R.id.b_search_vk);
        result = findViewById(R.id.tv_result);
        errorMessage = findViewById(R.id.tv_error_message);
        loadingIndicator = findViewById(R.id.pb_loading_indicator);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public  void onClick(View view) {
                URL generateURL = generateURL(searchField.getText().toString());

                new VKQueryTask().execute(generateURL);
            }
        };

        searchButton.setOnClickListener(onClickListener);
    }
}


// vk  key 256a8cca256a8cca256a8ccad2267c11f32256a256a8cca4012140150991304f709307c

// https://api.vk.com/method/users.get?user_ids=754344280&access_token=256a8cca256a8cca256a8ccad2267c11f32256a256a8cca4012140150991304f709307c&v=5.89
