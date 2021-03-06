package com.example.testewtje.repository;

import com.example.testewtje.model.entyties.ImageEntity;
import com.example.testewtje.model.entyties.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, Long>, JpaSpecificationExecutor<ImageEntity> {

    List<ImageEntity> findByTagsIn(Collection<TagEntity> tags);

}
