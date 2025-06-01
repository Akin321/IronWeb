package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.Dto.CategoryOfferDto;
import com.example.demo.Dto.CouponDto;
import com.example.demo.model.CategoryOffer;
import com.example.demo.model.ProductTypes;
import com.example.demo.service.OfferService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin")
public class CategoryOfferController {
	@Autowired
	OfferService offerService;
	
	@GetMapping("/add/cat-offer")
	public String AddcatOffer(Model model) {
		CategoryOfferDto dto=new CategoryOfferDto();
		model.addAttribute("categoryOfferDto",dto);
		List<ProductTypes> productTypes=offerService.getTypes();
		model.addAttribute("categories",productTypes);
		return "addOffer";
	}
	
	@PostMapping("/verify/offer")
	@ResponseBody
	public ResponseEntity<?> verifyOffer(@ModelAttribute @Valid CategoryOfferDto offerDto,BindingResult result) {
		if(result.hasErrors()) {
			Map<String,String> errors=new HashMap<>();
			  result.getFieldErrors().forEach(error -> 
	            errors.put(error.getField(), error.getDefaultMessage()));		

		        return ResponseEntity.badRequest().body(errors);
	            }
		offerService.addOffer(offerDto);
		return ResponseEntity.ok("offer added successfully");
	}
	
	@GetMapping("/view-offers")
	public String viewOffers(Model model,RedirectAttributes redirectAttributes,@RequestParam(required=false) String keyword,@RequestParam(defaultValue="0") int page,@RequestParam(defaultValue="5") int size) {
		try{
			Page<CategoryOffer> offers=offerService.getOffers(page,size,keyword);
			model.addAttribute("offers",offers);
			model.addAttribute("currentPage",page);
			model.addAttribute("keyword",keyword);
			model.addAttribute("totalPages",offers.getTotalPages());

			return "view-offer";
		}
		catch(Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("errorMessage","Something Unexpected happened");
			return "redirect:/admin/dashboard";
		}
	}
	@GetMapping("/edit-offer/{id}")
	public String editOffers(@PathVariable int id,Model model,RedirectAttributes redirectAttributes) {
		try {
			CategoryOfferDto offer=offerService.getOffers(id);
			model.addAttribute("offer",offer);
			return "editOffer";
		}
		catch(Exception e) {
			redirectAttributes.addFlashAttribute("errorMessage","something unexpected happened");
			return "redirect:/admin/view-offers";
		}
	}
	@PostMapping("/edit-offer")
	public ResponseEntity<?> editOffer(@ModelAttribute @Valid CategoryOfferDto offerDto,BindingResult result){

			if(result.hasErrors()) {
				Map<String,String> errors=new HashMap<>();
				  result.getFieldErrors().forEach(error -> 
		            errors.put(error.getField(), error.getDefaultMessage()));		

			        return ResponseEntity.badRequest().body(errors);
		            
			}
			offerService.editOffer(offerDto);
			return ResponseEntity.ok("offer edited successfully");
		
	}
	@PostMapping("change-offer-status/{id}")
	@ResponseBody
	public ResponseEntity<?> changeOffer(@PathVariable int id){
		try {
			offerService.changeStatus(id);
			return ResponseEntity.ok("status changed successfully");
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage()));

		}
	}
}
