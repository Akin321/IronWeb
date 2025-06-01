package com.example.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.Dto.CategoryOfferDto;
import com.example.demo.Repository.CategoryOfferRepo;
import com.example.demo.Repository.ProductTypeRepo;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.CategoryOffer;
import com.example.demo.model.OfferStatus;
import com.example.demo.model.ProductTypes;
import com.example.demo.model.Status;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
@Service
public class OfferService {
	@Autowired
	CategoryOfferRepo OfferRepo;
	
	@Autowired
	ProductTypeRepo productTypeRepo;

	public List<ProductTypes> getTypes() {
		// TODO Auto-generated method stub
		List<CategoryOffer> offerTypes=OfferRepo.findByStatus(OfferStatus.Active);
		List<ProductTypes> allTypes=productTypeRepo.findByIsActiveTrue();
		Set<Integer> offeredTypeIds = offerTypes.stream()
			    .map(offer -> offer.getProductType().getId())
			    .collect(Collectors.toSet());

			// Filter allTypes to get only those not in the offer list
			List<ProductTypes> notOfferedTypes = allTypes.stream()
			    .filter(type -> !offeredTypeIds.contains(type.getId()))
			    .collect(Collectors.toList());
			
			return notOfferedTypes;

		
	}

	public void addOffer(CategoryOfferDto offerDto) {
		// TODO Auto-generated method stub
		CategoryOffer offer=new CategoryOffer();
				offer.setCategoryOffer(offerDto.getCategoryOffer());
				offer.setOfferName(offerDto.getOfferName());
				ProductTypes productType=productTypeRepo.findById(offerDto.getProductTypeId()).orElseThrow(()->new ResourceNotFoundException("Category not found"));
				offer.setProductType(productType);
				offer.setStatus(OfferStatus.Active);
				OfferRepo.save(offer);
	}

	public Page<CategoryOffer> getOffers(int page, int size, String keyword) {
		// TODO Auto-generated method stub
		 Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

		    if (keyword != null && !keyword.isBlank()) {
		        List<ProductTypes> types = productTypeRepo.findByNameContainingIgnoreCase(keyword);

		        if (!types.isEmpty()) {
		            return OfferRepo.findByProductTypeIn(types, pageable);
		        } else {
		            return Page.empty(pageable);
		        }
		    }

		    return OfferRepo.findAll(pageable);

		}

	public CategoryOfferDto getOffers(int id) {
		// TODO Auto-generated method stub
		CategoryOffer offer=OfferRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Offer not found"));
		CategoryOfferDto dto=new CategoryOfferDto();
		dto.setCategoryOffer(offer.getCategoryOffer());
		dto.setId(offer.getId());
		dto.setOfferName(offer.getOfferName());
		dto.setProductTypeName(offer.getProductType().getName()+"("+offer.getProductType().getGender()+")");
		dto.setProductTypeId(offer.getProductType().getId());
		return dto;
	}

	public void editOffer(@Valid CategoryOfferDto offerDto) {
		// TODO Auto-generated method stub
		CategoryOffer offer=OfferRepo.findById(offerDto.getId()).orElseThrow(()-> new ResourceNotFoundException("Offer not found"));
		offer.setCategoryOffer(offerDto.getCategoryOffer());
		offer.setOfferName(offerDto.getOfferName());
		OfferRepo.save(offer);
	}
	@Transactional
	public void changeStatus(int id) {
		// TODO Auto-generated method stub
		CategoryOffer offer=OfferRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Offer Not Found"));
		if(offer.getStatus()==OfferStatus.Active) {
			offer.setStatus(OfferStatus.Disable);
		}
		else {
			 ProductTypes productType=offer.getProductType();
			 List<CategoryOffer> activeOffers=OfferRepo.findByProductTypeAndStatus(productType,OfferStatus.Active);
			 if (!activeOffers.isEmpty()) {
		            throw new IllegalStateException("An active offer for this category already exists. Please disable it first.");
		        }
			offer.setStatus(OfferStatus.Active);
		}
		OfferRepo.save(offer);
	}
}
