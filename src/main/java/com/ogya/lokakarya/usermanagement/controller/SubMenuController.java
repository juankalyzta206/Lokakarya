package com.ogya.lokakarya.usermanagement.controller;

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

import com.ogya.lokakarya.usermanagement.entity.SubMenu;
import com.ogya.lokakarya.usermanagement.service.SubMenuService;
import com.ogya.lokakarya.usermanagement.wrapper.SubMenuWrapper;
import com.ogya.lokakarya.util.DataResponse;
import com.ogya.lokakarya.util.DataResponseList;
import com.ogya.lokakarya.util.DataResponsePagination;
import com.ogya.lokakarya.util.PagingRequestWrapper;

@RestController
@RequestMapping(value = "/subMenu")
@CrossOrigin(origins = "*")
public class SubMenuController {
	@Autowired
	SubMenuService subMenuService;

	// findAllPagination
	@GetMapping(path = "/findAllWithPagination")
	public DataResponsePagination<SubMenuWrapper, SubMenu> findAllWithPagination(@RequestParam("page") int page,
			@RequestParam("size") int size) {
		return new DataResponsePagination<SubMenuWrapper, SubMenu>(subMenuService.findAllWithPagination(page, size));
	}

	@GetMapping(path = "/findAllPlan")
	public List<SubMenuWrapper> findAllPlan() {
		return subMenuService.findAll();
	}

	@GetMapping(path = "/findAll")
	public DataResponseList<SubMenuWrapper> findAll() {
		return new DataResponseList<SubMenuWrapper>(subMenuService.findAll());
	}

	@PostMapping(path = "/")
	public DataResponse<SubMenuWrapper> save(@RequestBody SubMenuWrapper wrapper) {
		return new DataResponse<SubMenuWrapper>(subMenuService.save(wrapper));
	}

	@PutMapping(path = "/")
	public DataResponse<SubMenuWrapper> update(@RequestBody SubMenuWrapper wrapper) {
		return new DataResponse<SubMenuWrapper>(subMenuService.save(wrapper));
	}

	@DeleteMapping(path = "/deleteById")
	public void deleteId(@RequestParam("id") Long menuId) {
		subMenuService.delete(menuId);
	}

	@RequestMapping(value = "/exportToPdfALL", method = RequestMethod.GET)
	public void exportToPdf(HttpServletResponse response) throws Exception {
		subMenuService.ExportToPdf(response);
	}

	@PostMapping(value = "/findAllWithPaginationAndFilter")
	public DataResponsePagination<SubMenuWrapper, SubMenu> findAllWithPaginationAndFilter(
			@RequestBody(required = true) PagingRequestWrapper request) {
		return new DataResponsePagination<SubMenuWrapper, SubMenu>(subMenuService.ListWithPaging(request));
	}

}
