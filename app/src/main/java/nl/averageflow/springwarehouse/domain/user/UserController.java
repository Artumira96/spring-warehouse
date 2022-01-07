package nl.averageflow.springwarehouse.domain.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import nl.averageflow.springwarehouse.domain.category.dto.CategoryResponseItem;
import nl.averageflow.springwarehouse.domain.user.dto.UpdateUserRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Tag(name = "User", description = "User API")
public class UserController {

    private final UserServiceContract userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/api/users")
    @Operation(summary = "Returns users",
            description = "Returns the requested page of users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Response",
                    content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    public Page<UserResponseItem> getUsers(final Pageable pageable) {
        return this.userService.getUsers(pageable);
    }

    @GetMapping("/api/users/{uid}")
    @Operation(summary = "Returns a single user",
            description = "Returns a single user by uuid")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Response",
                    content = @Content(schema = @Schema(implementation = CategoryResponseItem.class))),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    public UserResponseItem getUser(@PathVariable final UUID uid) {
        return this.userService.getUserByUid(uid);
    }

    @PatchMapping("/api/users/update-role")
    @Operation(summary = "Update a user's role",
            description = "Update a user's role. The requester has to have admin privileges.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Response"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    public ResponseEntity<String> updateRole(@RequestBody UpdateUserRequest request) {
        return this.userService.updateUserRole(request);
    }
}
