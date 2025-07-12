import { useEffect, useState } from "react";
import { getGrammarByLessonId } from "../../services/GrammarApi";

interface GrammarTheory {
  grammarId: number;
  grammarTitle: string;
  grammarContent: string;
  grammarExample: string;
}

interface GrammarProps {
  lessonId: string;
  setActiveTab?: React.Dispatch<React.SetStateAction<string>>;
}

export default function Grammar({ lessonId, setActiveTab: setLessonTab }: GrammarProps) {
  const [grammars, setGrammars] = useState<GrammarTheory[]>([]);
  const [loading, setLoading] = useState(true);
  const [expandedId, setExpandedId] = useState<number | null>(null);
  const [activeTab, setActiveTab] = useState<"all" | "learning" | "completed">("all");
  // Thêm state để theo dõi các mục ngữ pháp đã học
  const [completedIds, setCompletedIds] = useState<number[]>([]);

  useEffect(() => {
    getGrammarByLessonId(lessonId)
      .then((data) => {
        setGrammars(data);
        // Mở grammar đầu tiên tự động
        if (data.length > 0) {
          setExpandedId(data[0].grammarId);
        }
      })
      .catch((err) => console.error("Failed to fetch grammar:", err))
      .finally(() => setLoading(false));
    
    // Lấy danh sách ID đã hoàn thành từ localStorage
    const savedCompletedIds = localStorage.getItem(`grammar_completed_${lessonId}`);
    if (savedCompletedIds) {
      setCompletedIds(JSON.parse(savedCompletedIds));
    }
  }, [lessonId]);

  // Hàm xử lý khi nhấn nút "Đánh dấu đã học"
  const handleMarkAsCompleted = (grammarId: number) => {
    let newCompletedIds: number[];
    
    if (completedIds.includes(grammarId)) {
      // Nếu đã học rồi, bỏ đánh dấu
      newCompletedIds = completedIds.filter(id => id !== grammarId);
    } else {
      // Nếu chưa học, thêm vào danh sách đã học
      newCompletedIds = [...completedIds, grammarId];
    }
    
    // Cập nhật state
    setCompletedIds(newCompletedIds);
    
    // Lưu vào localStorage
    localStorage.setItem(`grammar_completed_${lessonId}`, JSON.stringify(newCompletedIds));
    
    // Gửi API để cập nhật trạng thái trên server (giả định)
    // markGrammarAsCompleted(grammarId, !completedIds.includes(grammarId))
    //   .then(() => console.log("Cập nhật trạng thái thành công"))
    //   .catch(err => console.error("Lỗi khi cập nhật trạng thái:", err));
  };

  // Lọc grammars dựa vào tab đang chọn
  const filteredGrammars = grammars.filter(grammar => {
    if (activeTab === "all") return true;
    if (activeTab === "completed") return completedIds.includes(grammar.grammarId);
    if (activeTab === "learning") return !completedIds.includes(grammar.grammarId);
    return true;
  });

  const toggleExpand = (id: number) => {
    setExpandedId(expandedId === id ? null : id);
  };

  if (loading) {
    return (
      <div className="flex justify-center items-center py-12">
        <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-blue-500"></div>
      </div>
    );
  }

  // Tính phần trăm tiến độ hoàn thành
  const completionPercentage = grammars.length > 0 
    ? Math.round((completedIds.length / grammars.length) * 100) 
    : 0;

  return (
    <div className="max-w-4xl mx-auto">
      {/* Tabs để lọc nội dung */}
      <div className="mb-6 border-b">
        <div className="flex space-x-4">
          <button 
            className={`py-3 px-4 ${activeTab === "all" ? "border-b-2 border-blue-500 text-blue-600 font-medium" : "text-gray-500"}`}
            onClick={() => setActiveTab("all")}
          >
            Tất cả
          </button>
          <button 
            className={`py-3 px-4 ${activeTab === "learning" ? "border-b-2 border-blue-500 text-blue-600 font-medium" : "text-gray-500"}`}
            onClick={() => setActiveTab("learning")}
          >
            Đang học
          </button>
          <button 
            className={`py-3 px-4 ${activeTab === "completed" ? "border-b-2 border-blue-500 text-blue-600 font-medium" : "text-gray-500"} flex items-center`}
            onClick={() => setActiveTab("completed")}
          >
            Đã hoàn thành
            {completedIds.length > 0 && (
              <span className="ml-2 bg-blue-100 text-blue-700 rounded-full px-2 py-0.5 text-xs">
                {completedIds.length}
              </span>
            )}
          </button>
        </div>
      </div>

      {filteredGrammars.length === 0 && (
        <div className="text-center py-10 bg-gray-50 rounded-xl border border-gray-200">
          <div className="text-4xl mb-3">
            {activeTab === "completed" ? "🎉" : "🔍"}
          </div>
          <p className="text-gray-500">
            {activeTab === "completed" 
              ? "Bạn chưa hoàn thành nội dung ngữ pháp nào" 
              : "Không có dữ liệu ngữ pháp cho bài học này."}
          </p>
          <p className="text-sm text-gray-400 mt-2">
            {activeTab === "completed" 
              ? "Hãy đánh dấu đã học khi bạn hiểu rõ nội dung" 
              : "Vui lòng chọn bài học khác hoặc quay lại sau."}
          </p>
        </div>
      )}

      {/* Thanh tiến độ học tập */}
      {grammars.length > 0 && (
        <div className="mb-6">
          <div className="flex justify-between items-center mb-2">
            <span className="text-sm font-medium text-gray-700">Tiến độ học ngữ pháp</span>
            <span className="text-sm font-medium text-blue-600">{completedIds.length}/{grammars.length}</span>
          </div>
          <div className="w-full bg-gray-200 rounded-full h-2.5">
            <div 
              className="bg-blue-600 h-2.5 rounded-full transition-all duration-500" 
              style={{ width: `${completionPercentage}%` }}
            ></div>
          </div>
        </div>
      )}

      <div className="space-y-6">
        {filteredGrammars.map((g, index) => {
          const isCompleted = completedIds.includes(g.grammarId);
          
          return (
            <div
              key={g.grammarId}
              className={`border rounded-xl shadow-sm transition-all duration-300 ${
                expandedId === g.grammarId ? "bg-blue-50 border-blue-200" : "bg-white"
              } ${isCompleted ? "border-l-4 border-l-green-500" : ""} hover:shadow-md`}
            >
              {/* Header - Luôn hiển thị */}
              <div 
                className="p-5 flex justify-between items-center cursor-pointer"
                onClick={() => toggleExpand(g.grammarId)}
              >
                <div className="flex items-center">
                  <div className={`w-8 h-8 rounded-full ${
                    isCompleted ? "bg-green-100 text-green-600" : "bg-blue-100 text-blue-600"
                  } flex items-center justify-center mr-3 font-bold`}>
                    {isCompleted ? "✓" : index + 1}
                  </div>
                  <h3 className="text-lg font-semibold">{g.grammarTitle}</h3>
                  {isCompleted && (
                    <span className="ml-3 px-2 py-1 bg-green-100 text-green-800 rounded text-xs font-medium">
                      Đã học
                    </span>
                  )}
                </div>
                <button className="text-gray-500 hover:bg-gray-100 p-2 rounded-full">
                  {expandedId === g.grammarId ? (
                    <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                      <polyline points="18 15 12 9 6 15"></polyline>
                    </svg>
                  ) : (
                    <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                      <polyline points="6 9 12 15 18 9"></polyline>
                    </svg>
                  )}
                </button>
              </div>

              {/* Nội dung chi tiết - Chỉ hiển thị khi được mở */}
              {expandedId === g.grammarId && (
                <div className="px-5 pb-5 space-y-4">
                  {/* Nội dung */}
                  <div className="bg-white rounded-lg p-5 border">
                    <div className="flex items-center mb-3">
                      <span className="w-8 h-8 rounded-full bg-blue-500 text-white flex items-center justify-center mr-2">
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                          <path d="M2 3h6a4 4 0 0 1 4 4v14a3 3 0 0 0-3-3H2z"></path>
                          <path d="M22 3h-6a4 4 0 0 0-4 4v14a3 3 0 0 1 3-3h7z"></path>
                        </svg>
                      </span>
                      <h4 className="text-md font-semibold text-blue-700">Nội dung</h4>
                    </div>
                    <div className="text-gray-800 whitespace-pre-line leading-relaxed pl-10">
                      {g.grammarContent}
                    </div>
                  </div>

                  {/* Ví dụ */}
                  <div className="bg-white rounded-lg p-5 border">
                    <div className="flex items-center mb-3">
                      <span className="w-8 h-8 rounded-full bg-green-500 text-white flex items-center justify-center mr-2">
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                          <line x1="8" y1="6" x2="21" y2="6"></line>
                          <line x1="8" y1="12" x2="21" y2="12"></line>
                          <line x1="8" y1="18" x2="21" y2="18"></line>
                          <line x1="3" y1="6" x2="3.01" y2="6"></line>
                          <line x1="3" y1="12" x2="3.01" y2="12"></line>
                          <line x1="3" y1="18" x2="3.01" y2="18"></line>
                        </svg>
                      </span>
                      <h4 className="text-md font-semibold text-green-700">Ví dụ</h4>
                    </div>
                    <div className="text-gray-800 whitespace-pre-line leading-relaxed pl-10">
                      {g.grammarExample}
                    </div>
                  </div>

                  {/* Nút hành động */}
                  <div className="flex justify-between items-center mt-4 pt-3 border-t">
                    <div>
                      <button className="inline-flex items-center px-3 py-1.5 mr-2 border border-blue-500 text-blue-500 rounded-full text-sm hover:bg-blue-50">
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" className="mr-1">
                          <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"></path>
                          <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"></path>
                        </svg>
                        Ghi chú
                      </button>
                      <button className="inline-flex items-center px-3 py-1.5 border border-gray-300 text-gray-500 rounded-full text-sm hover:bg-gray-50">
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" className="mr-1">
                          <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"></path>
                        </svg>
                        Hỏi đáp
                      </button>
                    </div>
                    
                    {/* Nút đánh dấu đã học - thay đổi dựa vào trạng thái */}
                    <button 
                      onClick={(e) => {
                        e.stopPropagation(); // Ngăn sự kiện click lan ra phần cha
                        handleMarkAsCompleted(g.grammarId);
                      }}
                      className={`inline-flex items-center px-4 py-2 rounded-lg text-sm shadow-sm transition-all ${
                        isCompleted 
                          ? "bg-gray-100 text-gray-700 hover:bg-gray-200" 
                          : "bg-green-600 text-white hover:bg-green-700"
                      }`}
                    >
                      {isCompleted ? (
                        <>
                          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" className="mr-2">
                            <circle cx="12" cy="12" r="10"></circle>
                            <line x1="15" y1="9" x2="9" y2="15"></line>
                            <line x1="9" y1="9" x2="15" y2="15"></line>
                          </svg>
                          Đánh dấu chưa học
                        </>
                      ) : (
                        <>
                          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" className="mr-2">
                            <polyline points="9 11 12 14 22 4"></polyline>
                            <path d="M21 12v7a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h11"></path>
                          </svg>
                          Đánh dấu đã học
                        </>
                      )}
                    </button>
                  </div>
                </div>
              )}
            </div>
          );
        })}
      </div>

      {/* Phần gợi ý tiếp theo */}
      {grammars.length > 0 && (
        <div className="mt-12 bg-gradient-to-r from-green-50 to-teal-50 p-6 rounded-xl border border-green-100 flex justify-between items-center">
          <div>
            <h3 className="text-lg font-bold text-green-800">Bước tiếp theo</h3>
            <p className="text-gray-600">Sau khi học xong ngữ pháp, hãy làm bài tập để củng cố kiến thức!</p>
          </div>
          <button 
            className="px-5 py-2 bg-green-600 text-white rounded-lg shadow-sm hover:bg-green-700 flex items-center"
            onClick={() => setLessonTab && setLessonTab("exercise")}
          >
            Làm bài tập
            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" className="ml-2">
              <path d="M5 12h14"></path>
              <path d="M12 5l7 7-7 7"></path>
            </svg>
          </button>
        </div>
      )}
    </div>
  );
}