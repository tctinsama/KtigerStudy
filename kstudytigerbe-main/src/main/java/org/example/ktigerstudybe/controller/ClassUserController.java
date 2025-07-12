package org.example.ktigerstudybe.controller;

import org.example.ktigerstudybe.dto.req.ClassUserRequest;
import org.example.ktigerstudybe.dto.resp.ClassUserResponse;
import org.example.ktigerstudybe.service.classService.ClassService;
import org.example.ktigerstudybe.service.classUser.ClassUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/class-users")
public class ClassUserController {

    @Autowired
    private ClassUserService classUserService;
    @Autowired
    private ClassService classService;


    @GetMapping
    public List<ClassUserResponse> getAll() {
        return classUserService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClassUserResponse> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(classUserService.getById(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ClassUserResponse create(@RequestBody ClassUserRequest request) {
        return classUserService.create(request);
    }

    @PostMapping("/join")
    public ResponseEntity<?> joinClass(@RequestParam Long classId,
                                       @RequestParam Long userId,
                                       @RequestParam String password) {
        try {
            // Kiểm tra classId + password có hợp lệ không
            classService.getClassByIdAndPassword(classId, password);

            // Tạo request để thêm user vào class
            ClassUserRequest request = new ClassUserRequest();
            request.setClassId(classId);
            request.setUserId(userId);

            // Thêm và trả về response
            ClassUserResponse response = classUserService.create(request);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid class ID or password");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Server error");
        }
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        classUserService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public List<ClassUserResponse> getByUser(@PathVariable Long userId) {
        return classUserService.getByUser(userId);
    }

    @GetMapping("/class/{classId}")
    public List<ClassUserResponse> getByClass(@PathVariable Long classId) {
        return classUserService.getByClass(classId);
    }

    // --- endpoint mới ---
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Void> deleteByUser(@PathVariable Long userId) {
        classUserService.deleteByUser(userId);
        return ResponseEntity.noContent().build();
    }

}
