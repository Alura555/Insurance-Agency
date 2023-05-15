package com.example.insuranceagency.mapper;

import com.example.insuranceagency.dto.PolicyDto;
import com.example.insuranceagency.entity.Document;
import com.example.insuranceagency.entity.DocumentType;
import com.example.insuranceagency.entity.Policy;
import com.example.insuranceagency.entity.User;
import org.mapstruct.*;

import java.time.Instant;
import java.util.*;

@Mapper(uses = InsuranceTypeMapper.class)
public interface PolicyMapper {
    @Mapping(source = "client", target = "client", qualifiedByName = "fullName")
    @Mapping(source = "manager", target = "manager", qualifiedByName = "fullName")
    @Mapping(target = "expiredDate", expression = "java(calculateExpiredDate(policy.getOffer().getPeriodInMonths(), policy.getStartDate()))", dateFormat = "yyyy-MM-dd")
    @Mapping(target = "documents", expression = "java(mapDocuments(policy.getOffer().getDocuments(), policy.getDocuments()))")
    @Mapping(target = "status", expression = "java(getStatus(policy.isApproved(), policy.getStartDate(), policy.getManager(), policyDto.getDocuments(), policyDto.getExpiredDate()))")
    PolicyDto toPolicyDto(Policy policy);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "existing.client", target = "client")
    @Mapping(source = "existing.offer", target = "offer")
    @Mapping(source = "existing.creationDate", target = "creationDate")
    @Mapping(source = "existing.status", target = "status")
    @Mapping(target = "startDate",
            expression = "java(setStartDateIfNotNull(policyDto.getStartDate(), existing.getStartDate()))")
    @Mapping(target = "documents",
            expression = "java(setDocumentsIfNotNull(policyDto.getDocuments(), existing.getDocuments()))")
    void updatePolicyDtoFromExisting(@MappingTarget PolicyDto policyDto, PolicyDto existing);

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

    default Date setStartDateIfNotNull(Date targetStartDate, Date existingStartDate) {
        if (existingStartDate != null){
            return existingStartDate;
        } else {
            return targetStartDate;
        }
    }

    default Map<DocumentType, Document> setDocumentsIfNotNull(Map<DocumentType, Document> targetDocuments,
                                                              Map<DocumentType, Document> existingDocuments) {
        if (targetDocuments != null){
            return targetDocuments;
        } else {
            return existingDocuments;
        }

    }
}
