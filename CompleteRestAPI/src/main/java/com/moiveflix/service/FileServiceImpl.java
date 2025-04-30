package com.moiveflix.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileServiceImpl implements FileService{

	@Override
	public String uploadFile(String path, MultipartFile file) throws IOException {
		// get filename
		String fileName = file.getOriginalFilename();
		
		//to get filePath
		String filePath = path  + File.separator + fileName;
		
		//create a file obj
		File f = new File(path);
		
		if(!f.exists()) {
			f.mkdir();
		}
		
		//copy the file or upload file to path
		Files.copy(file.getInputStream(), Paths.get(filePath));
		
		
		return fileName;
	}

	@Override
	public InputStream getResourceFile(String path, String fileName) throws FileNotFoundException {
		String filePath = path  + File.separator + fileName;
		return new FileInputStream(filePath);
	}

}
