package com.gitlab.alura.insuranceagency.controller;

import com.gitlab.alura.insuranceagency.dto.CompanyDto;
import com.gitlab.alura.insuranceagency.dto.DocumentTypeDto;
import com.gitlab.alura.insuranceagency.dto.InsuranceTypeDto;
import com.gitlab.alura.insuranceagency.service.CompanyService;
import com.gitlab.alura.insuranceagency.service.DocumentTypeService;
import com.gitlab.alura.insuranceagency.service.InsuranceTypeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("personal/admin")
public class AdminController {
    private final DocumentTypeService documentTypeService;
    private final InsuranceTypeService insuranceTypeService;
    private final CompanyService companyService;

    public AdminController(DocumentTypeService documentTypeService,
                           InsuranceTypeService insuranceTypeService,
                           CompanyService companyService) {
        this.documentTypeService = documentTypeService;
        this.insuranceTypeService = insuranceTypeService;
        this.companyService = companyService;
    }

    @GetMapping("/documentTypes")
    public String getDocumentTypes(@RequestParam(name = "page", defaultValue = "0") int page,
                                   @RequestParam(name = "size", defaultValue = "10") int size,
                                   @RequestParam(name = "id", defaultValue = "0") Long typeId,
                                   @RequestParam(name = "edit", defaultValue = "false") boolean edit,
                                   Model model){
        Pageable pageable = PageRequest.of(page, size);
        Page<DocumentTypeDto> documentTypes = documentTypeService.getAllActive(pageable);

        model.addAttribute("typesList", documentTypes);
        model.addAttribute("page", "documentTypes");
        model.addAttribute("edit", edit);
        if (edit && typeId != 0){
            DocumentTypeDto updateType = documentTypeService.getById(typeId);
            model.addAttribute("updatedType", updateType);
        } else {
            model.addAttribute("newType", new DocumentTypeDto());
        }
        model.addAttribute("typeId", typeId);
        return "personal/personal-account";
    }


    @PostMapping("/documentTypes/{id}")
    public String updateDocumentType(@PathVariable("id") Long id,
                                     @ModelAttribute("updatedType") DocumentTypeDto documentType,
                                     Model model) {
        documentTypeService.updateDocumentType(documentType);
        return "redirect:/personal/admin/documentTypes";
    }
    @PostMapping("/documentTypes/new")
    public String createNewDocumentType(@ModelAttribute("newType") DocumentTypeDto documentType,
                                        Model model) {
        documentTypeService.createNewDocumentType(documentType);
        return "redirect:/personal/admin/documentTypes";
    }

    @GetMapping("/documentTypes/{id}/delete")
    public String deleteDocumentType(@PathVariable("id") Long id,
                                     Model model) {
        documentTypeService.deleteById(id);
        return "redirect:/personal/admin/documentTypes";
    }

    @GetMapping("/insuranceTypes")
    public String getInsuranceTypes(@RequestParam(name = "page", defaultValue = "0") int page,
                                   @RequestParam(name = "size", defaultValue = "10") int size,
                                   @RequestParam(name = "id", defaultValue = "0") Long typeId,
                                   @RequestParam(name = "edit", defaultValue = "false") boolean edit,
                                   Model model){
        Pageable pageable = PageRequest.of(page, size);
        Page<InsuranceTypeDto> documentTypes = insuranceTypeService.getAllActive(pageable);

        model.addAttribute("typesList", documentTypes);
        model.addAttribute("page", "insuranceTypes");
        model.addAttribute("edit", edit);
        if (edit && typeId != 0){
            InsuranceTypeDto updateType = insuranceTypeService.getDtoById(typeId);
            model.addAttribute("updatedType", updateType);
        } else {
            model.addAttribute("newType", new InsuranceTypeDto());
        }
        model.addAttribute("typeId", typeId);
        return "personal/personal-account";
    }


    @PostMapping("/insuranceTypes/{id}")
    public String updateInsuranceType(@PathVariable("id") Long id,
                                     @ModelAttribute("updatedType") InsuranceTypeDto insuranceTypeDto) {
        insuranceTypeService.updateInsuranceType(insuranceTypeDto);
        return "redirect:/personal/admin/insuranceTypes";
    }
    @PostMapping("/insuranceTypes/new")
    public String createNewInsuranceType(@ModelAttribute("newType") InsuranceTypeDto insuranceTypeDto) {
        insuranceTypeService.createNewInsuranceType(insuranceTypeDto);
        return "redirect:/personal/admin/insuranceTypes";
    }

    @GetMapping("/insuranceTypes/{id}/delete")
    public String deleteInsuranceType(@PathVariable("id") Long id) {
        insuranceTypeService.deleteById(id);
        return "redirect:/personal/admin/insuranceTypes";
    }

    @GetMapping("/companies")
    public String getCompanies(@RequestParam(name = "page", defaultValue = "0") int page,
                               @RequestParam(name = "size", defaultValue = "10") int size,
                               Model model){
        Pageable pageable = PageRequest.of(page, size);
        Page<CompanyDto> companies = companyService.getAllActive(pageable);

        model.addAttribute("companies", companies);
        model.addAttribute("page", "companies");
        return "personal/personal-account";
    }

    @GetMapping("/companies/{id}")
    public String getCompanyById(@PathVariable(name="id") Long id,
                                 @RequestParam(name = "edit", defaultValue = "false") boolean edit,
                                 Model model){
        CompanyDto company = companyService.getCompanyDtoById(id);

        model.addAttribute("company", company);
        model.addAttribute("edit", edit);
        model.addAttribute("page", "companies");
        return "personal/company";
    }

    @PostMapping("/companies/{id}")
    public String updateCompany(@PathVariable(name="id") Long id,
                                @ModelAttribute("company") CompanyDto companyDto){
        Long companyId = companyService.updateCompany(companyDto);
        return "redirect:/personal/admin/companies/" + companyId;
    }

    @GetMapping("/companies/new")
    public String getNewCompanyForm(Model model){
        model.addAttribute("company", new CompanyDto());
        model.addAttribute("page", "companies");
        return "personal/company";
    }

    @PostMapping("/companies/new")
    public String createNewCompany(@ModelAttribute("company") CompanyDto companyDto){
        Long companyId = companyService.createNewCompany(companyDto);
        return "redirect:/personal/admin/companies/" + companyId;
    }

    @GetMapping("/companies/{id}/delete")
    public String deleteCompany(@PathVariable("id") Long id) {
        companyService.deleteById(id);
        return "redirect:/personal/admin/companies";
    }
}
