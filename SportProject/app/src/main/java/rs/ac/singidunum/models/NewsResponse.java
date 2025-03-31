package rs.ac.singidunum.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

// This class represents the response from a news API and uses Gson annotations for JSON serialization and deserialization
public class NewsResponse {

    // Fields representing various attributes of the news response
    @SerializedName("status")
    private String status;

    @SerializedName("totalResults")
    private int totalResults;

    @SerializedName("articles")
    private List<Article> articles;

    // Getter methods for accessing the fields
    public String getStatus() {
        return status;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public List<Article> getArticles() {
        return articles;
    }

    // Nested static class representing an individual article
    public static class Article {

        // Fields representing various attributes of an article
        @SerializedName("author")
        private String author;

        @SerializedName("title")
        private String title;

        @SerializedName("description")
        private String description;

        @SerializedName("url")
        private String url;

        @SerializedName("urlToImage")
        private String urlToImage;

        @SerializedName("publishedAt")
        private String publishedAt;

        // Getter methods for accessing the fields of the article
        public String getAuthor() {
            return author;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public String getUrl() {
            return url;
        }

        public String getUrlToImage() {
            return urlToImage;
        }

        public String getPublishedAt() {
            return publishedAt;
        }
    }
}
