package org.techtown.hello;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class Disease extends Activity {

    private TextView textView;
    private TextView Symptom;
    private TextView Prevention;
    private TextView Treatment;
    private TextView Species;

    ArrayList<HashMap<String, String>> DiseaseList = new ArrayList<HashMap<String, String>>();
    ArrayList<String> SymptomList = new ArrayList<String>();
    ArrayList<String> PreventionList = new ArrayList<String>();
    ArrayList<String> TreatmentList = new ArrayList<String>();
    ArrayList<String> SpeciesList = new ArrayList<String>();


    class BackgroundTask extends AsyncTask<Void, Void, String>
    {
        String target;

        @Override
        protected void onPreExecute() {
            target = "http://106.10.53.84/disease.php";
        }

        @Override
        protected String doInBackground(Void... voids) {
            try{
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while((temp = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(temp + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onProgressUpdate(Void... values) {
            super.onProgressUpdate();
        }

        @Override
        public void onPostExecute(String result) {
            try{
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("Disease");
                int count = 0;
                String DID, DName, Symptom, Prevention, Species, Treat;
                while(count < jsonArray.length()) {
                    JSONObject object = jsonArray.getJSONObject(count);
                    DID = object.getString("Did");
                    DName = object.getString("Dname");
                    Symptom = object.getString("Symptom");
                    Prevention = object.getString("Prevention");
                    Species = object.getString("Species");
                    Treat = object.getString("Treat");

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("Did", DID);
                    hashMap.put("Dname", DName);
                    hashMap.put("Symptom", Symptom);
                    hashMap.put("Prevention", Prevention);
                    hashMap.put("Species", Species);
                    hashMap.put("Treat", Treat);
                    SymptomList.add(Symptom);
                    PreventionList.add(Prevention);
                    TreatmentList.add(Treat);
                    SpeciesList.add(Species);
                    DiseaseList.add(hashMap);
                    count++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease);

        //textview 선언부분
        textView = (TextView)findViewById(R.id.textView);
        Symptom = (TextView)findViewById(R.id.Symptom);
        Prevention = (TextView)findViewById(R.id.Prevention);
        Treatment = (TextView)findViewById(R.id.Treatment);
        Species = (TextView)findViewById(R.id.Species);


        //아이디값 받아오는 부분
        Intent intent = getIntent();
        String id = intent.getExtras().getString("id"); /*String형*/
        //textView.setText(id);


        //spinner 부분
        final String[] data = getResources().getStringArray(R.array.종);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, data);
        Spinner Spinner = (Spinner)findViewById(R.id.spinner);
        Spinner.setAdapter(adapter);


        //디비접속부분
        new BackgroundTask().execute();


        //뒤로가기 버튼
        final ImageButton backbtn = (ImageButton)findViewById(R.id.back);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        //스피너 선택
        Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(data[position].equals("질병 목록")) {
                    textView.setText("조회해주세요.");
                    Symptom.setText("질병을 선택해주세요.");
                    Prevention.setText("질병을 선택해주세요.");
                    Treatment.setText("질병을 선택해주세요.");
                    Species.setText("질병을 선택해주세요.");
                }
                if(data[position].equals("파보바이러스감염증")) {
                    textView.setText(null);
                    Symptom.setText(SymptomList.get(0));
                    Prevention.setText(PreventionList.get(0));
                    Treatment.setText(TreatmentList.get(0));
                    Species.setText(SpeciesList.get(0));
                }
                if(data[position].equals("코로나바이러스감염증")) {
                    textView.setText(null);
                    Symptom.setText(SymptomList.get(1));
                    Prevention.setText(PreventionList.get(1));
                    Treatment.setText(TreatmentList.get(1));
                    Species.setText(SpeciesList.get(1));
                }
                if(data[position].equals("광견병")) {
                    textView.setText(null);
                    Symptom.setText(SymptomList.get(2));
                    Prevention.setText(PreventionList.get(2));
                    Treatment.setText(TreatmentList.get(2));
                    Species.setText(SpeciesList.get(2));
                }
                if(data[position].equals("고양이전염성복막염")) {
                    textView.setText(null);
                    Symptom.setText(SymptomList.get(3));
                    Prevention.setText(PreventionList.get(3));
                    Treatment.setText(TreatmentList.get(3));
                    Species.setText(SpeciesList.get(3));
                }
                if(data[position].equals("심장사상충")) {
                    textView.setText(null);
                    Symptom.setText(SymptomList.get(4));
                    Prevention.setText(PreventionList.get(4));
                    Treatment.setText(TreatmentList.get(4));
                    Species.setText(SpeciesList.get(4));
                }
                if(data[position].equals("고양이 심장사상충")) {
                    textView.setText(null);
                    Symptom.setText(SymptomList.get(5));
                    Prevention.setText(PreventionList.get(5));
                    Treatment.setText(TreatmentList.get(5));
                    Species.setText(SpeciesList.get(5));
                }
                if(data[position].equals("보데텔라폐렴")) {
                    textView.setText(null);
                    Symptom.setText(SymptomList.get(6));
                    Prevention.setText(PreventionList.get(6));
                    Treatment.setText(TreatmentList.get(6));
                    Species.setText(SpeciesList.get(6));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }
}
