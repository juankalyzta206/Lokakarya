package com.ogya.lokakarya.telepon.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ogya.lokakarya.telepon.entity.MasterPelanggan;
import com.ogya.lokakarya.telepon.helper.MasterPelangganExcelExporter;
import com.ogya.lokakarya.telepon.service.MasterPelangganService;
import com.ogya.lokakarya.telepon.wrapper.MasterPelangganWrapper;
import com.ogya.lokakarya.util.DataResponse;
import com.ogya.lokakarya.util.DataResponsePagination;

@RestController
@RequestMapping(value = "/masterpelanggan")
@CrossOrigin(origins = "*")
public class MasterPelangganController {
	@Autowired
	MasterPelangganService masterPelangganService;
	@Autowired
	
	@GetMapping(path = "/findAllPlan")
	public List<MasterPelangganWrapper> findAllPlan() {
		return masterPelangganService.findAll();
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
//	@GetMapping("/download")
//	  public ResponseEntity<Resource> getFile() {
//	    String filename = "masterpelanggan.xlsx";
//	    InputStreamResource file = new InputStreamResource(masterPelangganService.load());
//
//	    return ResponseEntity.ok()
//	        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
//	        .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
//	        .body(file);
//	  }
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
}
