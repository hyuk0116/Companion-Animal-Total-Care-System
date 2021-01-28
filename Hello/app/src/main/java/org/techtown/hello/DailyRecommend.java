package org.techtown.hello;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
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
import java.util.HashMap;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;

public class DailyRecommend extends AppCompatActivity {

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

    private static final String TAG_Weights="Weight";
    private static final String TAG_IDWEIGHTS = "idWeights";
    private static final String TAG_IDPET = "idPet";
    private static final String TAG_DATE ="date";
    private static final String TAG_WEIGHT ="weight";


    member currentMember = new member();
    Pet currentPet = new Pet();
    Feed currentFeed = new Feed();
    Weights currentWeight = new Weights ();

    String mJsonString, jsonResult;

    ArrayList<member> arrayList_member = new ArrayList<>();
    ArrayList<Pet> arrayList_pet = new ArrayList<>();
    ArrayList<Feed> arrayList_feed = new ArrayList<>();
    ArrayList<Weights> arrayList_weights = new ArrayList<>();


    String id;

    private Spinner spinner1, spinner2;
    String selectedSpcies = "", selectedBreed = "";
    ArrayAdapter spinnerAdapter1, spinnerAdapter2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily);


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

        try {
            GetData task4 = new GetData();
            jsonResult = task4.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"http://106.10.53.84/weight.php").get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        showResult_weights();

        for(int i=0; i<arrayList_weights.size (); i++){
            if (currentMember.getPetId ().equals (Integer.toString (arrayList_weights.get (i).getIdPet ())))
                currentWeight = arrayList_weights.get (i);
        }


        //스피너

        ArrayList<String> companies = new ArrayList<> ();
        final ArrayAdapter<String> arrayAdapter;
        final HashMap<String, ArrayList<String>> map_feed = new HashMap<String, ArrayList<String>> ();

        for (int i=0; i<arrayList_feed.size (); i++){
            companies.add(arrayList_feed.get (i).getFCompany ());
        }

        TreeSet<String> treeset = new TreeSet<String> (companies);
        companies = new ArrayList<String> (treeset);

        for (int i=0; i<arrayList_feed.size (); i++){
            ArrayList<String> item = map_feed.get(arrayList_feed.get (i).getFCompany ());

            if(item==null) {
                item = new ArrayList<String> ();
                item.add(arrayList_feed.get (i).getFName ());
                map_feed.put(arrayList_feed.get (i).getFCompany (), item);
            }else{
                if(!item.contains (arrayList_feed.get (i).getFName ())) item.add(arrayList_feed.get (i).getFName ());
            }
        }

        arrayAdapter = new ArrayAdapter<> (getApplicationContext (), R.layout.support_simple_spinner_dropdown_item, companies);
        Spinner spinner_daily_company = (Spinner)findViewById (R.id.spinner_daily_company);
        spinner_daily_company.setAdapter (arrayAdapter);
        final Spinner spinner_daily_feed = (Spinner)findViewById (R.id.spinner_daily_feed);
        final ArrayList<String> finalCompanies = companies;


        spinner_daily_company.setOnItemSelectedListener (new AdapterView.OnItemSelectedListener () {
            @Override
            public void onItemSelected (AdapterView<?> parent, View view, final int position, long id) {

                ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<> (getApplicationContext (), R.layout.support_simple_spinner_dropdown_item, map_feed.get (finalCompanies.get (position)));
                currentFeed.setFCompany (finalCompanies.get (position));
                spinner_daily_feed.setAdapter (arrayAdapter2);
                spinner_daily_feed.setOnItemSelectedListener (new AdapterView.OnItemSelectedListener () {
                    @Override
                    public void onItemSelected (AdapterView<?> parent, View view, int position2, long id) {
                        currentFeed.setFName ((map_feed.get (finalCompanies.get (position))).get (position2));
                    }

                    @Override
                    public void onNothingSelected (AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onNothingSelected (AdapterView<?> parent) {

            }
        });

        //사료분석 버튼
        Button btn_daily = (Button)findViewById (R.id.btn_daily);
        btn_daily.setOnClickListener (new Button.OnClickListener () {
            @Override
            public void onClick (View v) {
                for (int i=0; i<arrayList_feed.size(); i++){
                    if (arrayList_feed.get (i).getFCompany ().equals (currentFeed.getFCompany ())){
                        if (arrayList_feed.get (i).getFName ().equals (currentFeed.getFName ())){
                            currentFeed = arrayList_feed.get (i);
                        }
                    }
                }

                //선택된 사료의 정보를 출력
                TextView textView_feeed_selected = (TextView)findViewById (R.id.textView_feed_selected);
                TextView textView_feeed_ingredient = (TextView)findViewById (R.id.textView_feed_ingredient);
                TextView textView_feeed_i_rate = (TextView)findViewById (R.id.textView_feed_i_rate);
                ImageView img_daily_feed = (ImageView)findViewById (R.id.img_daily_feed);

                textView_feeed_selected.setText (currentFeed.getFCompany () + " " + currentFeed.getFName ());
                textView_feeed_ingredient.setText (currentFeed.getFIngredient ());
                textView_feeed_i_rate.setText (currentFeed.getFiRate ());
                LoadImage loadImage = new LoadImage(currentFeed.getFImg());
                Bitmap bitmap = loadImage.getBitmap();
                img_daily_feed.setImageBitmap(bitmap);

                //일일 권장량 충족 여부
                // 조단백 18% 이상
                // 조지방 5.5% 이상
                // 조섬유 5% 이상

                float protein = 0.0f, fat = 0.0f, fiber = 0.0f;
                String[] iRate = (currentFeed.getFiRate ()).split (", ");

                for (int i=0; i<iRate.length; i++){
                    String[] iRate_split = new String[2];
                    if (iRate[i].contains ("조단백")){
                        iRate_split = iRate[i].split (" ");
                        protein = Float.parseFloat (iRate_split[1]);

                    }else if (iRate[i].contains ("조지방")){
                        iRate_split = iRate[i].split (" ");
                        fat = Float.parseFloat (iRate_split[1]);

                    }else if (iRate[i].contains ("조섬유")){
                        iRate_split = iRate[i].split (" ");
                        fiber = Float.parseFloat (iRate_split[1]);

                    }
                }


                TextView textView_protein = (TextView)findViewById (R.id.textView_protein);
                TextView textView_fat = (TextView)findViewById (R.id.textView_fat);
                TextView textView_fiber = (TextView)findViewById (R.id.textView_fiber);

                if(protein > 18.0)
                    textView_protein.setText ("조단백 " + protein + "% 이므로 조단백 함량 기준을 만족합니다.");
                else textView_protein.setText ("조단백 " + protein + "% 이므로 조단백 함량 기준을 만족하지 않습니다.");

                if(fat > 5.5)
                    textView_fat.setText ("조지방 " + fat + "% 이므로 조지방 함량 기준을 만족합니다.");
                else textView_fat.setText ("조지방 " + fat + "% 이므로 조지방 함량 기준을 만족하지 않습니다.");

                if(fiber > 5.0)
                    textView_fiber.setText ("조섬유 " + fiber + "% 이므로 조섬유 함량 기준을 만족합니다.");
                else textView_fiber.setText ("조섬유 " + fiber + "% 이므로 조섬유 함량 기준을 만족하지 않습니다.");

            }
        });

    }

    private class GetData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(DailyRecommend.this,
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

    private void showResult_feed() {
        try {
            JSONObject jsonObject = new JSONObject (jsonResult);
            JSONArray jsonArray = jsonObject.getJSONArray (TAG_JSON);

            for (int i = 0; i < jsonArray.length (); i++) {

                JSONObject item = jsonArray.getJSONObject (i);

                String idFEED = item.getString (TAG_IDFEED);
                String FName = item.getString (TAG_FNAME);
                String FCompany = item.getString (TAG_FCOMPANY);
                String FCountry = item.getString (TAG_FCOUNTRY);
                String FPrice = item.getString (TAG_FPRICE);
                String FAge = item.getString (TAG_FAGE);
                String FIngredient = item.getString (TAG_FINGREDIENT);
                String FiRate = item.getString (TAG_FIRATE);
                String FImg = item.getString (TAG_FIMG);

                Feed tempFeed = new Feed ();

                tempFeed.setIdFEED (Integer.parseInt (idFEED));
                tempFeed.setFName (FName);
                tempFeed.setFCompany (FCompany);
                tempFeed.setFCountry (FCountry);
                if (FPrice != "null" && FPrice != "")
                    tempFeed.setFPrice (Integer.parseInt (FPrice));
                tempFeed.setFAge (FAge);
                tempFeed.setFIngredient (FIngredient);
                tempFeed.setFiRate (FiRate);
                tempFeed.setFImg (FImg);
                arrayList_feed.add (tempFeed);
            }

        } catch (JSONException e) {
            Log.d (TAG, "showResult : ", e);
        }
    }

    private void showResult_weights ( ) {
        try {
            JSONObject jsonObject = new JSONObject (jsonResult);
            JSONArray jsonArray = jsonObject.getJSONArray (TAG_Weights);

            for (int i = 0; i < jsonArray.length (); i++) {

                JSONObject item = jsonArray.getJSONObject (i);

                String idWeights = item.getString (TAG_IDWEIGHTS);
                String idPet = item.getString (TAG_IDPET);
                String date = item.getString (TAG_DATE);
                String weight = item.getString (TAG_WEIGHT);

                Weights tempweight = new Weights ();

                tempweight.setIdWeights (Integer.parseInt (idWeights));
                tempweight.setIdPet (Integer.parseInt (idPet));
                Date dbDate = new SimpleDateFormat ("yyyy-MM-dd").parse (date);
                tempweight.setDate (dbDate);
                tempweight.setWeight (Float.parseFloat (weight));

                arrayList_weights.add (tempweight);
            }

        } catch (JSONException e) {
            Log.d (TAG, "showResult : ", e);
        } catch (ParseException e) {
            e.printStackTrace ();
        }
    }

    private void showResult_weights(String str){
        try {
            JSONObject jsonObject = new JSONObject(str);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_Weights);

            for(int i=0;i<jsonArray.length();i++) {

                JSONObject item = jsonArray.getJSONObject(i);

                String idWeights = item.getString(TAG_IDWEIGHTS);
                String idPet = item.getString(TAG_IDPET);
                String date = item.getString(TAG_DATE);
                String weight = item.getString(TAG_WEIGHT);

                Weights tempweight = new Weights();

                tempweight.setIdWeights(Integer.parseInt(idWeights));
                tempweight.setIdPet(Integer.parseInt(idPet));
                Date dbDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
                tempweight.setDate(dbDate);
                tempweight.setWeight(Float.parseFloat(weight));

                arrayList_weights.add(tempweight);
            }

        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}


