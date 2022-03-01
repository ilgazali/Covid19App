package com.example.covid19app;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.hbb20.CountryCodePicker;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Covid19Fragment extends Fragment implements AdapterView.OnItemSelectedListener {


    CountryCodePicker countryCodePicker;

    TextView mCases, mTodayCases, mDeaths ,mTodayDeaths, mRecovered, mTodayRecovered, mActive;
    TextView mFilter;

    String [] types = {"Cases","Deaths","Active","Recovered"};

    String country;

    Spinner spinner;

    private List<ModelClass> modelClassList;
    private List<ModelClass> modelClassListForRecyclerView;

    PieChart mPieChart;

    private RecyclerView recyclerView;

    CardAdapter cardAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_covid19, container, false);

        countryCodePicker = rootView.findViewById(R.id.ccp);

        mCases = rootView.findViewById(R.id.total_case);
        mTodayCases = rootView.findViewById(R.id.today_total_case);

        mDeaths = rootView.findViewById(R.id.death_case);
        mTodayDeaths = rootView.findViewById(R.id.today_death_case);

        mRecovered = rootView.findViewById(R.id.recovered_case);
        mTodayRecovered = rootView.findViewById(R.id.today_recovered_case);

        mActive = rootView.findViewById(R.id.active_case);

        mPieChart = rootView.findViewById(R.id.piechart);

        spinner = rootView.findViewById(R.id.spinner);

        recyclerView = rootView.findViewById(R.id.recycler_view);

        mFilter = rootView.findViewById(R.id.filter);

        reachData();

        return rootView;
    }


    public void reachData() {

        modelClassList = new ArrayList<>();
        modelClassListForRecyclerView = new ArrayList<>();

        spinner.setOnItemSelectedListener(this);
        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item,types);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(arrayAdapter);


        ApiUtilities.getApiInterface().getCountryData().enqueue(new Callback<List<ModelClass>>() {
            @Override
            public void onResponse(Call<List<ModelClass>> call, Response<List<ModelClass>> response) {

                modelClassListForRecyclerView.addAll(response.body());

                cardAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<ModelClass>> call, Throwable t) {

            }
        });

        cardAdapter = new CardAdapter(getContext(),modelClassListForRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(cardAdapter);

        countryCodePicker.setAutoDetectedCountry(true);
        countryCodePicker.setDefaultCountryUsingNameCode("TR");
        countryCodePicker.resetToDefaultCountry();
        country = countryCodePicker.getSelectedCountryName();
        fetchdata();

        countryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                country = countryCodePicker.getSelectedCountryName();
                fetchdata();
            }
        });

    }

    private void fetchdata() {

        ApiUtilities.getApiInterface().getCountryData().enqueue(new Callback<List<ModelClass>>() {
            @Override
            public void onResponse(Call<List<ModelClass>> call, Response<List<ModelClass>> response) {

                modelClassList.addAll(response.body());

                for (int i = 0; i < modelClassList.size(); i++){

                    if (modelClassList.get(i).getCountry().equals(country)){

                        mActive.setText(NumberFormat.getInstance().format(Integer.parseInt(modelClassList.get(i).getActive())));
                        mDeaths.setText(NumberFormat.getInstance().format(Integer.parseInt(modelClassList.get(i).getDeaths())));
                        mRecovered.setText(NumberFormat.getInstance().format(Integer.parseInt(modelClassList.get(i).getRecovered())));
                        mCases.setText(NumberFormat.getInstance().format(Integer.parseInt(modelClassList.get(i).getCases())));
                        mTodayCases.setText("+"+NumberFormat.getInstance().format(Integer.parseInt(modelClassList.get(i).getTodayCases())));
                        mTodayDeaths.setText("+"+NumberFormat.getInstance().format(Integer.parseInt(modelClassList.get(i).getTodayDeaths())));
                        mTodayRecovered.setText("+"+NumberFormat.getInstance().format(Integer.parseInt(modelClassList.get(i).getTodayRecovered())));


                        int active, recovered, deaths, total;
                        active = Integer.parseInt(modelClassList.get(i).getActive());
                        recovered = Integer.parseInt(modelClassList.get(i).getRecovered());
                        deaths = Integer.parseInt(modelClassList.get(i).getDeaths());
                        total = Integer.parseInt(modelClassList.get(i).getCases());

                        updateGraph(active, recovered, deaths, total);



                    }

                }

            }

            @Override
            public void onFailure(Call<List<ModelClass>> call, Throwable t) {

            }
        });




    }

    private void updateGraph(int active, int recovered, int deaths, int total) {

        mPieChart.clearChart();
        mPieChart.addPieSlice(new PieModel("Confirm",total, Color.parseColor("#FFB701")));
        mPieChart.addPieSlice(new PieModel("Active",active, Color.parseColor("#FF4CAF50")));
        mPieChart.addPieSlice(new PieModel("Recovered",recovered, Color.parseColor("#38ACCD")));
        mPieChart.addPieSlice(new PieModel("Deaths",deaths, Color.parseColor("#f55c47")));
        mPieChart.startAnimation();
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String item = types[position];
        mFilter.setText(item);
        cardAdapter.filter(item);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {



    }


}
