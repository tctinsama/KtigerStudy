package org.example.ktigerstudybe.service.classUser;

import org.example.ktigerstudybe.dto.req.ClassUserRequest;
import org.example.ktigerstudybe.dto.resp.ClassUserResponse;

import java.util.List;

public interface ClassUserService {
    List<ClassUserResponse> getAll();
    ClassUserResponse getById(Long id);
    ClassUserResponse create(ClassUserRequest request);
    void delete(Long id);

    void deleteByUser(Long userId);

    List<ClassUserResponse> getByClass(Long classId);
    List<ClassUserResponse> getByUser(Long userId);
}
