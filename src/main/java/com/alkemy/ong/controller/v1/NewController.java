package com.alkemy.ong.controller.v1;

import com.alkemy.ong.controller.ControllerConstants;
import com.alkemy.ong.dto.*;
import com.alkemy.ong.exception.ErrorDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.alkemy.ong.service.NewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;

import static com.alkemy.ong.controller.ControllerConstants.V_1_NEWS;

@RestController
@RequestMapping(V_1_NEWS)
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class NewController {
    private final NewService service;

    @Operation(summary = "Get New by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "New found.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = NewDetailDto.class))}),
            @ApiResponse(responseCode = "403", description = "Invalid token or token expired | Accessing with invalid role",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDetails.class))}),
            @ApiResponse(responseCode = "404", description = "New not found.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDetails.class))})
    })
    @GetMapping("/{id}")
    public ResponseEntity<NewDetailDto> getNewById(@PathVariable Long id) {
        NewDetailDto detailNew = service.getNewById(id);
        return ResponseEntity.ok().body(detailNew);
    }

    @Operation(summary = "Update New by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated New.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = NewDto.class))}),
            @ApiResponse(responseCode = "403", description = "Invalid token or token expired | Accessing with invalid role",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDetails.class))}),
            @ApiResponse(responseCode = "404", description = "New not found.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDetails.class))})
    })
    @PutMapping("/{id}")
    public ResponseEntity<NewDetailDto> saveOrUpdateNews(@RequestBody NewDto news, @PathVariable(value = "id") Long id) {

        return new ResponseEntity<NewDetailDto>(service.addNews(news, id), HttpStatus.CREATED);
    }

    @Operation(summary = "Create New.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created New",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Field cannot be empty.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDetails.class))}),
            @ApiResponse(responseCode = "403", description = "Invalid token or token expired | Accessing with invalid role",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDetails.class))}),
            @ApiResponse(responseCode = "404", description = "Category not found.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDetails.class))})
    })
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<Void> createNew(UriComponentsBuilder uriComponentsBuilder, @Valid @RequestBody NewDto dto) {
        final long newId = service.createNew(dto);
        UriComponents uriComponents = uriComponentsBuilder.path(V_1_NEWS + "/{id}").buildAndExpand(newId);
        return ResponseEntity.created(uriComponents.toUri()).build();

    }

    @GetMapping
    public ResponseEntity<NewPagedList> list(@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                             @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                             UriComponentsBuilder uriComponentsBuilder) {
        if (pageNumber == null || pageNumber < 0) {
            pageNumber = ControllerConstants.DEFAULT_PAGE_NUMBER;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = ControllerConstants.DEFAULT_PAGE_SIZE;
        }
        UriComponentsBuilder uriBuilder = uriComponentsBuilder.path(V_1_NEWS).queryParam("pageNumber={page}");
        NewPagedList pagedList = service.pagedList(PageRequest.of(pageNumber, pageSize));
        if (pagedList.hasNext()) {
            pagedList.setNextUri(uriBuilder.buildAndExpand(pageNumber + 1).toUri());
        } else {
            pagedList.setNextUri(uriBuilder.buildAndExpand(pageNumber).toUri());
        }
        if (pagedList.hasPrevious()) {
            pagedList.setBackUri(uriBuilder.buildAndExpand(pageNumber - 1).toUri());
        } else {
            pagedList.setBackUri(uriBuilder.buildAndExpand(pageNumber).toUri());
        }

        return ResponseEntity.ok(pagedList);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteNew(@PathVariable Long id) {
        service.deleteNew(id);
    }

}
