package com.ogya.lokakarya.bankadm.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ogya.lokakarya.bankadm.entity.MasterBank;
import com.ogya.lokakarya.util.FilterWrapper;
import com.ogya.lokakarya.util.PagingRequestWrapper;
@Repository
public class MasterBankCriteriaRepository {
	@Autowired
	private EntityManager entityManager;

	public List<MasterBank> findByFilter(PagingRequestWrapper request) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<MasterBank> criteriaQuery = cb.createQuery(MasterBank.class);

		Root<MasterBank> root = criteriaQuery.from(MasterBank.class);

		if(request.getSortOrder().equalsIgnoreCase("asc"))
			criteriaQuery.orderBy(cb.asc(root.get(request.getSortField())));
		else
			criteriaQuery.orderBy(cb.desc(root.get(request.getSortField())));
				
	    
	    @SuppressWarnings("rawtypes")
    	List<FilterWrapper> filterList = request.getFilters();
	    for (@SuppressWarnings("rawtypes") FilterWrapper filter : filterList) {
	    	Predicate[] predicates = new Predicate[filter.getValue().size()];
	    	for (int j=0; j<filter.getValue().size(); j++) {
	    		String value = (String) filter.getValue().get(j).toString().toLowerCase();
		        predicates[j] = cb.like(cb.lower(root.get(filter.getName()).as(String.class)), "%"+value+"%");
	    	}
	    	criteriaQuery.where(cb.or(predicates));
	    }
	    
		List<MasterBank> result = entityManager.createQuery(criteriaQuery).getResultList();

		return result;
	}
	
	public Long countAll(PagingRequestWrapper request){ 	
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	    CriteriaQuery<Long> criteriaQuery = cb.createQuery(Long.class);
	    Root<MasterBank> root = criteriaQuery.from(MasterBank.class);
	    
	  
	    @SuppressWarnings("rawtypes")
    	List<FilterWrapper> filterList = request.getFilters();
	    for (@SuppressWarnings("rawtypes") FilterWrapper filter : filterList) {
	    	Predicate[] predicates = new Predicate[filter.getValue().size()];
	    	for (int j=0; j<filter.getValue().size(); j++) {
	    		String value = (String) filter.getValue().get(j).toString().toLowerCase();
		        predicates[j] = cb.like(cb.lower(root.get(filter.getName()).as(String.class)), "%"+value+"%");
	    	}
	    	criteriaQuery.where(cb.or(predicates));
	    }
	    
	    Long result = entityManager.createQuery(criteriaQuery).getSingleResult();
		return result;
	}
}
