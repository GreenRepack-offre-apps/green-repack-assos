package com.greenrepack.greenrepackassos.ui.projects.view;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.greenrepack.greenrepackassos.R;
import com.greenrepack.greenrepackassos.databinding.FragmentProjetBinding;
import com.greenrepack.greenrepackassos.service.ApiBuilder;
import com.greenrepack.greenrepackassos.service.ApiResult;
import com.greenrepack.greenrepackassos.service.ResponseData;
import com.greenrepack.greenrepackassos.service.projects.ProjectApiCall;
import com.greenrepack.greenrepackassos.service.projects.Projet;
import com.greenrepack.greenrepackassos.service.projects.ProjetStatut;
import com.greenrepack.greenrepackassos.utils.AppContextKeys;
import com.greenrepack.greenrepackassos.utils.AppContextValue;
import com.greenrepack.greenrepackassos.utils.SessionStore;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A fragment representing a list of Items.
 */
public class ProjetsFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private ApiResult<ResponseData<List<Projet>>> projetsResult;
    private SessionStore sessionStore;
    RecyclerView recyclerView;
    TextView emptyContent;

    public ProjetsFragment() {
    }

    @SuppressWarnings("unused")
    public static ProjetsFragment newInstance() {
        ProjetsFragment fragment = new ProjetsFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        projetsResult = new ApiResult<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.projet_item_list, container, false);
        sessionStore = new SessionStore(getContext());
        emptyContent = view.findViewById(R.id.empty_content);
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        Context context = view.getContext();
        getProjectsValidAssosCallService(getContext());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getProjectsValidAssosCallService(getContext());
    }

    public void getProjectsValidAssosCallService(Context context) {
        try{
            ProjectApiCall apiCall = ApiBuilder.builder(getContext()).create(ProjectApiCall.class);
            apiCall.getAllWithRna(sessionStore.get(AppContextKeys.RNA.name(), ""))
                    .enqueue(new Callback<ResponseData<List<Projet>>>() {
                        @Override
                        public void onResponse(Call<ResponseData<List<Projet>>> call, Response<ResponseData<List<Projet>>> response) {
                            if (response.isSuccessful()) {
                                projetsResult.setResult(response.body());
                                List<Projet> projetList = projetsResult.getResult().getData().stream()
                                        .filter(p -> p.getStatut().equals(ProjetStatut.VALIDER.name()))
                                        .collect(Collectors.toList());
                                if(!projetList.isEmpty()) {
                                    emptyContent.setVisibility(View.GONE);
//                                    recyclerView.setNestedScrollingEnabled(false);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                                    recyclerView.setAdapter(new ProjetsRecyclerViewAdapter(getContext(), projetList));
                                }else{
                                    emptyContent.setVisibility(View.VISIBLE);
                                }
                            } else {
                                projetsResult.setHasError(true);
                                projetsResult.setMsgError(AppContextValue.SERVICE_ERROR_MSG);
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseData<List<Projet>>> call, Throwable t) {
                            projetsResult.setHasError(true);
                            projetsResult.setMsgError(AppContextValue.SERVICE_ERROR_MSG);
                            t.printStackTrace();
                        }
                    });
        }catch (Exception e){
            projetsResult.setHasError(true);
            projetsResult.setMsgError(AppContextValue.SERVICE_ERROR_MSG);
            e.printStackTrace();
        }
    }
}