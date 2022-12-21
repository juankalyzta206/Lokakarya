package com.ogya.lokakarya.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ogya.lokakarya.repository.HistoryBankRepository;
import com.ogya.lokakarya.service.HistoryBankService;
import com.ogya.lokakarya.util.DataResponse;
import com.ogya.lokakarya.util.DataResponseList;
import com.ogya.lokakarya.wrapper.HistoryBankWrapper;

@RestController
@RequestMapping(value = "/historybank")
@CrossOrigin()
public class HistoryBankController {
	@Autowired
	HistoryBankRepository historyBankRepository;
	@Autowired
	HistoryBankService historyBankService;
	
	@GetMapping(path = "/getByIdPlan")
	public HistoryBankWrapper getByidHistoryBank(@RequestParam("id") Long idHistoryBank) {
		return historyBankService.getByidHistoryBank(idHistoryBank);
	}
	
	@GetMapping(path = "/getById")
	public DataResponse<HistoryBankWrapper> getByidTransaksiNasabah(@RequestParam("id") Long idHistoryBank) {
		return new DataResponse<HistoryBankWrapper>(historyBankService.getByidHistoryBank(idHistoryBank));
	}
	
	@GetMapping(path = "/findAllPlan")
	public List<HistoryBankWrapper> findAllPlan() {
		return historyBankService.findAll();
	}
	
	@GetMapping(path = "/findAll")
	public DataResponseList<HistoryBankWrapper> findAll() {
		return new DataResponseList<HistoryBankWrapper>(historyBankService.findAll());
	}
	
	@PostMapping(path = "/")
	public DataResponse<HistoryBankWrapper> save(@RequestBody HistoryBankWrapper wrapper){
		return new DataResponse<HistoryBankWrapper>(historyBankService.save(wrapper));
	}
	
	@PutMapping(path = "/")
	public DataResponse<HistoryBankWrapper> update(@RequestBody HistoryBankWrapper wrapper){
		return new DataResponse<HistoryBankWrapper>(historyBankService.save(wrapper));
	}
	
	@DeleteMapping(path = "/deleteById") 
	public String deleteId(@RequestParam("id")  Long norek) {
		historyBankService.delete(norek);
		return "History "+norek+" Berhasil di delete";
	}

}
