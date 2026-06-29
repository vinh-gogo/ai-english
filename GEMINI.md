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
| **Session 8** | 2026-06-29 | Gemini 3.5 Flash | `55431b0c-91ea-46a3-8468-72552d62451a` | Sửa lỗi lịch sử không ghi nhận: migration `user_id` column (4.sqm), auto schema migration Android + Desktop. | Hoàn thành |
| **Session 9** | 2026-06-29 | Gemini 3.5 Flash | `55431b0c-91ea-46a3-8468-72552d62451a` | Refactor: loại bỏ JSON blob storage, chuyển sang structured DB tables (lesson_question, lesson_feedback, alternatives). | Hoàn thành |
| **Session 10** | 2026-06-29 | Gemini 3.5 Flash | `55431b0c-91ea-46a3-8468-72552d62451a` | Squash toàn bộ migration files vào base schema, chuẩn hóa DB filename thành `ai_english.db` duy nhất. | Hoàn thành |
| **Session 11** | 2026-06-29 | Gemini 3.5 Flash | `55431b0c-91ea-46a3-8468-72552d62451a` | Cập nhật màu sắc Sunset Warmth, fix SeigaihaBackground, thêm tính năng chuyển đổi giao diện (Theme Switcher) trong Settings với 2 theme: Sunset Warmth & Nature Mint. | Hoàn thành |
| **Session 13** | 2026-06-29 | Claude Sonnet 4.6 | `16d7e0fa-80ff-43af-9dcc-87d9a481a702` | Tối ưu Layout token "Từ vựng chi tiết" & Refactor tối ưu hóa 7 PromptProvider bằng cách chuyển sang kế thừa BaseLanguagePromptProvider. | Hoàn thành |
| **Session 12** | 2026-06-29 | Gemini 3.5 Flash | `55431b0c-91ea-46a3-8468-72552d62451a` | Thêm theme Berry Sorbet (Berry Red / Blush Cream) — tổng cộng 3 themes có thể chuyển đổi. | Hoàn thành |
| **Session 14** | 2026-06-29 | Claude Sonnet 4.6 | `16d7e0fa-80ff-43af-9dcc-87d9a481a702` | Lặp (iterate) thiết kế lại Logo dựa trên hình khối gốc: thử nghiệm từ phong cách Glassmorphism chuyên nghiệp đến Flat 2D (TikTok/FB style), tối ưu độ bão hòa Deep/Rich colors, cập nhật trực tiếp lên mipmap Android. | Hoàn thành |
| **Session 15** | 2026-06-29 | Gemini 3.1 Pro | `f6c34fd4-afd8-448c-98e5-b576a6f1844b` | Cải thiện UX phần Từ vựng chi tiết: cơ chế Tap-to-reveal (ẩn/hiện chi tiết bằng công tắc và khi chạm), bọc thẻ Card để tăng độ tương phản, cập nhật gom cụm từ (chunking) thay vì từ đơn. | Hoàn thành |
| **Session 16** | 2026-06-29 | Gemini 3.1 Pro | `f6c34fd4-afd8-448c-98e5-b576a6f1844b` | Nâng cấp UI "Các đáp án chuẩn" (Highlighting từ đồng nghĩa CJK-aware, Accordion gọn gàng) và cập nhật AI Prompt cho tất cả ngôn ngữ. | Hoàn thành |


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

### 🟢 Session 7 (2026-06-29)

