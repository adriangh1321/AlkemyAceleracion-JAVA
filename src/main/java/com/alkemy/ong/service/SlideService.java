package com.alkemy.ong.service;

import com.alkemy.ong.dto.SlideDto;
import java.util.List;
import com.alkemy.ong.dto.SlideDetailDto;

public interface SlideService {

	SlideDetailDto getSlideById(Long id);

	Long saveSlide(SlideDto dto);

	List<SlideDetailDto> getAllSlides();

	List<SlideDetailDto> getAllSlidesbyOrg(Long id);

	void updateSlides(SlideDto dto, Long id);
  
	void deleteSlide(Long id);
}
