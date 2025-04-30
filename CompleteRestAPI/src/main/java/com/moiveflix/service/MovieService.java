package com.moiveflix.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.moiveflix.dto.MovieDto;
import com.moiveflix.dto.MoviePageResponse;

public interface MovieService {

	MovieDto addMovie(MovieDto movieDto, MultipartFile file) throws IOException;

	MovieDto getMovieDetails(Integer movieId);

	List<MovieDto> getAllMovies();

	MovieDto updateMovie(Integer movieId, MovieDto movieDto, MultipartFile file) throws IOException;

	String deleteMovie(Integer movieId) throws IOException;

	MoviePageResponse getAllMoviesWithPagination(Integer pageNumber, Integer pageSize);

	MoviePageResponse getAllMoviesWithPaginationAndSorting(Integer pageNumber, Integer pageSize, String sortBy, String direction);
}
