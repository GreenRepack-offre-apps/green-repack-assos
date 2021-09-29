package com.greenrepack.greenrepackassos.ui.projects.view;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.greenrepack.greenrepackassos.R;
import com.greenrepack.greenrepackassos.databinding.FragmentProjetBinding;
import com.greenrepack.greenrepackassos.service.projects.Projet;

import java.util.Arrays;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class ProjetsFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private FragmentProjetBinding binding;
    public ProjetsFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ProjetsFragment newInstance() {
        ProjetsFragment fragment = new ProjetsFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.projet_item_list, container, false);

        // Set the adapter
        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
            //recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        List<Projet> projets = Arrays.asList(Projet.builder().titre("azertergtr").argentcollect(0.5)
                            .description("sdcfgvkuuuuuuuuuuuuuudczzecvjzepùcvzepjcvzeergvzzervefverv  bej").build(),
                    Projet.builder().titre("projet zerteg").argentcollect(10.5)
                            .description("sdcfgvkuuuuuuuuuuuuuudczzecvjzepùcvzepjcvzeergvzzervefverv  bej").build(),
                    Projet.builder().titre("un projet ayuda").argentcollect(0.0)
                            .description("sdcfgvkuuuuuuuuuuuuuudczzecvjzepùcvzepjcvzeergvzzervefverv  bej").build()
                    );
        recyclerView.setAdapter(new ProjetsRecyclerViewAdapter(getContext(), projets));

        return view;
    }
}