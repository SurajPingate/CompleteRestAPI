package com.moiveflix.dto;

import java.util.List;

public record MoviePageResponse(List<MovieDto> movieDtos,
		Integer pageNumber,
		Integer pageSize,
		Long totalElements,
		Integer totalPages,
		boolean isLast) {

}
