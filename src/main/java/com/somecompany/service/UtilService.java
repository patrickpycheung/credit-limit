package com.somecompany.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

/**
 * Utility services.
 * 
 * @author patrick
 */
@Service
public class UtilService {

	/**
	 * Read a CSV file and return as a list of list of strings.
	 * 
	 * @param filePath
	 * @return List<List<String>>
	 * @throws IOException
	 */
	public List<List<String>> readCSV(String filePath) throws IOException {

		List<List<String>> dataList = new ArrayList<>();

		BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));

		String line;

		while ((line = bufferedReader.readLine()) != null) {
			String[] values = line.split(",");
			dataList.add(Arrays.asList(values));
		}

		bufferedReader.close();

		return dataList;
	}

	/**
	 * Removes the content of a file.
	 * 
	 * @param filePath
	 * @throws IOException
	 */
	public void removeFileContent(String filePath) throws IOException {

		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath));
		bufferedWriter.write("");
		bufferedWriter.close();
	}
}
