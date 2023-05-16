package com.boot3.myrestapi.lectures;

import com.boot3.myrestapi.common.ErrorsResource;
import com.boot3.myrestapi.exception.BusinessException;
import com.boot3.myrestapi.lectures.dto.LectureReqDto;
import com.boot3.myrestapi.lectures.dto.LectureResDto;
import com.boot3.myrestapi.lectures.hateoas.LectureResource;
import com.boot3.myrestapi.userinfo.CurrentUser;
import com.boot3.myrestapi.userinfo.UserInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/lectures", produces = MediaTypes.HAL_JSON_VALUE)
public class LectureController {

	private final LectureRepository lectureRepository;
	private final LectureValidator lectureValidator;
	private final ModelMapper modelMapper;

	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<?> createLecture(@RequestBody @Valid LectureReqDto lectureReqDto,
										   Errors errors,
										   @CurrentUser UserInfo currentUser) {
		lectureValidator.validate(lectureReqDto, errors);
		if (errors.hasErrors()) {
			return ResponseEntity.badRequest().body(new ErrorsResource(errors));
		}

		Lecture lecture = modelMapper.map(lectureReqDto, Lecture.class);
		lecture.update();
		lecture.setUserInfo(currentUser);
		Lecture addLecture = lectureRepository.save(lecture);

		LectureResDto lectureResDto = modelMapper.map(addLecture, LectureResDto.class);
		WebMvcLinkBuilder selfLinkBuilder = linkTo(LectureController.class).slash(lectureResDto.getId());
		URI createUri = selfLinkBuilder.toUri();

		LectureResource lectureResource = new LectureResource(lectureResDto);
		lectureResource.add(linkTo(LectureController.class).withRel("query-lectures"));
		lectureResource.add(selfLinkBuilder.withRel("update-lecture"));

		return ResponseEntity.created(createUri).body(lectureResource);
	}

	@GetMapping("/all")
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public ResponseEntity<?> queryLectures(Pageable pageable,
										   PagedResourcesAssembler<LectureResDto> assembler,
										   @CurrentUser UserInfo currentUser) {
		Page<Lecture> page = lectureRepository.findAll(pageable);
		Page<LectureResDto> lectureResDtoPage = page.map(lecture -> modelMapper.map(lecture, LectureResDto.class));
		PagedModel<LectureResource> pagedResources =
				assembler.toModel(lectureResDtoPage, LectureResource::new);

		if(currentUser != null) {
			pagedResources.add(linkTo(LectureController.class).withRel("create-Lecture"));
		}
		return ResponseEntity.ok(pagedResources);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public ResponseEntity<?> getLecture(@PathVariable Integer id, @CurrentUser UserInfo currentUser) {
		Optional<Lecture> optionalLecture = lectureRepository.findById(id);

		if (optionalLecture.isEmpty()) {
			throw new BusinessException(id + " Lecture Not Found", HttpStatus.NOT_FOUND);
		}
		Lecture lecture = optionalLecture.get();

		LectureResDto lectureResDto = modelMapper.map(lecture, LectureResDto.class);
		if (currentUser != null) {
			lectureResDto.setEmail(lecture.getUserInfo().getEmail());
		}

		LectureResource lectureResource = new LectureResource(lectureResDto);
		if ((lecture.getUserInfo() != null) && (lecture.getUserInfo().equals(currentUser))) {
			lectureResource.add(linkTo(LectureController.class)
					.slash(lecture.getId()).withRel("update-lecture"));
		}
		return ResponseEntity.ok(lectureResource);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<?> updateLecture(@PathVariable Integer id,
										   @RequestBody @Valid LectureReqDto lectureReqDto,
										   Errors errors,
										   @CurrentUser UserInfo currentUser) {
		Optional<Lecture> optionalLecture = lectureRepository.findById(id);

		if (optionalLecture.isEmpty()) {
			throw new BusinessException(id + " Lecture Not Found", HttpStatus.NOT_FOUND);
		}

		lectureValidator.validate(lectureReqDto, errors);
		if (errors.hasErrors()) {
			return ResponseEntity.badRequest().body(new ErrorsResource(errors));
		}


		Lecture existingLecture = optionalLecture.get();
		modelMapper.map(lectureReqDto, existingLecture);

		if ((existingLecture.getUserInfo() != null) && !existingLecture.getUserInfo().equals(currentUser)) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		existingLecture.update();
		Lecture savedLecture = lectureRepository.save(existingLecture);

		LectureResDto lectureResDto = modelMapper.map(savedLecture, LectureResDto.class);
		if (currentUser != null) {
			lectureResDto.setEmail(savedLecture.getUserInfo().getEmail());
		}

		LectureResource lectureResource = new LectureResource(lectureResDto);
		return ResponseEntity.ok(lectureResource);
	}

}
