# 🤖 Nhật Ký Session Chat - Gemini & AI English Teacher

Tài liệu này lưu trữ lịch sử các phiên làm việc (chat sessions) giữa lập trình viên và trợ lý AI (Gemini/Antigravity) trong quá trình phát triển dự án **AI English Teacher**.

---

## 📌 Danh sách các Session

| Session | Thời gian | Model | Conversation ID | Nội dung chính | Trạng thái |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **Session 1** | 2026-06-29 | Gemini 3.5 Flash | `2a008a49-cd1d-4bab-b247-bcc68426f630` | Khởi tạo file `GEMINI.md` để theo dõi tiến độ và lịch sử chat. | Hoàn thành |
| **Session 2** | 2026-06-29 | Gemini 3.5 Flash | `55431b0c-91ea-46a3-8468-72552d62451a` | Tăng chiều rộng nhóm 3 nút (History, Profile, Pro). | Hoàn thành |
| **Session 3** | 2026-06-29 | Gemini 3.5 Flash | `55431b0c-91ea-46a3-8468-72552d62451a` | Cấu hình Novita API Service và hỗ trợ chuyển đổi API Provider (Gemini / Novita). | Hoàn thành |
| **Session 4** | 2026-06-29 | Gemini 3.5 Flash | `55431b0c-91ea-46a3-8468-72552d62451a` | Sửa logic chấm điểm AI & bổ sung các đáp án chuẩn thay thế (Alternatives). | Hoàn thành |
| **Session 5** | 2026-06-29 | Gemini 3.5 Flash | `55431b0c-91ea-46a3-8468-72552d62451a` | Mặc định hóa GUID của Guest thành 16 chữ số 0. | Hoàn thành |
| **Session 6** | 2026-06-29 | Gemini 3.5 Flash | `55431b0c-91ea-46a3-8468-72552d62451a` | Cải tiến UI: Đồng bộ và tăng cỡ chữ Đáp án & Giải thích, hiển thị list đáp án chuẩn đánh số. | Hoàn thành |

---

## 📝 Chi tiết từng Session

### 🟢 Session 1 (2026-06-29)

- **Mục tiêu:** Tạo file `GEMINI.md` theo yêu cầu của người dùng để làm nơi lưu trữ và theo dõi các session làm việc tiếp theo.
- **Hoạt động & Thay đổi:**
  - Khởi tạo file `GEMINI.md` tại thư mục gốc của dự án.
  - Thiết lập cấu trúc ghi chép thông tin chi tiết bao gồm: mục tiêu, các thay đổi chính, ghi chú kỹ thuật và các bước tiếp theo.
- **Ghi chú kỹ thuật:**
  - File này sẽ được cập nhật thủ công hoặc tự động trong các phiên làm việc tiếp theo để duy trì ngữ cảnh dự án.
- **Kế hoạch tiếp theo (Next Steps):**
  - Chờ yêu cầu tiếp theo từ người dùng để thực hiện phát triển/refactor dự án.

---

### 🟢 Session 2 (2026-06-29)

