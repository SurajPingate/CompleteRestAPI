package com.moiveflix.controller;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.moiveflix.service.FileService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/file")
public class FileController {

	@Autowired
	private FileService fileService;
	
	@Value("${project.poster}")
	private String path;
	
	@PostMapping("/upload")
	public ResponseEntity<String> uploadFileHandler(@RequestPart MultipartFile file) throws IOException{
		String uploadFileName = fileService.uploadFile(path, file);
		return ResponseEntity.ok("File Uploaded : " + uploadFileName);
		//return new ResponseEntity<String>(uploadFileName, HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/{fileName}")
	public void serveFileHandler(@PathVariable String fileName, HttpServletResponse httpServletResponse) throws IOException {
		InputStream resourceFile = fileService.getResourceFile(path, fileName);
		httpServletResponse.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(resourceFile, httpServletResponse.getOutputStream());
	}
	
	
}
