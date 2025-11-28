package com.mig.patientservice.web.patient;

import static com.mig.patientservice.web.patient.UserController.BASE_PATH;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mig.patientservice.service.patient.UserService;
import com.mig.patientservice.web.patient.request.User;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin("*")
@RestController
@RequestMapping(path = BASE_PATH)
@Slf4j
public class UserController {
	
	static final String BASE_PATH = "/v1/user";
	
	private final UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
		@RequestMapping(
			method = PATCH,
			produces = APPLICATION_JSON_VALUE,
			consumes = APPLICATION_JSON_VALUE)
		//@PreAuthorize("hasAuthority('create:scan')")
		@ApiOperation(value = "Create scan", authorizations = {
			@Authorization(value = "auth0", scopes = {
				@AuthorizationScope(scope = "create:scan", description = "")
			})
		})
		public ResponseEntity<?> updateUser(@RequestBody User user) throws Exception {
			userService.updateUserMetadata(user);
			return ResponseEntity.noContent().build();
		}

}
