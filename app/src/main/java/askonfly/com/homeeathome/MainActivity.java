package askonfly.com.homeeathome;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class MainActivity extends ActionBarActivity {
    private static final String TAG = "MainActivity";

    private WebView mWebView;
    private EditText mEmail;
    private EditText mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWebView = (WebView) findViewById(R.id.webview);

        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);
        Button login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                login(email, password);
            }
        });
    }

    public void login(String email, String password) {
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("email", email);
            jsonParams.put("password", password);
            jsonParams.put("login", 1);

        } catch (JSONException e) {
            Log.w(TAG, e.toString());
        }

        RestClient.postJSON(this, "register_or_login_through_email", jsonParams,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onFinish() {
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, final JSONObject response) {
                        Log.d(TAG, response.toString());
                        mWebView.loadData("successfully logged in\n" + response.toString(), "text/plain", "UTF-8");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        if(statusCode == 409) {
                            Log.d(TAG, errorResponse.toString());
                            mWebView.loadData("Invalid credentials --- " + errorResponse.toString(), "text/plain", "UTF-8");
                        } else {
                            mWebView.loadData("Failed to connect to the server", "text/plain", "UTF-8");
                        }

                    }
                }
        );
    }
}
