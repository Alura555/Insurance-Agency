package com.gitlab.alura.insuranceagency.controller;

import com.gitlab.alura.insuranceagency.dto.CompanyDto;
import com.gitlab.alura.insuranceagency.dto.DocumentTypeDto;
import com.gitlab.alura.insuranceagency.dto.InsuranceTypeDto;
import com.gitlab.alura.insuranceagency.dto.RoleDto;
import com.gitlab.alura.insuranceagency.dto.UserDto;
import com.gitlab.alura.insuranceagency.exception.InvalidInputException;
import com.gitlab.alura.insuranceagency.service.CompanyService;
import com.gitlab.alura.insuranceagency.service.DocumentTypeService;
import com.gitlab.alura.insuranceagency.service.InsuranceTypeService;
import com.gitlab.alura.insuranceagency.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("personal/admin")
public class AdminController {
    private final DocumentTypeService documentTypeService;
    private final InsuranceTypeService insuranceTypeService;
    private final CompanyService companyService;
    private final UserService userService;

    public AdminController(DocumentTypeService documentTypeService,
                           InsuranceTypeService insuranceTypeService,
                           CompanyService companyService,
                           UserService userService) {
        this.documentTypeService = documentTypeService;
        this.insuranceTypeService = insuranceTypeService;
        this.companyService = companyService;
        this.userService = userService;
    }

    @GetMapping("/documentTypes")
    public String getDocumentTypes(@RequestParam(name = "page", defaultValue = "0") int page,
                                   @RequestParam(name = "size", defaultValue = "10") int size,
                                   Model model){
        Pageable pageable = PageRequest.of(page, size);
        Page<DocumentTypeDto> documentTypes = documentTypeService.getAllActive(pageable);

        model.addAttribute("typesList", documentTypes);
        model.addAttribute("page", "documentTypes");
        return "personal/personal-account";
    }

    @GetMapping("/documentTypes/{id}")
    public String getDocumentTypeForm(@PathVariable(name = "id") Long typeId,
                                      @RequestParam(name = "page", defaultValue = "0") int page,
                                      @RequestParam(name = "size", defaultValue = "10") int size,
                                      Model model){
        Pageable pageable = PageRequest.of(page, size);
        Page<DocumentTypeDto> documentTypes = documentTypeService.getAllActive(pageable);
        DocumentTypeDto updateType = documentTypeService.getDtoById(typeId);

        model.addAttribute("typesList", documentTypes);
        model.addAttribute("page", "documentTypes");
        model.addAttribute("updatedType", updateType);
        model.addAttribute("typeId", typeId);
        return "personal/personal-account";
    }


    @PutMapping("/documentTypes/{id}")
    public String updateDocumentType(@PathVariable("id") Long id,
                                                   @ModelAttribute("updatedType") DocumentTypeDto documentType) {
        documentTypeService.updateDocumentType(documentType);
        return "redirect:/personal/admin/documentTypes";
    }

    @GetMapping("/documentTypes/new")
    public String getDocumentTypeForm(@RequestParam(name = "page", defaultValue = "0") int page,
                                      @RequestParam(name = "size", defaultValue = "10") int size,
                                      Model model){
        Pageable pageable = PageRequest.of(page, size);
        Page<DocumentTypeDto> documentTypes = documentTypeService.getAllActive(pageable);

        model.addAttribute("typesList", documentTypes);
        model.addAttribute("page", "documentTypes");
        model.addAttribute("newType", new DocumentTypeDto());
        return "personal/personal-account";
    }
    @PostMapping("/documentTypes/new")
    public String createNewDocumentType(@ModelAttribute("newType") DocumentTypeDto documentType) {
        documentTypeService.createDocumentType(documentType);
        return "redirect:/personal/admin/documentTypes";
    }

    @DeleteMapping("/documentTypes/{id}")
    public String deleteDocumentType(@PathVariable("id") Long id) {
        documentTypeService.deleteById(id);
        return "redirect:/personal/admin/documentTypes";
    }

    @GetMapping("/insuranceTypes")
    public String getInsuranceTypes(@RequestParam(name = "page", defaultValue = "0") int page,
                                   @RequestParam(name = "size", defaultValue = "10") int size,
                                   Model model){
        Pageable pageable = PageRequest.of(page, size);
        Page<InsuranceTypeDto> documentTypes = insuranceTypeService.getAllActive(pageable);

        model.addAttribute("typesList", documentTypes);
        model.addAttribute("page", "insuranceTypes");
        return "personal/personal-account";
    }

    @GetMapping("/insuranceTypes/{id}")
    public String getInsuranceTypeForm(@PathVariable(name = "id") Long typeId,
                                        @RequestParam(name = "page", defaultValue = "0") int page,
                                        @RequestParam(name = "size", defaultValue = "10") int size,
                                        Model model){
        Pageable pageable = PageRequest.of(page, size);
        Page<InsuranceTypeDto> documentTypes = insuranceTypeService.getAllActive(pageable);
        InsuranceTypeDto updateType = insuranceTypeService.getDtoById(typeId);

        model.addAttribute("typesList", documentTypes);
        model.addAttribute("page", "insuranceTypes");
        model.addAttribute("updatedType", updateType);
        model.addAttribute("typeId", typeId);
        return "personal/personal-account";
    }
    @PutMapping("/insuranceTypes/{id}")
    public String updateInsuranceType(@PathVariable("id") Long id,
                                                    @ModelAttribute("updatedType") InsuranceTypeDto insuranceTypeDto) {
        insuranceTypeService.updateInsuranceType(insuranceTypeDto);
        return "redirect:/personal/admin/insuranceTypes";
    }

