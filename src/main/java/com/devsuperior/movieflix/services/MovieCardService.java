package com.devsuperior.movieflix.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.movieflix.dto.MovieCardDTO;
import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.projections.MovieProjection;
import com.devsuperior.movieflix.repositories.MovieCardRepository;

@Service
public class MovieCardService {

	@Autowired
	private MovieCardRepository repository;

	@Transactional(readOnly = true)
	public MovieCardDTO findByid(Long id) {
		Optional<Movie> obj = repository.findById(id);
		Movie entity = obj.get();
		return new MovieCardDTO(entity);
	}
	
	
	@Transactional(readOnly = true)
	public Page<MovieCardDTO> findAllPaged(String title, String genreId, Pageable pageable) {
		
		List<Long> genreIds = Arrays.asList();
		if (!"0".equals(genreId)) {
			genreIds = Arrays.asList(genreId.split(",")).stream().map(Long::parseLong).toList();
		}
		
		Page<MovieProjection> page = repository.searchMovies(genreIds, title, pageable);
		List<Long> movieIds = page.map(x -> x.getId()).toList();
		
		List<Movie> entities = repository.searchMoviesWithGenres(movieIds);
		
		List<MovieCardDTO> dtos = entities.stream().map(p -> new MovieCardDTO(p)).toList();
		
		Page<MovieCardDTO> pageDto = new PageImpl<>(dtos, page.getPageable(), page.getTotalElements());
		return pageDto;
		
	}	
		
}

