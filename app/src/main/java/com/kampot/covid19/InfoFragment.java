package com.kampot.covid19;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kampot.covid19.R;

public class InfoFragment extends Fragment {

    android.webkit.WebView webViewInfo;

    private String pdf = "https://www.coorstek.com/media/5155/hand-washing-poster-with-logo.pdf";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       View v = inflater.inflate(R.layout.fragment_info, container, false);

       webViewInfo = v.findViewById(R.id.webViewInfo);

      // webViewInfo.setWebViewClient(new WebViewClient());
        webViewInfo.getSettings().setJavaScriptEnabled(true);
        webViewInfo.loadUrl("https://drive.google.com/viewerng/viewer?embedded=true&url="+pdf);

        return v;
    }


}