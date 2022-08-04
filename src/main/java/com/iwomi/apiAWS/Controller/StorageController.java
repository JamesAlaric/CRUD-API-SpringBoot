package com.iwomi.apiAWS.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.iwomi.apiAWS.Service.StorageService;

@RestController
@RequestMapping("/file")
public class StorageController {

	@Autowired
	private StorageService service;
	
	// 1* Endpoint ./upload
	@PostMapping("/upload")
	public ResponseEntity<String> uploadFile(@RequestParam(value = "file") MultipartFile file){
		return new ResponseEntity<String>(service.uploadFile(file), HttpStatus.OK);
	}
	
	// 2* Endpoint ./download/{fileName}
	@GetMapping("/download/{fileName}")
	public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String fileName){
		byte[] data = service.downloadFile(fileName);
		ByteArrayResource resource = new ByteArrayResource(data);
		
		return ResponseEntity
				.ok()
				.contentLength(data.length)
				.header("content-type", "application: octet-Stream")
				.header("content-disposition","attachment; filename=\"" + fileName +"\"")
				.body(resource);
	}
	
	//3* Endpoint /delete/{fileName}
	@DeleteMapping("/delete/{fileName}")
	public ResponseEntity<String> deleteFile(@PathVariable String fileName){
		return new ResponseEntity<>(service.deleteFile(fileName), HttpStatus.OK);
	}
}
