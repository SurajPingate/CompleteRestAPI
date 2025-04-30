package com.moiveflix.dto;

import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.NotBlank;

public class MovieDto {

	private Integer movieId;

	@NotBlank(message = "Please provide movies title!.")
	private String title;

	@NotBlank(message = "Please provide Director name!.")
	private String director;

	@NotBlank(message = "Please provide Studio Name title!.")
	private String studio;

	private Set<String> movieCast;

	@NotBlank(message = "Please provide Movie's release year!.")
	private Integer releaseYear;

	@NotBlank(message = "Please provide Movie's poster!.")
	private String poster;

	@NotBlank(message = "Please provide poster's URL!.")
	private String posterUrl;

	public MovieDto() {
		super();
	}

	public MovieDto(Integer movieId, @NotBlank(message = "Please provide movies title!.") String title,
			@NotBlank(message = "Please provide Director name!.") String director,
			@NotBlank(message = "Please provide Studio Name title!.") String studio, Set<String> movieCast,
			@NotBlank(message = "Please provide Movie's release year!.") Integer releaseYear,
			@NotBlank(message = "Please provide Movie's poster!.") String poster,
			@NotBlank(message = "Please provide poster's URL!.") String posterUrl) {
		super();
		this.movieId = movieId;
		this.title = title;
		this.director = director;
		this.studio = studio;
		this.movieCast = movieCast;
		this.releaseYear = releaseYear;
		this.poster = poster;
		this.posterUrl = posterUrl;
	}

	public Integer getMovieId() {
		return movieId;
	}

	public void setMovieId(Integer movieId) {
		this.movieId = movieId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String getStudio() {
		return studio;
	}

	public void setStudio(String studio) {
		this.studio = studio;
	}

	public Set<String> getMovieCast() {
		return movieCast;
	}

	public void setMovieCast(Set<String> movieCast) {
		this.movieCast = movieCast;
	}

	public Integer getReleaseYear() {
		return releaseYear;
	}

	public void setReleaseYear(Integer releaseYear) {
		this.releaseYear = releaseYear;
	}

	public String getPoster() {
		return poster;
	}

	public void setPoster(String poster) {
		this.poster = poster;
	}

	public String getPosterUrl() {
		return posterUrl;
	}

	public void setPosterUrl(String posterUrl) {
		this.posterUrl = posterUrl;
	}

	@Override
	public String toString() {
		return "MovieDto [movieId=" + movieId + ", title=" + title + ", director=" + director + ", studio=" + studio
				+ ", movieCast=" + movieCast + ", releaseYear=" + releaseYear + ", poster=" + poster + ", posterUrl="
				+ posterUrl + "]";
	}

	

}
