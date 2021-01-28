package org.techtown.hello;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;


public class Chart extends AppCompatActivity {

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


    private static final String TAG_Weights="Weight";
    private static final String TAG_IDWEIGHTS = "idWeights";
    private static final String TAG_IDPET = "idPet";
    private static final String TAG_DATE ="date";
    private static final String TAG_WEIGHT ="weight";

    ArrayList<member> memberAL = new ArrayList<>();
    ArrayList<Pet> petAL = new ArrayList<>();
    ArrayList<Heights> heightsAL = new ArrayList<>();
    ArrayList<Weights> weightsAL = new ArrayList<>();

    ArrayList<HashMap<String, String>> mArrayList = new ArrayList<>();
    String mJsonString, result;
    CombinedChart chart;
    final int count = 12;

    member currentMember = new member();
    Pet currentPet = new Pet();

    //그래프 그릴때 사용
    ArrayList<BarEntry> weights = new ArrayList<>();
    ArrayList<Weights> chart_weights = new ArrayList<>();


    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight);


        //아이디값 받아오는 부분
        Intent intent = getIntent();
        id = intent.getExtras().getString("id");


        //뒤로가기 버튼
        ImageButton backbtn = (ImageButton) findViewById(R.id.back);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        GetData task = new GetData();

        //멤버를 가져오고 로그인 된 멤버를 저장
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


        //펫을 가져오는 부분
        try {
            GetData task2 = new GetData();
            result = task2.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"http://106.10.53.84/pet.php").get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        showResult_pet(result);

        for(int i=0; i<petAL.size(); i++){
            if (currentMember.getPetId().equals(Integer.toString(petAL.get(i).getIdPet())))
                currentPet = petAL.get(i);
        }

        TextView textView_obesity = (TextView)findViewById(R.id.textView_obesity);
        textView_obesity.setText(" " + currentPet.getpName()+"의 몸무게와 비만도");

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


        //반려동물의 몸무게를 가져오는 부분
        try {
            GetData task4 = new GetData();
            result = task4.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"http://106.10.53.84/weight.php").get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        showResult_weights(result);



        //차트
        chart = findViewById(R.id.chart);
        chart.getDescription().setEnabled(false);
        chart.setBackgroundColor(Color.WHITE);
        chart.setDrawGridBackground(false);
        chart.setDrawBarShadow(false);
        chart.setHighlightFullBarEnabled(false);

        chart.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.BUBBLE, CombinedChart.DrawOrder.CANDLE, CombinedChart.DrawOrder.LINE, CombinedChart.DrawOrder.SCATTER
        });

        Legend l = chart.getLegend();
        l.setWordWrapEnabled(true);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)


        final String[] months = {"1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월"};
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); //x축 값 밑으로 표시
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return months[(int) value % months.length];
            }
        });

        CombinedData data = new CombinedData();

        data.setData(generateBarData());
        data.setData(generateLineData());

        chart.setData(data);
        chart.invalidate();

    }


    private class GetData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(Chart.this,
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

                heightsAL.add(tempHeight);
            }

        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        } catch (ParseException e) {
            e.printStackTrace();
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

                weightsAL.add(tempweight);
            }

        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private LineData generateLineData(){
        LineData obesity = new LineData();
        ArrayList<Entry> entries = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance ();
        Float dateWeight=0.0f;

        Float BMI = 0.0f, height = 0.0f;
        //BMI = weight(lbs) / (height(inches) )^2 * 703
        for (int index = 0; index < count; index++) {
            for (int i=0; i<heightsAL.size(); i++) {
                if (Integer.toString(heightsAL.get(i).getPetID()).equals(currentMember.getPetId())) {

                    cal.setTime(heightsAL.get(i).getDate());
                    if (index == cal.get(Calendar.MONTH)){
                        height = heightsAL.get(i).getpHeights();
                    }


                }

            }



            for (int i=0; i<chart_weights.size (); i++){
                cal2.setTime (chart_weights.get (i).getDate ());
                int month = cal2.get(Calendar.MONTH);
                if(month == index){
                    dateWeight = chart_weights.get(i).getWeight();
                    System.out.println("dateWeight = " + dateWeight);

                    BMI = (float)(Math.pow((dateWeight/(height*0.394)),2))*703;

                    System.out.println("index = " + BMI);
                    entries.add(new Entry(index, BMI));

                }
            }




        }

        // 몸무게에 따라서 현재 상태를 출력해주고 사료량을 계산해 주는 기능
        TextView textStatusNow = (TextView)findViewById(R.id.textStatusNow);
        TextView textFeedAmount = (TextView)findViewById(R.id.textFeedAmount);

        if (BMI<50){
            textStatusNow.setText(" 저체중입니다! 체중관리가 필요하겠네요");
            textFeedAmount.setText(" 권장되는 사료량은 "+ (((float)(chart_weights.get(chart_weights.size ()-1).getWeight ())*1000)*0.025+15)+"g입니다.");
        }else if (BMI>93){
            textStatusNow.setText(" 비만입니다! 체중관리가 필요하겠네요");
            textFeedAmount.setText(" 권장되는 사료량은 "+ (((float)(chart_weights.get(chart_weights.size ()-1).getWeight ())*1000)*0.025-15)+"g입니다.");
        }else{
            textStatusNow.setText(" 정상 체중입니다! 앞으로도 이렇게 유지해주세요!");
            textFeedAmount.setText(" 권장되는 사료량은 "+ (((float)(chart_weights.get(chart_weights.size ()-1).getWeight ())*1000)*0.025)+"g입니다.");
        }


        LineDataSet set = new LineDataSet(entries, "비만도");
        set.setColor(Color.rgb(		226, 125, 119
        ));
        set.setLineWidth(2.5f);
        set.setCircleColor(Color.rgb(		226, 125, 119
        ));
        set.setCircleRadius(5f);
        set.setFillColor(Color.rgb(		226, 125, 119
        ));
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(true);
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.rgb(88, 81, 73));

        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        obesity.addDataSet(set);

        return obesity;

    }

    private BarData generateBarData(){

        boolean taken = false;
        for (int index = 0; index < count; index++) {
            taken = false;
            for (int i=0; i<weightsAL.size(); i++) {
                if (Integer.toString(weightsAL.get(i).getIdPet()).equals(currentMember.getPetId())) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(weightsAL.get(i).getDate());
                    if (index == cal.get(Calendar.MONTH) && taken==false) {
                        taken = true;
                        weights.add(new BarEntry(index, weightsAL.get(i).getWeight()));
                        chart_weights.add(weightsAL.get(i));
                    }else if (index == cal.get(Calendar.MONTH) && taken==true){
                        //weights.add(new BarEntry(index, weightsAL.get(i).getWeight()));
                        weights.set (weights.size ()-1, new BarEntry(index, weightsAL.get(i).getWeight()));
                        chart_weights.set(chart_weights.size()-1,weightsAL.get (i));
                    }
                }
            }
        }

        BarDataSet set1 = new BarDataSet(weights, "몸무게");
        set1.setColor(Color.rgb(	178, 119, 226));
        set1.setValueTextColor(Color.rgb(88, 81, 73));
        set1.setValueTextSize(10f);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        BarData data = new BarData(dataSets);
        data.setValueTextSize(10f);
        //data.setValueTypeface(mTfLight);
        data.setBarWidth(0.8f);

        float barWidth = 0.1f; // x2 dataset
        BarData d = new BarData(set1);
        d.setBarWidth(barWidth);

        return data;

    }

}
