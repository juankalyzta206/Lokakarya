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

import com.ogya.lokakarya.usermanagement.entity.Menu;
import com.ogya.lokakarya.usermanagement.entity.RoleMenu;
import com.ogya.lokakarya.usermanagement.entity.Roles;
import com.ogya.lokakarya.util.FilterWrapper;
import com.ogya.lokakarya.util.PagingRequestWrapper;


@Repository
public class RoleMenuCriteriaRepository {
	@Autowired
	private EntityManager entityManager;

	public List<RoleMenu> findByFilter(PagingRequestWrapper request) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<RoleMenu> criteriaQuery = cb.createQuery(RoleMenu.class);
		Root<RoleMenu> root = criteriaQuery.from(RoleMenu.class);
		Join<RoleMenu, Menu> join = root.join("menu", JoinType.INNER);
		Join<RoleMenu, Roles> join2 = root.join("roles", JoinType.INNER);
		criteriaQuery.select(root);
		
		
		if (request.getSortField().equalsIgnoreCase("menuId")) {
		    if(request.getSortOrder().equalsIgnoreCase("asc"))
		        criteriaQuery.orderBy(cb.asc(join.get("menuId")));
		    else
		        criteriaQuery.orderBy(cb.desc(join.get("userId")));
		} else if (request.getSortField().equalsIgnoreCase("roleId")) {
			if(request.getSortOrder().equalsIgnoreCase("asc"))
		        criteriaQuery.orderBy(cb.asc(join2.get("roleId")));
		    else
		        criteriaQuery.orderBy(cb.desc(join2.get("roleId")));
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
		    	} else if (filter.getName().equalsIgnoreCase("roleName")) {
		    		predicatesValue[j] = cb.like(cb.lower(join2.get("nama").as(String.class)), "%"+value+"%");
		    	} else if (filter.getName().equalsIgnoreCase("roleId")) {
		    		predicatesValue[j] = cb.like(cb.lower(join2.get(filter.getName()).as(String.class)), "%"+value+"%");
		    	} else {
		    		predicatesValue[j] = cb.like(cb.lower(root.get(filter.getName()).as(String.class)), "%"+value+"%");
		    	}
	    	}
	    	predicatesList.add(cb.or(predicatesValue));
	    }
	    

	    Predicate[] finalPredicates = new Predicate[predicatesList.size()];
	    predicatesList.toArray(finalPredicates);
	    criteriaQuery.where(finalPredicates);
	
		List<RoleMenu> result = entityManager.createQuery(criteriaQuery).getResultList();

		return result;
	}
	
	public Long countAll(PagingRequestWrapper request){ 	
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	    CriteriaQuery<Long> criteriaQuery = cb.createQuery(Long.class);
	    Root<RoleMenu> root = criteriaQuery.from(RoleMenu.class);
	    List<Predicate> predicatesList = new ArrayList<>();
	    Join<RoleMenu, Menu> join = root.join("menu", JoinType.INNER);
		Join<RoleMenu, Roles> join2 = root.join("roles", JoinType.INNER);
	    
	    
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
		    	} else if (filter.getName().equalsIgnoreCase("roleName")) {
		    		predicatesValue[j] = cb.like(cb.lower(join2.get("nama").as(String.class)), "%"+value+"%");
		    	} else if (filter.getName().equalsIgnoreCase("roleId")) {
		    		predicatesValue[j] = cb.like(cb.lower(join2.get(filter.getName()).as(String.class)), "%"+value+"%");
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

