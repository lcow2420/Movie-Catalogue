package com.example.moviecatalogue.controller;

import com.example.moviecatalogue.model.FavoriteMovie;
import com.example.moviecatalogue.repository.FavoriteMovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites") // ✅ changed from "/favorites" to "/api/favorites"
public class FavoriteMovieController {

    @Autowired
    private FavoriteMovieRepository favoriteMovieRepository;

    @GetMapping
    public List<FavoriteMovie> getAllFavorites() {
        return favoriteMovieRepository.findAll();
    }

    @PostMapping
    public FavoriteMovie addFavorite(@RequestBody FavoriteMovie movie) {
        return favoriteMovieRepository.save(movie);
    }

    @DeleteMapping("/{id}")
    public void removeFavorite(@PathVariable Long id) {
        favoriteMovieRepository.deleteById(id);
    }
}
