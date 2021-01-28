package org.techtown.hello;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class MyInfo extends AppCompatActivity {

    private static String TAG = "TEST";

    private static final String TAG_Member="member";
    private static final String TAG_email = "email";
    private static final String TAG_password = "password";
    private static final String TAG_name ="name";
    private static final String TAG_petId ="petId";

    private static final String TAG_Pet="Pet";
    private static final String TAG_idPet="idPet";
    private static final String TAG_pName="pName";
    private static final String TAG_pSpecies="pSpecies";
    private static final String TAG_pBreed="pBreed";
    private static final String TAG_pAge="pAge";

    private static final String TAG_Heights="Heights";
    private static final String TAG_idHeights="idHeights";
    private static final String TAG_pHeights="pHeights";
    private static final String TAG_hPetID="petID";
    private static final String TAG_date="date";

    ArrayList<member> memberAL = new ArrayList<>();
    ArrayList<Pet> petAL = new ArrayList<>();
    ArrayList<Heights> HeightsAL = new ArrayList<>();

    member currentMember = new member();
    boolean no_pet = false;

    String mJsonString;

    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        String result = "";

        //뒤로가기 버튼
        final ImageButton backbtn = (ImageButton)findViewById(R.id.back);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        //아이디값 받아오는 부분
        Intent intent = getIntent();
        id = intent.getExtras().getString("id");

        GetData task = new GetData();

        //멤버를 가져오고 로그인 된 멤버를 출력
        try {
            result = task.execute("http://106.10.53.84/member.php").get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        showResult_member(result);

        for (int i = 0; i < memberAL.size(); i++) {
            if (memberAL.get(i).email.equals(id))
                currentMember = memberAL.get(i);
        }

        TextView textView_id = (TextView) findViewById(R.id.textView_Id);
        textView_id.setText(currentMember.getEmail());
        TextView textView_name = (TextView) findViewById(R.id.textView_name);
        textView_name.setText(currentMember.getName());

        //펫을 가져오고 펫 닉네임과 정보를 출력하는 부분
        try {
            GetData task2 = new GetData();
            result = task2.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"http://106.10.53.84/pet.php").get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        showResult_pet(result);

        TextView textView_petName = (TextView)findViewById(R.id.textView_pet_name);
        TextView textView_pSpecies = (TextView)findViewById(R.id.textView_pet_species);
        TextView textView_pBreed = (TextView)findViewById(R.id.textView_pet_breed);

        if(currentMember.getPetId().equals("0")) {
            no_pet = true;
            textView_petName.setText("정보를 입력해주세요");
        }else {
            for (int i = 0; i < petAL.size(); i++) {
                if (currentMember.getPetId().equals(Integer.toString(petAL.get(i).getIdPet()))) {
                    textView_petName.setText(petAL.get(i).getpName());
                    textView_pSpecies.setText(petAL.get(i).getpSpecies());
                    textView_pBreed.setText(petAL.get(i).getpBreed());
                }
            }
        }

        //반려동물의 키를 가져오는 부분
        try {
            GetData task3 = new GetData();
            result = task3.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"http://106.10.53.84/heights.php").get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        showResult_heights(result);
        Heights currenth = new Heights();
        currenth = HeightsAL.get(0);
        TextView textView_pet_height = (TextView)findViewById(R.id.textView_pet_height);

        for(int i = 0; i<HeightsAL.size(); i++) {
            if (currentMember.getPetId().equals(Integer.toString(HeightsAL.get(i).getPetID()))){
                currenth = HeightsAL.get(i);
            }
        }
        if (currentMember.getPetId().equals(Integer.toString(currenth.getPetID())))
            textView_pet_height.setText(Float.toString(currenth.getpHeights()));

        Button btn_edit = findViewById(R.id.btn_edit);
        btn_edit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                if (no_pet == true){
                    Intent intent_db = new Intent(getApplicationContext(), EditInfo.class);
                    intent_db.putExtra("member", memberAL);
                    intent_db.putExtra("pet", petAL);
                    intent_db.putExtra("heights", HeightsAL);
                    intent_db.putExtra("ID", id);
                    startActivity(intent_db);
                }
                else {
                    //인텐트를 통해서 다음 액티비티로 DB에서 받아온 정보 넘겨줌
                    Intent intent_db = new Intent(getApplicationContext(), EditInfo2.class);
                    intent_db.putExtra("member", memberAL);
                    intent_db.putExtra("pet", petAL);
                    intent_db.putExtra("heights", HeightsAL);
                    intent_db.putExtra("ID", id);
                    startActivity(intent_db);
                }
            }
        });
    }

    private class GetData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(MyInfo.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, "response  - " + result);

            mJsonString = result;


        }

        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];
            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.connect();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                bufferedReader.close();
                return sb.toString().trim();


            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);
                errorString = e.toString();

                return null;
            }

        }
    }

    //json 데이터 파싱하는 부분
    private void showResult_member(String str){
        try {
            JSONObject jsonObject = new JSONObject(str);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_Member);

            for(int i=0;i<jsonArray.length();i++) {

                JSONObject item = jsonArray.getJSONObject(i);

                String email = item.getString(TAG_email);
                String password = item.getString(TAG_password);
                String name = item.getString(TAG_name);
                String petId = item.getString(TAG_petId);

                member tempMem = new member();

                tempMem.setEmail(email);
                tempMem.setPassword(password);
                if (name != null )
                    tempMem.setName(name);
                else tempMem.setName("닉네임을 등록해주세요");
                if (petId.equals("null") || name.isEmpty())
                    petId = "0";
                tempMem.setPetId(petId);

                memberAL.add(tempMem);
            }

        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }

    private void showResult_pet(String str){
        try {
            JSONObject jsonObject = new JSONObject(str);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_Pet);

            for(int i=0;i<jsonArray.length();i++) {

                JSONObject item = jsonArray.getJSONObject(i);

                String idPet = item.getString(TAG_idPet);
                String pName = item.getString(TAG_pName);
                String pSpecies = item.getString(TAG_pSpecies);
                String pBreed = item.getString(TAG_pBreed);
                String pAge = item.getString(TAG_pAge);

                Pet tempPet = new Pet();

                tempPet.setIdPet(Integer.parseInt(idPet));
                tempPet.setpName(pName);
                tempPet.setpSpecies(pSpecies);
                tempPet.setpBreed(pBreed);
                tempPet.setpAge(pAge);

                petAL.add(tempPet);
            }

        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }

    private void showResult_heights(String str){
        try {
            JSONObject jsonObject = new JSONObject(str);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_Heights);

            for(int i=0;i<jsonArray.length();i++) {

                JSONObject item = jsonArray.getJSONObject(i);

                String idHeights = item.getString(TAG_idHeights);
                String pHeights = item.getString(TAG_pHeights);
                String petID = item.getString(TAG_hPetID);
                String date = item.getString(TAG_date);

                Heights tempHeight = new Heights();

                tempHeight.setIdHeights(Integer.parseInt(idHeights));
                tempHeight.setpHeights(Float.parseFloat(pHeights));
                tempHeight.setPetID(Integer.parseInt(petID));
                SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date to = transFormat.parse(date);
                tempHeight.setDate(to);

                HeightsAL.add(tempHeight);
            }

        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
