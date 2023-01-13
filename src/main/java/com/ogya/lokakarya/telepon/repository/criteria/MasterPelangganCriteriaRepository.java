package com.ogya.lokakarya.telepon.repository.criteria;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ogya.lokakarya.telepon.entity.MasterPelanggan;
import com.ogya.lokakarya.usermanagement.entity.Users;
import com.ogya.lokakarya.util.FilterWrapper;
import com.ogya.lokakarya.util.PagingRequestWrapper;
@Repository
public class MasterPelangganCriteriaRepository {
	@Autowired
	private EntityManager entityManager;

	public List<MasterPelanggan> findByFilter(PagingRequestWrapper request) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<MasterPelanggan> criteriaQuery = cb.createQuery(MasterPelanggan.class);
		Root<MasterPelanggan> root = criteriaQuery.from(MasterPelanggan.class);
		Join<MasterPelanggan, Users> join = root.join("users", JoinType.INNER);
		if(request.getSortField().toLowerCase().equals("userid")) {
			if(request.getSortOrder().equalsIgnoreCase("asc")) {
				criteriaQuery.orderBy(cb.asc(join.get(request.getSortField())));}
			else {
				criteriaQuery.orderBy(cb.desc(join.get(request.getSortField())));}
		}
		else {
			if(request.getSortOrder().equalsIgnoreCase("asc")) {
				criteriaQuery.orderBy(cb.asc(root.get(request.getSortField())));}
			else {
				criteriaQuery.orderBy(cb.desc(root.get(request.getSortField())));}
		}
		
				
	    List<Predicate> predicatesList = new ArrayList<>();
	    
	    @SuppressWarnings("rawtypes")
		List<FilterWrapper> filterList = request.getFilters();
	    for (@SuppressWarnings("rawtypes") FilterWrapper filter : filterList) {
	    	String value = (String) filter.getValue().toString().toLowerCase();
	    	Join<MasterPelanggan,Users > join2 = root.join("users", JoinType.INNER);
	    	if(filter.getName().toLowerCase().equals("userid") ) {
	    		predicatesList.add(cb.like(cb.lower(join2.get(filter.getName()).as(String.class)), "%"+value+"%"));
	    	}
	    	else {
	    		predicatesList.add(cb.like(cb.lower(root.get(filter.getName()).as(String.class)), "%"+value+"%"));
	    	}
		}
	    
	    Predicate[] finalPredicates = new Predicate[predicatesList.size()];
	    predicatesList.toArray(finalPredicates);
	    criteriaQuery.where(finalPredicates);
	
		List<MasterPelanggan> result = entityManager.createQuery(criteriaQuery).getResultList();

		return result;
	}
	
	public Long countAll(PagingRequestWrapper request){ 	
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	    CriteriaQuery<Long> criteriaQuery = cb.createQuery(Long.class);
	    Root<MasterPelanggan> root = criteriaQuery.from(MasterPelanggan.class);
	    
	    List<Predicate> predicatesList = new ArrayList<>();
	    
	    @SuppressWarnings("rawtypes")
		List<FilterWrapper> filterList = request.getFilters();
	    for (@SuppressWarnings("rawtypes") FilterWrapper filter : filterList) {
	    	 predicatesList.add(cb.like(cb.lower(root.get(filter.getName())), "%"+ (filter.getValue().toString()).toLowerCase()+"%"));
		}
	    Predicate[] finalPredicates = new Predicate[predicatesList.size()];
	    predicatesList.toArray(finalPredicates);
	    criteriaQuery.select(cb.count(root));
	    criteriaQuery.where(finalPredicates);
	    
	    Long result = entityManager.createQuery(criteriaQuery).getSingleResult();
		return result;
	}
}
