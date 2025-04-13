package com.example.moviecatalogue.controller;

import com.example.moviecatalogue.model.FavoriteMovie;
import com.example.moviecatalogue.model.Movie;
import com.example.moviecatalogue.repository.FavoriteMovieRepository;
import com.example.moviecatalogue.service.TmdbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class MovieController {

    @Autowired
    private TmdbService tmdbService;

    @Autowired
    private FavoriteMovieRepository favoriteMovieRepository;

    @GetMapping("/")
    public String showPopularMovies(Model model) {
        List<Movie> movies = tmdbService.getTrendingMovies();
        model.addAttribute("movies", movies);
        return "movies";
    }

    @GetMapping("/search")
    public String searchMovies(@RequestParam("query") String query, Model model) {
        List<Movie> movies = tmdbService.searchMovies(query);
        model.addAttribute("movies", movies);
        return "movies";
    }

    @GetMapping("/movie/{id}")
    public String movieDetails(@PathVariable Long id, Model model) {
        Movie movie = tmdbService.getMovieById(id);
        boolean isFavorite = favoriteMovieRepository.existsById(id);
        model.addAttribute("movie", movie);
        model.addAttribute("isFavorite", isFavorite);
        return "movie-details";
    }

    @PostMapping("/favorite/{id}")
    public String addFavorite(@PathVariable Long id) {
        Movie movie = tmdbService.getMovieById(id);
        FavoriteMovie favorite = new FavoriteMovie(movie.getId(), movie.getTitle(), movie.getPosterPath());
        favoriteMovieRepository.save(favorite);
        return "redirect:/movie/" + id;
    }

    @PostMapping("/unfavorite/{id}")
    public String removeFavorite(@PathVariable Long id) {
        favoriteMovieRepository.deleteById(id);
        return "redirect:/movie/" + id;
    }

    @GetMapping("/favorites")
    public String showFavorites(Model model) {
        List<FavoriteMovie> favorites = favoriteMovieRepository.findAll();
        model.addAttribute("favorites", favorites);
        return "favorites";
    }
}
