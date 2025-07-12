package org.example.ktigerstudybe.controller;

import org.example.ktigerstudybe.dto.req.ChangePasswordRequest;
import org.example.ktigerstudybe.dto.req.ForgotPasswordRequest;
import org.example.ktigerstudybe.dto.req.UserRequest;
import org.example.ktigerstudybe.dto.resp.UserResponse;
import org.example.ktigerstudybe.model.User;
import org.example.ktigerstudybe.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*") // ✅ Enable CORS for testing
public class UserController {

  @Autowired
  private UserService userService;

  // Lấy tất cả user có role = "user" (có phân trang)
  @GetMapping("/learners")
  public Page<UserResponse> getAllLearners(
          @RequestParam(defaultValue = "0") int page,
          @RequestParam(defaultValue = "5") int size) {
    return userService.getAllLearners(PageRequest.of(page, size));
  }

  // Tìm kiếm user có role = "user" (phân trang)
  @GetMapping("/learners/search")
  public Page<UserResponse> searchLearners(
          @RequestParam String keyword,
          @RequestParam(defaultValue = "0") int page,
          @RequestParam(defaultValue = "5") int size) {
    return userService.searchLearners(keyword, PageRequest.of(page, size));
  }

  // Lấy user theo id
  @GetMapping("/{id}")
  public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
    try {
      UserResponse resp = userService.getUserById(id);
      return ResponseEntity.ok(resp);
    } catch (Exception e) {
      return ResponseEntity.notFound().build();
    }
  }

  // Tạo mới user
  @PostMapping
  public UserResponse createUser(@RequestBody UserRequest request) {
    return userService.createUser(request);
  }

  // Cập nhật user
  @PutMapping("/{id}")
  public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @RequestBody UserRequest request) {
    try {
      UserResponse updated = userService.updateUser(id, request);
      return ResponseEntity.ok(updated);
    } catch (Exception e) {
      return ResponseEntity.notFound().build();
    }
  }

  // Xóa user
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    userService.deleteUser(id);
    return ResponseEntity.noContent().build();
  }

  // ✅ UPDATED: Đóng băng user - Better response format
  @PostMapping("/{id}/freeze")
  public ResponseEntity<Map<String, Object>> freezeUser(@PathVariable Long id) {
    try {
      UserResponse resp = userService.freezeUser(id);

      Map<String, Object> response = new HashMap<>();
      response.put("success", true);
      response.put("message", "Tài khoản đã được đóng băng thành công");
      response.put("userId", id);
      response.put("userStatus", 0);
      response.put("userData", resp);

      System.out.println("User " + id + " has been frozen successfully");

      return ResponseEntity.ok(response);
    } catch (Exception e) {
      Map<String, Object> error = new HashMap<>();
      error.put("success", false);
      error.put("message", "Không thể đóng băng tài khoản: " + e.getMessage());
      error.put("userId", id);

      System.err.println("Failed to freeze user " + id + ": " + e.getMessage());

      return ResponseEntity.badRequest().body(error);
    }
  }

  // ✅ UPDATED: Mở băng user - Better response format
  @PostMapping("/{id}/unfreeze")
  public ResponseEntity<Map<String, Object>> unfreezeUser(@PathVariable Long id) {
    try {
      UserResponse resp = userService.unfreezeUser(id);

      Map<String, Object> response = new HashMap<>();
      response.put("success", true);
      response.put("message", "Tài khoản đã được kích hoạt thành công");
      response.put("userId", id);
      response.put("userStatus", 1);
      response.put("userData", resp);

      System.out.println("User " + id + " has been unfrozen successfully");

      return ResponseEntity.ok(response);
    } catch (Exception e) {
      Map<String, Object> error = new HashMap<>();
      error.put("success", false);
      error.put("message", "Không thể kích hoạt tài khoản: " + e.getMessage());
      error.put("userId", id);

      System.err.println("Failed to unfreeze user " + id + ": " + e.getMessage());

      return ResponseEntity.badRequest().body(error);
    }
  }

  // ✅ NEW: Get user status - Useful for admin dashboard
  @GetMapping("/{id}/status")
  public ResponseEntity<Map<String, Object>> getUserStatus(@PathVariable Long id) {
    try {
      UserResponse user = userService.getUserById(id);

      Map<String, Object> response = new HashMap<>();
      response.put("userId", id);
      response.put("userStatus", user.getUserStatus()); // Assuming UserResponse has getUserStatus()
      response.put("statusText", user.getUserStatus() == 1 ? "Hoạt động" : "Đóng băng");
      response.put("email", user.getEmail());
      response.put("fullName", user.getFullName());

      return ResponseEntity.ok(response);
    } catch (Exception e) {
      Map<String, Object> error = new HashMap<>();
      error.put("message", "Không tìm thấy user: " + e.getMessage());
      return ResponseEntity.notFound().build();
    }
  }

  // ✅ NEW: Bulk freeze/unfreeze users
  @PostMapping("/bulk-freeze")
  public ResponseEntity<Map<String, Object>> bulkFreezeUsers(@RequestBody Map<String, Object> request) {
    try {
      @SuppressWarnings("unchecked")
      java.util.List<Long> userIds = (java.util.List<Long>) request.get("userIds");

      int successCount = 0;
      int failCount = 0;

      for (Long userId : userIds) {
        try {
          userService.freezeUser(userId);
          successCount++;
        } catch (Exception e) {
          failCount++;
          System.err.println("Failed to freeze user " + userId + ": " + e.getMessage());
        }
      }

      Map<String, Object> response = new HashMap<>();
      response.put("success", true);
      response.put("message", "Đóng băng hàng loạt hoàn tất");
      response.put("successCount", successCount);
      response.put("failCount", failCount);
      response.put("totalProcessed", userIds.size());

      return ResponseEntity.ok(response);
    } catch (Exception e) {
      Map<String, Object> error = new HashMap<>();
      error.put("success", false);
      error.put("message", "Lỗi đóng băng hàng loạt: " + e.getMessage());
      return ResponseEntity.badRequest().body(error);
    }
  }

  @GetMapping("/email/{email}")
  public ResponseEntity<UserResponse> getByEmail(@PathVariable String email) {
    try {
      UserResponse resp = userService.getUserByEmail(email);
      return ResponseEntity.ok(resp);
    } catch (NoSuchElementException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @PostMapping("/change-password")
  public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request) {
    try {
      userService.changePassword(request);
      return ResponseEntity.ok("Đổi mật khẩu thành công");
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  // ✅ NEW: Test endpoint for admin functions
  @GetMapping("/admin/test")
  public ResponseEntity<Map<String, Object>> testAdminEndpoint() {
    Map<String, Object> response = new HashMap<>();
    response.put("message", "Admin user management endpoints are working");
    response.put("timestamp", System.currentTimeMillis());
    response.put("availableEndpoints", java.util.Arrays.asList(
            "POST /{id}/freeze - Đóng băng user",
            "POST /{id}/unfreeze - Mở băng user",
            "GET /{id}/status - Lấy trạng thái user",
            "POST /bulk-freeze - Đóng băng hàng loạt"
    ));

    return ResponseEntity.ok(response);
  }
}
