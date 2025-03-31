package rs.ac.singidunum.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rs.ac.singidunum.models.NewsResponse;

// Interface for defining the API endpoints and parameters for Retrofit to call the news API
public interface NewsApiService {

    // Annotation for the HTTP GET request to the "v2/top-headlines" endpoint
    @GET("v2/top-headlines")
    Call<NewsResponse> getTopHeadlines(
            @Query("category") String category, // Query parameter for news category (e.g., sports, technology)
            @Query("apiKey") String apiKey, // Query parameter for the API key for authentication
            @Query("language") String language // Query parameter for the language of the news articles
    );
}
