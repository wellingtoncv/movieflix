package com.devsuperior.movieflix.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.devsuperior.movieflix.entities.Genre; // Importar a entidade Genre
import com.devsuperior.movieflix.entities.Movie;

public interface MovieCardRepository extends JpaRepository<Movie, Long> {

    // JPQL para filtrar por Gênero (opcional) E Título (opcional)
    // O parâmetro :genreObject aceita NULL, o que permite buscar todos.
    @Query("SELECT obj FROM Movie obj WHERE (:genre IS NULL OR obj.genre = :genre) "
         + "AND (:title = '' OR LOWER(obj.title) LIKE LOWER(CONCAT('%', :title, '%'))) "
         + "ORDER BY obj.title") // Adicionar ordenação padrão
    Page<Movie> findByGenreAndTitle(Genre genre, String title, Pageable pageable);
    
}

