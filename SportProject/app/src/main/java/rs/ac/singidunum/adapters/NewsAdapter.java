package rs.ac.singidunum.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

import rs.ac.singidunum.models.NewsResponse;
import rs.ac.singidunum.R;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private List<NewsResponse.Article> articles;  // List to hold news articles

    // Constructor to initialize adapter with articles list
    public NewsAdapter(List<NewsResponse.Article> articles) {
        this.articles = articles;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for individual news items
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        // Bind the article data to the ViewHolder
        NewsResponse.Article article = articles.get(position);
        holder.tvTitle.setText(article.getTitle());
        holder.tvDescription.setText(article.getDescription());
        // Load the article image using Glide library
        if (article.getUrlToImage() != null && !article.getUrlToImage().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(article.getUrlToImage())
                    .placeholder(R.drawable.ic_profile_placeholder)
                    .into(holder.ivThumbnail);
        } else {
            holder.ivThumbnail.setImageResource(R.drawable.ic_profile_placeholder);
        }
    }

    @Override
    public int getItemCount() {
        // Return the total number of articles
        return articles.size();
    }

    // Method to clear the current articles list and notify the adapter
    public void clear() {
        articles.clear();
        notifyDataSetChanged();
    }

    // Method to add new articles to the list and notify the adapter
    public void addAll(List<NewsResponse.Article> newArticles) {
        articles.addAll(newArticles);
        notifyDataSetChanged();
    }

    // ViewHolder class to hold and manage the views for each news item
    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;  // TextView to display the article title
        TextView tvDescription;  // TextView to display the article description
        ImageView ivThumbnail;  // ImageView to display the article image

        // Constructor to initialize the views
        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ivThumbnail = itemView.findViewById(R.id.ivThumbnail);
        }
    }
}
