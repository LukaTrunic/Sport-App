package rs.ac.singidunum.activities;

import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rs.ac.singidunum.R;
import rs.ac.singidunum.adapters.NewsAdapter;
import rs.ac.singidunum.models.NewsResponse;
import rs.ac.singidunum.network.NewsApiService;
import rs.ac.singidunum.network.RetrofitClient;

public class SportsNewsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NewsAdapter newsAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sports_news);

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Initialize RecyclerView and set its layout manager
        recyclerView = findViewById(R.id.recyclerViewNews);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize SwipeRefreshLayout and set its refresh listener
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this::fetchSportsNews);

        // Fetch sports news when the activity is created
        fetchSportsNews();
    }

    // Handle the navigation up (back) button in the toolbar
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    // Method to fetch sports news from the API
    private void fetchSportsNews() {
        // Show the refresh indicator
        swipeRefreshLayout.setRefreshing(true);

        // Create an instance of NewsApiService using Retrofit
        NewsApiService newsApiService = RetrofitClient.getRetrofitInstance().create(NewsApiService.class);
        Call<NewsResponse> call = newsApiService.getTopHeadlines("sports", "Your_Api_Key", "en");

        // Enqueue the call to make the network request
        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                // Hide the refresh indicator
                swipeRefreshLayout.setRefreshing(false);

                // Handle the API response
                if (response.isSuccessful() && response.body() != null) {
                    List<NewsResponse.Article> articles = response.body().getArticles();
                    if (newsAdapter == null) {
                        // If newsAdapter is null, initialize it with the articles and set it to the RecyclerView
                        newsAdapter = new NewsAdapter(articles);
                        recyclerView.setAdapter(newsAdapter);
                    } else {
                        // If newsAdapter is not null, update its data
                        newsAdapter.clear(); // Clear existing data
                        newsAdapter.addAll(articles); // Add new data
                        newsAdapter.notifyDataSetChanged();
                    }
                } else {
                    // Show a toast message if the response is not successful
                    Toast.makeText(SportsNewsActivity.this, "Failed to retrieve news", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                // Hide the refresh indicator
                swipeRefreshLayout.setRefreshing(false);
                // Show a toast message if the network request fails
                Toast.makeText(SportsNewsActivity.this, "An error occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
