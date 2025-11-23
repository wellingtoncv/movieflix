package com.devsuperior.movieflix.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.movieflix.dto.MovieCardDTO;
import com.devsuperior.movieflix.entities.Genre;
import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.repositories.GenreRepository;
import com.devsuperior.movieflix.repositories.MovieCardRepository;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;

@Service
public class MovieCardService {

	@Autowired
	private MovieCardRepository repository;
	
	@Autowired
    private MovieCardRepository movieRepository;
    
    // Supondo que você tenha um GenreRepository para buscar a entidade Genre
    @Autowired
    private GenreRepository genreRepository;

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
        
        // 1. Determina o Gênero para a consulta JPQL
        Genre genre = null;
        if (genreId != null && !genreId.equals("0")) {
            Long id = Long.parseLong(genreId);
            // Busca a entidade Genre completa, se o ID for fornecido
            genre = genreRepository.findById(id).orElse(null); 
        }

        // 2. Consulta o repositório usando o método JPQL simplificado
        // Se o título ou gênero for NULL/0, o JPQL ignora esse filtro.
        Page<Movie> page = movieRepository.findByGenreAndTitle(genre, title, pageable);
        
        // 3. Converte para DTO
        return page.map(x -> new MovieCardDTO(x));
    }
}
	

