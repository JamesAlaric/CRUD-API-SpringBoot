package com.iwomi.apiAWS.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StorageService {
	
	@Value("${application.bucket.name}")
	private String bucketname;
	
	@Autowired
	private AmazonS3 s3Client;
	
	//Charger un fichier(first Endpoint)
	public String uploadFile(MultipartFile file) {
		
		File fileObj = convertMultiPartFileToFile(file);
		String fileName = System.currentTimeMillis()+"_"+ file.getOriginalFilename();
		s3Client.putObject(new PutObjectRequest(bucketname, fileName, fileObj));
		
		fileObj.delete();
		return "File Uploaded! : " + fileName;
		
	}
	
	//Telecharger un fichier depuis le compartiment(bucket) aws-s3
	public byte[] downloadFile(String fileName) {
		
		S3Object s3Object= s3Client.getObject(bucketname,fileName);
		S3ObjectInputStream inputStream = s3Object.getObjectContent();
		
		try {
			byte[] content = IOUtils.toByteArray(inputStream);
			return content;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	//Supprimer un fichier
	public String deleteFile(String fileName) {
		s3Client.deleteObject(bucketname,fileName);
		return fileName + "  Removed ...";
	}
				
	
	// Creation de la methode convertMultiPartFile to file
	private File convertMultiPartFileToFile(MultipartFile file) {
		File convertedFile = new File(file.getOriginalFilename());
		try (FileOutputStream fos = new FileOutputStream(convertedFile)){
			fos.write(file.getBytes());	
		} catch(IOException e) {
			e.printStackTrace();
		}
		return convertedFile;
	}

}
