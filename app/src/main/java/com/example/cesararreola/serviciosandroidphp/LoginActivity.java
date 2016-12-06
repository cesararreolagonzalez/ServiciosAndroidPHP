package com.example.cesararreola.serviciosandroidphp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    public final static String DATA  = "DATA";
    private final static String USER = "USER";
    private static final String REGISTER_REQUEST_URL = "";
    private static final int RESULT_SUCCESS = 1;
    private static final int RESULT_ERROR   = 0;
    private static final int USERNAME_POSITION   = 0;
    private static final int PASSWORD_POSITION   = 1;
    ProgressDialog mProgressDialog;
    private Button btn_login;
    private EditText edtxt_user, edtxt_password;
    private TextView txt_username, txt_password;
    private String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btn_login      = (Button) findViewById(R.id.btn_login);
        edtxt_user     = (EditText) findViewById(R.id.edtxt_user);
        edtxt_password = (EditText) findViewById(R.id.edtxt_password);
        txt_username = (TextView) findViewById(R.id.txt_user);
        txt_password   = (TextView) findViewById(R.id.txt_password);

        edtxt_password.setTypeface(Typeface.SANS_SERIF);
        //edtxt_user.setCompoundDrawablesWithIntrinsicBounds(R.drawable.users, 0, 0, 0);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                username = edtxt_user.getText().toString();
                password = edtxt_password.getText().toString();
                new TaskLogin().execute();
            }
        });
        /*btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Thread thread = new Thread()
                {
                    @Override
                    public void run()
                    {
                        username     = edtxt_user.getText().toString();
                        password = edtxt_password.getText().toString();
                        final String response_url = sendDataGET(username, password);
                        //=================================================
                        //System.out.println("response_url = " + response_url);

                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                int response_json = getDataJSON(response_url);
                                System.out.println("response_json = " + response_json);
                                if(response_json > 0)
                                {
                                    Intent intent = new Intent(getApplicationContext(), UserMainActivity.class);
                                    intent.putExtra(USER, username);
                                    startActivity(intent);
                                }
                                else
                                    Toast.makeText(getApplicationContext(), R.string.err_login, Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                };
                thread.start();
            }
        });*/

        edtxt_user.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0){
                    txt_username.setText(R.string.str_user);
                }
                else{
                    txt_username.setText("");
                }

                if(s.length() > 0 && txt_password.getText().toString().length() > 0)
                    btn_login.setEnabled(true);
                else
                    btn_login.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtxt_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0){
                    txt_password.setText(R.string.str_password);
                }
                else{
                    txt_password.setText("");
                }

                if(s.length() > 0 && txt_username.getText().toString().length() > 0)
                    btn_login.setEnabled(true);
                else
                    btn_login.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
        {
            switch (resultCode) {
                case RESULT_OK:
                    // Resultado correcto
                    break;

                case RESULT_CANCELED:
                    // Cancelación o cualquier situación de error
                    break;
            }
        }
    }

    public String sendDataGET(String user, String password)
    {
        final String ADRESS  = "http://192.168.15.2:80/WebService/valida.php?user=" + user + "&password=" + password;
        StringBuilder result = null;
        String line;

        try
        {
            URL url                     = new URL(ADRESS);
            HttpURLConnection conection = (HttpURLConnection) url.openConnection();
            int response                = conection.getResponseCode();
            result                      = new StringBuilder();

            if(response == HttpURLConnection.HTTP_OK)
            {
                InputStream in = new BufferedInputStream(conection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                while ((line = reader.readLine()) != null){
                    result.append(line);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e2){
            e2.printStackTrace();
        }

        return result.toString();
    }

    public int getDataJSON(String response)
    {
        int res = 0;

        try {
            JSONArray json = new JSONArray(response);
            System.out.println("La longitud del JSONArray es = " + json.length());
            if(json.length() > 0) res =  1;
        } catch (JSONException e){
            System.out.println("[getDataJSON] : El error es = " + e.getMessage());
            e.printStackTrace();
        }
        return res;
    }


    public class TaskLogin extends AsyncTask<String, Void, Integer>
    {
        String data;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            //Open progress dialog during login
            mProgressDialog = ProgressDialog.show(LoginActivity.this, "Please wait...", "Processing...", true);
        }

        @Override
        protected Integer doInBackground(final String... params)
        {
            //String username   = params[USERNAME_POSITION];
            //String password   = params[PASSWORD_POSITION];
            data = sendDataGET(username, password);
            int result = getDataJSON(data);

            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);

            mProgressDialog.dismiss();

            if(result == RESULT_SUCCESS){
                Intent intent = new Intent(getApplicationContext(), UserMainActivity.class);
                intent.putExtra(DATA, data);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), R.string.err_login, Toast.LENGTH_LONG).show();
            }
        }

    }
}