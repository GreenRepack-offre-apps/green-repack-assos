package com.greenrepack.greenrepackassos.ui.projects.form;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.greenrepack.greenrepackassos.R;
import com.greenrepack.greenrepackassos.databinding.FragmentAjoutSuiviBinding;
import com.greenrepack.greenrepackassos.service.projects.Projet;
import com.greenrepack.greenrepackassos.ui.projects.view.ProjetsRecyclerViewAdapter;

import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AjoutSuiviFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AjoutSuiviFragment extends Fragment {

    private String mParam1;
    private String mParam2;
    private FragmentAjoutSuiviBinding binding;
    public AjoutSuiviFragment() {
    }

    // TODO: Rename and change types and number of parameters
    public static AjoutSuiviFragment newInstance() {
        AjoutSuiviFragment fragment = new AjoutSuiviFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAjoutSuiviBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        TextView titreAjout = binding.addProj;
        EditText titreInput = binding.titre;
        EditText descriptionInput = binding.description;
        TextView titreSuivi = binding.suiviProj;

        Context context = root.getContext();
        RecyclerView recyclerView = binding.listSuivi;
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        List<Projet> projets = Arrays.asList(Projet.builder().titre("azertergtr").argentcollect(0.5)
                        .description("sdcfgvkuuuuuuuuuuuuuudczzecvjzepùcvzepjcvzeergvzzervefverv  bej").build(),
                Projet.builder().titre("projet zerteg").argentcollect(10.5)
                        .description("sdcfgvkuuuuuuuuuuuuuudczzecvjzepùcvzepjcvzeergvzzervefverv  bej").build(),
                Projet.builder().titre("un projet ayuda").argentcollect(0.0)
                        .description("sdcfgvkuuuuuuuuuuuuuudczzecvjzepùcvzepjcvzeergvzzervefverv  bej").build()
        );
        recyclerView.setAdapter(new ProjetsRecyclerViewAdapter(getContext(), projets));

        return root;
    }
}