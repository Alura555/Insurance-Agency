package com.example.insuranceagency.mapper;

import com.example.insuranceagency.dto.PolicyDto;
import com.example.insuranceagency.entity.Document;
import com.example.insuranceagency.entity.DocumentType;
import com.example.insuranceagency.entity.Policy;
import com.example.insuranceagency.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.Instant;
import java.util.*;

@Mapper
public interface PolicyMapper {
    @Mapping(source = "client", target = "client", qualifiedByName = "fullName")
    @Mapping(source = "manager", target = "manager", qualifiedByName = "fullName")
    @Mapping(target = "offer", source = "offer.title")
    @Mapping(target = "insuranceType", source = "offer.insuranceType.title")
    @Mapping(target = "expiredDate", expression = "java(calculateExpiredDate(policy.getOffer().getPeriodInMonths(), policy.getStartDate()))", dateFormat = "yyyy-MM-dd")
    @Mapping(target = "documents", expression = "java(mapDocuments(policy.getOffer().getDocuments(), policy.getDocuments()))")
    @Mapping(target = "status", expression = "java(getStatus(policy.isApproved(), policy.getStartDate(), policy.getManager(), policyDto.getDocuments(), policyDto.getExpiredDate()))")
    PolicyDto toPolicyDto(Policy policy);

    @Named("fullName")
    default String mapClientToFullName(User user) {
        return user != null ? user.getName() + " " + user.getSecondName() : null;
    }

    default Date calculateExpiredDate(int periodInMonths, Date startDate) {
        if (startDate == null){
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        cal.add(Calendar.MONTH, periodInMonths);
        return cal.getTime();
    }

    default Map<DocumentType, Document> mapDocuments(Set<DocumentType> offerDocuments,
                                                     Set<Document> policyDocuments){
        Map<DocumentType, Document> documents = new HashMap<>();
        policyDocuments.forEach(x -> documents.put(x.getDocumentType(), x));
        offerDocuments.forEach(x -> documents.putIfAbsent(x, null));
        return documents;
    }

    default String getStatus(Boolean isApproved, Date startDate, User manager, Map<DocumentType, Document> documents, Date expiredDate){
        Date currentDate = Date.from(Instant.now());
        if (isApproved && startDate.after(currentDate))
            return "Approved";
        else if (isApproved && expiredDate.after(currentDate))
            return "Active";
        else if (isApproved)
            return "Expired";
        else if (manager == null)
            return documents.entrySet()
                    .stream()
                    .anyMatch(x -> x.getValue() == null) ? "Waiting for documents" : "Awaiting manager approval";
        else return "Rejected";

    }


}
