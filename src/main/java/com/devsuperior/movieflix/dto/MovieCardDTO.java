package com.devsuperior.movieflix.dto;

import java.util.HashSet;
import java.util.Set;

import com.devsuperior.movieflix.entities.Genre;
import com.devsuperior.movieflix.entities.Movie;

public class MovieCardDTO {

	private Long id;
	private String title;
	private String subTitle;
	private Integer year;
	private String imgUrl;
	private Set<GenreDTO> genres;
	
	

	public MovieCardDTO(Long id, String title, String subTitle, Integer year, String imgUrl) {
		this.id = id;
		this.title = title;
		this.subTitle = subTitle;
		this.year = year;
		this.imgUrl = imgUrl;
	}

	public MovieCardDTO(Movie entity) {
		id = entity.getId();
		title = entity.getTitle();
		subTitle = entity.getSubTitle();
		year = entity.getYear();
		imgUrl = entity.getImgUrl();

	}

	public MovieCardDTO(Movie entity, Set<Genre> genres) {
		this(entity);
        
        // ✅ CORREÇÃO: Inicializa o campo 'genres' antes de usá-lo
        this.genres = new HashSet<>(); 

		genres.forEach(cat -> this.genres.add(new GenreDTO(cat)));
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	
	public Set<GenreDTO> getGenres() {
        return genres;
	}
}
