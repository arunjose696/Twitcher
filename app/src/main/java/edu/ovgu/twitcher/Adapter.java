package edu.ovgu.twitcher;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private List<ListBirdsRecycler> birdList;

    public Adapter(List<ListBirdsRecycler>birdList) {
        this.birdList = birdList;
    }
    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.birds_card_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
        int img_resource = birdList.get(position).getImageView();
        String name = birdList.get(position).getBirdName();

        holder.setData(img_resource, name);
    }

    @Override
    public int getItemCount() {
        return birdList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img);
            textView = itemView.findViewById(R.id.name);
        }

        public void setData(int img_resource, String name) {
            imageView.setImageResource(img_resource);
            textView.setText(name);
        }
    }
}
