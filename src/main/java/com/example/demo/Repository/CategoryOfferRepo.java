package com.example.demo.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.CategoryOffer;
import com.example.demo.model.OfferStatus;
import com.example.demo.model.ProductTypes;
import com.example.demo.model.Status;

public interface CategoryOfferRepo extends JpaRepository<CategoryOffer,Integer>{

	List<CategoryOffer> findByStatus(OfferStatus active);




	Page<CategoryOffer> findByProductType(ProductTypes productTypes, Pageable pageable);




	Page<CategoryOffer> findByProductTypeIn(List<ProductTypes> types, Pageable pageable);








	List<CategoryOffer> findByProductTypeAndStatus(ProductTypes productType, OfferStatus active);


}
