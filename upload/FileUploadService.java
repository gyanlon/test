package com.eg.egsc.web.upload;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileUploadService {
	@Value("${fileupload.metaDataDirectory}")
	private String metaDataDirectory;

	@PostConstruct
	public void prepareDirectories() throws IOException {
		Files.createDirectories(Paths.get(metaDataDirectory));

        /*Fail the context booting if directories are not accessible*/
		if (!isDirectoryExist(metaDataDirectory)) {
			throw new IOException("Cannot access directories.");
		}
	}


	private boolean isDirectoryExist(String directory) {
		return Paths.get(directory).toFile().exists();
	}

	public void processFile(MultipartFile file) {

		//create metaDataFile
		File metaDataFile = new File(metaDataDirectory+"metaData.csv");

		//process the content of the file if each row is valid
		AtomicInteger rowNumber = new AtomicInteger();
		Map<String, String> requestData = new HashMap<>();
		List<Map<String,Object>> contentErrors = new ArrayList<>();
		
		String savePath = null;
    String fileFullName = null;
    try {
      SaveFile.saveFile(savePath, fileFullName, file);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
	}

	@PreDestroy
	public void deleteDirectories() throws IOException {
		Files.deleteIfExists(Paths.get(metaDataDirectory));
	}
}
