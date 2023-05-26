package com.gitlab.alura.insuranceagency.service;

import com.gitlab.alura.insuranceagency.dto.InsuranceTypeDto;
import com.gitlab.alura.insuranceagency.entity.InsuranceType;
import com.gitlab.alura.insuranceagency.exception.NotFoundException;
import com.gitlab.alura.insuranceagency.mapper.InsuranceTypeMapper;
import com.gitlab.alura.insuranceagency.repository.InsuranceTypeRepository;
import com.gitlab.alura.insuranceagency.service.implementation.InsuranceTypeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class InsuranceTypeServiceImplTest extends BaseClassTest {

    @Mock
    private InsuranceTypeRepository insuranceTypeRepository;
    @Mock
    private InsuranceTypeMapper insuranceTypeMapper;
    @InjectMocks
    private InsuranceTypeServiceImpl insuranceTypeService;

    @Test
    void getAllInsuranceTypeTest() {
        InsuranceType type1 = new InsuranceType();
        type1.setTitle(INSURANCE_TITLE_1);
        InsuranceType type2 = new InsuranceType();
        type2.setTitle(INSURANCE_TITLE_2);
        InsuranceType type3 = new InsuranceType();
        type3.setTitle(INSURANCE_TITLE_3);

        List<InsuranceType> insuranceTypes = Arrays.asList(
                type1,
                type2,
                type3
        );

        InsuranceTypeDto typeDto1 = new InsuranceTypeDto();
        typeDto1.setTitle(INSURANCE_TITLE_1);
        InsuranceTypeDto typeDto2 = new InsuranceTypeDto();
        typeDto2.setTitle(INSURANCE_TITLE_2);
        InsuranceTypeDto typeDto3 = new InsuranceTypeDto();
        typeDto3.setTitle(INSURANCE_TITLE_3);
        List<InsuranceTypeDto> expectedInsuranceTypeDtos = Arrays.asList(
                typeDto1,
                typeDto2,
                typeDto3
        );

        when(insuranceTypeRepository.findAllByIsActive(true)).thenReturn(insuranceTypes);
        when(insuranceTypeMapper.toDto(type1)).thenReturn(typeDto1);
        when(insuranceTypeMapper.toDto(type2)).thenReturn(typeDto2);
        when(insuranceTypeMapper.toDto(type3)).thenReturn(typeDto3);

        List<InsuranceTypeDto> actualInsuranceTypeList = insuranceTypeService.getInsuranceTypes();

        assertEquals(expectedInsuranceTypeDtos, actualInsuranceTypeList);
        verify(insuranceTypeRepository, times(1)).findAllByIsActive(true);
        verify(insuranceTypeMapper, times(3)).toDto(any(InsuranceType.class));
    }

    @Test
    void testGetPopularInsuranceTypes() {
        InsuranceType insuranceType1 = new InsuranceType(INSURANCE_TITLE_1, true, 2);
        InsuranceType insuranceType2 = new InsuranceType(INSURANCE_TITLE_2, true, 3);
        List<InsuranceType> insuranceTypes = new ArrayList<>();
        insuranceTypes.add(insuranceType1);
        insuranceTypes.add(insuranceType2);

        InsuranceTypeDto insuranceTypeDto1 = new InsuranceTypeDto("Type 1 insurance");
        InsuranceTypeDto insuranceTypeDto2 = new InsuranceTypeDto("Type 2 insurance");
        List<InsuranceTypeDto> insuranceTypeDtos = new ArrayList<>();
        insuranceTypeDtos.add(insuranceTypeDto1);
        insuranceTypeDtos.add(insuranceTypeDto2);

        when(insuranceTypeMapper.toDto(insuranceType1)).thenReturn(insuranceTypeDto1);
        when(insuranceTypeMapper.toDto(insuranceType2)).thenReturn(insuranceTypeDto2);

        when(insuranceTypeRepository.findAllByIsActive(true)).thenReturn(insuranceTypes);

        List<InsuranceTypeDto> popularTypes = insuranceTypeService.getPopularInsuranceTypes(2);

        Collections.reverse(insuranceTypeDtos);
        assertIterableEquals(insuranceTypeDtos, popularTypes);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void testGetPopularTypesWithInvalidArgument(int invalidArgument) {
        assertThrows(IllegalArgumentException.class,
                () -> insuranceTypeService.getPopularInsuranceTypes(invalidArgument));
    }

    @Test
    void getById() {
        InsuranceType insuranceType = new InsuranceType();
        insuranceType.setId(INSURANCE_TYPE_ID);

        when(insuranceTypeRepository.findByIdAndIsActive(INSURANCE_TYPE_ID, true))
                .thenReturn(Optional.of(insuranceType));

        InsuranceType actualInsuranceType = insuranceTypeService.getById(INSURANCE_TYPE_ID);

        assertEquals(insuranceType, actualInsuranceType);
        verify(insuranceTypeRepository, times(1)).findByIdAndIsActive(INSURANCE_TYPE_ID, true);
        verify(insuranceTypeMapper, times(0)).toDto(insuranceType);
    }

    @Test
    void getByNonExistingId() {
        when(insuranceTypeRepository.findByIdAndIsActive(INSURANCE_TYPE_ID, true))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> insuranceTypeService.getById(INSURANCE_TYPE_ID));
        verify(insuranceTypeRepository, times(1)).findByIdAndIsActive(INSURANCE_TYPE_ID, true);
        verify(insuranceTypeMapper, times(0)).toDto(any(InsuranceType.class));
    }

    @Test
    void getAllActiveByPage() {
        Pageable pageable = Pageable.ofSize(10).withPage(0);
        InsuranceType type1 = new InsuranceType();
        type1.setTitle(INSURANCE_TITLE_1);
        InsuranceType type2 = new InsuranceType();
        type2.setTitle(INSURANCE_TITLE_2);
        InsuranceType type3 = new InsuranceType();
        type3.setTitle(INSURANCE_TITLE_3);

        List<InsuranceType> insuranceTypes = Arrays.asList(
                type1,
                type2,
                type3
        );

        Page<InsuranceType> insuranceTypePage = new PageImpl<>(insuranceTypes, pageable, insuranceTypes.size());

        InsuranceTypeDto typeDto1 = new InsuranceTypeDto();
        typeDto1.setTitle(INSURANCE_TITLE_1);
        InsuranceTypeDto typeDto2 = new InsuranceTypeDto();
        typeDto2.setTitle(INSURANCE_TITLE_1);
        InsuranceTypeDto typeDto3 = new InsuranceTypeDto();
        typeDto3.setTitle(INSURANCE_TITLE_1);
        List<InsuranceTypeDto> expectedInsuranceTypeDtos = Arrays.asList(
                typeDto1,
                typeDto2,
                typeDto3
        );

        when(insuranceTypeRepository.findAllByIsActive(pageable, true)).thenReturn(insuranceTypePage);
        when(insuranceTypeMapper.toDto(type1)).thenReturn(typeDto1);
        when(insuranceTypeMapper.toDto(type2)).thenReturn(typeDto2);
        when(insuranceTypeMapper.toDto(type3)).thenReturn(typeDto3);

        Page<InsuranceTypeDto> actualInsuranceTypeDtos = insuranceTypeService.getAllActive(pageable);

        assertEquals(expectedInsuranceTypeDtos, actualInsuranceTypeDtos.getContent());
        assertEquals(insuranceTypePage.getTotalElements(), actualInsuranceTypeDtos.getTotalElements());
        verify(insuranceTypeRepository, times(1)).findAllByIsActive(pageable, true);
        verify(insuranceTypeMapper, times(3)).toDto(any(InsuranceType.class));
    }

    @Test
    void deleteById() {
        InsuranceType insuranceType = new InsuranceType();
        insuranceType.setId(INSURANCE_TYPE_ID);
        insuranceType.setActive(true);

        when(insuranceTypeRepository.findByIdAndIsActive(INSURANCE_TYPE_ID, true))
                .thenReturn(Optional.of(insuranceType));
        insuranceTypeService.deleteById(INSURANCE_TYPE_ID);

        assertFalse(insuranceType.isActive());
        verify(insuranceTypeRepository, times(1)).save(insuranceType);
    }

    @Test
    void deleteByNonExistingId() {
        when(insuranceTypeRepository.findByIdAndIsActive(INSURANCE_TYPE_ID, true))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> insuranceTypeService.deleteById(INSURANCE_TYPE_ID)
        );
        verify(insuranceTypeRepository, times(0)).save(any(InsuranceType.class));
    }

    @Test
    void getDtoById() {
        InsuranceType insuranceType = new InsuranceType();
        insuranceType.setId(INSURANCE_TYPE_ID);

        InsuranceTypeDto expectedInsuranceTypeDto = new InsuranceTypeDto();
        expectedInsuranceTypeDto.setId(1L);

        when(insuranceTypeRepository.findByIdAndIsActive(INSURANCE_TYPE_ID, true))
                .thenReturn(Optional.of(insuranceType));
        when(insuranceTypeMapper.toDto(insuranceType))
                .thenReturn(expectedInsuranceTypeDto);

        InsuranceTypeDto actualInsuranceTypeDto = insuranceTypeService.getDtoById(INSURANCE_TYPE_ID);

        assertEquals(expectedInsuranceTypeDto, actualInsuranceTypeDto);
        verify(insuranceTypeRepository, times(1)).findByIdAndIsActive(INSURANCE_TYPE_ID, true);
        verify(insuranceTypeMapper, times(1)).toDto(insuranceType);
    }

    @Test
    void getDtoByNonExistingId() {
        when(insuranceTypeRepository.findByIdAndIsActive(INSURANCE_TYPE_ID, true))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> insuranceTypeService.getDtoById(INSURANCE_TYPE_ID));
        verify(insuranceTypeRepository, times(1)).findByIdAndIsActive(INSURANCE_TYPE_ID, true);
        verify(insuranceTypeMapper, times(0)).toDto(any(InsuranceType.class));
    }

    @Test
    void updateInsuranceType() {
        InsuranceType insuranceType = new InsuranceType();
        insuranceType.setId(INSURANCE_TYPE_ID);

        InsuranceTypeDto insuranceTypeDto = new InsuranceTypeDto();
        insuranceTypeDto.setId(INSURANCE_TYPE_ID);

        when(insuranceTypeRepository.findByIdAndIsActive(INSURANCE_TYPE_ID, true))
                .thenReturn(Optional.of(insuranceType));
        when(insuranceTypeMapper.toEntity(insuranceTypeDto)).thenReturn(insuranceType);
        when(insuranceTypeRepository.save(insuranceType)).thenReturn(insuranceType);

        insuranceTypeService.updateInsuranceType(insuranceTypeDto);

        verify(insuranceTypeMapper, times(1)).toEntity(insuranceTypeDto);
        verify(insuranceTypeRepository, times(2)).save(any(InsuranceType.class));
    }


    @Test
    void createInsuranceType() {
        InsuranceTypeDto insuranceTypeDto = new InsuranceTypeDto();
        InsuranceType insuranceType = new InsuranceType();
        insuranceType.setId(1L);

        when(insuranceTypeMapper.toEntity(insuranceTypeDto)).thenReturn(insuranceType);
        when(insuranceTypeRepository.save(insuranceType)).thenReturn(insuranceType);

        insuranceTypeService.createInsuranceType(insuranceTypeDto);

        verify(insuranceTypeMapper, times(1)).toEntity(insuranceTypeDto);
        verify(insuranceTypeRepository, times(1)).save(insuranceType);
    }
}