- **Mục tiêu:** Khắc phục lỗi crash ứng dụng khi khởi động trên thiết bị Android do không khớp khóa mã hóa (Android KeyStore AEADBadTagException) khi gỡ cài đặt và cài lại ứng dụng.
- **Hoạt động & Thay đổi:**
  - **Sửa Secure Storage trên Android ([AndroidSecureStorage.kt](file:///D:/ai-english/shared/core/common/src/androidMain/kotlin/com/aienglish/core/common/security/AndroidSecureStorage.kt)):**
    - Bọc logic khởi tạo `EncryptedSharedPreferences` vào khối `try-catch`.
    - Khi xảy ra lỗi (ví dụ `AEADBadTagException` do KeyStore bị lệch/hết hạn), tự động xóa file Preferences bị lỗi và xóa entry key cũ trong Android KeyStore (`masterKeyAlias`), sau đó thử tạo lại.
    - Thêm fallback cuối cùng chuyển về sử dụng `SharedPreferences` thông thường nếu vẫn thất bại để đảm bảo ứng dụng **tuyệt đối không bị crash khi khởi động**.
- **Ghi chú kỹ thuật:**
  - Biên dịch thành công và cài đặt thành công lên thiết bị Android qua adb.
- **Kế hoạch tiếp theo (Next Steps):**
  - Chờ yêu cầu tiếp theo từ người dùng.

---

### 🟢 Session 8 (2026-06-29)

- **Mục tiêu:** Sửa lỗi lịch sử học tập không ghi nhận bất kỳ bài nào sau khi người dùng bắt đầu bài học trên Android.
- **Hoạt động & Thay đổi:**
  - Xác định nguyên nhân gốc: thiếu cột `user_id` trong bảng `lesson_history` trên các thiết bị đã cài app trước đây.
  - Tạo file [4.sqm](file:///D:/ai-english/shared/core/database/src/commonMain/sqldelight/com/aienglish/core/database/4.sqm): `ALTER TABLE lesson_history ADD COLUMN user_id TEXT NOT NULL DEFAULT '0000000000000000'`.
  - Cập nhật [AndroidDriverFactory.kt](file:///D:/ai-english/shared/core/database/src/androidMain/kotlin/com/aienglish/core/database/driver/AndroidDriverFactory.kt): dùng `AndroidSqliteDriver` thuần, SQLDelight tự chạy migration qua `Schema.migrate()`.
  - Cập nhật [DesktopDriverFactory.kt](file:///D:/ai-english/shared/core/database/src/desktopMain/kotlin/com/aienglish/core/database/driver/DesktopDriverFactory.kt): thêm logic đọc `PRAGMA user_version` và gọi `Schema.migrate()` thủ công cho Desktop/JVM.
  - Loại bỏ callback `AfterVersion(3)` trùng lặp gây lỗi `duplicate column name`.
- **Ghi chú kỹ thuật:**
  - Build và cài đặt thành công lên Android.
- **Kế hoạch tiếp theo (Next Steps):**
  - Chờ yêu cầu tiếp theo từ người dùng.

---

### 🟢 Session 9 (2026-06-29)

- **Mục tiêu:** Refactor toàn bộ hệ thống lưu trữ — loại bỏ JSON blob columns, chuyển sang structured DB tables hoàn toàn.
- **Hoạt động & Thay đổi:**
  - **DB Schema (5.sqm):** Thêm `alternatives_json TEXT` vào `lesson_question`; tạo bảng `lesson_feedback` (history_id, overall_score, review, sentence_scores_json).
  - **LessonHistory.sq:** Xóa 4 cột JSON blob (`lesson_json`, `user_answers_json`, `explanations_json`, `feedback_json`) khỏi `lesson_history`. Thêm query `insertOrReplaceFeedback`, `getFeedbackForHistory`. Cập nhật `getAllHistoryWithCounts` không còn SELECT các cột JSON.
  - **Domain Layer:** Xóa 4 field JSON khỏi `LessonHistory.kt`. Đơn giản hóa `HistoryRepository.kt` — `completeLesson` chỉ nhận `(id, score)`. Thay `parseHistory(json...)` bằng `loadHistory(historyId): TeacherLessonHistory?` trong `TeacherHistoryRepository.kt`.
  - **Data Layer:** Rewrite `HistoryRepositoryImpl.kt` — không còn JSON parsing. Rewrite `TeacherHistoryRepositoryImpl.kt` — inject `AIEnglishDatabase` trực tiếp, implement `loadHistory()` từ structured tables, `saveQuestionsToDb()` nhận domain objects. Cập nhật DI trong `TeacherDataModule.kt`.
  - **UI Layer:** `TeacherViewModel` — `onReviewLesson`/`onResumeLesson` gọi `loadHistory(id)` thay vì `parseHistory(json)`. `HistorySheet.kt` — `hasLessonData = totalCount > 0`.
- **Ghi chú kỹ thuật:**
  - Build thành công cho cả Desktop và Android. Chỉ còn alternatives và sentenceScores lưu dạng JSON array nhỏ gọn.
- **Kế hoạch tiếp theo (Next Steps):**
  - Chờ yêu cầu tiếp theo từ người dùng.

---

### 🟢 Session 10 (2026-06-29)

- **Mục tiêu:** Squash toàn bộ migration files (1.sqm → 5.sqm) vào base schema; chuẩn hóa DB filename thành `ai_english.db` duy nhất, vĩnh viễn.
- **Hoạt động & Thay đổi:**
  - Xóa các file `1.sqm` → `5.sqm` — toàn bộ schema final đã được tổng hợp vào `LessonHistory.sq` và `UserSchema.sq`.
  - Reset `version = 1` trong `build.gradle.kts` — khởi điểm mới, sạch sẽ.
  - Đổi DB filename từ `ai_english_v5.db` → **`ai_english.db`** — không bao giờ đổi tên nữa.
  - Đơn giản hóa `AndroidDriverFactory.kt` — `AndroidSqliteDriver` tự migrate qua `.sqm`.
  - Cập nhật `DesktopDriverFactory.kt` — `PRAGMA user_version` tracking + `Schema.migrate()` cho Desktop.
- **Ghi chú kỹ thuật:**
  - Build thành công, cài đặt thành công. Quy tắc từ giờ: thêm schema → tạo `[N].sqm` + tăng `version = N`. Không đổi tên file DB.
- **Kế hoạch tiếp theo (Next Steps):**
  - Chờ yêu cầu tiếp theo từ người dùng.

---

---

### 🟢 Session 14 (2026-06-29)

- **Mục tiêu:** Thiết kế lại Logo ứng dụng dựa trên hình khối gốc `logo_ai_lingua copy.png`, lặp lại thử nghiệm để tìm ra phong cách và màu sắc tốt nhất, đồng thời tự động hóa việc cập nhật lên thiết bị Android.
- **Hoạt động & Thay đổi:**
  - **Giữ nguyên Geometry:** Giữ chuẩn 100% hình khối của logo cũ (2 bong bóng chat lồng nhau + 1 ngôi sao AI), chỉ thay đổi chất liệu và màu sắc thông qua prompt tạo ảnh.
  - **Vòng lặp chất liệu (Styles):**
    - Chuyển từ **3D Clay** (đất sét) mập mạp sang **Glassmorphism / Acrylic** (kính mờ chuyên nghiệp).
    - Chuyển sang **Flat 2D Vector** (tối giản, phẳng) học tập theo style của TikTok & Facebook để dễ nhận diện.
  - **Vòng lặp màu sắc (Colors):**
    - Chuyển tiếp từ *Neon/Luminous* (sáng chói nhưng có thể nhạt) sang **Deep & Rich Colors** (màu đặc, độ bão hòa cực cao, độ tương phản mạnh): Bong bóng trước là **Royal Blue**, bong bóng sau là **Rich Vibrant Orange**, ngôi sao là **Golden Yellow**.
  - **Tích hợp Android:** Mỗi lần chốt ảnh, tự động chạy script Powershell đè ảnh vào tất cả thư mục `mipmap-*` (ic_launcher) và chạy `gradlew installDebug` để kiểm thử thực tế trên điện thoại.
- **Ghi chú kỹ thuật:**
  - Để tránh lỗi mất chi tiết khi dùng AI tạo ảnh, yêu cầu kiên quyết "Keep exact same shape and layout" kết hợp cùng `ImagePaths` của ảnh gốc làm nền tảng gen ảnh.
- **Kế hoạch tiếp theo (Next Steps):**
  - Chờ yêu cầu tiếp theo từ người dùng.

---

### 🟢 Session 15 (2026-06-29)

- **Mục tiêu:** Nâng cấp trải nghiệm UX tại phần hiển thị "Từ vựng chi tiết" trong màn hình Luyện tập: giảm thiểu sự lộn xộn, tăng độ tương phản và tối ưu hóa cho việc học cụm từ thay vì từ rời rạc.
- **Hoạt động & Thay đổi:**
  - **Cải tiến UI (LessonContent.kt):** 
    - Thêm cơ chế **Toggle Switch** ("Phân tích") để bật/tắt toàn bộ chi tiết (IPA, từ loại). Mặc định là Tắt để giao diện thoáng hơn.
    - Thêm cơ chế **Tap-to-reveal**: Khi đang tắt phân tích, chạm vào một từ sẽ mở chi tiết riêng của từ đó.
    - Bọc cụm `FlowRow` vào một `Card` có độ trong suốt `95%` (`colors.surface.copy(alpha = 0.95f)`) và viền góc tròn để che đi background pattern phức tạp bên dưới, giúp chữ trở nên dễ đọc và có độ tương phản cao hơn.
  - **Cải tiến Data/AI Prompt (EnglishPromptProvider.kt & BaseLanguagePromptProvider.kt):** 
    - Cập nhật System Prompt yêu cầu AI sinh từ vựng theo dạng **cụm từ (chunk/collocation)** (ví dụ "I'm", "my name") thay vì cắt nhỏ từng chữ lẻ.
- **Ghi chú kỹ thuật:**
  - `FlowRow` của Compose Multiplatform kết hợp `clickable` state per-index đã giải quyết triệt để yêu cầu hiển thị tùy chỉnh mượt mà.
  - Biên dịch thành công cho Android qua `installDebug` task.
- **Kế hoạch tiếp theo (Next Steps):**
  - Đón nhận phản hồi trải nghiệm thực tế từ người dùng.

---

### 🟢 Session 16 (2026-06-29)

- **Mục tiêu:** Nâng cấp trải nghiệm UI/UX cho phần "Các đáp án chuẩn" ở cả màn hình Bài học và Ôn tập, đồng thời đồng bộ hóa khả năng sinh đáp án thay thế & chấm điểm nương tay (lenient grading) cho **tất cả** ngôn ngữ thay vì chỉ tiếng Anh.
- **Hoạt động & Thay đổi:**
  - **Text Highlighting thuật toán Client-side:** Viết hàm `highlightDifferences` tự động so sánh đáp án thay thế với đáp án chính để bôi đậm/tô màu từ đồng nghĩa. Hỗ trợ cả ngắt từ bằng dấu cách (Anh, Pháp...) và ngắt ký tự (Trung, Nhật - CJK) mà không cần can thiệp DB hay Prompt.
  - **Tối ưu không gian bằng Accordion:** Chỉ hiển thị đáp án chính (số 1), ẩn các đáp án còn lại dưới dạng dropdown (Xem thêm X cách diễn đạt khác).
  - **Bổ sung Action Buttons:** Thêm nút Copy (sử dụng `LocalClipboardManager`) cho từng câu và placeholder icon Loa (TTS).
  - **Đồng bộ BaseLanguagePromptProvider:** 
    - Thêm trường `alternatives` vào JSON Prompt chung để hỗ trợ sinh đáp án thay thế cho mọi ngôn ngữ.
    - Cập nhật `getEvaluateLessonSystemPrompt` bắt buộc AI chấm "10/10" nếu tương đương ngữ nghĩa, bỏ qua lỗi vặt (dấu câu, viết hoa).
- **Ghi chú kỹ thuật:**
  - `highlightDifferences` giúp đem lại giá trị sư phạm lớn (learner dễ dàng nhận ra idiom/từ đồng nghĩa mới) với effort cực thấp, không break schema.
  - Fix deprecated warnings: Chuyển icon sang bản `AutoMirrored`, thay thế `LocalClipboardManager` thành `LocalClipboard` trong các dự định tương lai.
- **Kế hoạch tiếp theo (Next Steps):**
  - Xem xét tích hợp giải pháp Text-To-Speech (TTS) thực thụ cho Kotlin Multiplatform.

---

### 🟢 Session 17 (2026-06-29)

- **Mục tiêu:** Căn chỉnh (Alignment) từ tiếng Anh gốc trong phần "Từ vựng chi tiết" để chúng luôn thẳng hàng ngang với nhau ở dưới cùng, không bị xô lệch khi bật/tắt hiển thị chi tiết (Phiên âm, Từ loại).
- **Hoạt động & Thay đổi:**
  - **Căn chỉnh Column:** Bổ sung `Modifier.align(Alignment.CenterVertically)` vào `Column` bọc mỗi token bên trong `FlowRow`.
  - **Bảo toàn cấu trúc (Structural Placeholder):** Thay vì ẩn hoàn toàn thẻ `Text` của phiên âm và từ loại khi chúng rỗng, tôi đã đổi thành luôn render chúng với kích thước chuẩn nhưng đặt màu sắc thành `Color.Transparent` (vô hình).
  - **Kết quả:** Vì mọi `Column` (đang được mở rộng chi tiết) đều có chung cấu trúc gồm: `Top (10-11sp)` + `Middle (16sp)` + `Bottom (10-11sp)`, phần chữ chính (Middle) luôn nằm chính xác ở vị trí trung tâm. Kết hợp với `Alignment.CenterVertically`, chữ chính của mọi từ (kể cả từ không có phiên âm hay từ loại) đều được gióng thẳng hàng ngang một cách hoàn hảo, sửa lỗi vỡ cấu trúc dòng đọc (baseline alignment).
- **Ghi chú kỹ thuật:**
  - Trong Compose Multiplatform, `FlowRow` mặc định căn đỉnh (`Alignment.Top`), gây ra lỗi từ ngắn bị đẩy lên bằng chiều cao với phiên âm của từ đầy đủ. Giải pháp dùng `CenterVertically` và placeholder text giải quyết triệt để vấn đề này mà không cần dùng Custom Layout hay `alignByBaseline` (vốn chưa hỗ trợ đầy đủ trên `FlowRow`).
  - Đã biên dịch thành công cho Desktop/Android.
- **Kế hoạch tiếp theo (Next Steps):**
  - Chờ yêu cầu tiếp theo từ người dùng.

---

### 🟢 Session 18 (2026-06-29)

- **Mục tiêu:** Sửa lỗi logic của nút Toggle "Phân tích" ở phần "Từ vựng chi tiết" (tắt switch nhưng một số từ vẫn hiển thị chi tiết). Đảm bảo tính toàn vẹn và ưu tiên tuyệt đối của trạng thái toggle.
- **Hoạt động & Thay đổi:**
- **Mục tiêu:** Cải thiện độ chuẩn xác trong việc chấm điểm của AI (tránh trừ điểm lỗi viết hoa/thường, khoảng trắng, dấu câu) và bổ sung thêm các đáp án thay thế chuẩn khác (alternatives).
- **Hoạt động & Thay đổi:**
  - **Domain Model:** Mở rộng [EnglishLesson.kt](file:///D:/ai-english/shared/feature/ai-teacher/domain/src/commonMain/kotlin/com/aienglish/feature/aiteacher/domain/model/EnglishLesson.kt) thêm trường `alternatives: List<String> = emptyList()` vào model `EnglishSentence`.
  - **Data Transfer Object:** Đồng bộ trường `alternatives` và cập nhật mapper tương ứng trong [EnglishLessonDto.kt](file:///D:/ai-english/shared/feature/ai-teacher/data/src/commonMain/kotlin/com/aienglish/feature/aiteacher/data/model/EnglishLessonDto.kt).
  - **Prompt Configuration:** Cập nhật [EnglishPromptProvider.kt](file:///D:/ai-english/shared/feature/ai-teacher/data/src/commonMain/kotlin/com/aienglish/feature/aiteacher/data/repository/prompt/EnglishPromptProvider.kt):
    - `getGenerateLessonSystemPrompt`: Yêu cầu AI sinh thêm mảng `alternatives` chứa 2-4 câu dịch tiếng Anh chuẩn khác nhau.
    - `getEvaluateLessonSystemPrompt` & `getExplainSentenceSystemPrompt`: Thêm hướng dẫn chấm điểm **lenient grading** (không trừ điểm nếu chỉ khác biệt về viết hoa/thường, khoảng trắng, dấu câu, viết tắt; ưu tiên tính tương đương về mặt ý nghĩa - semantic equivalence).
  - **User Interface:**
    - Cập nhật [LessonContent.kt](file:///D:/ai-english/feature/aiteacher/ui/src/commonMain/kotlin/com/aienglish/feature/aiteacher/ui/screen/LessonContent.kt) để hiển thị đầy đủ câu đáp án chuẩn chính và danh sách các đáp án chuẩn thay thế khác ngay khi người dùng kiểm tra câu trả lời.
    - Cập nhật [FeedbackContent.kt](file:///D:/ai-english/feature/aiteacher/ui/src/commonMain/kotlin/com/aienglish/feature/aiteacher/ui/screen/FeedbackContent.kt) hiển thị danh sách đáp án thay thế dưới dạng danh sách bullet point trong màn hình xem lại kết quả.
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
    - **Cơ chế migration dữ liệu cũ:** Bổ sung truy vấn `migrateGuestHistory` trong [LessonHistory.sq](file:///D:/ai-english/shared/core/database/src/commonMain/sqldelight/com/aienglish/core/database/LessonHistory.sq) và `deleteUserById` trong [UserSchema.sq](file:///D:/ai-english/shared/core/database/src/commonMain/sqldelight/com/aienglish/core/database/UserSchema.sq). Thực thi lazy migration dữ liệu `checkAndMigrateGuestData()` chuyển toàn bộ lịch sử học tập cũ có ID là `"guest"` sang ID `"0000000000000000"`, sau đó xóa an toàn user `"guest"` cũ khi bắt đầu hoặc lấy lịch sử.
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

### 🟢 Session 7 (2026-06-29)

- **Mục tiêu:** Khắc phục lỗi crash ứng dụng khi khởi động trên thiết bị Android do không khớp khóa mã hóa (Android KeyStore AEADBadTagException) khi gỡ cài đặt và cài lại ứng dụng.
- **Hoạt động & Thay đổi:**
  - **Sửa Secure Storage trên Android ([AndroidSecureStorage.kt](file:///D:/ai-english/shared/core/common/src/androidMain/kotlin/com/aienglish/core/common/security/AndroidSecureStorage.kt)):**
    - Bọc logic khởi tạo `EncryptedSharedPreferences` vào khối `try-catch`.
    - Khi xảy ra lỗi (ví dụ `AEADBadTagException` do KeyStore bị lệch/hết hạn), tự động xóa file Preferences bị lỗi và xóa entry key cũ trong Android KeyStore (`masterKeyAlias`), sau đó thử tạo lại.
    - Thêm fallback cuối cùng chuyển về sử dụng `SharedPreferences` thông thường nếu vẫn thất bại để đảm bảo ứng dụng **tuyệt đối không bị crash khi khởi động**.
- **Ghi chú kỹ thuật:**
  - Biên dịch thành công và cài đặt thành công lên thiết bị Android qua adb.
- **Kế hoạch tiếp theo (Next Steps):**
  - Chờ yêu cầu tiếp theo từ người dùng.

---

### 🟢 Session 8 (2026-06-29)

- **Mục tiêu:** Sửa lỗi lịch sử học tập không ghi nhận bất kỳ bài nào sau khi người dùng bắt đầu bài học trên Android.
- **Hoạt động & Thay đổi:**
  - Xác định nguyên nhân gốc: thiếu cột `user_id` trong bảng `lesson_history` trên các thiết bị đã cài app trước đây.
  - Tạo file [4.sqm](file:///D:/ai-english/shared/core/database/src/commonMain/sqldelight/com/aienglish/core/database/4.sqm): `ALTER TABLE lesson_history ADD COLUMN user_id TEXT NOT NULL DEFAULT '0000000000000000'`.
  - Cập nhật [AndroidDriverFactory.kt](file:///D:/ai-english/shared/core/database/src/androidMain/kotlin/com/aienglish/core/database/driver/AndroidDriverFactory.kt): dùng `AndroidSqliteDriver` thuần, SQLDelight tự chạy migration qua `Schema.migrate()`.
  - Cập nhật [DesktopDriverFactory.kt](file:///D:/ai-english/shared/core/database/src/desktopMain/kotlin/com/aienglish/core/database/driver/DesktopDriverFactory.kt): thêm logic đọc `PRAGMA user_version` và gọi `Schema.migrate()` thủ công cho Desktop/JVM.
  - Loại bỏ callback `AfterVersion(3)` trùng lặp gây lỗi `duplicate column name`.
- **Ghi chú kỹ thuật:**
  - Build và cài đặt thành công lên Android.
- **Kế hoạch tiếp theo (Next Steps):**
  - Chờ yêu cầu tiếp theo từ người dùng.

---

### 🟢 Session 9 (2026-06-29)

- **Mục tiêu:** Refactor toàn bộ hệ thống lưu trữ — loại bỏ JSON blob columns, chuyển sang structured DB tables hoàn toàn.
- **Hoạt động & Thay đổi:**
  - **DB Schema (5.sqm):** Thêm `alternatives_json TEXT` vào `lesson_question`; tạo bảng `lesson_feedback` (history_id, overall_score, review, sentence_scores_json).
  - **LessonHistory.sq:** Xóa 4 cột JSON blob (`lesson_json`, `user_answers_json`, `explanations_json`, `feedback_json`) khỏi `lesson_history`. Thêm query `insertOrReplaceFeedback`, `getFeedbackForHistory`. Cập nhật `getAllHistoryWithCounts` không còn SELECT các cột JSON.
  - **Domain Layer:** Xóa 4 field JSON khỏi `LessonHistory.kt`. Đơn giản hóa `HistoryRepository.kt` — `completeLesson` chỉ nhận `(id, score)`. Thay `parseHistory(json...)` bằng `loadHistory(historyId): TeacherLessonHistory?` trong `TeacherHistoryRepository.kt`.
  - **Data Layer:** Rewrite `HistoryRepositoryImpl.kt` — không còn JSON parsing. Rewrite `TeacherHistoryRepositoryImpl.kt` — inject `AIEnglishDatabase` trực tiếp, implement `loadHistory()` từ structured tables, `saveQuestionsToDb()` nhận domain objects. Cập nhật DI trong `TeacherDataModule.kt`.
  - **UI Layer:** `TeacherViewModel` — `onReviewLesson`/`onResumeLesson` gọi `loadHistory(id)` thay vì `parseHistory(json)`. `HistorySheet.kt` — `hasLessonData = totalCount > 0`.
- **Ghi chú kỹ thuật:**
  - Build thành công cho cả Desktop và Android. Chỉ còn alternatives và sentenceScores lưu dạng JSON array nhỏ gọn.
- **Kế hoạch tiếp theo (Next Steps):**
  - Chờ yêu cầu tiếp theo từ người dùng.

---

### 🟢 Session 10 (2026-06-29)

- **Mục tiêu:** Squash toàn bộ migration files (1.sqm → 5.sqm) vào base schema; chuẩn hóa DB filename thành `ai_english.db` duy nhất, vĩnh viễn.
- **Hoạt động & Thay đổi:**
  - Xóa các file `1.sqm` → `5.sqm` — toàn bộ schema final đã được tổng hợp vào `LessonHistory.sq` và `UserSchema.sq`.
  - Reset `version = 1` trong `build.gradle.kts` — khởi điểm mới, sạch sẽ.
  - Đổi DB filename từ `ai_english_v5.db` → **`ai_english.db`** — không bao giờ đổi tên nữa.
  - Đơn giản hóa `AndroidDriverFactory.kt` — `AndroidSqliteDriver` tự migrate qua `.sqm`.
  - Cập nhật `DesktopDriverFactory.kt` — `PRAGMA user_version` tracking + `Schema.migrate()` cho Desktop.
- **Ghi chú kỹ thuật:**
  - Build thành công, cài đặt thành công. Quy tắc từ giờ: thêm schema → tạo `[N].sqm` + tăng `version = N`. Không đổi tên file DB.
- **Kế hoạch tiếp theo (Next Steps):**
  - Chờ yêu cầu tiếp theo từ người dùng.

---

---

### 🟢 Session 14 (2026-06-29)

- **Mục tiêu:** Thiết kế lại Logo ứng dụng dựa trên hình khối gốc `logo_ai_lingua copy.png`, lặp lại thử nghiệm để tìm ra phong cách và màu sắc tốt nhất, đồng thời tự động hóa việc cập nhật lên thiết bị Android.
- **Hoạt động & Thay đổi:**
  - **Giữ nguyên Geometry:** Giữ chuẩn 100% hình khối của logo cũ (2 bong bóng chat lồng nhau + 1 ngôi sao AI), chỉ thay đổi chất liệu và màu sắc thông qua prompt tạo ảnh.
  - **Vòng lặp chất liệu (Styles):**
    - Chuyển từ **3D Clay** (đất sét) mập mạp sang **Glassmorphism / Acrylic** (kính mờ chuyên nghiệp).
    - Chuyển sang **Flat 2D Vector** (tối giản, phẳng) học tập theo style của TikTok & Facebook để dễ nhận diện.
  - **Vòng lặp màu sắc (Colors):**
    - Chuyển tiếp từ *Neon/Luminous* (sáng chói nhưng có thể nhạt) sang **Deep & Rich Colors** (màu đặc, độ bão hòa cực cao, độ tương phản mạnh): Bong bóng trước là **Royal Blue**, bong bóng sau là **Rich Vibrant Orange**, ngôi sao là **Golden Yellow**.
  - **Tích hợp Android:** Mỗi lần chốt ảnh, tự động chạy script Powershell đè ảnh vào tất cả thư mục `mipmap-*` (ic_launcher) và chạy `gradlew installDebug` để kiểm thử thực tế trên điện thoại.
- **Ghi chú kỹ thuật:**
  - Để tránh lỗi mất chi tiết khi dùng AI tạo ảnh, yêu cầu kiên quyết "Keep exact same shape and layout" kết hợp cùng `ImagePaths` của ảnh gốc làm nền tảng gen ảnh.
- **Kế hoạch tiếp theo (Next Steps):**
  - Chờ yêu cầu tiếp theo từ người dùng.

---

### 🟢 Session 15 (2026-06-29)

- **Mục tiêu:** Nâng cấp trải nghiệm UX tại phần hiển thị "Từ vựng chi tiết" trong màn hình Luyện tập: giảm thiểu sự lộn xộn, tăng độ tương phản và tối ưu hóa cho việc học cụm từ thay vì từ rời rạc.
- **Hoạt động & Thay đổi:**
  - **Cải tiến UI (LessonContent.kt):** 
    - Thêm cơ chế **Toggle Switch** ("Phân tích") để bật/tắt toàn bộ chi tiết (IPA, từ loại). Mặc định là Tắt để giao diện thoáng hơn.
    - Thêm cơ chế **Tap-to-reveal**: Khi đang tắt phân tích, chạm vào một từ sẽ mở chi tiết riêng của từ đó.
    - Bọc cụm `FlowRow` vào một `Card` có độ trong suốt `95%` (`colors.surface.copy(alpha = 0.95f)`) và viền góc tròn để che đi background pattern phức tạp bên dưới, giúp chữ trở nên dễ đọc và có độ tương phản cao hơn.
  - **Cải tiến Data/AI Prompt (EnglishPromptProvider.kt & BaseLanguagePromptProvider.kt):** 
    - Cập nhật System Prompt yêu cầu AI sinh từ vựng theo dạng **cụm từ (chunk/collocation)** (ví dụ "I'm", "my name") thay vì cắt nhỏ từng chữ lẻ.
- **Ghi chú kỹ thuật:**
  - `FlowRow` của Compose Multiplatform kết hợp `clickable` state per-index đã giải quyết triệt để yêu cầu hiển thị tùy chỉnh mượt mà.
  - Biên dịch thành công cho Android qua `installDebug` task.
- **Kế hoạch tiếp theo (Next Steps):**
  - Đón nhận phản hồi trải nghiệm thực tế từ người dùng.

---

### 🟢 Session 16 (2026-06-29)

- **Mục tiêu:** Nâng cấp trải nghiệm UI/UX cho phần "Các đáp án chuẩn" ở cả màn hình Bài học và Ôn tập, đồng thời đồng bộ hóa khả năng sinh đáp án thay thế & chấm điểm nương tay (lenient grading) cho **tất cả** ngôn ngữ thay vì chỉ tiếng Anh.
- **Hoạt động & Thay đổi:**
  - **Text Highlighting thuật toán Client-side:** Viết hàm `highlightDifferences` tự động so sánh đáp án thay thế với đáp án chính để bôi đậm/tô màu từ đồng nghĩa. Hỗ trợ cả ngắt từ bằng dấu cách (Anh, Pháp...) và ngắt ký tự (Trung, Nhật - CJK) mà không cần can thiệp DB hay Prompt.
  - **Tối ưu không gian bằng Accordion:** Chỉ hiển thị đáp án chính (số 1), ẩn các đáp án còn lại dưới dạng dropdown (Xem thêm X cách diễn đạt khác).
  - **Bổ sung Action Buttons:** Thêm nút Copy (sử dụng `LocalClipboardManager`) cho từng câu và placeholder icon Loa (TTS).
  - **Đồng bộ BaseLanguagePromptProvider:** 
    - Thêm trường `alternatives` vào JSON Prompt chung để hỗ trợ sinh đáp án thay thế cho mọi ngôn ngữ.
    - Cập nhật `getEvaluateLessonSystemPrompt` bắt buộc AI chấm "10/10" nếu tương đương ngữ nghĩa, bỏ qua lỗi vặt (dấu câu, viết hoa).
- **Ghi chú kỹ thuật:**
  - `highlightDifferences` giúp đem lại giá trị sư phạm lớn (learner dễ dàng nhận ra idiom/từ đồng nghĩa mới) với effort cực thấp, không break schema.
  - Fix deprecated warnings: Chuyển icon sang bản `AutoMirrored`, thay thế `LocalClipboardManager` thành `LocalClipboard` trong các dự định tương lai.
- **Kế hoạch tiếp theo (Next Steps):**
  - Xem xét tích hợp giải pháp Text-To-Speech (TTS) thực thụ cho Kotlin Multiplatform.

---

### 🟢 Session 17 (2026-06-29)

- **Mục tiêu:** Căn chỉnh (Alignment) từ tiếng Anh gốc trong phần "Từ vựng chi tiết" để chúng luôn thẳng hàng ngang với nhau ở dưới cùng, không bị xô lệch khi bật/tắt hiển thị chi tiết (Phiên âm, Từ loại).
- **Hoạt động & Thay đổi:**
  - **Căn chỉnh Column:** Bổ sung `Modifier.align(Alignment.CenterVertically)` vào `Column` bọc mỗi token bên trong `FlowRow`.
  - **Bảo toàn cấu trúc (Structural Placeholder):** Thay vì ẩn hoàn toàn thẻ `Text` của phiên âm và từ loại khi chúng rỗng, tôi đã đổi thành luôn render chúng với kích thước chuẩn nhưng đặt màu sắc thành `Color.Transparent` (vô hình).
  - **Kết quả:** Vì mọi `Column` (đang được mở rộng chi tiết) đều có chung cấu trúc gồm: `Top (10-11sp)` + `Middle (16sp)` + `Bottom (10-11sp)`, phần chữ chính (Middle) luôn nằm chính xác ở vị trí trung tâm. Kết hợp với `Alignment.CenterVertically`, chữ chính của mọi từ (kể cả từ không có phiên âm hay từ loại) đều được gióng thẳng hàng ngang một cách hoàn hảo, sửa lỗi vỡ cấu trúc dòng đọc (baseline alignment).
- **Ghi chú kỹ thuật:**
  - Trong Compose Multiplatform, `FlowRow` mặc định căn đỉnh (`Alignment.Top`), gây ra lỗi từ ngắn bị đẩy lên bằng chiều cao với phiên âm của từ đầy đủ. Giải pháp dùng `CenterVertically` và placeholder text giải quyết triệt để vấn đề này mà không cần dùng Custom Layout hay `alignByBaseline` (vốn chưa hỗ trợ đầy đủ trên `FlowRow`).
  - Đã biên dịch thành công cho Desktop/Android.
- **Kế hoạch tiếp theo (Next Steps):**
  - Chờ yêu cầu tiếp theo từ người dùng.

---

### 🟢 Session 18 (2026-06-29)

- **Mục tiêu:** Sửa lỗi logic của nút Toggle "Phân tích" ở phần "Từ vựng chi tiết" (tắt switch nhưng một số từ vẫn hiển thị chi tiết). Đảm bảo tính toàn vẹn và ưu tiên tuyệt đối của trạng thái toggle.
- **Hoạt động & Thay đổi:**
  - **Phân tích vấn đề:** Lỗi xảy ra do cơ chế "Tap-to-reveal" (từ Session 15) lưu trạng thái mở rộng của từng từ độc lập vào biến `expandedTokens`. Nếu người dùng chạm vào một từ khi switch đang tắt, từ đó sẽ hiện chi tiết, gây hiểu lầm rằng switch bị lỗi logic hoặc bị kẹt.
  - **Khắc phục:** Đã loại bỏ hoàn toàn cơ chế "Tap-to-reveal" cùng với state `expandedTokens` và thuộc tính `.clickable` khỏi các token (`Column`). 
  - **Ràng buộc UI:** Ràng buộc trực tiếp biến `isDetailed` chỉ phụ thuộc duy nhất vào biến state `showDetailedTokens` (được điều khiển bởi Toggle Switch). Giờ đây, khi switch mang giá trị `false`, tất cả các thành phần phụ (Phiên âm, Từ loại) của mọi từ đều không được render, đảm bảo tuân thủ đúng yêu cầu "Visibility.GONE".
- **Ghi chú kỹ thuật:**
  - Biên dịch thành công cho Desktop/Android. Đã đẩy bản build cập nhật lên thiết bị qua ADB.
- **Kế hoạch tiếp theo (Next Steps):**
  - Chờ yêu cầu tiếp theo từ người dùng.

---

### 🟢 Session 19 (2026-06-29)

- **Mục tiêu:** Bỏ hoàn toàn tính năng chấm điểm ở toàn bộ AI agents và ẩn điểm số khỏi giao diện người dùng theo yêu cầu.
- **Hoạt động & Thay đổi:**
  - **AI Prompt:** Sửa đổi `BaseLanguagePromptProvider` để loại bỏ yêu cầu trả về điểm số tổng và điểm từng câu trong `getEvaluateLessonSystemPrompt` và `getExplainSentenceSystemPrompt`.
  - **Data Transfer Object:** Thêm các giá trị mặc định cho trường `score` (thành 0) và `sentenceScores` (thành mảng rỗng) trong `EnglishFeedbackDto` và `SentenceExplanationDto` tại `EnglishLessonDto.kt` để tránh lỗi Parse JSON khi AI không trả về các thông tin này.
  - **UI/ViewModel:** Loại bỏ logic tính toán và xử lý điểm số trong `TeacherViewModel.kt`. Ẩn/Xóa tất cả các thành phần UI hiển thị điểm số trên `LessonContent.kt` và `FeedbackContent.kt`.
  - **Lịch sử học tập (HistorySheet.kt):** Sửa lại logic phân nhóm (grouping), chuyển từ nhóm theo dải điểm sang nhóm theo trạng thái Hoàn thành và Chưa hoàn thành, đồng thời xóa các Badge hiển thị điểm của lịch sử.
- **Ghi chú kỹ thuật:**
  - Đã compile và build thành công trên ComposeApp Desktop mà không phát sinh lỗi.
- **Kế hoạch tiếp theo (Next Steps):**
  - Chờ yêu cầu tiếp theo từ người dùng.

> [!NOTE]
> File này giúp duy trì tính liên tục của ngữ cảnh phát triển dự án khi chuyển đổi giữa các phiên chat hoặc khi cần xem lại các quyết định thiết kế quan trọng.
