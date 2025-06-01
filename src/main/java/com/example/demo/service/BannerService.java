package com.example.demo.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.Dto.BannerDto;
import com.example.demo.Repository.BannerRepo;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.BannerModel;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class BannerService {
	@Autowired
	BannerRepo bannerRepo;
	
	@Autowired
	AdminService adminService;
	
	public List<Integer> getAvailableOrders(){
		List<BannerModel> banners=bannerRepo.findByEnabled(true);
		
		  Set<Integer> usedOrders = banners.stream()
			        .map(BannerModel::getDisplayOrder)
			        .filter(Objects::nonNull)
			        .collect(Collectors.toSet());

			    // Step 2: Build full list of possible orders (1 to 5)
			    List<Integer> allOrders = IntStream.rangeClosed(1, 5)
			        .boxed()
			        .collect(Collectors.toList());

			    // Step 3: Remove used orders
			    allOrders.removeAll(usedOrders);

			    return allOrders;
		
		
	}



	public void addBanner(@Valid BannerDto bannerdto) throws IOException {
		// TODO Auto-generated method stub
		BannerModel banner=new BannerModel();
		banner.setDescription(bannerdto.getDescription());
		if(bannerdto.getDisplayOrder()!=null) {
			Optional<BannerModel> opbanner=bannerRepo.findByDisplayOrder(bannerdto.getDisplayOrder());
			if(opbanner.isPresent()) {
				updateDisableStatus(opbanner.get().getId());
			}
			banner.setDisplayOrder(bannerdto.getDisplayOrder());
			banner.setEnabled(true);

		}
		String url=adminService.getImagePath(bannerdto.getImageUrl());
		banner.setImageUrl(url);
		banner.setTitle(bannerdto.getTitle());
		bannerRepo.save(banner);
	}

	public Page<BannerModel> getBanners(int page,int size) {
		// TODO Auto-generated method stub
		Pageable pageable=PageRequest.of(page, size,Sort.by("displayOrder"));
		return bannerRepo.findAll(pageable);
	}

	public BannerDto getBannerDto(int id) {
		// TODO Auto-generated method stub
		 BannerModel banner=bannerRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("resource not found"));
		 BannerDto dto=new BannerDto();
		 dto.setDescription(banner.getDescription());
		 dto.setDisplayOrder(banner.getDisplayOrder());
		 dto.setEnabled(banner.isEnabled());
		 dto.setId(id);
		 dto.setImage(banner.getImageUrl());
		 dto.setTitle(banner.getTitle());
		 return dto;
		 
		
		
	}
	
	public void editBanner(@Valid BannerDto bannerdto) throws IOException {
		// TODO Auto-generated method stub
		BannerModel banner=bannerRepo.findById(bannerdto.getId()).orElseThrow(()->new ResourceNotFoundException("banner not found"));
		banner.setDescription(bannerdto.getDescription());
		 Integer oldValue = banner.getDisplayOrder();
		    Integer newValue = bannerdto.getDisplayOrder();

		    BannerModel otherBanner = null;

		    // Swap logic if new displayOrder is different
		    if (newValue != null && !newValue.equals(oldValue)) {
		        Optional<BannerModel> opBannerWithNewOrder = bannerRepo.findByDisplayOrder(newValue);
		        if (opBannerWithNewOrder.isPresent()) {
		            otherBanner = opBannerWithNewOrder.get();
		            // Set it to null temporarily to avoid unique constraint violation
		            otherBanner.setDisplayOrder(null);
		            bannerRepo.save(otherBanner);
		        }

		        banner.setDisplayOrder(newValue);
		    }
		    banner.setEnabled(true);

		if(bannerdto.getImageUrl()!=null && !bannerdto.getImageUrl().isEmpty()) {
			adminService.deleteImage(banner.getImageUrl());
			String url=adminService.getImagePath(bannerdto.getImageUrl());
			banner.setImageUrl(url);

		}
		banner.setTitle(bannerdto.getTitle());
		bannerRepo.save(banner);
		if (otherBanner != null && oldValue != null) {
	        otherBanner.setDisplayOrder(oldValue);
	        bannerRepo.save(otherBanner);
	    }
	}

	public void updateDisableStatus(int id) {
		// TODO Auto-generated method stub
		BannerModel banner=bannerRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Banner not found"));
		banner.setEnabled(false);
		banner.setDisplayOrder(null);
		bannerRepo.save(banner);
		
	}

	public void updateEnableStatus(int id) {
		// TODO Auto-generated method stub
		BannerModel banner=bannerRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Banner not found"));

		List<Integer> orders=getAvailableOrders();
		if(orders.size()==0) {
	        throw new IllegalArgumentException("Already 5 active banners are present");
		}
		banner.setEnabled(true);
		banner.setDisplayOrder(orders.get(0));
		bannerRepo.save(banner);
		
	}



	public List<BannerModel> getActiveBanners() {
		// TODO Auto-generated method stub
		List<BannerModel> banners=bannerRepo.findByEnabled(true);
		banners.sort(Comparator.comparing(BannerModel::getDisplayOrder));
		return banners;
	}


}
