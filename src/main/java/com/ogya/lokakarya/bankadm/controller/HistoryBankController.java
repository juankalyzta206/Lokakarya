package com.ogya.lokakarya.bankadm.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
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

import com.ogya.lokakarya.bankadm.entity.HistoryBank;
import com.ogya.lokakarya.bankadm.repository.HistoryBankRepository;
import com.ogya.lokakarya.bankadm.service.HistoryBankService;
import com.ogya.lokakarya.bankadm.wrapper.HistoryBankWrapper;
import com.ogya.lokakarya.usermanagement.entity.Users;
import com.ogya.lokakarya.usermanagement.wrapper.UsersWrapper;
import com.ogya.lokakarya.util.DataResponse;
import com.ogya.lokakarya.util.DataResponseList;
import com.ogya.lokakarya.util.DataResponsePagination;
import com.ogya.lokakarya.util.PagingRequestWrapper;

@RestController
@RequestMapping(value = "/historybank")
@CrossOrigin(origins = "*")
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
	
	@GetMapping(path = "/findAllWithPagination")
	public DataResponsePagination<HistoryBankWrapper, HistoryBank> findAllWithPagination(@RequestParam("page") 
	int page, @RequestParam("size") int size) {
		return new DataResponsePagination<HistoryBankWrapper, HistoryBank>(historyBankService.findAllWithPagination(page, size));
	}
	
	
	@GetMapping(path = "/getByStatusKet")
	public DataResponseList<HistoryBankWrapper> getByStatusKetTransaksiNasabah(@RequestParam("statusKet") Byte statusKet) {
		return new DataResponseList<HistoryBankWrapper>(historyBankService.getBystatusKet(statusKet));
	}
	
	@GetMapping(path = "/findByStatusKetPagination")
	public DataResponsePagination<HistoryBankWrapper, HistoryBank> findByStatusKetPagination(@RequestParam("statusKet") Byte statusKet ,@RequestParam("page") 
	int page, @RequestParam("size") int size) {
		return new DataResponsePagination<HistoryBankWrapper, HistoryBank>(historyBankService.findByStatusKetPagination(statusKet, page, size));
	}
	
	@GetMapping(path = "/sumStatus1")
	public Long sumStatus1() {
		return historyBankService.sumStatus1();
	}
	
	@GetMapping(path = "/sumStatus2")
	public Long sumStatus2() {
		return historyBankService.sumStatus2();
	}
	
	@GetMapping(path = "/sumStatus3")
	public Long sumStatus3() {
		return historyBankService.sumStatus3();
	}
	
	@GetMapping(path = "/sumStatus4")
	public Long sumStatus4() {
		return historyBankService.sumStatus4();
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
	
	
    @RequestMapping(value = "/exportToPdfALL", method = RequestMethod.GET)
    public void exportToPdf(HttpServletResponse response) throws Exception {
        historyBankService.ExportToPdf(response);
       }

	
    @RequestMapping(value = "/exportToPdfALLSetor", method = RequestMethod.GET)
    public void exportToPdfsetor(HttpServletResponse response) throws Exception {
        historyBankService.ExportToPdfSetor(response);
       }
    
	
    @RequestMapping(value = "/exportToPdfALLTarik", method = RequestMethod.GET)
    public void exportToPdftarik(HttpServletResponse response) throws Exception {
        historyBankService.ExportToPdfTarik(response);
       }
    
	
    @RequestMapping(value = "/exportToPdfALLTransfer", method = RequestMethod.GET)
    public void exportToPdftransfer(HttpServletResponse response) throws Exception {
        historyBankService.ExportToPdfTransfer(response);
       }
    
	
    @RequestMapping(value = "/exportToPdfALLBayarTelepon", method = RequestMethod.GET)
    public void exportToPdfbayartelepon(HttpServletResponse response) throws Exception {
        historyBankService.ExportToPdfBayarTelepon(response);
       }
    
    

 // findAllPagination
 	@PostMapping(path = "/findAllWithPaginationAndFilter")
 	public DataResponsePagination<HistoryBankWrapper, HistoryBank> findAllWithPaginationAndFilter(@RequestBody PagingRequestWrapper wrapper) {
 		return new DataResponsePagination<HistoryBankWrapper, HistoryBank>(historyBankService.findAllWithPaginationAndFilter(wrapper));
 	}

}
