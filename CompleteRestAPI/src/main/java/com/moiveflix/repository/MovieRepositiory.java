package com.moiveflix.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moiveflix.entities.Movie;

public interface MovieRepositiory extends JpaRepository<Movie, Integer>{

}
