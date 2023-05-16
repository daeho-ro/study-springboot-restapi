package com.boot3.myrestapi.lectures.hateoas;

import com.boot3.myrestapi.lectures.LectureController;
import com.boot3.myrestapi.lectures.dto.LectureResDto;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Getter
public class LectureResource extends RepresentationModel<LectureResource> {

	public LectureResource(LectureResDto lectureResDto) {
		this.lectureResDto = lectureResDto;
		add(linkTo(LectureController.class).slash(lectureResDto.getId()).withSelfRel());
	}

	@JsonUnwrapped
	private final LectureResDto lectureResDto;

}
