package com.greenrepack.greenrepackassos.ui.projects.view;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.greenrepack.greenrepackassos.databinding.FragmentAjoutSuiviBinding;
import com.greenrepack.greenrepackassos.service.ApiBuilder;
import com.greenrepack.greenrepackassos.service.ApiResult;
import com.greenrepack.greenrepackassos.service.ResponseData;
import com.greenrepack.greenrepackassos.service.Status;
import com.greenrepack.greenrepackassos.service.assos.AssosApiCall;
import com.greenrepack.greenrepackassos.service.projects.ProjectApiCall;
import com.greenrepack.greenrepackassos.service.projects.Projet;
import com.greenrepack.greenrepackassos.service.projects.ProjetStatut;
import com.greenrepack.greenrepackassos.utils.AppContextKeys;
import com.greenrepack.greenrepackassos.utils.AppContextValue;
import com.greenrepack.greenrepackassos.utils.SessionStore;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AjoutSuiviFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AjoutSuiviFragment extends Fragment implements ProjetsRecyclerViewAdapter.ProjetsRecyclerViewAdapterListener {

    private String mParam1;
    private String mParam2;
    private FragmentAjoutSuiviBinding binding;
    private SessionStore sessionStore;
    private ApiResult<ResponseData<List<Projet>>> projetsSuiviResult;
    private ApiResult<Status> projetsFormResult;

    private ApiResult<Status> deleteResult;
    RecyclerView recyclerView;
    TextView emptyContent;
    EditText titreInput;
    EditText descriptionInput;

    public AjoutSuiviFragment() {
    }

    // TODO: Rename and change types and number of parameters
    public static AjoutSuiviFragment newInstance() {
        AjoutSuiviFragment fragment = new AjoutSuiviFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        projetsSuiviResult = new ApiResult<>();
        projetsFormResult = new ApiResult<>();
        deleteResult = new ApiResult<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        sessionStore = new SessionStore(getContext());
        binding = FragmentAjoutSuiviBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        TextView titreAjoutText = binding.addProj;
        titreInput = binding.titre;
        descriptionInput = binding.description;
        Button ajoutBtn = binding.add;
        TextView titreSuiviText = binding.suiviProj;
        emptyContent = binding.emptyContent;

        Context context = root.getContext();
        recyclerView = binding.listSuivi;
        getProjectsEnAttenteAssosCallService(getContext());
        ajoutBtn.setOnClickListener(v -> {
            if(titreInput.getText().toString() != "" &&  titreInput.getText().toString() != "") {
                addProjectsAssosCallService(Projet.builder().refassos(sessionStore.get(AppContextKeys.ID_ASSOS.name(), ""))
                        .titre(titreInput.getText().toString())
                        .description(descriptionInput.getText().toString()).build());
            }
        });
        return root;
    }
    @Override
    public void onResume() {
        super.onResume();
        getProjectsEnAttenteAssosCallService(getContext());
    }

    private void buildAdapter(Context context) {
        List<Projet> projetList = projetsSuiviResult.getResult().getData().stream()
                .filter(p -> !p.getStatut().equals(ProjetStatut.VALIDER.name()))
                .collect(Collectors.toList());
        if(!projetList.isEmpty()){
            emptyContent.setVisibility(View.GONE);
            //recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new ProjetsRecyclerViewAdapter(getContext(), projetList, this));
        }else{
            emptyContent.setVisibility(View.VISIBLE);
        }
    }

    public void getProjectsEnAttenteAssosCallService(Context context) {
        try{
            ProjectApiCall apiCall = ApiBuilder.builder(getContext()).create(ProjectApiCall.class);
            apiCall.getAllWithRna(sessionStore.get(AppContextKeys.RNA.name(), ""))
                    .enqueue(new Callback<ResponseData<List<Projet>>>() {
                        @Override
                        public void onResponse(Call<ResponseData<List<Projet>>> call, Response<ResponseData<List<Projet>>> response) {
                            if (response.isSuccessful()) {
                                projetsSuiviResult.setResult(response.body());
                                buildAdapter(context);

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

    public void addProjectsAssosCallService(Projet projet) {
        try{
            ProjectApiCall apiCall = ApiBuilder.builder(getContext()).create(ProjectApiCall.class);
            apiCall.create(projet)
                    .enqueue(new Callback<Status>() {
                        @Override
                        public void onResponse(Call<Status> call, Response<Status> response) {
                            if (response.isSuccessful()) {
                                projetsFormResult.setResult(response.body());
                                if(projetsFormResult.getResult().getStatus().equals("SUCCES")) {
                                    //clean
                                    titreInput.setText("");
                                    descriptionInput.setText("");
                                    // refresh
                                    getProjectsEnAttenteAssosCallService(getContext());
                                }
                            } else {
                                projetsFormResult.setHasError(true);
                                projetsFormResult.setMsgError(AppContextValue.SERVICE_ERROR_MSG);
                            }
                        }

                        @Override
                        public void onFailure(Call<Status> call, Throwable t) {
                            projetsFormResult.setHasError(true);
                            projetsFormResult.setMsgError(AppContextValue.SERVICE_ERROR_MSG);
                            t.printStackTrace();
                        }
                    });
        }catch (Exception e){
            projetsFormResult.setHasError(true);
            projetsFormResult.setMsgError(AppContextValue.SERVICE_ERROR_MSG);
            e.printStackTrace();
        }
    }

    void removeProjectCallService(String idProject){
        try{
            ProjectApiCall apiCall = ApiBuilder.builder(getContext()).create(ProjectApiCall.class);
            apiCall.removeOne(idProject)
                    .enqueue(new Callback<Status>() {
                        @Override
                        public void onResponse(Call<Status> call, Response<Status> response) {
                            if (response.isSuccessful()) {
                                deleteResult.setResult(response.body());
                                if(deleteResult.getResult().equals("SUCCES")) {
//                                    Projet myPrj = projetsAssos.stream().filter(p -> idProject.equals(p.getIdproj())).findAny().get();
//                                    projetsAssos.remove(myPrj);
                                    getProjectsEnAttenteAssosCallService(getContext());
                                }
                            } else {
                                deleteResult.setHasError(true);
                                deleteResult.setMsgError(AppContextValue.SERVICE_ERROR_MSG);
                            }
                        }

                        @Override
                        public void onFailure(Call<Status> call, Throwable t) {
                            deleteResult.setHasError(true);
                            deleteResult.setMsgError(AppContextValue.SERVICE_ERROR_MSG);
                            t.printStackTrace();
                        }
                    });
        }catch (Exception e){
            deleteResult.setHasError(true);
            deleteResult.setMsgError(AppContextValue.SERVICE_ERROR_MSG);
            e.printStackTrace();
        }
    }

    @Override
    public void onClickToRemove(int position, String idprojet) {
        removeProjectCallService(idprojet);
    }
}