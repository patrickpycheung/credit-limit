package com.somecompany.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.somecompany.service.CreditLimitService;
import com.somecompany.service.UtilService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * API endpoints for services related to credit limit.
 * 
 * @author patrick
 */
@RestController
@RequestMapping("/api/creditLimit")
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
	@GetMapping(path = "/report", produces = "text/plain")
	@ApiOperation(value = "Generate credit limit report.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully generated credit limit report.") })
	public ResponseEntity<Object> generateCreditLimitReport() {
		List<List<String>> dataList = utilService.readCSV(dataPath);

		creditLimitService.generateCreditLimitReport(dataList);

		return ResponseEntity.ok("Successfully generated credit limit report.");
	}
}
