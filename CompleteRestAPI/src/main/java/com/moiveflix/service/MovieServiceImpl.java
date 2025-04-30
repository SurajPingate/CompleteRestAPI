package com.moiveflix.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.moiveflix.dto.MovieDto;
import com.moiveflix.entities.Movie;
import com.moiveflix.exceptions.FileExistsException;
import com.moiveflix.exceptions.MovieNotFoundException;
import com.moiveflix.repository.MovieRepositiory;

@Service
public class MovieServiceImpl implements MovieService{
	
	@Autowired
	private MovieRepositiory movieRepositiory;
	
	@Autowired
	private FileService fileService;
	
	@Value("${project.poster}")
	private String path;
	
	@Value("${base.url}")
	private String baseUrl;

	@Override
	public MovieDto addMovie(MovieDto movieDto, MultipartFile file) throws IOException {
		// 1. Upload the file.
		if(Files.exists(Paths.get(path + File.separator + file.getOriginalFilename()))){
			throw new FileExistsException("File already exists! Please upload another file");
		}
		
		String uploadFileName = fileService.uploadFile(path, file);
		
		// 2. Set the value of filed poster as filename.
		movieDto.setPoster(uploadFileName);
		
		// 3. map dto to movie obj
		Movie movie = new Movie(null, 
				movieDto.getTitle(), 
				movieDto.getDirector(), 
				movieDto.getStudio(), 
				movieDto.getMovieCast(), 
				movieDto.getReleaseYear(), 
				movieDto.getPoster());
		
		// 4. save the movie obj.
		Movie savedMovie = movieRepositiory.save(movie);
		
		// 5. genereate the poster URL.
		String posterUrl = baseUrl + "/file/" + uploadFileName;
		
		// 6. map movie obj to dto obj and return.
		MovieDto response =  new MovieDto(savedMovie.getMovieId(),
				savedMovie.getTitle(),
				savedMovie.getDirector(),
				savedMovie.getStudio(),
				savedMovie.getMovieCast(),
				savedMovie.getReleaseYear(),
				savedMovie.getPoster(), 
				posterUrl
				);
		
		return response;
	}

	@Override
	public MovieDto getMovieDetails(Integer movieId) {
		// 1. check the data in the db and if exists fetch the data for given Id
		Movie movieFromDb = movieRepositiory.findById(movieId)
				.orElseThrow(() -> new MovieNotFoundException("Movie not found with id : " + movieId));
		
		// 2. Generate the posterurl
		String posterUrl = baseUrl + "/file/" + movieFromDb.getPoster();
		
		// 3. map to MovieDto obj and return
		MovieDto movieDto = new MovieDto(movieFromDb.getMovieId(), 
				movieFromDb.getTitle(), 
				movieFromDb.getDirector(), 
				movieFromDb.getStudio(), 
				movieFromDb.getMovieCast(), 
				movieFromDb.getReleaseYear(), 
				movieFromDb.getPoster(), 
				posterUrl);
		return movieDto;
	}

	@Override
	public List<MovieDto> getAllMovies() {
		// 1. fetch all data from db.
		List<Movie> allMoviesList = movieRepositiory.findAll();
		
		// 2. iterate through the list and generate posterurl for each movie and map it to MovieDto Obj.
		List<MovieDto> responseMovies = new ArrayList<>();
		for(Movie movie : allMoviesList) {
			String posterUrl = baseUrl + "/file/" + movie.getPoster();
			
			MovieDto movieDto = new MovieDto(movie.getMovieId(), 
					movie.getTitle(), 
					movie.getDirector(), 
					movie.getStudio(), 
					movie.getMovieCast(), 
					movie.getReleaseYear(), 
					movie.getPoster(), 
					posterUrl);
			responseMovies.add(movieDto);
		}
		return responseMovies;
	}

	@Override
	public MovieDto updateMovie(Integer movieId, MovieDto movieDto, MultipartFile file) 
			throws IOException {
		

		// 1. Check if the given movie id exists or not.
		Movie movie = movieRepositiory.findById(movieId)
				.orElseThrow(() -> new MovieNotFoundException("Movie not found with id : " + movieId));
		
		// 2. if file is null, do nothing but file is not null then delete existing file and upload new file.
		String uploadFileName = movie.getPoster();
		if(file != null) {
			Files.deleteIfExists(Paths.get(path + File.separator + uploadFileName));
			uploadFileName = fileService.uploadFile(path, file);
		}
		
		// 3. set movieDto's poster value according to step 2.
		if(file != null) {
			movieDto.setPoster(uploadFileName);
		}
		
		// 4. Map it to movie object and save the movie obj.
		Movie movieObj = new Movie(movie.getMovieId(), 
				movieDto.getTitle(), 
				movieDto.getDirector(), 
				movieDto.getStudio(), 
				movieDto.getMovieCast(), 
				movieDto.getReleaseYear(), 
				movieDto.getPoster());
		
		Movie savedMovie = movieRepositiory.save(movieObj);
		
		// 5. generate the posterUrl
		String posterUrl = baseUrl + "/file/" + savedMovie.getPoster();
		
		// 6. map to movieDto and return it.
		MovieDto response =  new MovieDto(savedMovie.getMovieId(),
				savedMovie.getTitle(),
				savedMovie.getDirector(),
				savedMovie.getStudio(),
				savedMovie.getMovieCast(),
				savedMovie.getReleaseYear(),
				savedMovie.getPoster(), 
				posterUrl
				);
		
		return response;
	}

	@Override
	public String deleteMovie(Integer movieId) throws IOException {
		// 1. Check if movie exists in db
		Movie movie = movieRepositiory.findById(movieId)
				.orElseThrow(() -> new MovieNotFoundException("Movie not found with id : " + movieId));
		
		// 2. delete the file associated with this object.
		Files.deleteIfExists(Paths.get(path + File.separator + movie.getPoster()));
		
		// 3. delete the object.
		movieRepositiory.delete(movie);
		return "Movie deleted with Id : " + movieId;
	}

}
