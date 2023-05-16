package com.boot3.myrestapi.lectures;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LectureRepository extends JpaRepository<Lecture, Integer> {

	List<Lecture> findByName(String name);
	Page<Lecture> findAll(Pageable pageable);
	Optional<Lecture> findById(Integer id);

}
