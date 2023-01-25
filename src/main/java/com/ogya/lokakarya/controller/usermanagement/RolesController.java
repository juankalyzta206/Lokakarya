package com.ogya.lokakarya.controller.usermanagement;

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

import com.ogya.lokakarya.entity.usermanagement.Roles;
import com.ogya.lokakarya.service.usermanagement.RolesService;
import com.ogya.lokakarya.util.DataResponse;
import com.ogya.lokakarya.util.DataResponseList;
import com.ogya.lokakarya.util.DataResponsePagination;
import com.ogya.lokakarya.util.PagingRequestWrapper;
import com.ogya.lokakarya.wrapper.usermanagement.RolesWrapper;

@RestController
@RequestMapping(value = "/roles")
@CrossOrigin(origins = "*")
public class RolesController {
	@Autowired
	RolesService rolesService;

	// findAllPagination
	@GetMapping(path = "/findAllWithPagination")
	public DataResponsePagination<RolesWrapper, Roles> findAllWithPagination(@RequestParam("page") int page,
			@RequestParam("size") int size) {
		return new DataResponsePagination<RolesWrapper, Roles>(rolesService.findAllWithPagination(page, size));
	}

	@GetMapping(path = "/findAllPlan")
	public List<RolesWrapper> findAllPlan() {
		return rolesService.findAll();
	}

	@GetMapping(path = "/findAll")
	public DataResponseList<RolesWrapper> findAll() {
		return new DataResponseList<RolesWrapper>(rolesService.findAll());
	}

	@PostMapping(path = "/")
	public DataResponse<RolesWrapper> save(@RequestBody RolesWrapper wrapper) {
		return new DataResponse<RolesWrapper>(rolesService.save(wrapper));
	}

	@PutMapping(path = "/")
	public DataResponse<RolesWrapper> update(@RequestBody RolesWrapper wrapper) {
		return new DataResponse<RolesWrapper>(rolesService.save(wrapper));
	}

	@DeleteMapping(path = "/deleteById")
	public void deleteId(@RequestParam("id") Long roleId) {
		rolesService.delete(roleId);
	}

	@RequestMapping(value = "/exportToPdfALL", method = RequestMethod.GET)
	public void exportToPdf(HttpServletResponse response) throws Exception {
		rolesService.ExportToPdf(response);
	}

	@PostMapping(value = "/findAllWithPaginationAndFilter")
	public DataResponsePagination<RolesWrapper, Roles> findAllWithPaginationAndFilter(
			@RequestBody(required = true) PagingRequestWrapper request) {
		return new DataResponsePagination<RolesWrapper, Roles>(rolesService.ListWithPaging(request));
	}

}
