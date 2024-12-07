package com.devsuperior.movieflix.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.projections.MovieProjection;

public interface MovieCardRepository extends JpaRepository<Movie, Long> {

	@Query(nativeQuery = true, value = """
			SELECT DISTINCT tb_movie.id, tb_movie.title
			FROM tb_movie
			INNER JOIN tb_genre ON tb_movie.id = tb_genre.id
			WHERE (:genreId IS NULL OR tb_movie.genre_id IN :genreId)
			AND LOWER (tb_movie.title) LIKE LOWER(CONCAT('%',:title,'%'))
			ORDER BY tb_movie.title
			""",countQuery = """
			SELECT COUNT(*) FROM (
			SELECT DISTINCT tb_movie.id, tb_movie.title
			FROM tb_movie
			INNER JOIN tb_genre ON tb_movie.id = tb_genre.id
			WHERE (:genreId IS NULL OR tb_movie.genre_id IN :genreId)
			AND LOWER (tb_movie.title) LIKE LOWER(CONCAT('%',:title,'%'))
			ORDER BY tb_movie.title
			) AS tb_result			
			""" )
	Page<MovieProjection> searchMovies(List<Long> genreId, String title,Pageable pageable);
	/*
	@Query("SELECT obj FROM Movie obj JOIN FETCH obj.genres WHERE obj.id IN:movieIds")
	List<Movie> searchMoviesWithGenres(List<Long> movieIds);
	*/
}
