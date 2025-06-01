package com.example.demo.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.BannerModel;

public interface BannerRepo extends JpaRepository<BannerModel,Integer> {


	List<BannerModel> findByEnabled(boolean b);

	Optional<BannerModel> findByDisplayOrder(Integer displayOrder);



}
