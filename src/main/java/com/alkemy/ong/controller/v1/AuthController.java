package com.alkemy.ong.controller.v1;

import com.alkemy.ong.exception.ErrorDetails;
import com.alkemy.ong.dto.UserDto;
import com.alkemy.ong.dto.UserResponseDto;
import com.alkemy.ong.dto.RegisterRequest;
import com.alkemy.ong.service.AuthService;
import com.alkemy.ong.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.alkemy.ong.controller.ControllerConstants.V_1_AUTH;

@RestController
@RequiredArgsConstructor
@RequestMapping(V_1_AUTH)
public class AuthController {

    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private AuthService authService;

    @Operation(summary = "Add a new user to the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Create user",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid field",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDetails.class)) })
    })
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> saveUser(@Valid @RequestBody RegisterRequest user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveUser(user));
    }


    @Operation(summary = "Get an user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the user",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class)) }),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDetails.class)) })
            })
    @GetMapping("/me")
    public ResponseEntity<UserDto> userLogged(){
        return new ResponseEntity<>(authService.getUserLogged(), HttpStatus.OK);
    }
}
