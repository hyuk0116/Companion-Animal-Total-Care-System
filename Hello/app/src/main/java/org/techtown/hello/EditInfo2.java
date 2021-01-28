package org.techtown.hello;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EditInfo2 extends AppCompatActivity {

    private static String IP_ADDRESS = "106.10.53.84";
    private static String TAG = "TEST";


    member currentMember = new member();
    Pet currentPet = new Pet();
    Heights currentHeight = new Heights();

    ArrayList<member> memberAL = new ArrayList<member>();
    ArrayList<Pet> petAL = new ArrayList<Pet>();
    ArrayList<Heights> heightsAL = new ArrayList<Heights>();

    boolean no_pet = false;

    /*
        //id 받아오는 과정(로그인 유지)
        Intent getId = new Intent();
        String id = getIntent().getStringExtra("id");
        */
    //로그인 기능이 저한테 없어서 id 그냥 임의로 정해놓고 할게요! 나중에 합칠때 이렇게 하는거 아니면 고쳐주세요!ㅠㅠ

    String id = "";

    private Spinner spinner1, spinner2, spinner3;
    String selectedSpcies = "", selectedBreed = "";
    ArrayAdapter spinnerAdapter1, spinnerAdapter2, spinnerAdapter3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modinfo);

        //넘어온 인텐트 값 받기
        final Intent intent_db = getIntent();
        id = intent_db.getStringExtra("ID");
        memberAL = (ArrayList<member>) intent_db.getSerializableExtra("member");
        petAL = (ArrayList<Pet>) intent_db.getSerializableExtra("pet");
        heightsAL = (ArrayList<Heights>) intent_db.getSerializableExtra("heights");

        //닉네임, 아이디 출력해주기
        for (int i = 0; i < memberAL.size(); i++) {
            if (memberAL.get(i).email.equals(id))
                currentMember = memberAL.get(i);
        }

        TextView textView_id = (TextView) findViewById(R.id.textView_Id);
        textView_id.setText(currentMember.getEmail());
        TextView textView_m_name = (TextView) findViewById(R.id.textView_m_name);
        textView_m_name.setText(currentMember.getName());


        //뒤로가기 버튼
        final ImageButton backbtn = (ImageButton)findViewById(R.id.back);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        //비밀번호 바꾸는 기능
        final EditText editText_pw = (EditText)findViewById(R.id.editText_pw);
        final EditText editText_pw_check = (EditText)findViewById(R.id.editText_pw_check);

        Button btn_pw = (Button)findViewById(R.id.btn_pw);
        btn_pw.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                if (editText_pw.getText().toString().equals(editText_pw_check.getText().toString())){
                    currentMember.setPassword(editText_pw.getText().toString());
                    Toast.makeText(getApplicationContext(), "비밀번호 변경 완료", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(), "비밀번호가 다릅니다", Toast.LENGTH_LONG).show();
                }
            }
        });



        //펫 정보 출력

        for (int i = 0; i < petAL.size(); i++) {
            if (Integer.toString(petAL.get(i).getIdPet()).equals(currentMember.getPetId()))
                currentPet = petAL.get(i);
        }

        TextView textView_third_petname = (TextView) findViewById(R.id.textView_third_petname);
        textView_third_petname.setText(currentPet.getpName());
        TextView textView_third_species = (TextView) findViewById(R.id.textView_third_species);
        textView_third_species.setText(currentPet.getpSpecies() + " / " + currentPet.getpBreed());
        TextView textView_third_age = (TextView) findViewById(R.id.textView_third_age);
        textView_third_age.setText(currentPet.getpAge());

        //키 입력 & 최종수정

        EditText editText_h = (EditText) findViewById(R.id.editText_h);

        Button btn_done = (Button) findViewById(R.id.btn_edit_done);
        btn_done.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date time = new Date();
                String now = format.format(time);

                final EditText editText_h = (EditText) findViewById(R.id.editText_h);

                currentHeight.setIdHeights((heightsAL.get(heightsAL.size()-1).getIdHeights())+1);
                try {
                    currentHeight.setDate(format.parse(now));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                currentHeight.setPetID(currentPet.getIdPet());
                currentHeight.setpHeights(Float.parseFloat(editText_h.getText().toString()));

                InsertData_member task = new InsertData_member();
                task.execute("http://" + IP_ADDRESS + "/insert_member.php", currentMember.getEmail(), currentMember.getPassword(), currentMember.getName(), currentMember.getPetId());


                heightsAL.add(currentHeight);
                InsertData_height task_height = new InsertData_height();
                task_height.execute("http://" + IP_ADDRESS + "/insert_height.php", Integer.toString(currentHeight.getIdHeights()), Float.toString(currentHeight.getpHeights()), Integer.toString(currentHeight.getPetID()), now);
                System.out.println("Heightsid = " + currentHeight.getIdHeights());

                //인텐트를 통해서 다음 액티비티로 DB에서 받아온 정보 넘겨줌
                Intent intent_db = new Intent(getApplicationContext(), showInfo.class);
                intent_db.putExtra("member", memberAL);
                intent_db.putExtra("pet", petAL);
                intent_db.putExtra("heights", heightsAL);
                intent_db.putExtra("ID", id);
                startActivity(intent_db);

            }
        });

    }

    class InsertData_member extends AsyncTask<String, Void, String>{
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(EditInfo2.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, "POST response  - " + result);
        }


        @Override
        protected String doInBackground(String... params) {

            String email = (String)params[1];
            String password = (String)params[2];
            String name = (String)params[3];
            String petId = (String)params[4];

            String serverURL = (String)params[0];
            String postParameters = "email=" + email + "&password=" + password+ "&name=" + name+ "&petId=" + petId ;


            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString();


            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }
    }



    class InsertData_height extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(EditInfo2.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, "POST response  - " + result);
        }


        @Override
        protected String doInBackground(String... params) {

            String idHeights = (String) params[1];
            String pHeights = (String) params[2];
            String petID = (String) params[3];
            String date = (String) params[4];

            String serverURL = (String) params[0];
            String postParameters = "idHeights=" + idHeights + "&pHeights=" + pHeights + "&petID=" + petID + "&date=" + date;


            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString();


            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }
    }

}