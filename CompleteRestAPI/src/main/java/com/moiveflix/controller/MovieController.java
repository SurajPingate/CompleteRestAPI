package com.moiveflix.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moiveflix.dto.MovieDto;
import com.moiveflix.dto.MoviePageResponse;
import com.moiveflix.exceptions.EmptyFileException;
import com.moiveflix.service.MovieService;
import com.moiveflix.utils.AppConstants;

@RestController
@RequestMapping("/api/v1/movie")
public class MovieController {

	@Autowired
	private MovieService movieService;
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping("/add-movie")
	public ResponseEntity<MovieDto> addMovieHandler(@RequestPart MultipartFile file, 
			@RequestPart String movieDto) throws IOException{
		
		if(file.isEmpty()) {
			throw new EmptyFileException("File is empty, Please upload file");
		}
		
		MovieDto convertToMovieDto = convertToMovieDto(movieDto);
		return new ResponseEntity<>(movieService.addMovie(convertToMovieDto, file), HttpStatus.CREATED);
	}
	
	@GetMapping("/{movieId}")
	public ResponseEntity<MovieDto> getMovieById(@PathVariable Integer movieId) {
		return  ResponseEntity.ok(movieService.getMovieDetails(movieId));
	}
	
	@GetMapping("/all")
	public ResponseEntity<List<MovieDto>> getAllMovies(){
		return ResponseEntity.ok(movieService.getAllMovies());
	}
	
	@PutMapping("/update/{movieId}")
	public ResponseEntity<MovieDto> updateMovieById(@PathVariable Integer movieId, 
			@RequestPart MultipartFile file, 
			@RequestPart String movieDtoObj) throws IOException{
		if(file.isEmpty()) {
			file = null;
		}
		MovieDto convertToMovieDto = convertToMovieDto(movieDtoObj);
		return ResponseEntity.ok(movieService.updateMovie(movieId, convertToMovieDto, file));
		
	}
	
	@DeleteMapping("/delete/{movieId}")
	public ResponseEntity<String> deleteById(@PathVariable Integer movieId) throws IOException{
		return ResponseEntity.ok(movieService.deleteMovie(movieId));
	}
	
	@GetMapping("/allMoviesPage")
	public ResponseEntity<MoviePageResponse> getMovieswithPagination(
			@RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize
			){
		return ResponseEntity.ok(movieService.getAllMoviesWithPagination(pageNumber, pageSize));
	}
	
	
	@GetMapping("/allMoviesPageSort")
	public ResponseEntity<MoviePageResponse> getMovieswithPaginationAndSorting(
			@RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
			@RequestParam(defaultValue = AppConstants.SORT_DIRECTION, required = false) String direction
			){
		return ResponseEntity.ok(movieService.getAllMoviesWithPaginationAndSorting(pageNumber, pageSize, sortBy, direction));
	}
	
	/**
	 * Converts String to JSON
	 * @param movieDtoObj
	 * @return
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 */
	private MovieDto convertToMovieDto(String movieDtoObj) throws JsonMappingException, JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		MovieDto movieDto = objectMapper.readValue(movieDtoObj, MovieDto.class);
		
		return movieDto;
	}
}
