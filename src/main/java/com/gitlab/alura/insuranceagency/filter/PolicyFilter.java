package com.gitlab.alura.insuranceagency.filter;

import com.gitlab.alura.insuranceagency.entity.Policy;
import com.gitlab.alura.insuranceagency.entity.User;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class PolicyFilter implements Specification<Policy> {
    private User user;
    private boolean isPolicy;
    private boolean isActive;

    private long id;

    @Override
    public Predicate toPredicate(Root<Policy> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(criteriaBuilder.equal(root.get("isActive"), isActive));

        if (isPolicy) {
            predicates.add(criteriaBuilder.isNotNull(root.get("manager")));
        } else {
            predicates.add(criteriaBuilder.isNull(root.get("manager")));
        }

        if (id != 0L){
            predicates.add(criteriaBuilder.equal(root.get("id"), id));
        }

        switch (user.getRole().getTitle()){
            case "CLIENT":
                predicates.add(criteriaBuilder.equal(root.get("client"), user));
                break;
            case "MANAGER":
                if (isPolicy) {
                    predicates.add(criteriaBuilder.equal(root.get("manager"), user));
                }
                break;
            case "COMPANY MANAGER":
                predicates.add(criteriaBuilder.isMember(user, root.get("offer")
                        .get("company")
                        .get("managers")));
                break;
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isPolicy() {
        return isPolicy;
    }

    public void setPolicy(boolean policy) {
        isPolicy = policy;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
