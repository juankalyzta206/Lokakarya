package com.ogya.lokakarya.usermanagement.repository.criteria;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ogya.lokakarya.usermanagement.entity.Roles;
import com.ogya.lokakarya.util.FilterWrapper;
import com.ogya.lokakarya.util.PagingRequestWrapper;


@Repository
public class RolesCriteriaRepository {
	@Autowired
	private EntityManager entityManager;

	public List<Roles> findByFilter(PagingRequestWrapper request) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Roles> criteriaQuery = cb.createQuery(Roles.class);
		Root<Roles> root = criteriaQuery.from(Roles.class);
		criteriaQuery.select(root);
		
		if(request.getSortOrder().equalsIgnoreCase("asc"))
		    criteriaQuery.orderBy(cb.asc(root.get(request.getSortField())));
		else
		    criteriaQuery.orderBy(cb.desc(root.get(request.getSortField())));
				
	    List<Predicate> predicatesList = new ArrayList<>();
	    
	    @SuppressWarnings("rawtypes")
    	List<FilterWrapper> filterList = request.getFilters();
	    for (@SuppressWarnings("rawtypes") FilterWrapper filter : filterList) {
	    	Predicate[] predicatesValue = new Predicate[filter.getValue().size()];
	    	for (int j=0; j<filter.getValue().size(); j++) {
	    		String value = (String) filter.getValue().get(j).toString().toLowerCase();
		    	predicatesValue[j] = cb.like(cb.lower(root.get(filter.getName()).as(String.class)), "%"+value+"%");
	    	}
	    	predicatesList.add(cb.or(predicatesValue));
	    }
	    

	    Predicate[] finalPredicates = new Predicate[predicatesList.size()];
	    predicatesList.toArray(finalPredicates);
	    criteriaQuery.where(finalPredicates);
	
		List<Roles> result = entityManager.createQuery(criteriaQuery).getResultList();

		return result;
	}
	
	public Long countAll(PagingRequestWrapper request){ 	
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	    CriteriaQuery<Long> criteriaQuery = cb.createQuery(Long.class);
	    Root<Roles> root = criteriaQuery.from(Roles.class);
	    List<Predicate> predicatesList = new ArrayList<>();
	    
	    
		@SuppressWarnings("rawtypes")
    	List<FilterWrapper> filterList = request.getFilters();
	    for (@SuppressWarnings("rawtypes") FilterWrapper filter : filterList) {
	    	Predicate[] predicatesValue = new Predicate[filter.getValue().size()];
	    	for (int j=0; j<filter.getValue().size(); j++) {
	    		String value = (String) filter.getValue().get(j).toString().toLowerCase();
		        predicatesValue[j] = cb.like(cb.lower(root.get(filter.getName()).as(String.class)), "%"+value+"%");
	    	}
	    	predicatesList.add(cb.or(predicatesValue));
	    }
  
	    Predicate[] finalPredicates = new Predicate[predicatesList.size()];
	    predicatesList.toArray(finalPredicates);
	    criteriaQuery.select(cb.count(root));
	    criteriaQuery.where(finalPredicates);
	    
	    Long result = entityManager.createQuery(criteriaQuery).getSingleResult();
		return result;
	}
}

