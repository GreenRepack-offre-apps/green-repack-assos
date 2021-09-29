package com.greenrepack.greenrepackassos.ui.projects.view;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.greenrepack.greenrepackassos.databinding.FragmentProjetBinding;
import com.greenrepack.greenrepackassos.service.projects.Projet;
import com.greenrepack.greenrepackassos.ui.projects.view.placeholder.PlaceholderContent.PlaceholderItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ProjetsRecyclerViewAdapter extends RecyclerView.Adapter<ProjetsRecyclerViewAdapter.ViewHolder> {

    //private final List<PlaceholderItem> mValues;
    private  Context mContext;
    private final List<Projet> projetsAssos;

    public ProjetsRecyclerViewAdapter(Context context, List<Projet> items) {
        projetsAssos = items;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentProjetBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.titre.setText(projetsAssos.get(position).getTitre());
        holder.description.setText(projetsAssos.get(position).getDescription());
        holder.argentRecolte.setText(projetsAssos.get(position).getArgentcollect() + " euros");
        holder.actionBtn.setOnClickListener(v -> {
           if( projetsAssos.get(position).getStatus() == "EN_ATTENTE") {

           }else if(projetsAssos.get(position).getStatus() == "REFUSER"){

           }else{

           }
        });
    }

    @Override
    public int getItemCount() {
        return projetsAssos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView titre;
        public final TextView description;
        public final TextView argentRecolte;
        public ImageButton actionBtn;

        public ViewHolder(FragmentProjetBinding binding) {
            super(binding.getRoot());

            titre = binding.titre;
            description = binding.description;
            argentRecolte = binding.argtCollt;
            actionBtn = binding.actionBtn;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + titre.getText() + "'";
        }
    }

    void removeProjectCallService(String idProject){

    }
}