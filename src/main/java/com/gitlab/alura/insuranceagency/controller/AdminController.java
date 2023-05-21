package com.gitlab.alura.insuranceagency.controller;

import com.gitlab.alura.insuranceagency.dto.DocumentTypeDto;
import com.gitlab.alura.insuranceagency.service.DocumentTypeService;
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

    public AdminController(DocumentTypeService documentTypeService) {
        this.documentTypeService = documentTypeService;
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
    public String deleteOffer(@PathVariable("id") Long id,
                              Model model) {
        documentTypeService.deleteById(id);
        return "redirect:/personal/admin/documentTypes";
    }
}