- **Mục tiêu:** Nhóm 3 nút History, Profile, Pro: tăng chiều rộng hơn một chút để cải thiện UX và giúp người dùng dễ thao tác.
- **Hoạt động & Thay đổi:**
  - Chỉnh sửa file [TeacherScreen.kt](file:///D:/ai-english/shared/feature/ai-teacher/ui/src/commonMain/kotlin/com/aienglish/feature/aiteacher/ui/screen/TeacherScreen.kt) tại phần header điều hướng bên phải.
  - Tăng chiều rộng nút **History** và **Profile** từ `40.dp` lên `48.dp`.
  - Tăng horizontal padding cho nút **PRO** từ `(start = 8.dp, end = 12.dp)` lên `(start = 12.dp, end = 16.dp)`.
- **Kế hoạch tiếp theo (Next Steps):**
  - Nhận phản hồi từ người dùng và tiếp tục hoàn thiện giao diện hoặc tính năng khác theo yêu cầu.

---

### 🟢 Session 3 (2026-06-29)

- **Mục tiêu:** Cấu hình tích hợp Novita API Service và hỗ trợ chuyển đổi AI Provider linh hoạt (Gemini hoặc Novita) trong Settings.
- **Hoạt động & Thay đổi:**
  - **Network layer:** Tạo [NovitaClient.kt](file:///D:/ai-english/shared/core/network/src/commonMain/kotlin/com/aienglish/core/network/client/NovitaClient.kt) thực thi các request OpenAI-compatible đến API Novita AI, đồng thời đăng ký DI trong [CoreNetworkModule.kt](file:///D:/ai-english/shared/core/network/src/commonMain/kotlin/com/aienglish/core/network/di/CoreNetworkModule.kt).
  - **Common Settings:** Mở rộng [AppPreferences.kt](file:///D:/ai-english/shared/core/common/src/commonMain/kotlin/com/aienglish/core/common/prefs/AppPreferences.kt) để lưu `apiProvider` (dạng thường) và `novitaApiKey` (dạng bảo mật thông qua SecureStorage). Thực thi cụ thể trên cả [DesktopAppPreferences.kt](file:///D:/ai-english/shared/core/common/src/desktopMain/kotlin/com/aienglish/core/common/prefs/DesktopAppPreferences.kt) và [AndroidAppPreferences.kt](file:///D:/ai-english/shared/core/common/src/androidMain/kotlin/com/aienglish/core/common/prefs/AndroidAppPreferences.kt).
  - **Repository Layer:** Cập nhật [TeacherRepositoryImpl.kt](file:///D:/ai-english/shared/feature/ai-teacher/data/src/commonMain/kotlin/com/aienglish/feature/aiteacher/data/repository/TeacherRepositoryImpl.kt) để tự động kiểm tra `apiProvider` và định tuyến request (Gemini API hoặc Novita API) kèm theo API key tương ứng. Đăng ký DI trong [TeacherDataModule.kt](file:///D:/ai-english/shared/feature/ai-teacher/data/src/commonMain/kotlin/com/aienglish/feature/aiteacher/data/di/TeacherDataModule.kt).
  - **Profile Domain & Application:** Mở rộng model [UserProfile.kt](file:///D:/ai-english/shared/feature/profile/domain/src/commonMain/kotlin/com/aienglish/feature/profile/domain/model/UserProfile.kt), interface [ProfileRepository.kt](file:///D:/ai-english/shared/feature/profile/domain/src/commonMain/kotlin/com/aienglish/feature/profile/domain/repository/ProfileRepository.kt) và implementation [ProfileRepositoryImpl.kt](file:///D:/ai-english/shared/feature/profile/data/src/commonMain/kotlin/com/aienglish/feature/profile/data/repository/ProfileRepositoryImpl.kt). Tạo 3 usecase mới: [GetNovitaApiKeyUseCase.kt](file:///D:/ai-english/shared/feature/profile/application/src/commonMain/kotlin/com/aienglish/feature/profile/application/usecase/GetNovitaApiKeyUseCase.kt), [SaveNovitaApiKeyUseCase.kt](file:///D:/ai-english/shared/feature/profile/application/src/commonMain/kotlin/com/aienglish/feature/profile/application/usecase/SaveNovitaApiKeyUseCase.kt), [UpdateApiProviderUseCase.kt](file:///D:/ai-english/shared/feature/profile/application/src/commonMain/kotlin/com/aienglish/feature/profile/application/usecase/UpdateApiProviderUseCase.kt), đăng ký chúng trong [ProfileApplicationModule.kt](file:///D:/ai-english/shared/feature/profile/application/src/commonMain/kotlin/com/aienglish/feature/profile/application/di/ProfileApplicationModule.kt).
  - **Profile UI & Presentation:** Nâng cấp [ProfileViewModel.kt](file:///D:/ai-english/shared/feature/profile/ui/src/commonMain/kotlin/com/aienglish/feature/profile/ui/viewmodel/ProfileViewModel.kt), đăng ký DI trong [ProfileUiModule.kt](file:///D:/ai-english/shared/feature/profile/ui/src/commonMain/kotlin/com/aienglish/feature/profile/ui/di/ProfileUiModule.kt). Tích hợp Segmented Control (đổi provider) và form cấu hình Novita API Key trong [ProfileSheet.kt](file:///D:/ai-english/shared/feature/profile/ui/src/commonMain/kotlin/com/aienglish/feature/profile/ui/screen/ProfileSheet.kt).
- **Ghi chú kỹ thuật:**
  - Build kiểm tra thành công với target Desktop, đảm bảo các modules tích hợp mượt mà.
- **Kế hoạch tiếp theo (Next Steps):**
  - Chờ yêu cầu tiếp theo từ người dùng.

---

### 🟢 Session 4 (2026-06-29)

- **Mục tiêu:** Cải thiện độ chuẩn xác trong việc chấm điểm của AI (tránh trừ điểm lỗi viết hoa/thường, khoảng trắng, dấu câu) và bổ sung thêm các đáp án thay thế chuẩn khác (alternatives).
- **Hoạt động & Thay đổi:**
  - **Domain Model:** Mở rộng [EnglishLesson.kt](file:///D:/ai-english/shared/feature/ai-teacher/domain/src/commonMain/kotlin/com/aienglish/feature/aiteacher/domain/model/EnglishLesson.kt) thêm trường `alternatives: List<String> = emptyList()` vào model `EnglishSentence`.
  - **Data Transfer Object:** Đồng bộ trường `alternatives` và cập nhật mapper tương ứng trong [EnglishLessonDto.kt](file:///D:/ai-english/shared/feature/ai-teacher/data/src/commonMain/kotlin/com/aienglish/feature/aiteacher/data/model/EnglishLessonDto.kt).
  - **Prompt Configuration:** Cập nhật [EnglishPromptProvider.kt](file:///D:/ai-english/shared/feature/ai-teacher/data/src/commonMain/kotlin/com/aienglish/feature/aiteacher/data/repository/prompt/EnglishPromptProvider.kt):
    - `getGenerateLessonSystemPrompt`: Yêu cầu AI sinh thêm mảng `alternatives` chứa 2-4 câu dịch tiếng Anh chuẩn khác nhau.
    - `getEvaluateLessonSystemPrompt` & `getExplainSentenceSystemPrompt`: Thêm hướng dẫn chấm điểm **lenient grading** (không trừ điểm nếu chỉ khác biệt về viết hoa/thường, khoảng trắng, dấu câu, viết tắt; ưu tiên tính tương đương về mặt ý nghĩa - semantic equivalence).
  - **User Interface:**
    - Cập nhật [LessonContent.kt](file:///D:/ai-english/shared/feature/aiteacher/ui/src/commonMain/kotlin/com/aienglish/feature/aiteacher/ui/screen/LessonContent.kt) để hiển thị đầy đủ câu đáp án chuẩn chính và danh sách các đáp án chuẩn thay thế khác ngay khi người dùng kiểm tra câu trả lời.
    - Cập nhật [FeedbackContent.kt](file:///D:/ai-english/shared/feature/aiteacher/ui/src/commonMain/kotlin/com/aienglish/feature/aiteacher/ui/screen/FeedbackContent.kt) hiển thị danh sách đáp án thay thế dưới dạng danh sách bullet point trong màn hình xem lại kết quả.
- **Ghi chú kỹ thuật:**
  - Biên dịch thành công với target Desktop, đảm bảo các API serialization tương thích mượt mà nhờ cơ chế default values của Kotlin.
- **Kế hoạch tiếp theo (Next Steps):**
  - Chờ yêu cầu tiếp theo từ người dùng.

---

### 🟢 Session 5 (2026-06-29)

- **Mục tiêu:** Chuyển đổi GUID của người dùng ở chế độ Guest (chưa đăng nhập hoặc chọn Guest trial) mặc định thành chuỗi 16 số 0 (`"0000000000000000"`).
- **Hoạt động & Thay đổi:**
  - Cập nhật [HistoryRepositoryImpl.kt](file:///D:/ai-english/shared/feature/history/data/src/commonMain/kotlin/com/aienglish/feature/history/data/repository/HistoryRepositoryImpl.kt):
    - `getCurrentUserId()`: Thay vì trả về `"guest"` như trước, nay trả về `"0000000000000000"` nếu người dùng chưa đăng nhập hoặc có email đăng nhập tạm thời là `"guest@aienglish.com"`.
    - `getAllHistory()`: Đồng bộ hóa query flow để dùng GUID `"0000000000000000"` tải lịch sử học tập của Guest.
    - Sửa phương thức tạo tài khoản mặc định `ensureUserExists` để gán chính xác `display_name = "Guest"` và `email = "guest@local"` khi `user_id` là `"0000000000000000"`.
    - **Cơ chế migration dữ liệu cũ:** Bổ sung truy vấn `migrateGuestHistory` trong [LessonHistory.sq](file:///D:/ai-english/shared/core/database/src/commonMain/sqldelight/com/aienglish/core/database/LessonHistory.sq) và `deleteUserById` trong [UserSchema.sq](file:///D:/ai-english/shared/core/database/src/commonMain/sqldelight/com/aienglish/core/database/UserSchema.sq`). Thực thi lazy migration dữ liệu `checkAndMigrateGuestData()` chuyển toàn bộ lịch sử học tập cũ có ID là `"guest"` sang ID `"0000000000000000"`, sau đó xóa an toàn user `"guest"` cũ khi bắt đầu hoặc lấy lịch sử.
    - **Sửa lỗi xung đột UNIQUE constraint email:** Do cả user `"guest"` cũ và user `"0000000000000000"` mới đều dùng chung email `"guest@local"`, câu lệnh `ensureUserExists` cho user mới bị SQLite bỏ qua (IGNORE). Dẫn đến lỗi vi phạm khóa ngoại (foreign key constraint) làm hủy bỏ toàn bộ Transaction và chặn đứng việc lưu lịch sử học tập mới. Tôi đã thêm logic tự động đổi email của user `"guest"` cũ thành tạm thời trước khi tạo user mới và thực hiện dịch chuyển lịch sử học tập, đồng thời dọn dẹp sạch cả tài khoản Guest có ID `"guest@aienglish.com"` cũ.
    - **Sửa lỗi vòng lặp vô tận (Infinite Loop) của Database Flow:** Việc gọi `checkAndMigrateGuestData()` làm ghi/sửa đổi database trực tiếp bên trong block `flatMapLatest` (trước khi trả về database flow) làm kích hoạt database flow chạy lại vô hạn lần, khiến UI bị kẹt. Đã khắc phục bằng cách sử dụng một cờ `isMigrated` cùng với `Mutex` để bảo đảm việc migration chỉ thực thi chính xác duy nhất một lần khi khởi tạo hoặc gọi lần đầu tiên.
- **Ghi chú kỹ thuật:**
  - Biên dịch thành công cho cả 2 target Desktop và Android, bảo đảm tính nhất quán của cơ sở dữ liệu local SQLite.
- **Kế hoạch tiếp theo (Next Steps):**
  - Chờ yêu cầu tiếp theo từ người dùng.

---

### 🟢 Session 6 (2026-06-29)

- **Mục tiêu:** Cải tiến UI hiển thị các đáp án chuẩn và giải thích: đồng bộ và tăng kích thước font chữ, đồng thời gộp các đáp án thành danh sách đánh số rõ ràng.
- **Hoạt động & Thay đổi:**
  - **Explanations Markdown ([MarkdownText.kt](file:///D:/ai-english/shared/feature/ai-teacher/ui/src/commonMain/kotlin/com/aienglish/feature/aiteacher/ui/screen/MarkdownText.kt)):**
    - Tăng kích thước font chữ của `Paragraph`, `Bullet`, `Ordered` block từ `13.sp` lên `15.sp` (lineHeight từ `20.sp` lên `22.sp`).
    - Tăng font size của `Heading 1` và `Heading 2` lên `18.sp` và `16.sp`.
  - **Lesson UI ([LessonContent.kt](file:///D:/ai-english/shared/feature/aiteacher/ui/src/commonMain/kotlin/com/aienglish/feature/aiteacher/ui/screen/LessonContent.kt)):**
    - Thay thế tiêu đề hiển thị từ "Đáp án chuẩn:" thành "Các đáp án chuẩn:" (cỡ chữ `13.sp` in đậm).
    - Trình bày đáp án chính và các đáp án thay thế cùng chung một danh sách được đánh số thứ tự `1), 2), 3)...` có kích thước font `15.sp` đồng bộ.
    - **Đồng bộ và tăng cỡ chữ Từ vựng chi tiết (Tokens):** Cấu hình thống nhất font size cho các tokens trên cả 3 ngôn ngữ (Anh, Nhật, Trung) gồm: từ chính `16.sp` Bold, phiên âm `11.sp`, từ loại / nghĩa dịch `10.sp`.
    - **Sắp xếp lại thứ tự hiển thị Token:** Cập nhật thứ tự hiển thị các thành phần trong Token chip thành **Cách đọc ở trên cùng (phonetic/IPA), Từ chính ở giữa (word), và Loại từ/Nghĩa dịch ở dưới cùng (partOfSpeech)** để đồng bộ nhất quán trên toàn bộ các ngôn ngữ.
  - **Review UI ([FeedbackContent.kt](file:///D:/ai-english/shared/feature/aiteacher/ui/src/commonMain/kotlin/com/aienglish/feature/aiteacher/ui/screen/FeedbackContent.kt)):**
    - Thiết kế lại `SentenceReviewCard` hiển thị khối "Các đáp án chuẩn:" đánh số thứ tự với kích thước font `15.sp` đồng bộ, kèm theo câu trả lời của user rõ ràng và thoáng hơn.
- **Ghi chú kỹ thuật:**
  - Biên dịch thành công cho cả hai target Desktop và Android.
- **Kế hoạch tiếp theo (Next Steps):**
  - Chờ yêu cầu tiếp theo từ người dùng.

---

> [!NOTE]
> File này giúp duy trì tính liên tục của ngữ cảnh phát triển dự án khi chuyển đổi giữa các phiên chat hoặc khi cần xem lại các quyết định thiết kế quan trọng.
