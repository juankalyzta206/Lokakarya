package com.ogya.lokakarya.bankadm.repository;

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

import com.ogya.lokakarya.bankadm.entity.HistoryBank;
import com.ogya.lokakarya.bankadm.entity.MasterBank;
import com.ogya.lokakarya.util.FilterWrapper;
import com.ogya.lokakarya.util.PagingRequestWrapper;

@Repository
public class HistoryBankCriteriaRepository {
	@Autowired
	private EntityManager entityManager;

	public List<HistoryBank> findByFilter(PagingRequestWrapper request) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<HistoryBank> criteriaQuery = cb.createQuery(HistoryBank.class);

		Root<HistoryBank> root = criteriaQuery.from(HistoryBank.class);
		if (request.getSortField().equalsIgnoreCase("norek")) {
		    Join<HistoryBank, MasterBank> join = root.join("rekening", JoinType.INNER);
		    if(request.getSortOrder().equalsIgnoreCase("asc"))
		        criteriaQuery.orderBy(cb.asc(join.get("norek")));
		    else
		        criteriaQuery.orderBy(cb.desc(join.get("norek")));
		} else {
		    // sort by other field without join
		    if(request.getSortOrder().equalsIgnoreCase("asc"))
		        criteriaQuery.orderBy(cb.asc(root.get(request.getSortField())));
		    else
		        criteriaQuery.orderBy(cb.desc(root.get(request.getSortField())));
		}

	    List<Predicate> predicatesList = new ArrayList<>();
	    
	    @SuppressWarnings("rawtypes")
		List<FilterWrapper> filterList = request.getFilters();
	    for (@SuppressWarnings("rawtypes") FilterWrapper filter : filterList) {
	    	 String value2 = (String) filter.getValue().toString().toLowerCase();
	    	 if (filter.getName().equalsIgnoreCase("norek")) {
	    		    Join<HistoryBank, MasterBank> join2 = root.join("rekening", JoinType.INNER);
	    		    predicatesList.add(cb.like(cb.lower(join2.get(filter.getName()).as(String.class)), "%"+value2+"%"));
	    		} else {
	    		    // add other predicates without join
	    		    predicatesList.add(cb.like(cb.lower(root.get(filter.getName()).as(String.class)), "%"+value2+"%"));
	    		}
	    }
	    
	    Predicate[] predicates = new Predicate[filterList.size()];
	    int i = 0;
	    for (FilterWrapper filter : filterList) {
	        String value = (String) filter.getValue().toString().toLowerCase();
	        predicates[i] = cb.like(cb.lower(root.get(filter.getName()).as(String.class)), "%"+value+"%");
	        i++;
	    }
	    criteriaQuery.where(cb.or(predicates));
		List<HistoryBank> result = entityManager.createQuery(criteriaQuery).getResultList();

		return result;
	}
	
	public Long countAll(PagingRequestWrapper request){ 	
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	    CriteriaQuery<Long> criteriaQuery = cb.createQuery(Long.class);
	    Root<HistoryBank> root = criteriaQuery.from(HistoryBank.class);
	    
	    List<Predicate> predicatesList = new ArrayList<>();
	    
	    @SuppressWarnings("rawtypes")
		List<FilterWrapper> filterList = request.getFilters();
	    for (@SuppressWarnings("rawtypes") FilterWrapper filter : filterList) {
	    	String value = (String) filter.getValue().toString().toLowerCase();
	    	  
	    	if (filter.getName().equalsIgnoreCase("norek")) {
	    	    Join<HistoryBank, MasterBank> join = root.join("rekening", JoinType.INNER);
	    	    predicatesList.add(cb.like(cb.lower(join.get(filter.getName()).as(String.class)), "%"+value+"%"));
	    	} else {
	    	    // add other predicates without join
	    	    predicatesList.add(cb.like(cb.lower(root.get(filter.getName()).as(String.class)), "%"+value+"%"));
	    	}
	    	
	    	
		}
	    Predicate[] predicates = new Predicate[filterList.size()];
	    int i = 0;
	    for (FilterWrapper filter : filterList) {
	        String value = (String) filter.getValue().toString().toLowerCase();
	        predicates[i] = cb.like(cb.lower(root.get(filter.getName()).as(String.class)), "%"+value+"%");
	        i++;
	    }
	    criteriaQuery.where(cb.or(predicates));
	    Long result = entityManager.createQuery(criteriaQuery).getSingleResult();
		return result;
	}
}
