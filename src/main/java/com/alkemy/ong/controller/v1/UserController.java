package com.alkemy.ong.controller.v1;

import com.alkemy.ong.exception.ErrorDetails;
import com.alkemy.ong.dto.UserPatchDTO;
import com.alkemy.ong.exception.UserNotFoundException;
import com.alkemy.ong.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.alkemy.ong.controller.ControllerConstants.V_1_USERS;

@RestController
@RequestMapping(V_1_USERS)
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    /**
     * Este metodo modifica los campos firstName, lastName y photo de User.
     * @param id Id del User a patchear.
     * @param patchDto Dto con los cambios a realizar.
     */

    @Operation(summary = "Update user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Update user",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid field",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDetails.class)) }),
            @ApiResponse(responseCode = "404", description = "Invalid id supplied",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDetails.class)) })
    })
    @PatchMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void userPatch(
            @PathVariable("id") Long id,
            @Valid @RequestBody UserPatchDTO patchDto){
            service.userPatch(id, patchDto);
    }


    @Operation(summary = "Delete user by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Delete the user",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDetails.class)) })
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("id") long id) {
        service.deleteUser(id);
    }
}
