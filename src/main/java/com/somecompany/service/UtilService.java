package com.somecompany.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * Utility services.
 * 
 * @author patrick
 */
@Service
@Slf4j
public class UtilService {

	/**
	 * Read a CSV file and return as a list of list of strings.
	 * 
	 * @param filePath
	 * @return List<List<String>>
	 */
	public List<List<String>> readCSV(String filePath) {

		List<List<String>> dataList = new ArrayList<>();

		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));

			String line;

			while ((line = bufferedReader.readLine()) != null) {
				String[] values = line.split(",");
				dataList.add(Arrays.asList(values));
			}

			bufferedReader.close();
		} catch (FileNotFoundException e) {
			log.error("Failed to read file {}", filePath);
		} catch (IOException e) {
			log.error("I/O error occurred with file {}", filePath);
		}

		return dataList;
	}

	/**
	 * Removes the content of a file.
	 * 
	 * @param filePath
	 */
	public void removeFileContent(String filePath) {
		try {
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath));
			bufferedWriter.write("");
			bufferedWriter.close();
		} catch (IOException e) {
			log.error("I/O error occurred with file {}", filePath);
		}
	}
}
