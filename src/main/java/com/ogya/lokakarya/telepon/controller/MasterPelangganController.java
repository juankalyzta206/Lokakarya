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

import com.ogya.lokakarya.telepon.entity.MasterPelanggan;
import com.ogya.lokakarya.telepon.helper.MasterPelangganExcelExporter;
import com.ogya.lokakarya.telepon.service.MasterPelangganService;
import com.ogya.lokakarya.util.DataResponse;
import com.ogya.lokakarya.util.DataResponsePagination;
import com.ogya.lokakarya.util.PagingRequestWrapper;
import com.ogya.lokakarya.wrapper.telepon.MasterPelangganWrapper;
import com.ogya.lokakarya.wrapper.telepon.TeleponPagingRequestWrapper;

@RestController
@RequestMapping(value = "/masterpelanggan")
@CrossOrigin(origins = "*")
public class MasterPelangganController {
	@Autowired
	MasterPelangganService masterPelangganService;
	
	
	@GetMapping(path = "/findAllPlan")
	public List<MasterPelangganWrapper> findAllPlan() {
		return masterPelangganService.findAll();
	}
	@GetMapping(path = "/findName")
	public MasterPelanggan findName(@RequestParam("nama") String nama) {
		return masterPelangganService.findByNama(nama);
	}
	@PostMapping(path = "/")
	public DataResponse<MasterPelangganWrapper> save(@RequestBody MasterPelangganWrapper wrapper) {
		return new DataResponse<MasterPelangganWrapper>(masterPelangganService.save(wrapper));
	}
	@PutMapping(path = "/")
	public DataResponse<MasterPelangganWrapper> update(@RequestBody MasterPelangganWrapper wrapper) {
		return new DataResponse<MasterPelangganWrapper>(masterPelangganService.save(wrapper));
	}
	@DeleteMapping(path = "/deleteById")
	public void delete(@RequestParam("id") Long masterPelangganId) {
		masterPelangganService.deleteById(masterPelangganId);
	}
	@GetMapping(path = "/findAllWithPagination")
	public DataResponsePagination<MasterPelangganWrapper, MasterPelanggan> findAllWithPagination(@RequestParam("page") int page,
			@RequestParam("size") int size) {
		return new DataResponsePagination<MasterPelangganWrapper, MasterPelanggan>(masterPelangganService.findAllWithPagination(page, size));
	}
//	@GetMapping(path = "/findAllWithPaginationFilter")
//	public DataResponsePagination<MasterPelangganWrapper, MasterPelanggan> findAllWithPagination(@RequestParam("page") int page,
//			@RequestParam("size") int size,@RequestParam("idPelanggan") Long idPelanggan) {
//		return new DataResponsePagination<MasterPelangganWrapper, MasterPelanggan>(masterPelangganService.findAllWithPagination(page, size,idPelanggan));
//	}
//	@GetMapping(path = "/findAllWithPaginationFilterGeneric")
//	public DataResponsePagination<MasterPelangganWrapper, MasterPelanggan> findAllWithPagination(@RequestParam("page") int page,
//			@RequestParam("size") int size,@RequestParam("filter") String filter, @RequestParam("Lvalue") Long Lvalue,@RequestParam("Svalue") String Svalue) {
//		if(filter.equals("idPelanggan") || filter.equals("userId") ) {
//			return new DataResponsePagination<MasterPelangganWrapper, MasterPelanggan>(masterPelangganService.findAllWithPagination(page, size,filter,Lvalue));
//		}
//		else if(filter.equals("nama") || filter.equals("noTelp") || filter.equals("alamat") ) {
//			return new DataResponsePagination<MasterPelangganWrapper, MasterPelanggan>(masterPelangganService.findAllWithPagination(page, size,filter,Svalue));
//		}
//		else {
//			throw new BusinessException("filter tidak ditemukan");
//		}
//	}
	
	@PostMapping(path = "/findAllWithPaginationFilter")
	public DataResponsePagination<MasterPelangganWrapper, MasterPelanggan> findAllWithPaginationAndFilter(@RequestBody TeleponPagingRequestWrapper wrapper) {
		return new DataResponsePagination<MasterPelangganWrapper, MasterPelanggan>(masterPelangganService.findAllWithPaginationFilter(wrapper));
	}
	@RequestMapping(value = "/findAllWithPaginationAndFilter", method = RequestMethod.POST)
	public DataResponsePagination<MasterPelangganWrapper, MasterPelanggan> findAllWithPaginationAndFilter(@RequestBody(required = true) PagingRequestWrapper request) {
		return new DataResponsePagination<MasterPelangganWrapper, MasterPelanggan>(masterPelangganService.ListWithPaging(request));
	}
    @GetMapping("/download")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
         
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);
         
        List<MasterPelangganWrapper> listUsers = masterPelangganService.findAll();
        MasterPelangganExcelExporter excelExporter = new MasterPelangganExcelExporter(listUsers);
         
        excelExporter.export(response);    
    }
    @RequestMapping(value = "/exportToPdfALL", method = RequestMethod.GET)
	public void exportToPdf(HttpServletResponse response) throws Exception {
		masterPelangganService.ExportToPdf(response);
	}
    
//	@GetMapping(path = "/findByname")
//	public List<MasterPelangganWrapper> findByName(@RequestParam("nama") String nama,@RequestParam("id") Long idPelanggan,
//			@RequestParam("alamat") String alamat,@RequestParam("noTelp") Long noTelp,@RequestParam("userId") Long userId) {
//		return masterPelangganService.findByNama(nama,idPelanggan,alamat,noTelp,userId);
//	}
    
    
}
