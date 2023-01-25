package com.ogya.lokakarya.repository.usermanagement.criteria;

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

import com.ogya.lokakarya.entity.usermanagement.Menu;
import com.ogya.lokakarya.entity.usermanagement.SubMenu;
import com.ogya.lokakarya.entity.usermanagement.Users;
import com.ogya.lokakarya.util.FilterWrapper;
import com.ogya.lokakarya.util.PagingRequestWrapper;


@Repository
public class SubMenuCriteriaRepository {
	@Autowired
	private EntityManager entityManager;

	public List<SubMenu> findByFilter(PagingRequestWrapper request) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<SubMenu> criteriaQuery = cb.createQuery(SubMenu.class);
		Root<SubMenu> root = criteriaQuery.from(SubMenu.class);
		Join<SubMenu, Menu> join = root.join("menu", JoinType.INNER);
		criteriaQuery.select(root);
		
		
		if (request.getSortField().equalsIgnoreCase("menuId")) {
		    if(request.getSortOrder().equalsIgnoreCase("asc"))
		        criteriaQuery.orderBy(cb.asc(join.get("menuId")));
		    else
		        criteriaQuery.orderBy(cb.desc(join.get("userId")));
		} 
		else {
		    if(request.getSortOrder().equalsIgnoreCase("asc"))
		        criteriaQuery.orderBy(cb.asc(root.get(request.getSortField())));
		    else
		        criteriaQuery.orderBy(cb.desc(root.get(request.getSortField())));
		}
				
	    List<Predicate> predicatesList = new ArrayList<>();
	    
	    @SuppressWarnings("rawtypes")
    	List<FilterWrapper> filterList = request.getFilters();
	    for (@SuppressWarnings("rawtypes") FilterWrapper filter : filterList) {
	    	Predicate[] predicatesValue = new Predicate[filter.getValue().size()];
	    	for (int j=0; j<filter.getValue().size(); j++) {
	    		String value = (String) filter.getValue().get(j).toString().toLowerCase();
	    		if ((filter.getName().equalsIgnoreCase("menuId"))) {
	    			predicatesValue[j] = cb.like(cb.lower(join.get(filter.getName()).as(String.class)), "%"+value+"%");
		    	} else if (filter.getName().equalsIgnoreCase("menuName")) {
		    		predicatesValue[j] = cb.like(cb.lower(join.get("nama").as(String.class)), "%"+value+"%");
		    	}  else {
		    		predicatesValue[j] = cb.like(cb.lower(root.get(filter.getName()).as(String.class)), "%"+value+"%");
		    	}
	    	}
	    	predicatesList.add(cb.or(predicatesValue));
	    }
	    

	    Predicate[] finalPredicates = new Predicate[predicatesList.size()];
	    predicatesList.toArray(finalPredicates);
	    criteriaQuery.where(finalPredicates);
	
		List<SubMenu> result = entityManager.createQuery(criteriaQuery).getResultList();

		return result;
	}
	
	public Long countAll(PagingRequestWrapper request){ 	
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	    CriteriaQuery<Long> criteriaQuery = cb.createQuery(Long.class);
	    Root<SubMenu> root = criteriaQuery.from(SubMenu.class);
	    List<Predicate> predicatesList = new ArrayList<>();
	    Join<SubMenu, Users> join = root.join("users", JoinType.INNER);
	    
	    
		@SuppressWarnings("rawtypes")
    	List<FilterWrapper> filterList = request.getFilters();
	    for (@SuppressWarnings("rawtypes") FilterWrapper filter : filterList) {
	    	Predicate[] predicatesValue = new Predicate[filter.getValue().size()];
	    	for (int j=0; j<filter.getValue().size(); j++) {
	    		String value = (String) filter.getValue().get(j).toString().toLowerCase();
	    		if ((filter.getName().equalsIgnoreCase("menuId"))) {
	    			predicatesValue[j] = cb.like(cb.lower(join.get(filter.getName()).as(String.class)), "%"+value+"%");
		    	} else if (filter.getName().equalsIgnoreCase("menuName")) {
		    		predicatesValue[j] = cb.like(cb.lower(join.get("nama").as(String.class)), "%"+value+"%");
		    	} else {
		    		predicatesValue[j] = cb.like(cb.lower(root.get(filter.getName()).as(String.class)), "%"+value+"%");
		    	}
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

