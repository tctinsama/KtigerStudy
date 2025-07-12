package org.example.ktigerstudybe.service.classService;

import org.example.ktigerstudybe.dto.req.ClassRequest;
import org.example.ktigerstudybe.dto.resp.ClassResponse;
import org.example.ktigerstudybe.model.ClassEntity;
import org.example.ktigerstudybe.model.User;
import org.example.ktigerstudybe.repository.ClassRepository;
import org.example.ktigerstudybe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClassServiceImpl implements ClassService {

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private UserRepository userRepository;

    private ClassResponse toResponse(ClassEntity entity) {
        ClassResponse res = new ClassResponse();
        res.setClassId(entity.getClassId());
        res.setClassName(entity.getClassName());
        res.setDescription(entity.getDescription());
        res.setUserId(entity.getUser().getUserId());
        res.setUserFullName(entity.getUser().getFullName());
        res.setCreatedAt(entity.getCreatedAt());
        res.setPassword(entity.getPassword());
        return res;
    }

    @Override
    public List<ClassResponse> getAllClasses() {
        return classRepository.findAll()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public ClassResponse getClassById(Long classId) {
        ClassEntity entity = classRepository.findById(classId)
                .orElseThrow(() -> new IllegalArgumentException("Not found ClassID: " + classId));
        return toResponse(entity);
    }

    @Override
    public ClassResponse createClass(ClassRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        ClassEntity entity = ClassEntity.builder()
                .className(request.getClassName())
                .description(request.getDescription())
                .user(user)
                .password(request.getPassword())
                .build();

        return toResponse(classRepository.save(entity));
    }

    @Override
    public ClassResponse updateClass(Long classId, ClassRequest request) {
        ClassEntity entity = classRepository.findById(classId)
                .orElseThrow(() -> new IllegalArgumentException("Not found ClassID: " + classId));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        entity.setClassName(request.getClassName());
        entity.setDescription(request.getDescription());
        entity.setUser(user);
        entity.setPassword(request.getPassword());

        return toResponse(classRepository.save(entity));
    }
    @Override
    public ClassResponse getClassByIdAndPassword(Long classId, String password) {
        ClassEntity entity = classRepository.findByClassIdAndPassword(classId, password)
                .orElseThrow(() -> new IllegalArgumentException("Class not found or wrong password"));
        return toResponse(entity);
    }


    @Override
    public void deleteClass(Long classId) {
        if (!classRepository.existsById(classId)) {
            throw new IllegalArgumentException("Cannot delete: Class not found");
        }
        classRepository.deleteById(classId);
    }

    @Override
    public List<ClassResponse> getClassesByUserId(Long userId) {
        return classRepository.findByUser_UserId(userId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }
}
