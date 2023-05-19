package com.gitlab.alura.insuranceagency.mapper;

import com.gitlab.alura.insuranceagency.dto.OfferDto;
import com.gitlab.alura.insuranceagency.entity.Offer;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = InsuranceTypeMapper.class)
public interface OfferMapper {
    @Mapping(target = "period", expression = "java(formatPeriod(offer.getPeriodInMonths()))")
    @Mapping(target = "companyName", source = "offer.company.name")
    @Mapping(source = "price", target = "formattedPrice", numberFormat = "$#.##")
    @Mapping(source = "documents", target = "documents")
    OfferDto toOfferDto(Offer offer);

    @InheritInverseConfiguration
    @Mapping(target = "price", source = "price")
    @Mapping(target = "periodInMonths", expression = "java(calculatePeriod(offerDto.getYears(), offerDto.getMonths()))")
    Offer toOffer(OfferDto offerDto);


    default String formatPeriod(int periodInMonths) {
        int years = periodInMonths / 12;
        int months = periodInMonths % 12;
        StringBuilder stringBuilder = new StringBuilder();
        if (years > 0) {
            stringBuilder.append(years)
                    .append(" year")
                    .append(years != 1 ? "s":"");
        }
        if (months > 0) {
            stringBuilder
                    .append(years > 0 ? " " : "")
                    .append(months)
                    .append(" month")
                    .append(months != 1 ? "s":"");
        }
        return stringBuilder.toString();
    }

    default int calculatePeriod(int years, int months) {
        return years * 12 + months;
    }
}
