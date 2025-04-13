package com.example.moviecatalogue.service;

import com.example.moviecatalogue.model.Movie;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
@Service
public class TmdbService {

    @Value("${tmdb.api.key}")
    private String apiKey;

    @Value("${tmdb.api.base-url}")
    private String baseUrl;

    private final RestTemplate restTemplate;

    public TmdbService() {
        // Set connection and read timeouts to avoid app hanging
        var factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(1000); // 1 seconds
        factory.setReadTimeout(1000);    // 1 seconds
        this.restTemplate = new RestTemplate(factory);
    }

    @PostConstruct
    public void testConfig() {
        System.out.println(">>> TMDB API Key Loaded: " + (apiKey != null));
        System.out.println(">>> TMDB Base URL: " + baseUrl);
    }

    public List<Movie> getTrendingMovies() {
        String uri = UriComponentsBuilder
                .fromHttpUrl(baseUrl + "/movie/popular")
                .queryParam("api_key", apiKey)
                .toUriString();

        System.out.println(">>> Fetching trending movies from: " + uri);
        Map<String, Object> response = restTemplate.getForObject(uri, Map.class);
        System.out.println(">>> Response received");

        List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");
        return results.stream().map(this::mapToMovie).toList();
    }

    public Movie getMovieById(Long id) {
        String uri = UriComponentsBuilder
                .fromHttpUrl(baseUrl + "/movie/" + id)
                .queryParam("api_key", apiKey)
                .toUriString();

        System.out.println(">>> Fetching movie by ID from: " + uri);
        Map<String, Object> result = restTemplate.getForObject(uri, Map.class);
        return mapToMovie(result);
    }

    public List<Movie> searchMovies(String query) {
        String uri = UriComponentsBuilder
                .fromHttpUrl(baseUrl + "/search/movie")
                .queryParam("api_key", apiKey)
                .queryParam("query", query)
                .toUriString();

        System.out.println(">>> Searching movies with query: " + query);
        Map<String, Object> response = restTemplate.getForObject(uri, Map.class);
        List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");

        return results.stream().map(this::mapToMovie).toList();
    }

    private Movie mapToMovie(Map<String, Object> data) {
        Movie movie = new Movie();
        movie.setId(((Number) data.get("id")).longValue());
        movie.setTitle((String) data.get("title"));
        movie.setOverview((String) data.get("overview"));
        movie.setPosterPath((String) data.get("poster_path"));
        movie.setReleaseDate((String) data.get("release_date"));
        movie.setVoteAverage(data.get("vote_average") != null
                ? ((Number) data.get("vote_average")).doubleValue()
                : 0);
        return movie;
    }
}
