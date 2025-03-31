package rs.ac.singidunum.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// Singleton class for creating and managing the Retrofit instance
public class RetrofitClient {

    // Singleton Retrofit instance
    private static Retrofit retrofit;

    // Base URL for the API
    private static final String BASE_URL = "https://newsapi.org/";

    // Method to get the Retrofit instance
    public static Retrofit getRetrofitInstance() {
        // Check if the Retrofit instance is null
        if (retrofit == null) {
            // Create a new Retrofit instance
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL) // Set the base URL for the API
                    .addConverterFactory(GsonConverterFactory.create()) // Add Gson converter for JSON deserialization
                    .build(); // Build the Retrofit instance
        }
        // Return the Retrofit instance
        return retrofit;
    }
}
