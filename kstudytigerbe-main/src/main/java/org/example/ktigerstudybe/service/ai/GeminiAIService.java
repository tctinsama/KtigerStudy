package org.example.ktigerstudybe.service.ai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class GeminiAIService {

    private static final Logger logger = LoggerFactory.getLogger(GeminiAIService.class);

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Value("${gemini.api.url:https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent}")
    private String geminiApiUrl;

    @Value("${gemini.api.mock:false}")
    private boolean useMockResponse;

    private final RestTemplate restTemplate;
    private final Random random = new Random();

    public GeminiAIService() {
        this.restTemplate = new RestTemplate();
    }

    public String generateKoreanResponse(String userMessage, String scenario, String difficulty) {
        if (useMockResponse) {
            logger.info("Using mock response for scenario: {}, difficulty: {}, message: {}",
                    scenario, difficulty, userMessage);
            return getMockResponse(userMessage, scenario, difficulty);
        }

        try {
            String prompt = buildKoreanPrompt(userMessage, scenario, difficulty);
            logger.info("Calling Gemini API with prompt length: {}", prompt.length());

            Map<String, Object> requestBody = Map.of(
                    "contents", List.of(
                            Map.of("parts", List.of(
                                    Map.of("text", prompt)
                            ))
                    ),
                    "generationConfig", Map.of(
                            "temperature", 0.8,
                            "maxOutputTokens", 150,
                            "topP", 0.9,
                            "topK", 40
                    )
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            String url = geminiApiUrl + "?key=" + geminiApiKey;
            ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);

            String result = extractResponseText(response.getBody());
            logger.info("Gemini API response: {}", result);
            return result;

        } catch (Exception e) {
            logger.error("Gemini API Error: {}", e.getMessage(), e);
            logger.info("Falling back to mock response");
            return getMockResponse(userMessage, scenario, difficulty);
        }
    }

    private String buildKoreanPrompt(String userMessage, String scenario, String difficulty) {
        String rolePrompt = switch (scenario) {
            case "restaurant" ->
                    "Bạn là Min-jun (민준), 25 tuổi, nhân viên nhà hàng Hàn Quốc vui vẻ và am hiểu về đồ ăn. " +
                            "Bạn rất thích giới thiệu món ăn ngon và luôn hỏi thêm về sở thích của khách để tư vấn phù hợp. " +
                            "Bạn biết rõ về vị, cách chế biến, giá cả và có thể so sánh các món ăn. ";

            case "shopping" ->
                    "Bạn là So-young (소영), 23 tuổi, nhân viên bán hàng thời trang nhiệt tình. " +
                            "Bạn hiểu rõ về sản phẩm, style, và luôn tư vấn tận tình để khách hàng hài lòng. " +
                            "Bạn có thể mô tả chi tiết về chất liệu, màu sắc, cách phối đồ. ";

            case "direction" ->
                    "Bạn là Hyun-woo (현우), 28 tuổi, sinh viên Seoul rất quen thuộc với địa điểm trong thành phố. " +
                            "Bạn thích giúp đỡ du khách và luôn đưa ra hướng dẫn chi tiết, dễ hiểu. " +
                            "Bạn biết về giao thông, thời gian di chuyển và các landmark nổi tiếng. ";

            case "introduction" ->
                    "Bạn là Ji-hye (지혜), 24 tuổi, sinh viên đại học Seoul thân thiện và cởi mở. " +
                            "Bạn thích làm quen với bạn bè quốc tế và luôn tò mò về văn hóa của người khác. " +
                            "Bạn hay chia sẻ về cuộc sống, sở thích và hỏi thăm về đối phương. ";

            case "daily" ->
                    "Bạn là Tae-min (태민), 26 tuổi, bạn thân của họ đã quen biết 2 năm. " +
                            "Bạn hay nói chuyện về cuộc sống hàng ngày, công việc, thời tiết, phim ảnh, âm nhạc. " +
                            "Bạn rất thoải mái và thân thiết, thường dùng ngôn ngữ thân mật. ";

            default -> "Bạn là một người Hàn Quốc thân thiện 25 tuổi. ";
        };

        String personalityPrompt = switch (scenario) {
            case "restaurant" ->
                    "Tính cách: Nhiệt tình, am hiểu ẩm thực, thích mô tả chi tiết về món ăn. " +
                            "Luôn hỏi thêm về khẩu vị (매운 맛, 담백한 맛, 달콤한 맛) để tư vấn phù hợp. " +
                            "Thích kể về nguồn gốc món ăn và cách ăn ngon nhất. ";

            case "shopping" ->
                    "Tính cách: Thời trang, tỉ mỉ, thích tư vấn style. " +
                            "Luôn hỏi về dịp mặc, sở thích màu sắc, và budget để gợi ý phù hợp. " +
                            "Thích so sánh sản phẩm và giải thích lý do chọn. ";

            case "direction" ->
                    "Tính cách: Nhiệt tình giúp đỡ, rất quen địa bàn Seoul. " +
                            "Luôn đưa ra ít nhất 2 cách đi và cho biết thời gian, chi phí. " +
                            "Thích gợi ý thêm địa điểm hay gần đó. ";

            case "introduction" ->
                    "Tính cách: Tò mò, thân thiện, thích tìm hiểu về văn hóa khác. " +
                            "Luôn hỏi đáp lại và chia sẻ về bản thân. " +
                            "Thích hỏi về ấn tượng đầu tiên về Hàn Quốc. ";

            case "daily" ->
                    "Tính cách: Thoải mái, hay trêu đùa nhẹ nhàng, quan tâm bạn bè. " +
                            "Thích chia sẻ về chuyện hàng ngày và hỏi ý kiến. " +
                            "Hay dùng từ ngữ thân mật như 야, 진짜, 대박. ";

            default -> "Tính cách: Thân thiện và nhiệt tình. ";
        };

        String difficultyPrompt = switch (difficulty) {
            case "beginner" ->
                    "Ngôn ngữ: Dùng từ vựng cơ bản, câu ngắn 1-2 câu. " +
                            "Thi thoảng thêm romanization cho từ khó: 안녕하세요 (annyeonghaseyo). " +
                            "Nói chậm rãi, rõ ràng, dễ hiểu. ";

            case "intermediate" ->
                    "Ngôn ngữ: Dùng từ vựng thông dụng, câu trung bình 2-3 câu. " +
                            "Đôi khi dùng ngữ pháp vừa phải, giải thích nghĩa nếu cần. " +
                            "Tự nhiên nhưng không quá khó. ";

            case "advanced" ->
                    "Ngôn ngữ: Tiếng Hàn tự nhiên như người bản địa. " +
                            "Có thể dùng slang trẻ, thành ngữ, cách nói địa phương Seoul. " +
                            "Nói nhanh và tự nhiên như bạn bè thật. ";

            default -> "Ngôn ngữ: Vừa phải, tự nhiên. ";
        };

        String contextPrompt = switch (scenario) {
            case "restaurant" ->
                    "Bối cảnh: Nhà hàng Hàn Quốc truyền thống ở Myeongdong, Seoul. " +
                            "Menu có: 김치찌개 (8,000원), 불고기 (15,000원), 비빔밥 (9,000원), " +
                            "삼겹살 (12,000원), 제육볶음 (10,000원), 냉면 (8,000원), 순두부찌개 (7,000원). " +
                            "Đang là giờ ăn trưa, nhà hàng khá đông. ";

            case "shopping" ->
                    "Bối cảnh: Cửa hàng thời trang ở Hongdae, Seoul. " +
                            "Có quần áo casual, formal, phụ kiện. Đang có sale 20-30%. " +
                            "Size từ XS đến XL, nhiều màu sắc trendy. ";

            case "direction" ->
                    "Bối cảnh: Ga tàu điện Gangnam, Seoul vào buổi chiều. " +
                            "Có subway, bus, taxi. Traffic hơi đông. " +
                            "Nhiều landmark nổi tiếng gần đó. ";

            case "introduction" ->
                    "Bối cảnh: Café ở Hongdae vào cuối tuần. " +
                            "Không khí thoải mái, nhiều bạn trẻ. " +
                            "Đang uống coffee và trò chuyện làm quen. ";

            case "daily" ->
                    "Bối cảnh: Cuối tuần ở Seoul, thời tiết đẹp. " +
                            "Đang nhắn tin qua KakaoTalk hoặc gặp mặt tại café. " +
                            "Tâm trạng thoải mái, muốn chia sẻ và tám chuyện. ";

            default -> "Bối cảnh: Đang ở Seoul, Hàn Quốc. ";
        };

        String responseRules =
                "QUY TẮC TRẢ LỜI: " +
                        "1. CHỈ trả lời bằng tiếng Hàn, KHÔNG dịch, KHÔNG giải thích. " +
                        "2. Trả lời 2-4 câu tiếng Hàn, tự nhiên như người thật. " +
                        "3. Thể hiện tính cách và cảm xúc rõ ràng. " +
                        "4. Hỏi lại hoặc gợi ý để tiếp tục cuộc trò chuyện. " +
                        "5. Dùng emoji phù hợp (😊, 😄, 🤔, 👍) nhưng không quá nhiều. " +
                        "6. Phản ứng cụ thể với nội dung tin nhắn của người dùng. " +
                        "7. Đưa ra thông tin chi tiết, hữu ích trong ngữ cảnh. "+
                        "8. LUÔN sử dụng dấu câu rõ ràng cho từng câu: dấu chấm (.), dấu hỏi (?), dấu cảm thán (!) ở cuối câu phù hợp. Không được bỏ dấu câu.";
                        String examplePrompt = switch (scenario) {
            case "restaurant" ->
                    "\nVÍ DỤ CÁCH TRẢ LỜI:\n" +
                            "User: 메뉴 추천해 주세요\n" +
                            "AI: 오늘 김치찌개가 정말 맛있어요! 김치가 잘 익어서 국물이 깊고 시원해요. 매운 거 좋아하세요? 아니면 순한 제육볶음도 인기 많아요! 🍽️\n\n" +
                            "User: 너무 싱거워요\n" +
                            "AI: 아, 그러시구나! 그럼 매콤한 걸로 바꿔드릴게요. 김치찌개나 부대찌개 어떠세요? 아니면 양념이 진한 불고기도 맛있어요! 어떤 맛 선호하세요? 🌶️";

            case "shopping" ->
                    "\nVÍ DỤ CÁCH TRẢ LỜI:\n" +
                            "User: 이거 얼마예요?\n" +
                            "AI: 이 셔츠요? 원래 5만원인데 지금 30% 할인해서 3만 5천원이에요! 면 100%라서 착용감이 정말 좋아요. 사이즈 몇 찾으세요? 👕";

            case "direction" ->
                    "\nVÍ DỤ CÁCH TRẢ LỜI:\n" +
                            "User: 명동 어떻게 가요?\n" +
                            "AI: 여기서 명동까지는 지하철이 제일 빨라요! 2호선 타고 을지로입구에서 4호선으로 갈아타시면 돼요. 약 20분 걸려요. 아니면 택시로 15분? 지금 교통이 좀 막혀서 지하철 추천해요! 🚇";

            case "introduction" ->
                    "\nVÍ DỤ CÁCH TRẢ LỜI:\n" +
                            "User: 안녕하세요!\n" +
                            "AI: 안녕하세요! 처음 뵙겠습니다 😊 저는 지혜라고 해요. 이름이 뭐예요? 한국 처음이세요?";

            case "daily" ->
                    "\nVÍ DỤ CÁCH TRẢ LỜI:\n" +
                            "User: 오늘 뭐 해?\n" +
                            "AI: 야! 오늘 카페에서 공부하고 있어 😅 너무 심심해 죽겠다. 너는 뭐 해? 날씨 좋은데 같이 한강 갈래?";

            default -> "";
        };

        return rolePrompt + personalityPrompt + difficultyPrompt + contextPrompt + responseRules + examplePrompt +
                "\n\n현재 상황에서 사용자가 말했습니다: \"" + userMessage + "\"" +
                "\n당신의 자연스러운 응답 (tiếng Hàn only):";
    }

    private String extractResponseText(Map<String, Object> responseBody) {
        try {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) responseBody.get("candidates");
            if (candidates != null && !candidates.isEmpty()) {
                @SuppressWarnings("unchecked")
                Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
                if (content != null) {
                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
                    if (parts != null && !parts.isEmpty()) {
                        String text = (String) parts.get(0).get("text");
                        return cleanResponse(text);
                    }
                }
            }
            return "죄송해요, 다시 말해 주세요.";
        } catch (Exception e) {
            logger.error("Response parsing error: {}", e.getMessage(), e);
            return "미안해요, 잘 못 들었어요.";
        }
    }

    // [CHANGED] Thêm hàm dịch sang tiếng Việt
    public String translateToVietnamese(String koreanText) {
        try {
            // Prompt yêu cầu dịch sang tiếng Việt tự nhiên
            String prompt = "Hãy dịch câu sau sang tiếng Việt tự nhiên, không thêm giải thích:\n" + koreanText;

            Map<String, Object> requestBody = Map.of(
                    "contents", List.of(
                            Map.of("parts", List.of(
                                    Map.of("text", prompt)
                            ))
                    ),
                    "generationConfig", Map.of(
                            "temperature", 0.2,
                            "maxOutputTokens", 150
                    )
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            String url = geminiApiUrl + "?key=" + geminiApiKey;

            ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);
            String result = extractResponseText(response.getBody());
            logger.info("Gemini Translate result: {}", result);
            return result;
        } catch (Exception e) {
            logger.error("Gemini Translate Error: {}", e.getMessage(), e);
            return "(Không dịch được)";
        }
    }





    // giữ dấu câu và không xóa quá nhiều
    private String cleanResponse(String response) {
        if (response == null) return "네, 알겠어요!";
        // Chỉ loại bỏ phần giải thích không cần thiết, giữ lại dấu chấm, dấu hỏi, dấu cảm
        String cleaned = response
                .replaceAll("\\([^)]*\\)", "")  // xóa (phần giải thích)
                .replaceAll("\\[[^]]*\\]", "")  // xóa [phần giải thích]
                .replaceAll("^AI:", "")         // xóa tiền tố AI:
                .replaceAll("^User:", "")       // xóa tiền tố User:
                .trim();
        return cleaned.isEmpty() ? "네!" : cleaned.trim();
    }

    private boolean containsKorean(String text) {
        return text.matches(".*[가-힣].*");
    }

    private String getMockResponse(String userMessage, String scenario, String difficulty) {
        // Simulate API delay
        try {
            Thread.sleep(800 + random.nextInt(1200)); // 0.8-2s delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        String message = userMessage.toLowerCase();

        return switch (scenario) {
            case "restaurant" -> {
                if (message.contains("메뉴") || message.contains("추천")) {
                    yield getRandomRestaurantRecommendation(difficulty);
                } else if (message.contains("싱겁") || message.contains("다른") || message.contains("조절")) {
                    yield "아, 그러시구나! 그럼 매콤한 걸로 바꿔드릴게요. 김치찌개나 부대찌개 어떠세요? 양념이 진해서 맛있어요! 🌶️";
                } else if (message.contains("맛있는") || message.contains("맛있")) {
                    yield "오늘 특별히 제육볶음이랑 김치찌개가 정말 맛있어요! 제육볶음은 달콤매콤하고 고기가 부드러워요. 김치찌개는 국물이 깊어요. 어떤 걸 드셔보실래요? 😋";
                } else if (message.contains("얼마") || message.contains("가격")) {
                    yield "김치찌개는 8천원, 제육볶음은 만원, 불고기는 1만 5천원이에요. 밥이랑 반찬은 무료로 나와요! 가성비 정말 좋죠? 💰";
                } else if (message.contains("주문")) {
                    yield "네! 뭘 드시고 싶으세요? 음료는 어떻게 하시겠어요? 콜라, 사이다, 맥주 다 있어요! 🥤";
                } else if (message.contains("계산")) {
                    yield "네, 총 1만 5천원 나왔어요. 현금으로 하시겠어요, 아니면 카드로 결제하시겠어요? 💳";
                } else if (message.contains("안녕")) {
                    yield "어서오세요! 몇 분이세요? 오늘 날씨 좋은데 따뜻한 국물 어떠세요? 😊";
                } else {
                    yield "네, 말씀하세요! 뭔가 더 필요하신 거 있으면 언제든 불러주세요! 👍";
                }
            }
            case "shopping" -> {
                if (message.contains("얼마") || message.contains("가격")) {
                    yield "이거요? 원래 5만원인데 지금 30% 할인해서 3만 5천원이에요! 면 100%라서 정말 편하고 세탁도 쉬워요. 👕";
                } else if (message.contains("사이즈")) {
                    yield "S부터 XL까지 다 있어요! 한국 사이즈로 되어 있고, 실측 사이즈도 알려드릴 수 있어요. 몇 사이즈 찾으세요? 📏";
                } else if (message.contains("색깔") || message.contains("컬러")) {
                    yield "블랙, 화이트, 네이비, 베이지, 그레이 있어요. 네이비가 지금 제일 인기 많아요! 어떤 색 좋아하세요? 🎨";
                } else if (message.contains("안녕")) {
                    yield "어서오세요! 오늘 새로 들어온 옷들 구경해 보세요. 정말 예쁜 거 많아요! ✨";
                } else {
                    yield "네, 도와드릴게요! 어떤 스타일 찾으세요? 캐주얼이요, 정장이요? 👗";
                }
            }
            case "direction" -> {
                if (message.contains("지하철") || message.contains("역")) {
                    yield "지하철이 제일 빨라요! 여기서 2호선 타고 15분 정도 걸려요. 2번 출구로 나오시면 바로 보이실 거예요! 🚇";
                } else if (message.contains("버스")) {
                    yield "버스는 152번이나 360번 타시면 돼요. 저기 파란 표지판에서 기다리시면 5분마다 와요! 배차간격 짧아서 편해요. 🚌";
                } else if (message.contains("명동") || message.contains("강남")) {
                    yield "아, 거기요! 지하철로 20분 정도 걸려요. 2호선에서 4호선 갈아타시면 돼요. 택시로는 지금 교통 막혀서 30분 정도? 🗺️";
                } else if (message.contains("안녕") || message.contains("실례")) {
                    yield "네, 어디 가시려고요? 지하철이랑 버스 둘 다 방법 알려드릴게요! 😊";
                } else {
                    yield "어디로 가시려고 하세요? 가장 빠른 길 알려드릴게요! 🚇";
                }
            }
            case "introduction" -> {
                if (message.contains("이름")) {
                    yield "저는 지혜라고 해요! 대학교 3학년이에요. 이름이 뭐예요? 한국 이름도 있어요? 😊";
                } else if (message.contains("어디") || message.contains("나라")) {
                    yield "저는 서울에서 태어나고 자랐어요! 홍대 근처에 살고 있어요. 어느 나라에서 오셨어요? 🇰🇷";
                } else if (message.contains("학교") || message.contains("공부")) {
                    yield "저는 연세대학교에서 국제학 전공하고 있어요! 외국어 배우는 걸 좋아해요. 뭐 공부하세요? 📚";
                } else if (message.contains("안녕")) {
                    yield "안녕하세요! 만나서 정말 반가워요! 😄 한국 어떠세요? 처음 와보셨어요?";
                } else {
                    yield "처음 뵙겠습니다! 잘 부탁드려요. 한국 생활은 어떠세요? 👋";
                }
            }
            case "daily" -> {
                if (message.contains("날씨")) {
                    yield "야, 오늘 날씨 진짜 좋다! 🌤️ 이런 날엔 한강 가서 치킨 먹고 싶어. 너도 같이 갈래?";
                } else if (message.contains("뭐 해") || message.contains("뭐해")) {
                    yield "나 지금 넷플릭스 보면서 배달음식 먹고 있어 😅 너무 게을러졌나? 너는 뭐 하고 있어?";
                } else if (message.contains("오늘")) {
                    yield "야! 오늘 어땠어? 나는 하루 종일 과제하느라 죽는 줄 알았다 😵 너는 어땠어?";
                } else if (message.contains("주말") || message.contains("토요일") || message.contains("일요일")) {
                    yield "주말이다! 진짜 행복해 😆 오늘 뭐 할 거야? 나는 친구들이랑 홍대 가려고 해!";
                } else if (message.contains("안녕")) {
                    yield "야야! 오늘 기분 어때? 나는 아침부터 기분 좋아! ☀️";
                } else {
                    yield "응응, 맞아! 너 진짜 재미있다 😄 또 뭔가 얘기해봐!";
                }
            }
            default -> "네, 그렇군요! 더 이야기해 보세요! 😊";
        };
    }

    private String getRandomRestaurantRecommendation(String difficulty) {
        String[] recommendations = {
                "오늘 김치찌개가 정말 맛있어요! 김치가 잘 익어서 국물이 깊고 시원해요. 매운 거 좋아하세요? 🍽️",
                "제육볶음 어떠세요? 달콤매콤하고 고기가 부드러워요. 밥이랑 같이 드시면 정말 맛있어요! 🥩",
                "불고기 추천해요! 양념이 달콤하고 고기가 연해서 외국분들이 정말 좋아하세요. 어떠세요? 🥘",
                "순두부찌개는 어떠세요? 부드럽고 건강한 맛이에요. 매운 정도도 조절 가능해요! 🍲"
        };
        return recommendations[random.nextInt(recommendations.length)];
    }
}