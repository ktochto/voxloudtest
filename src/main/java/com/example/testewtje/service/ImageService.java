package com.example.testewtje.service;

import com.example.testewtje.model.dto.Image;
import com.example.testewtje.model.entyties.ImageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ImageService {

    List<ImageEntity> getAllImages();
    
    Page<ImageEntity> getAllImagesByProperty(Image image, Pageable pageable);

    void createImages(List<Image> images, String username);

    void deleteImage(Long id);

    void modifyImage(Long id, Image image);

    ImageEntity findById(Long id);

}
