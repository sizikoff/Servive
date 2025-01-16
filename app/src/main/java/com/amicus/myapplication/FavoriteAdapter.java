package com.amicus.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    private final List<FavoriteRecipe> favorites;

    public FavoriteAdapter(List<FavoriteRecipe> favorites) {
        this.favorites = favorites;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        FavoriteRecipe recipe = favorites.get(position);
        holder.nameTextView.setText(recipe.getName());
        Glide.with(holder.itemView.getContext()).load(recipe.getImageUrl()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }

    static class FavoriteViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        ImageView imageView;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.favoriteName);
            imageView = itemView.findViewById(R.id.favoriteImage);
        }
    }
}