    @GetMapping("/insuranceTypes/new")
    public String getNewInsuranceTypesForm(@RequestParam(name = "page", defaultValue = "0") int page,
                                           @RequestParam(name = "size", defaultValue = "10") int size,
                                           Model model){
        Pageable pageable = PageRequest.of(page, size);
        Page<InsuranceTypeDto> documentTypes = insuranceTypeService.getAllActive(pageable);

        model.addAttribute("typesList", documentTypes);
        model.addAttribute("page", "insuranceTypes");
        model.addAttribute("newType", new InsuranceTypeDto());
        return "personal/personal-account";
    }
    @PostMapping("/insuranceTypes/new")
    public String createNewInsuranceType(@ModelAttribute("newType") InsuranceTypeDto insuranceTypeDto) {
        insuranceTypeService.createInsuranceType(insuranceTypeDto);
        return "redirect:/personal/admin/insuranceTypes";
    }

    @DeleteMapping("/insuranceTypes/{id}")
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
                                 Model model){
        CompanyDto company = companyService.getCompanyDtoById(id);

        model.addAttribute("company", company);
        model.addAttribute("edit", false);
        model.addAttribute("page", "companies");
        return "personal/company";
    }

    @GetMapping("/companies/{id}/getCompanyForm")
    public String getCompanyFormById(@PathVariable(name="id") Long id,
                                     Model model){
        CompanyDto company = companyService.getCompanyDtoById(id);

        model.addAttribute("company", company);
        model.addAttribute("edit", true);
        model.addAttribute("page", "companies");
        return "personal/company";
    }

    @PutMapping("/companies/{id}")
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
        Long companyId = companyService.createCompany(companyDto);
        return "redirect:/personal/admin/companies/" + companyId;
    }

    @DeleteMapping("/companies/{id}")
    public String deleteCompany(@PathVariable("id") Long id) {
        companyService.deleteById(id);
        return "redirect:/personal/admin/companies";
    }

    @GetMapping("/users")
    public String getUsers(@RequestParam(name = "page", defaultValue = "0") int page,
                           @RequestParam(name = "size", defaultValue = "10") int size,
                           Model model){
        Pageable pageable = PageRequest.of(page, size);
        Page<UserDto> users = userService.getAllActive(pageable);

        model.addAttribute("users", users);
        model.addAttribute("page", "users");
        return "personal/personal-account";
    }

    @GetMapping("/users/{id}")
    public String getUserById(@PathVariable(name="id") Long id,
                              Model model){
        UserDto userDto = userService.getUserDtoById(id);
        model.addAttribute("user", userDto);
        model.addAttribute("edit", false);
        model.addAttribute("page", "users");
        return "personal/user";
    }

    @GetMapping("/users/{id}/getUserForm")
    public String getUserFormById(@PathVariable(name="id") Long id,
                                  Model model){
        UserDto userDto = userService.getUserDtoById(id);
        List<RoleDto> roles = userService.getUserRoles();
        List<CompanyDto> companies = companyService.getActiveCompanies();

        model.addAttribute("roles", roles);
        model.addAttribute("companies", companies);

        model.addAttribute("user", userDto);
        model.addAttribute("edit", true);
        model.addAttribute("page", "users");
        return "personal/user";
    }

    @PutMapping("/users/{id}")
    public String updateUser(@PathVariable(name="id") Long id,
                             @ModelAttribute("user") UserDto userDto,
                             @RequestParam(name = "userRole", required = false) String roleTitle,
                             BindingResult result){
        Long userId = null;
        try {
            userId = userService.updateUser(userDto, roleTitle);
        } catch (InvalidInputException e){
            result.rejectValue(e.getField(), "error.userDto", e.getMessage());
            return "personal/user";
        }
        return "redirect:/personal/admin/users/" + userId;
    }

    @GetMapping("/users/new")
    public String getNewUserForm(Model model){
        List<RoleDto> roles = userService.getUserRoles();
        List<CompanyDto> companies = companyService.getActiveCompanies();

        model.addAttribute("roles", roles);
        model.addAttribute("companies", companies);
        model.addAttribute("user", new UserDto());
        model.addAttribute("page", "users");
        return "personal/user";
    }

    @PostMapping("/users/new")
    public String createUser(@ModelAttribute("user") UserDto userDto,
                             @RequestParam(name = "userRole", required = false) String roleTitle,
                             @RequestParam(name = "company", defaultValue = "0") Long companyId,
                             BindingResult result){
        Long userId = null;
        try {
            userId = userService.registerUser(userDto, roleTitle, companyId);
        } catch (InvalidInputException e){
            result.rejectValue(e.getField(), "error.userDto", e.getMessage());
            return "personal/user";
        }
        return "redirect:/personal/admin/users/" + userId;
    }

    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return "redirect:/personal/admin/users";
    }

}
