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
import com.ogya.lokakarya.telepon.entity.TransaksiTelkom;
import com.ogya.lokakarya.util.FilterWrapper;
import com.ogya.lokakarya.util.PagingRequestWrapper;
@Repository
public class TransaksiTelkomCriteriaRepository {
	@Autowired
	private EntityManager entityManager;
	
	public List<TransaksiTelkom> findByFilter(PagingRequestWrapper request) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<TransaksiTelkom> criteriaQuery = cb.createQuery(TransaksiTelkom.class);
		Root<TransaksiTelkom> root = criteriaQuery.from(TransaksiTelkom.class);
		Join<TransaksiTelkom, MasterPelanggan> join = root.join("idPelanggan", JoinType.INNER);
		if(request.getSortField().toLowerCase().equals("idpelanggan")) {
			if(request.getSortOrder().equalsIgnoreCase("asc")) {
				criteriaQuery.orderBy(cb.asc(join.get(request.getSortField())));}
			else {
				criteriaQuery.orderBy(cb.desc(join.get(request.getSortField())));}
		}
		else if(request.getSortField().toLowerCase().equals("nama")) {
			if(request.getSortOrder().equalsIgnoreCase("asc")) {
				criteriaQuery.orderBy(cb.asc(join.get("nama")));}
			else {
				criteriaQuery.orderBy(cb.desc(join.get("nama")));}
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
	    	Join<TransaksiTelkom,MasterPelanggan > join2 = root.join("idPelanggan", JoinType.INNER);
	    	if(filter.getName().toLowerCase().equals("idpelanggan") ) {
	    		predicatesList.add(cb.like(cb.lower(join2.get(filter.getName()).as(String.class)), "%"+value+"%"));
	    	}
	    	else if(filter.getName().toLowerCase().equals("nama")) {
	    		predicatesList.add(cb.like(cb.lower(join2.get("nama").as(String.class)), "%"+value+"%"));
	    	}
	    	else {
	    		predicatesList.add(cb.like(cb.lower(root.get(filter.getName()).as(String.class)), "%"+value+"%"));
	    	}
		}
	    
	    Predicate[] finalPredicates = new Predicate[predicatesList.size()];
	    predicatesList.toArray(finalPredicates);
	    criteriaQuery.where(finalPredicates);
	
		List<TransaksiTelkom> result = entityManager.createQuery(criteriaQuery).getResultList();

		return result;
	}
	
	public Long countAll(PagingRequestWrapper request){ 	
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	    CriteriaQuery<Long> criteriaQuery = cb.createQuery(Long.class);
	    Root<TransaksiTelkom> root = criteriaQuery.from(TransaksiTelkom.class);
	    
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
