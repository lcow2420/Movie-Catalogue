package com.example.moviecatalogue.repository;

import com.example.moviecatalogue.model.FavoriteMovie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteMovieRepository extends JpaRepository<FavoriteMovie, Long> {
    // Spring Data JPA handles everything — no need to write queries
}
