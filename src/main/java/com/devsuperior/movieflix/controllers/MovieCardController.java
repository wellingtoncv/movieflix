package com.devsuperior.movieflix.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.devsuperior.movieflix.dto.MovieCardDTO;
import com.devsuperior.movieflix.services.MovieCardService;

@RestController
@RequestMapping(value = "movies")
public class MovieCardController {

	@Autowired
	private MovieCardService service;

	@PreAuthorize("hasAnyRole('ROLE_VISITOR', 'ROLE_MEMBER')")
	@GetMapping(value = "/{id}")
	public ResponseEntity<MovieCardDTO> finById(@PathVariable Long id) {
		MovieCardDTO dto = service.findByid(id);
		return ResponseEntity.ok().body(dto);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_VISITOR', 'ROLE_MEMBER')")
	@GetMapping(value = "/{id}/reviews")
	public ResponseEntity<MovieCardDTO> findByidReviews(@PathVariable Long id) {
		MovieCardDTO dto = service.findByidReviews(id);
		return ResponseEntity.ok().body(dto);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_VISITOR', 'ROLE_MEMBER')")
	@GetMapping
	public ResponseEntity<Page<MovieCardDTO>> findAll(
			@RequestParam(value = "title", defaultValue = "") String title,
			@RequestParam(value = "genreId", defaultValue = "0") String genreId,
			
			Pageable pageable) {
		Page<MovieCardDTO> list = service.findAllPaged(title, genreId, pageable);
		return ResponseEntity.ok().body(list);
	}

}
