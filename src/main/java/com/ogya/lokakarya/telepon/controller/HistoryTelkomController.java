package com.ogya.lokakarya.telepon.controller;

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
import com.ogya.lokakarya.telepon.service.HistoryService;
import com.ogya.lokakarya.telepon.wrapper.HistoryWrapper;
import com.ogya.lokakarya.util.DataResponse;
import com.ogya.lokakarya.util.DataResponsePagination;

@RestController
@RequestMapping(value = "/historytelkom")
@CrossOrigin(origins = "*")
public class HistoryTelkomController {
	@Autowired
	HistoryService historyService;

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
}
