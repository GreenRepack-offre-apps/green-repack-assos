package com.greenrepack.greenrepackassos.ui.projects.view;

import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.AdapterListUpdateCallback;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.greenrepack.greenrepackassos.R;
import com.greenrepack.greenrepackassos.data.Result;
import com.greenrepack.greenrepackassos.databinding.FragmentProjetBinding;
import com.greenrepack.greenrepackassos.service.ApiBuilder;
import com.greenrepack.greenrepackassos.service.ApiResult;
import com.greenrepack.greenrepackassos.service.ResponseData;
import com.greenrepack.greenrepackassos.service.Status;
import com.greenrepack.greenrepackassos.service.projects.ProjectApiCall;
import com.greenrepack.greenrepackassos.service.projects.Projet;
import com.greenrepack.greenrepackassos.service.projects.ProjetStatut;
import com.greenrepack.greenrepackassos.ui.projects.view.placeholder.PlaceholderContent.PlaceholderItem;
import com.greenrepack.greenrepackassos.utils.AppContextKeys;
import com.greenrepack.greenrepackassos.utils.AppContextValue;
import com.greenrepack.greenrepackassos.utils.SessionStore;

import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ProjetsRecyclerViewAdapter extends RecyclerView.Adapter<ProjetsRecyclerViewAdapter.ViewHolder> {

    //private final List<PlaceholderItem> mValues;
    Context mContext;
    ProjetsRecyclerViewAdapterListener mListener;
    private final List<Projet> projetsAssos;
    private SessionStore sessionStore;

    public ProjetsRecyclerViewAdapter(Context context, List<Projet> items, ProjetsRecyclerViewAdapterListener listener) {
        projetsAssos = items;
        mContext = context;
        mListener = listener;
    }

    public interface ProjetsRecyclerViewAdapterListener { // create an interface
        void onClickToRemove(int position, String idprojet); // create callback function
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        sessionStore = new SessionStore(mContext.getApplicationContext());
        return new ViewHolder(FragmentProjetBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.titre.setText(projetsAssos.get(position).getTitre());
        holder.description.setText(projetsAssos.get(position).getDescription());
        holder.argentRecolte.setText(projetsAssos.get(position).getArgentcollect() + " euros");
        Projet project = projetsAssos.get(position);
        if( project.getStatut().equals("EN_ATTENTE")) {
            holder.statutBtn.setBackgroundResource(R.mipmap.ic_waiting);
        }else if(project.getStatut().equals("REFUSER")) {
            holder.statutBtn.setBackgroundResource(R.mipmap.ic_canceled);
        }else if(project.getStatut().equals("VALIDER")) {
            holder.statutBtn.setBackgroundResource(R.mipmap.ic_verified);
        }
        holder.actionBtn.setOnClickListener(v -> {
            mListener.onClickToRemove(position, project.getIdproj());
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
        public ImageButton statutBtn;

        public ViewHolder(FragmentProjetBinding binding) {
            super(binding.getRoot());

            titre = binding.titre;
            description = binding.description;
            argentRecolte = binding.argtCollt;
            actionBtn = binding.actionBtn;
            statutBtn = binding.statutBtn;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + titre.getText() + "'";
        }
    }
}