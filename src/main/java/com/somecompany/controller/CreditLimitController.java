package com.somecompany.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.somecompany.error.ApiError;
import com.somecompany.service.CreditLimitService;
import com.somecompany.service.UtilService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

/**
 * API endpoints for services related to credit limit.
 * 
 * @author patrick
 */
@RestController
@RequestMapping("/api/creditLimit")
@Slf4j
public class CreditLimitController {

	@Autowired
	private CreditLimitService creditLimitService;

	@Autowired
	private UtilService utilService;

	@Value("${dataPath}")
	String dataPath;

	/**
	 * Generate credit limit report.
	 * 
	 * @return ResponseEntity<Object>
	 */
	@GetMapping(path = "/report", produces = "application/json")
	@ApiOperation(value = "Generate credit limit report.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully generated credit limit report.") })
	public ResponseEntity<Object> generateCreditLimitReport() {

		try {
			List<List<String>> dataList = utilService.readCSV(dataPath);
			creditLimitService.generateCreditLimitReport(dataList);

			return ResponseEntity.ok("{\"message\": \"Successfully generated credit limit report.\"}");
		} catch (Exception e) {
			return getErrorResponse(e);
		}
	}

	/**
	 * Create error response from exception messages.
	 * 
	 * @param exception
	 * @return A list of all error responses
	 */
	private ResponseEntity<Object> getErrorResponse(Exception exception) {
		List<String> errors = new ArrayList<String>();
		String error = "An error has occurred.";
		errors.add(error);

		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, exception.getLocalizedMessage(), errors);
		log.error("\n##################################################\n" + "Exception:\n"
				+ exception.getLocalizedMessage() + "\n##################################################");
		return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
	}
}
