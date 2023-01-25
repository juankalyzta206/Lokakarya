package com.ogya.lokakarya.telepon.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ogya.lokakarya.telepon.entity.HistoryTelkom;
import com.ogya.lokakarya.telepon.helper.LaporanPelunasanExcelExporter;
import com.ogya.lokakarya.telepon.helper.LaporanPenunggakanExcelExporter;
import com.ogya.lokakarya.telepon.repository.HistoryTelkomRepository;
import com.ogya.lokakarya.telepon.service.HistoryService;
import com.ogya.lokakarya.util.DataResponse;
import com.ogya.lokakarya.util.DataResponseList;
import com.ogya.lokakarya.util.DataResponsePagination;
import com.ogya.lokakarya.util.PagingRequestWrapper;
import com.ogya.lokakarya.wrapper.telepon.HistoryWrapper;
import com.ogya.lokakarya.wrapper.telepon.TransaksiTelkomWrapper;

@RestController
@RequestMapping(value = "/historytelkom")
@CrossOrigin(origins = "*")
public class HistoryTelkomController {
	@Autowired
	HistoryService historyService;
	@Autowired
	HistoryTelkomRepository historyTelkomRepository;

	@GetMapping(path = "/findAllPlan")
	public List<HistoryWrapper> findAllPlan() {
		return historyService.findAll();
	}

	@PostMapping(path = "/")
	public DataResponse<HistoryWrapper> save(@RequestBody HistoryWrapper wrapper) {
		return new DataResponse<HistoryWrapper>(historyService.save(wrapper));
	}

	@PutMapping(path = "/")
	public DataResponse<HistoryWrapper> update(@RequestBody HistoryWrapper wrapper) {
		return new DataResponse<HistoryWrapper>(historyService.save(wrapper));
	}

	@DeleteMapping(path = "/deleteById")
	public void delete(@RequestParam("id") Long historyId) {
		historyService.deleteById(historyId);
	}

	@GetMapping(path = "/sumAll")
	public Long sumAll() {
		return historyService.sumAll();
	}

	@GetMapping(path = "/findAllWithPagination")
	public DataResponsePagination<HistoryWrapper, HistoryTelkom> findAllWithPagination(@RequestParam("page") int page,
			@RequestParam("size") int size) {
		return new DataResponsePagination<HistoryWrapper, HistoryTelkom>(
				historyService.findAllWithPagination(page, size));
	}

	@RequestMapping(value = "/exportToPdfALL", method = RequestMethod.GET)
	public void exportToPdf(HttpServletResponse response) throws Exception {
		historyService.ExportToPdf(response);
	}
	
	@GetMapping(path = "/testData")
	public DataResponseList<HistoryWrapper> testData() {
		return new DataResponseList<HistoryWrapper>(historyService.testData());
	}
	@RequestMapping(value = "/findAllWithPaginationAndFilter", method = RequestMethod.POST)
	public DataResponsePagination<HistoryWrapper, HistoryTelkom> findAllWithPaginationAndFilter(@RequestBody(required = true) PagingRequestWrapper request) {
		return new DataResponsePagination<HistoryWrapper, HistoryTelkom>(historyService.ListWithPaging(request));
	}
	 @GetMapping("/download")
   public void exportToExcel(HttpServletResponse response) throws IOException {
       response.setContentType("application/octet-stream");
       DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
       String currentDateTime = dateFormatter.format(new Date());
        
       String headerKey = "Content-Disposition";
       String headerValue = "attachment; filename=users_" + currentDateTime + ".xlsx";
       response.setHeader(headerKey, headerValue);
        
       List<HistoryTelkom> listUsers = historyTelkomRepository.findAll();
       LaporanPelunasanExcelExporter excelExporter = new LaporanPelunasanExcelExporter(listUsers);
        
       excelExporter.export(response);    
   }
}
