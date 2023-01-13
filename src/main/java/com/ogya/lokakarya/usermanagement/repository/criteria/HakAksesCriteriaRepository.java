package com.ogya.lokakarya.usermanagement.repository.criteria;

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

import com.ogya.lokakarya.usermanagement.entity.HakAkses;
import com.ogya.lokakarya.usermanagement.entity.Menu;
import com.ogya.lokakarya.usermanagement.entity.Roles;
import com.ogya.lokakarya.usermanagement.entity.SubMenu;
import com.ogya.lokakarya.usermanagement.entity.Users;
import com.ogya.lokakarya.util.FilterWrapper;
import com.ogya.lokakarya.util.PagingRequestWrapper;


@Repository
public class HakAksesCriteriaRepository {
	@Autowired
	private EntityManager entityManager;

	public List<HakAkses> findByFilter(PagingRequestWrapper request) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<HakAkses> criteriaQuery = cb.createQuery(HakAkses.class);
		Root<HakAkses> root = criteriaQuery.from(HakAkses.class);
		Join<HakAkses, Users> join = root.join("users", JoinType.INNER);
		Join<HakAkses, Roles> join2 = join.join("roles", JoinType.INNER);
		criteriaQuery.select(root);
		
		
		if (request.getSortField().equalsIgnoreCase("userId")) {
		    if(request.getSortOrder().equalsIgnoreCase("asc"))
		        criteriaQuery.orderBy(cb.asc(join2.get("userId")));
		    else
		        criteriaQuery.orderBy(cb.desc(join2.get("userId")));
		} else if (request.getSortField().equalsIgnoreCase("roleId")) {
			if(request.getSortOrder().equalsIgnoreCase("asc"))
		        criteriaQuery.orderBy(cb.asc(join2.get("roleId")));
		    else
		        criteriaQuery.orderBy(cb.desc(join2.get("roleId")));
		}
		else {
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
	    	 String value = (String) filter.getValue().toString().toLowerCase();
	    	 if (filter.getName().equalsIgnoreCase("userId")) {
	    		    predicatesList.add(cb.like(cb.lower(join2.get(filter.getName()).as(String.class)), "%"+value+"%"));
	    		} else if (filter.getName().equalsIgnoreCase("roleId")) {
	    		    predicatesList.add(cb.like(cb.lower(join2.get(filter.getName()).as(String.class)), "%"+value+"%"));
	    		} else {
	    		    predicatesList.add(cb.like(cb.lower(root.get(filter.getName()).as(String.class)), "%"+value+"%"));
	    		}
	    }
	    

	    Predicate[] finalPredicates = new Predicate[predicatesList.size()];
	    predicatesList.toArray(finalPredicates);
	    criteriaQuery.where(finalPredicates);
	
		List<HakAkses> result = entityManager.createQuery(criteriaQuery).getResultList();

		return result;
	}
	
	public Long countAll(PagingRequestWrapper request){ 	
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	    CriteriaQuery<Long> criteriaQuery = cb.createQuery(Long.class);
	    Root<HakAkses> root = criteriaQuery.from(HakAkses.class);
	    List<Predicate> predicatesList = new ArrayList<>();
	    Join<HakAkses, Users> join = root.join("users", JoinType.INNER);
		Join<HakAkses, Roles> join2 = join.join("roles", JoinType.INNER);
	    
	    @SuppressWarnings("rawtypes")
		List<FilterWrapper> filterList = request.getFilters();
	    for (@SuppressWarnings("rawtypes") FilterWrapper filter : filterList) {
	    	String value = (String) filter.getValue().toString().toLowerCase();
	    	if (filter.getName().equalsIgnoreCase("userId")) {
	    	    predicatesList.add(cb.like(cb.lower(join2.get(filter.getName()).as(String.class)), "%"+value+"%"));
	    	} else if (filter.getName().equalsIgnoreCase("roleId")) {
	    	    predicatesList.add(cb.like(cb.lower(join2.get(filter.getName()).as(String.class)), "%"+value+"%"));
	    	} else {
	    	    predicatesList.add(cb.like(cb.lower(root.get(filter.getName()).as(String.class)), "%"+value+"%"));
	    	}
	    }
	    
	    Predicate[] finalPredicates = new Predicate[predicatesList.size()];
	    predicatesList.toArray(finalPredicates);
	    criteriaQuery.select(cb.count(root));
	    criteriaQuery.where(finalPredicates);
	    
	    Long result = entityManager.createQuery(criteriaQuery).getSingleResult();
		return result;
	}
}

