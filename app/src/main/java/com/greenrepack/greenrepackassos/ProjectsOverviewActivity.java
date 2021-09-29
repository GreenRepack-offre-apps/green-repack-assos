package com.greenrepack.greenrepackassos;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import com.greenrepack.greenrepackassos.service.ApiBuilder;
import com.greenrepack.greenrepackassos.service.ApiResult;
import com.greenrepack.greenrepackassos.service.ResponseData;
import com.greenrepack.greenrepackassos.service.assos.AssosApiCall;
import com.greenrepack.greenrepackassos.service.assos.AssosGet;
import com.greenrepack.greenrepackassos.service.assos.ReponseAssos;
import com.greenrepack.greenrepackassos.ui.login.LoginActivity;
import com.greenrepack.greenrepackassos.ui.projects.SectionsPagerAdapter;
import com.greenrepack.greenrepackassos.databinding.ActivityProjectsOverviewBinding;
import com.greenrepack.greenrepackassos.utils.AppContextKeys;
import com.greenrepack.greenrepackassos.utils.AppContextValue;
import com.greenrepack.greenrepackassos.utils.SessionStore;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProjectsOverviewActivity extends AppCompatActivity {

    private ActivityProjectsOverviewBinding binding;
    private SessionStore sessionStore;
    private ApiResult<ResponseData<AssosGet>> assosResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProjectsOverviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sessionStore = new SessionStore(getApplicationContext());
        assosResult = new ApiResult<>();
        if(sessionStore.get(AppContextKeys.RNA.name(), "") != null && sessionStore.get(AppContextKeys.RNA.name(), "") != "") {
            assosApiCallService();
        }else{
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = binding.fab;
        fab.setVisibility(View.INVISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, assosResult.isHasError()?assosResult.getMsgError():
                        assosResult.getResult() != null?assosResult.getResult().getStatus():"data not fetch",
                        Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });


    }
    void assosApiCallService(){
        try{
            AssosApiCall apiCall = ApiBuilder.builder(getApplicationContext()).create(AssosApiCall.class);
            apiCall.getAssos(sessionStore.get(AppContextKeys.RNA.name(), "")).enqueue(new Callback<ResponseData<AssosGet>>() {
                @Override
                public void onResponse(Call<ResponseData<AssosGet>> call, Response<ResponseData<AssosGet>> response) {
                    if(response.isSuccessful()) {
                        assosResult.setResult(response.body());
                    }
                }

                @Override
                public void onFailure(Call<ResponseData<AssosGet>> call, Throwable t) {
                    assosResult.setHasError(true);
                    assosResult.setMsgError(AppContextValue.SERVICE_ERROR_MSG);
                    t.printStackTrace();
                }
            });
        }catch (Exception e){
            assosResult.setHasError(true);
            assosResult.setMsgError(AppContextValue.SERVICE_ERROR_MSG);
            e.printStackTrace();
        }

    }

}