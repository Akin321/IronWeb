package com.example.demo.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.Dto.BannerDto;
import com.example.demo.model.BannerModel;
import com.example.demo.service.BannerService;

import jakarta.validation.Valid;
@Controller
@RequestMapping("/admin")
public class BannerController {
	
	@Autowired
	BannerService bannerService;
	
	@GetMapping("/add-banner")
	public String viewBanner(Model model) {
		BannerDto banner=new BannerDto();
		model.addAttribute("banner",banner);
		return "add-Banner";

	}
	@PostMapping("/verify-banner")
	@ResponseBody
	public ResponseEntity<?> verifyBanner(@ModelAttribute @Valid BannerDto bannerdto,BindingResult result) throws IOException {
		 MultipartFile file = bannerdto.getImageUrl();
		    if (file == null || file.isEmpty()) {
		        result.rejectValue("imageUrl", "error.imageUrl", "Image file is required.");
		    }
		if(result.hasErrors()) {
			Map<String,String> errors=new HashMap<>();
			  result.getFieldErrors().forEach(error -> 
	            errors.put(error.getField(), error.getDefaultMessage()));		

		        return ResponseEntity.badRequest().body(errors);
		}
		else {
			bannerService.addBanner(bannerdto);
			return ResponseEntity.ok("banner successfully added");
		}
	}
	
	@GetMapping("/view-banner")
	public String viewBanner(Model model,RedirectAttributes redirectAttributes,@RequestParam(defaultValue="0") int page,@RequestParam(defaultValue="5") int size) {
		try {
			Page<BannerModel> banners=bannerService.getBanners(page,size);
			model.addAttribute("banners", banners);
			model.addAttribute("currentPage",page);
			model.addAttribute("totalPages",banners.getTotalPages());
			return "view-banner";
		}
		catch(Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("errorMessage","something unexpected happened");
			return "redirect:/admin/dashboard";
		}
	}
	@GetMapping("/edit-banner/{id}")
	public String editBanner(@PathVariable int id,RedirectAttributes redirectAttributes,Model model) {
		try {
			BannerDto bannerDto=bannerService.getBannerDto(id);
			model.addAttribute("bannerDto",bannerDto);
			return "edit-banner";
		}
		catch(Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("errorMessage","Something unexpected happened");
			return "redirect:/admin/view-banner";
		}
	}
	@PostMapping("/edit/verify-banner")
	@ResponseBody
	public ResponseEntity<?> verifyEcitBanner(@ModelAttribute @Valid BannerDto bannerdto,BindingResult result) throws IOException {	
		
		if(result.hasErrors()) {
			Map<String,String> errors=new HashMap<>();
			  result.getFieldErrors().forEach(error -> 
	            errors.put(error.getField(), error.getDefaultMessage()));		

		        return ResponseEntity.badRequest().body(errors); 
		}
		else {
			bannerService.editBanner(bannerdto);
			return ResponseEntity.ok("banner successfully added");
		}
		
	}
	
	@PostMapping("/banner/updateStatus")
	@ResponseBody
	public ResponseEntity<?> updateStatus(@RequestParam boolean enabled,@RequestParam int id){
		try {
			if(enabled==true) {
				bannerService.updateDisableStatus(id);
			}
			else {
				bannerService.updateEnableStatus(id);
			}
			return ResponseEntity.ok("updated successfully");
		}
		catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}


}
