package com.somecompany.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CreditLimitService {

	@Value("${filePath}")
	String filePath;

	public List<List<String>> readCSV() {

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
			log.error("I/O error occurred with file %s", filePath);
		}

		return dataList;
	}
}
