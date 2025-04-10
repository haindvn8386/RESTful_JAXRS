package restful.jaxrs.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import restful.jaxrs.service.SeedDataService;
import restful.jaxrs.util.ApiResponse;

@RestController
@RequestMapping("/api/v1/fakes")
public class FakeController {

    @Autowired
    private SeedDataService fake;

    /// fake data
    @PostMapping
    public ResponseEntity<ApiResponse<String>> createFakeUser() {
        String result = fake.addUserFake();
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.CREATED.value(), "User created successfully", result));

    }
}
