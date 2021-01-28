package org.techtown.hello;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Member;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.ExecutionException;

public class Recommend extends AppCompatActivity {


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

    private static final String TAG_JSON = "FEED";
    private static final String TAG_IDFEED = "idFEED";
    private static final String TAG_FNAME = "FName";
    private static final String TAG_FCOMPANY = "FCompany";
    private static final String TAG_FCOUNTRY = "FCountry";
    private static final String TAG_FPRICE = "FPrice";
    private static final String TAG_FAGE = "FAge";
    private static final String TAG_FINGREDIENT = "FIngredient";
    private static final String TAG_FIRATE = "FiRate";
    private static final String TAG_FIMG = "FImg";

    ArrayList<member> arrayList_member = new ArrayList<>();
    ArrayList<Pet> arrayList_pet = new ArrayList<>();
    ArrayList<Feed> arrayList_feed = new ArrayList<>();
    ListView listView;
    String mJsonString, jsonResult;

    member currentMember = new member();
    Pet currentPet = new Pet();

    ArrayList<Feed> feed_age = new ArrayList<>();
    ArrayList<Feed> feed_age_price = new ArrayList<>();
    ArrayAdapter spinnerAdapter;

    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);


        //아이디값 받아오는 부분
        Intent intent = getIntent();
        id = intent.getExtras().getString("id");


        //뒤로가기 버튼
        final ImageButton backbtn = (ImageButton) findViewById(R.id.back);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        final Button btn_daily=(Button)findViewById(R.id.btn_daily);
        //일일권장량 사료추천으로 가는 버튼동작
        btn_daily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_daily=new Intent(Recommend.this, DailyRecommend.class);
                intent_daily.putExtra("id", id);
                startActivity(intent_daily);
            }
        });


        try {
            GetData task = new GetData();
            jsonResult = task.execute("http://106.10.53.84/member.php").get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        showResult_member(jsonResult);
        for (int i = 0; i < arrayList_member.size(); i++) {
            if (arrayList_member.get(i).email.equals(id))
                currentMember = arrayList_member.get(i);
        }

        try {
            GetData task2 = new GetData();
            jsonResult = task2.execute("http://106.10.53.84/pet.php").get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        showResult_pet();

        for (int i = 0; i < arrayList_pet.size(); i++) {
            if (currentMember.getPetId().equals(Integer.toString(arrayList_pet.get(i).getIdPet()))) {
                currentPet = arrayList_pet.get(i);
            }
        }


        try {
            GetData task3 = new GetData();
            jsonResult = task3.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"http://106.10.53.84/getFeed.php").get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        showResult_feed();



        TextView textView_status = (TextView)findViewById(R.id.textView_status);
        TextView textView_recommend = (TextView)findViewById(R.id.textView_recommend);

        textView_status.setText(currentPet.getpName()+"은(는) "+currentPet.getpAge()+"입니다.");
        textView_recommend.setText(currentPet.getpAge()+"에게 맞는 사료를 추천해드리겠습니다!");

        for (int i=0; i<arrayList_feed.size(); i++){
            if (currentPet.getpAge().equals(arrayList_feed.get(i).getFAge()) || arrayList_feed.get(i).getFAge().equals("전연령")) {
                feed_age.add(arrayList_feed.get(i));
            }
        }
        feed_age_price.addAll(feed_age) ;

        final String[] feedArr = getResources().getStringArray(R.array.Feed_selection);
        spinnerAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, feedArr);
        Spinner spinner_feed = (Spinner)findViewById(R.id.spinner_feed);
        spinner_feed.setAdapter(spinnerAdapter);
        spinner_feed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinnerAdapter.getItem(position).equals("가격순")){
                    Collections.sort(feed_age_price);
                    listView = (ListView)findViewById(R.id.listView_main);
                    FeedListView flistView = new FeedListView(getLayoutInflater(), feed_age_price);
                    listView.setAdapter(flistView);
                }
                else{
                    listView = (ListView)findViewById(R.id.listView_main);
                    FeedListView flistView = new FeedListView(getLayoutInflater(), feed_age);
                    listView.setAdapter(flistView);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                listView = (ListView)findViewById(R.id.listView_main);
                FeedListView flistView = new FeedListView(getLayoutInflater(), feed_age);
                listView.setAdapter(flistView);
            }
        });

    }


    private class GetData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(Recommend.this,
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
                if (name != null)
                    tempMem.setName(name);
                else tempMem.setName("닉네임을 등록해주세요");
                if (petId==null)
                    petId = "0";
                tempMem.setPetId(petId);

                arrayList_member.add(tempMem);
            }

        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }

    private void showResult_pet() {
        try {
            JSONObject jsonObject = new JSONObject(jsonResult);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_Pet);

            for (int i = 0; i < jsonArray.length(); i++) {

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

                arrayList_pet.add(tempPet);
            }

        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }

    private void showResult_feed(){
        try {
            JSONObject jsonObject = new JSONObject(jsonResult);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++) {

                JSONObject item = jsonArray.getJSONObject(i);

                String idFEED = item.getString(TAG_IDFEED);
                String FName = item.getString(TAG_FNAME);
                String FCompany = item.getString(TAG_FCOMPANY);
                String FCountry = item.getString(TAG_FCOUNTRY);
                String FPrice = item.getString(TAG_FPRICE);
                String FAge = item.getString(TAG_FAGE);
                String FIngredient = item.getString(TAG_FINGREDIENT);
                String FiRate = item.getString(TAG_FIRATE);
                String FImg = item.getString(TAG_FIMG);

                Feed tempFeed = new Feed();

                tempFeed.setIdFEED(Integer.parseInt(idFEED));
                tempFeed.setFName(FName);
                tempFeed.setFCompany(FCompany);
                tempFeed.setFCountry(FCountry);
                if (FPrice != "null" && FPrice != "")
                    tempFeed.setFPrice(Integer.parseInt(FPrice));
                tempFeed.setFAge(FAge);
                tempFeed.setFIngredient(FIngredient);
                tempFeed.setFiRate(FiRate);
                tempFeed.setFImg(FImg);
                arrayList_feed.add(tempFeed);
            }

        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }

}
