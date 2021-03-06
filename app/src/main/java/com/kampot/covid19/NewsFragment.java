package com.kampot.covid19;



import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import androidx.navigation.NavController;

import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kampot.covid19.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.hbb20.CountryCodePicker;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NewsFragment extends Fragment {


    String apiKey = "12c0646cee1f428bbf6964a755f71afd";
    private RecyclerView recyclerViewOfNews;
    Adapter adapter;
    ArrayList<NewsModelClass> modelClassArrayList;

    String category = "health";

    private MainActivityViewModel viewModel;


    public String newCountry="tr";


    CountryCodePicker countryCodePicker;

    private AdView mAdView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news,null);

        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });



        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
               // Toast.makeText(getContext(), "Reklam y??klendi", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {


            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });

        viewModel = new ViewModelProvider(getActivity()).get(MainActivityViewModel.class);

        modelClassArrayList = new ArrayList<>();

        recyclerViewOfNews = view.findViewById(R.id.recyclerViewOfNews);

        recyclerViewOfNews.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new Adapter(getContext(),modelClassArrayList);

        recyclerViewOfNews.setAdapter(adapter);

        countryCodePicker = view.findViewById(R.id.ccpForNews);

        countryCodePicker.setAutoDetectedCountry(true);
        countryCodePicker.setCustomMasterCountries("ae,ar,at,au,be,bg,br" +
                ",ca,ch,cn,co,cu,cz,de,eg,fr,gb,gr,hk" +
                ",hu,id,ie,il,in,it,jp,kr,lt,lv,ma" +
                ",mx,my,ng,nl,no,nz,ph,pl,pt,ro," +
                "rs,ru,sa,se,sg,si,sk,th,tr,tw,ua,us,ve,za");

        String s = viewModel.getCountry().getValue().toLowerCase(Locale.ROOT);

        countryCodePicker.setDefaultCountryUsingNameCode(s);

        countryCodePicker.resetToDefaultCountry();



        viewModel.getCountry().observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {

                findNews(s,category,100,apiKey);

            }
        });


        countryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                newCountry = countryCodePicker.getSelectedCountryNameCode().toLowerCase(Locale.ROOT);

                viewModel.updateData(newCountry);


                viewModel.getCountry().observe(getActivity(), new Observer<String>() {
                    @Override
                    public void onChanged(String s) {

                        findNews(s,category,100,apiKey);

                    }
                });




                refreshCurrentFragment();
            }
        });




        return view;

    }
    public void refreshCurrentFragment(){

        NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().
                findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        int id = navController.getCurrentDestination().getId();
        navController.popBackStack(id,true);
        navController.navigate(id);
    }

    public void findNews(String country,String category, int pageSize,String apiKey) {

        NewsApiUtilities.getApiInterface().getCategoryNews(country,category,pageSize,apiKey).enqueue(new Callback<MainNews>() {
            @Override
            public void onResponse(Call<MainNews> call, Response<MainNews> response) {

                modelClassArrayList.addAll(response.body().getArticles());
                adapter.notifyDataSetChanged();


            }

            @Override
            public void onFailure(Call<MainNews> call, Throwable t) {

            }
        });



    }


}