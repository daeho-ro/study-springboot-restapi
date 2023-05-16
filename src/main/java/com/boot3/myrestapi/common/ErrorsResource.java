package com.boot3.myrestapi.common;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;
import org.springframework.hateoas.EntityModel;
import org.springframework.validation.Errors;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Getter
public class ErrorsResource extends EntityModel<Errors> {

	@JsonUnwrapped
	private final Errors errors;

	public ErrorsResource(Errors content) {
		this.errors = content;
		add(linkTo(methodOn(IndexController.class).index()).withRel("index"));
	}

}
