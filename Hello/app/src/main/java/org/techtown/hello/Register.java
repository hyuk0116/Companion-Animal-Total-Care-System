package org.techtown.hello;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class Register extends Activity {
    private EditText editTextId;
    private EditText editTextPw;
    private EditText CheckPW;
    private EditText userName;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextId = (EditText) findViewById(R.id.new_id);
        editTextPw = (EditText) findViewById(R.id.new_pw);
        CheckPW = (EditText) findViewById(R.id.pw_check);
        userName = (EditText) findViewById(R.id.new_name);


        //뒤로가기 버튼
        ImageButton backbtn = (ImageButton) findViewById(R.id.back);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

        public void insert(View view) {
            String Id = editTextId.getText().toString();
            String Pw = editTextPw.getText().toString();
            String Name = userName.getText().toString();

            insertoToDatabase(Id, Pw, Name);
        }

        private void insertoToDatabase(String Id, String Pw, String Name) {
            String Pw_ck = CheckPW.getText().toString();
            class InsertData extends AsyncTask<String, Void, String> {
                ProgressDialog loading;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    loading = ProgressDialog.show(Register.this, "Please Wait", null, true, true);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    loading.dismiss();
                    Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                }

                @Override
                protected String doInBackground(String... params) {

                    try {
                        String Id = (String) params[0];
                        String Pw = (String) params[1];
                        String Name = (String) params[2];

                        String link = "http://106.10.53.84/register.php";
                        String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(Id, "UTF-8");
                        data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(Pw, "UTF-8");
                        data += "&" + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(Name, "UTF-8");

                        URL url = new URL(link);
                        URLConnection conn = url.openConnection();

                        conn.setDoOutput(true);
                        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                        wr.write(data);
                        wr.flush();

                        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                        StringBuilder sb = new StringBuilder();
                        String line = null;

                        // 서버 응답 부분
                        while ((line = reader.readLine()) != null) {
                            sb.append(line);
                            break;
                        }
                        return sb.toString();
                    } catch (Exception e) {
                        return new String("Exception: " + e.getMessage());
                    }
                }
            }
            //비밀번호 일치 비교부분
            if (Pw.equals(Pw_ck)) {
                InsertData task = new InsertData();
                task.execute(Id, Pw, Name);
                finish();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                dialog = builder.setMessage("비밀번호를 다시 확인하세요.")
                        .setNegativeButton("다시 시도", null)
                        .create();
                dialog.show();
            }
        }
    }