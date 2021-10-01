package com.greenrepack.greenrepackassos.ui.projects;

import android.content.Context;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.greenrepack.greenrepackassos.R;
import com.greenrepack.greenrepackassos.service.ApiBuilder;
import com.greenrepack.greenrepackassos.service.ApiResult;
import com.greenrepack.greenrepackassos.service.ResponseData;
import com.greenrepack.greenrepackassos.service.projects.ProjectApiCall;
import com.greenrepack.greenrepackassos.service.projects.Projet;
import com.greenrepack.greenrepackassos.service.projects.ProjetStatut;
import com.greenrepack.greenrepackassos.ui.projects.view.AjoutSuiviFragment;
import com.greenrepack.greenrepackassos.ui.projects.view.ProjetsFragment;
import com.greenrepack.greenrepackassos.ui.projects.view.ProjetsRecyclerViewAdapter;
import com.greenrepack.greenrepackassos.utils.AppContextKeys;
import com.greenrepack.greenrepackassos.utils.AppContextValue;
import com.greenrepack.greenrepackassos.utils.SessionStore;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2};
    private final Context mContext;
    private SessionStore sessionStore;
    ApiResult<ResponseData<List<Projet>>> projetsSuiviResult;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        if(position == 0) {
          return ProjetsFragment.newInstance();
        }else{
            return AjoutSuiviFragment.newInstance();
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return TAB_TITLES.length;
    }

    public void getProjectsEnAttenteAssosCallService() {
        try{
            ProjectApiCall apiCall = ApiBuilder.builder(mContext).create(ProjectApiCall.class);
            apiCall.getAllWithRna(sessionStore.get(AppContextKeys.RNA.name(), ""))
                    .enqueue(new Callback<ResponseData<List<Projet>>>() {
                        @Override
                        public void onResponse(Call<ResponseData<List<Projet>>> call, Response<ResponseData<List<Projet>>> response) {
                            if (response.isSuccessful()) {
                                projetsSuiviResult.setResult(response.body());
                            } else {
                                projetsSuiviResult.setHasError(true);
                                projetsSuiviResult.setMsgError(AppContextValue.SERVICE_ERROR_MSG);
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseData<List<Projet>>> call, Throwable t) {
                            projetsSuiviResult.setHasError(true);
                            projetsSuiviResult.setMsgError(AppContextValue.SERVICE_ERROR_MSG);
                            t.printStackTrace();
                        }
                    });
        }catch (Exception e){
            projetsSuiviResult.setHasError(true);
            projetsSuiviResult.setMsgError(AppContextValue.SERVICE_ERROR_MSG);
            e.printStackTrace();
        }
    }
}