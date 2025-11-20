package com.devsuperior.movieflix.services;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

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
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;

@Service
public class MovieCardService {

	@Autowired
	private MovieCardRepository repository;

	@Transactional(readOnly = true)
	public MovieCardDTO findByid(Long id) {
		Optional<Movie> obj = repository.findById(id);
		Movie entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		return new MovieCardDTO(entity,entity.getReviews());
	}
	
	@Transactional(readOnly = true)
	public MovieCardDTO findByidReviews(Long id) {
		Optional<Movie> obj = repository.findById(id);
		Movie entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		return new MovieCardDTO(entity, entity.getReviews());
	}
	
	@Transactional(readOnly = true)
	public Page<MovieCardDTO> findAllPaged(Pageable pageable) {
		Page<Movie> list = repository.findAll(pageable);
		return list.map(x -> new MovieCardDTO(x)); // utilizando a função de alta ordem e lambida
	}
	
	/*
	@Transactional(readOnly = true)
	public Page<MovieCardDTO> findAllPaged(String title, String genreId, Pageable pageable) {
		
		List<Long> genreIds = Arrays.asList();
		if (!"0".equals(genreId)) {
			genreIds = Arrays.asList(genreId.split(",")).stream().map(Long::parseLong).toList();
		}
		
		Page<MovieProjection> page = repository.searchMovies(genreIds, title, pageable);
		List<Long> movieIds = page.map(x -> x.getId()).toList();
		
		List<Movie> entities = repository.searchMoviesWithGenres(movieIds);
		entities = (List<Movie>) Utils.replace(page.getContent(), entities);
		
		List<MovieCardDTO> dtos = entities.stream().map(p -> new MovieCardDTO(p)).toList();
		
		Page<MovieCardDTO> pageDto = new PageImpl<>(dtos, page.getPageable(), page.getTotalElements());
		return pageDto;
		
	}	
	
	*/
	
	@Transactional(readOnly = true)
	public Page<MovieCardDTO> findAllPaged(String title, String genreId, Pageable pageable) {
	    
	    List<Long> genreIds = Arrays.asList();
	    if (!"0".equals(genreId)) {
	        genreIds = Arrays.asList(genreId.split(",")).stream().map(Long::parseLong).toList();
	    }
	    
	    Page<MovieProjection> page = repository.searchMovies(genreIds, title, pageable);
	    List<Long> movieIds = page.map(x -> x.getId()).toList();
	    
	    List<Movie> unorderedEntities = repository.searchMoviesWithGenres(movieIds);
	    
	    Map<Long, Movie> map = unorderedEntities.stream()
	    	    .collect(Collectors.toMap(Movie::getId, Function.identity()));
	    
	    List<Movie> orderedEntities = page.getContent().stream()
	    	    .map(projection -> map.get(projection.getId()))
	    	    .collect(Collectors.toList());
	    	    
	    List<MovieCardDTO> dtos = orderedEntities.stream().map(p -> new MovieCardDTO(p)).toList();
	    
	    Page<MovieCardDTO> pageDto = new PageImpl<>(dtos, page.getPageable(), page.getTotalElements());
	    return pageDto;
	}
	
	
}

