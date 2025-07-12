package org.example.ktigerstudybe.service.lesson;

import org.example.ktigerstudybe.dto.req.LessonRequest;
import org.example.ktigerstudybe.dto.req.UserXPUpdateRequest;
import org.example.ktigerstudybe.dto.resp.LessonResponse;
import org.example.ktigerstudybe.dto.resp.LessonWithProgressResponse;
import org.example.ktigerstudybe.dto.resp.UserXPResponse;
import org.example.ktigerstudybe.model.Lesson;
import org.example.ktigerstudybe.model.UserProgress;
import org.example.ktigerstudybe.repository.LessonRepository;
import org.example.ktigerstudybe.repository.UserProgressRepository;
import org.example.ktigerstudybe.repository.UserRepository;
import org.example.ktigerstudybe.service.userxp.UserXPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LessonServiceImpl implements LessonService {

    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private UserProgressRepository userProgressRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserXPService userXPService;

    // Mapping từ Entity sang Response DTO
    private LessonResponse toResponse(Lesson lesson) {
        LessonResponse resp = new LessonResponse();
        resp.setLessonId(lesson.getLessonId());
        resp.setLessonName(lesson.getLessonName());
        resp.setLessonDescription(lesson.getLessonDescription());
        return resp;
    }

    // Mapping từ Request DTO sang Entity (cho create mới)
    private Lesson toEntity(LessonRequest req) {
        Lesson lesson = new Lesson();
        lesson.setLessonName(req.getLessonName());
        lesson.setLessonDescription(req.getLessonDescription());
        return lesson;
    }

    @Override
    public LessonResponse createLesson(LessonRequest request) {
        Lesson lesson = toEntity(request);
        lesson = lessonRepository.save(lesson);
        return toResponse(lesson);
    }

    @Override
    public LessonResponse updateLesson(Long lessonId, LessonRequest request) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new IllegalArgumentException("Lesson not found with id: " + lessonId));
        lesson.setLessonName(request.getLessonName());
        lesson.setLessonDescription(request.getLessonDescription());
        lesson = lessonRepository.save(lesson);
        return toResponse(lesson);
    }

    @Override
    public List<LessonResponse> getAllLessons() {
        return lessonRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public LessonResponse getLessonById(Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new IllegalArgumentException("Lesson not found with id: " + lessonId));
        return toResponse(lesson);
    }

    @Override
    public void deleteLesson(Long lessonId) {
        lessonRepository.deleteById(lessonId);
    }

    @Override
    public List<LessonResponse> getLessonsByLevelId(Long levelId) {
        return lessonRepository.findByLevel_LevelId(levelId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<LessonWithProgressResponse> getLessonsWithProgress(Long levelId, Long userId) {
        List<Lesson> lessons = lessonRepository.findByLevel_LevelId(levelId);
        List<UserProgress> progressList = userProgressRepository.findByUser_UserId(userId);

        Map<Long, Boolean> completedMap = progressList.stream()
                .filter(UserProgress::getIsLessonCompleted)
                .collect(Collectors.toMap(
                        p -> p.getLesson().getLessonId(),
                        p -> true
                ));

        List<LessonWithProgressResponse> response = new ArrayList<>();

        for (int i = 0; i < lessons.size(); i++) {
            Lesson l = lessons.get(i);
            LessonWithProgressResponse dto = new LessonWithProgressResponse();
            dto.setLessonId(l.getLessonId());
            dto.setLessonName(l.getLessonName());
            dto.setLessonDescription(l.getLessonDescription());
            dto.setLessonCompleted(completedMap.containsKey(l.getLessonId()));

            if (i == 0) {
                dto.setLocked(false); // bài đầu tiên luôn mở
            } else {
                Long prevLessonId = lessons.get(i - 1).getLessonId();
                boolean prevCompleted = completedMap.containsKey(prevLessonId);
                dto.setLocked(!prevCompleted);
            }
            response.add(dto);
        }

        return response;
    }

    //admin
    // admin
    @Override
    public Page<LessonResponse> getLessons(int page, int size, Long levelId, String keyword) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("lessonName").ascending());
        Page<Lesson> pageData;
        if (levelId != null && keyword != null && !keyword.isEmpty()) {
            pageData = lessonRepository.findByLessonNameContainingIgnoreCaseAndLevel_LevelId(
                    keyword, levelId, pageable);
        } else if (levelId != null) {
            pageData = lessonRepository.findByLevel_LevelId(levelId, pageable);
        } else if (keyword != null && !keyword.isEmpty()) {
            pageData = lessonRepository.findByLessonNameContainingIgnoreCase(keyword, pageable);
        } else {
            pageData = lessonRepository.findAll(pageable);
        }
        return pageData.map(this::toResponse);
    }

    @Override
    @Transactional
    public Map<String, Object> completeLesson(Long userId, Long lessonId, Integer score) {
        // ✅ Kiểm tra UserProgress để xem đã hoàn thành chưa
        Optional<UserProgress> existingProgressOpt = userProgressRepository
                .findByUser_UserIdAndLesson_LessonId(userId, lessonId);

        UserProgress existingProgress = existingProgressOpt.orElse(null);

        // ✅ Kiểm tra lần đầu hoàn thành dựa trên IsLessonCompleted
        boolean isFirstTimeCompletion = (existingProgress == null ||
                !existingProgress.getIsLessonCompleted());

        // ✅ Cập nhật/tạo mới UserProgress (chỉ lưu trạng thái hoàn thành)
        UserProgress progress = existingProgress != null ? existingProgress : new UserProgress();
        progress.setUser(userRepository.findById(userId).orElseThrow());
        progress.setLesson(lessonRepository.findById(lessonId).orElseThrow());
        progress.setIsLessonCompleted(true);
        progress.setLastAccessed(LocalDateTime.now());

        userProgressRepository.save(progress);

        // ✅ Chỉ cộng XP nếu là lần đầu hoàn thành
        UserXPResponse xpData = null;
        if (isFirstTimeCompletion) {
            // Tạo request object
            UserXPUpdateRequest xpRequest = new UserXPUpdateRequest();
            xpRequest.setUserId(userId);
            xpRequest.setXpToAdd(score);

            xpData = userXPService.addXP(xpRequest);
        }

        return Map.of(
                "completed", true,
                "isFirstTime", isFirstTimeCompletion,
                "xpAdded", isFirstTimeCompletion,
                "xpData", xpData != null ? xpData : Map.of()
        );
    }
}
