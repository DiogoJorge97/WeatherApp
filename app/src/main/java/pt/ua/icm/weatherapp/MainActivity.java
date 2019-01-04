package pt.ua.icm.weatherapp;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Models.DistritsData;
import Models.WeatherData;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MyTag MainActivity";

    private LinearLayout linearLayout;
    private Map<String, Integer> distritMap;
    private List<String> distritList;
    private WeatherViewModel weatherViewModel;
    private AutoCompleteTextView autoCompleteTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        distritMap = new HashMap<>();
        distritList = new ArrayList<>();

        final Button button = findViewById(R.id.button);

        linearLayout = findViewById(R.id.linear);


        weatherViewModel = ViewModelProviders.of(this).get(WeatherViewModel.class);
        weatherViewModel.getAllWeatherData(1010500).observe(this, this::updateUI);

        weatherViewModel.getAllDistritData().observe(this, this::saveDistritMap);





    }

    private void saveDistritMap(List<DistritsData> distritsDataList) {
        int localId;
        String localName;
        for (DistritsData distritsData: distritsDataList){
            localId = distritsData.getGlobalIdLocal();
            localName = distritsData.getLocal();
            distritMap.put(localName, localId);
            distritList.add(localName);
        }

        autoCompleteTextView = findViewById(R.id.auto_complete);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, distritList);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {
                Toast.makeText(MainActivity.this," Selected: " + adapter.getItem(pos), Toast.LENGTH_SHORT).show();
                weatherViewModel.getAllWeatherData(distritMap.get(adapter.getItem(pos))).observe(MainActivity.this, MainActivity.this:: updateUI);

            }
        });

        Log.d("MyTag", "Distrit data:" + distritMap.toString());
    }

    private void updateUI(List<WeatherData> weatherDataList) {
        boolean flag = false;

        if(linearLayout.getChildCount() > 0){
            linearLayout.removeAllViews();
        }

        for (WeatherData weatherData: weatherDataList){
            View view = getLayoutInflater().inflate(R.layout.item, null);
            TextView weatherType = view.findViewById(R.id.weather_type);
            TextView tMax = view.findViewById(R.id.t_max);
            TextView tMin = view.findViewById(R.id.t_min);
            TextView windSpeed = view.findViewById(R.id.wind_speed);
            TextView windDiretion = view.findViewById(R.id.wind_direction);
            TextView preipitationProb = view.findViewById(R.id.prec_prob);
            TextView date = view.findViewById(R.id.date);
            View divider = view.findViewById(R.id.divider);


            weatherType.setText(Integer.toString(weatherData.getIdWeatherType()));
            tMax.setText(weatherData.getTMax());
            tMin.setText(weatherData.getTMin());
            windSpeed.setText(Integer.toString(weatherData.getClassWindSpeed()));
            windDiretion.setText(weatherData.getPredWindDir());
            preipitationProb.setText(weatherData.getPrecipitaProb());
            date.setText(weatherData.getForecastDate());
            if (flag==false){
                flag=true;
                divider.setVisibility(View.GONE);
            }



            linearLayout.addView(view);
        }

    }
}
