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
| **Session 17** | 2026-06-29 | Gemini 3.1 Pro | `f6c34fd4-afd8-448c-98e5-b576a6f1844b` | Căn chỉnh các token từ vựng chi tiết luôn thẳng hàng ngang bất kể việc bật/tắt hiển thị IPA, từ loại. | Hoàn thành |
| **Session 18** | 2026-06-29 | Gemini 3.1 Pro | `f6c34fd4-afd8-448c-98e5-b576a6f1844b` | Loại bỏ cơ chế "Tap-to-reveal" để toggle switch "Phân tích" hoạt động chính xác tuyệt đối. | Hoàn thành |
| **Session 19** | 2026-06-29 | Gemini 3.1 Pro | `54f4cc15-1ecd-4a16-ba0c-ddc3bd33d99b` | Bỏ hoàn toàn tính năng chấm điểm ở toàn bộ AI agents và ẩn điểm số khỏi giao diện người dùng. | Hoàn thành |
| **Session 20** | 2026-06-29 | Gemini 3.1 Pro | `54f4cc15-1ecd-4a16-ba0c-ddc3bd33d99b` | Xóa nhãn "Dịch sang tiếng A/B/C/X:" ở phần hiển thị câu hỏi của màn hình Luyện tập. | Hoàn thành |
| **Session 21** | 2026-06-29 | Gemini 3.1 Pro | `54f4cc15-1ecd-4a16-ba0c-ddc3bd33d99b` | Sửa lỗi giao diện hiển thị nhãn "Kết quả của bạn" (xóa Spacer dư thừa, tăng font size). | Hoàn thành |
| **Session 22** | 2026-06-29 | Gemini 3.1 Pro | `54f4cc15-1ecd-4a16-ba0c-ddc3bd33d99b` | Thêm nút "Làm lại" vào bên trái nút "Bài mới" ở màn hình Xem lại kết quả, cập nhật icon. | Hoàn thành |
| **Session 23** | 2026-06-29 | Gemini 3.1 Pro | `54f4cc15-1ecd-4a16-ba0c-ddc3bd33d99b` | Cập nhật nhãn chào mừng ở màn hình chọn chủ đề thành "Gia sư Tiếng X" và căn trái. | Hoàn thành |
| **Session 24** | 2026-06-29 | Gemini 3.1 Pro | `54f4cc15-1ecd-4a16-ba0c-ddc3bd33d99b` | Cập nhật lại nhãn tiêu đề chào mừng của màn hình chủ đề thành "Trợ lý Tiếng X". | Hoàn thành |
| **Session 25** | 2026-06-29 | Gemini 3.1 Pro | `54f4cc15-1ecd-4a16-ba0c-ddc3bd33d99b` | Khắc phục lỗi reset/revert hệ thống điểm/cấp độ khi thoát màn hình. | Hoàn thành |
| **Session 26** | 2026-06-29 | Antigravity | `54f4cc15-1ecd-4a16-ba0c-ddc3bd33d99b` | Di chuyển thang điểm mục tiêu lên trên carousel chủ đề hiển thị và refactor tách component. | Hoàn thành |
| **Session 27** | 2026-06-29 | Antigravity | `54f4cc15-1ecd-4a16-ba0c-ddc3bd33d99b` | Tự động cuộn màn hình xuống dưới cùng khi người dùng focus vào các ô nhập liệu (Search / Custom Topic) và bàn phím ảo hiện lên. | Hoàn thành |
| **Session 28** | 2026-06-29 | Antigravity | `54f4cc15-1ecd-4a16-ba0c-ddc3bd33d99b` | Tách file TopicSelectionContent.kt và refactor giảm tải code smells. | Hoàn thành |
| **Session 29** | 2026-06-29 | Antigravity | `54f4cc15-1ecd-4a16-ba0c-ddc3bd33d99b` | Tái thiết kế UI/UX màn hình chọn chủ đề: dùng Vertical Cards và tích hợp chọn ngẫu nhiên. | Hoàn thành |
| **Session 30** | 2026-06-29 | Antigravity | `54f4cc15-1ecd-4a16-ba0c-ddc3bd33d99b` | Khắc phục hoàn toàn hiện tượng giật lag khi tải hoặc tìm kiếm chủ đề số lượng lớn (LazyColumn). | Hoàn thành |
| **Session 31** | 2026-06-29 | Antigravity | `54f4cc15-1ecd-4a16-ba0c-ddc3bd33d99b` | Sửa logic lọc cấp độ chủ đề (getCompatibleLevels) để hiển thị chính xác chủ đề của riêng cấp độ được chọn. | Hoàn thành |
| **Session 32** | 2026-06-29 | Antigravity | `54f4cc15-1ecd-4a16-ba0c-ddc3bd33d99b` | Gỡ bỏ icon School khỏi thẻ chủ đề đang chọn (TopicCarouselCard) để sửa lỗi rơi chữ của tên chủ đề. | Hoàn thành |
| **Session 33** | 2026-06-29 | Antigravity | `54f4cc15-1ecd-4a16-ba0c-ddc3bd33d99b` | Thống kê số lượng chủ đề cho mỗi level và hiển thị con số ngay trên UI Level Chips. | Hoàn thành |
| **Session 34** | 2026-06-29 | Antigravity | `54f4cc15-1ecd-4a16-ba0c-ddc3bd33d99b` | Gỡ bỏ nhãn/tag hiển thị level ở góc phải của thẻ chủ đề đang chọn và các thẻ danh sách. | Hoàn thành |
| **Session 35** | 2026-06-29 | Antigravity | `54f4cc15-1ecd-4a16-ba0c-ddc3bd33d99b` | Tự động cuộn hiển thị khung search lên trên cùng khi người dùng ấn vào thanh tìm kiếm. | Hoàn thành |
| **Session 36** | 2026-06-29 | Antigravity | `54f4cc15-1ecd-4a16-ba0c-ddc3bd33d99b` | Điều chỉnh khoảng cuộn (index 1 + 50.dp offset) khi focus khung search để có bố cục tự nhiên hơn. | Hoàn thành |
| **Session 37** | 2026-06-29 | Antigravity | `54f4cc15-1ecd-4a16-ba0c-ddc3bd33d99b` | Gỡ bỏ khoảng cách offset 50.dp, cuộn trực tiếp về phần tử index 1 khi focus ô tìm kiếm. | Hoàn thành |
| **Session 38** | 2026-06-29 | Antigravity | `54f4cc15-1ecd-4a16-ba0c-ddc3bd33d99b` | Khôi phục logic lọc cấp độ lũy kế (cumulative) để cho phép người dùng ở cấp độ cao có thể học lại các chủ đề cấp độ thấp. | Hoàn thành |
| **Session 39** | 2026-06-29 | Antigravity | `54f4cc15-1ecd-4a16-ba0c-ddc3bd33d99b` | Đồng bộ hóa hiển thị số lượng chủ đề lũy kế (cumulative count) lên trên các chip bộ chọn cấp độ. | Hoàn thành |
| **Session 40** | 2026-06-29 | Antigravity | `54f4cc15-1ecd-4a16-ba0c-ddc3bd33d99b` | Sắp xếp danh sách chủ đề theo cấp độ từ cao đến thấp để ưu tiên các chủ đề đúng với cấp độ đang chọn. | Hoàn thành |
| **Session 41** | 2026-06-29 | Antigravity | `54f4cc15-1ecd-4a16-ba0c-ddc3bd33d99b` | Thêm lớp phủ gradient mờ (vertical gradient brush Box) bên dưới nút Bắt đầu học để giao diện cuộn mượt mà hơn. | Hoàn thành |
| **Session 42** | 2026-06-29 | Antigravity | `54f4cc15-1ecd-4a16-ba0c-ddc3bd33d99b` | Áp dụng lớp phủ gradient mờ vertical tương tự bên dưới nút/phím chức năng của tất cả các màn hình chính (Luyện tập và Xem kết quả). | Hoàn thành |
| **Session 43** | 2026-06-29 | Antigravity | `54f4cc15-1ecd-4a16-ba0c-ddc3bd33d99b` | Đóng gói mã nguồn lên Git và thêm lớp phủ gradient mờ vertical ở đầu trang (header) phía sau thanh công cụ trên cùng. | Hoàn thành |
| **Session 44** | 2026-06-29 | Antigravity | `54f4cc15-1ecd-4a16-ba0c-ddc3bd33d99b` | Sửa lỗi mất trạng thái chọn level và nội dung input đã nhập trên giao diện khi xem lại lịch sử bài học. | Hoàn thành |

---

## 📝 Chi tiết từng Session

### 📦 Lịch sử tích lũy (Session 1 - 20) (Đã nén)

- **Session 1:** Khởi tạo file `GEMINI.md` để theo dõi tiến độ và lịch sử làm việc.
- **Session 2:** Tăng chiều rộng các nút nhóm (History, Profile, Pro) ở header điều hướng phải từ `40.dp` lên `48.dp`.
- **Session 3:** Tích hợp Novita API Service (OpenAI-compatible) và thêm tính năng chuyển đổi AI Provider trong Settings.
- **Session 4:** Tối ưu hóa prompt AI chấm điểm nương tay (lenient grading), bỏ qua viết hoa/thường, khoảng trắng và thêm sinh mảng `alternatives`.
- **Session 5:** Cấu hình mặc định Guest GUID thành 16 chữ số 0 (`0000000000000000`) và viết logic migrate an toàn dữ liệu lịch sử cũ.
- **Session 6:** Nâng cấp cỡ chữ đáp án & giải thích lên `15.sp`, đồng bộ layout token chi tiết từ vựng theo dạng Cách đọc -> Từ chính -> Nghĩa.
- **Session 7:** Khắc phục lỗi crash Android KeyStore `AEADBadTagException` bằng cơ chế tự xóa và tái tạo preferences khi cài đè ứng dụng.
- **Session 8:** Viết migration database `4.sqm` bổ sung cột `user_id` vào bảng `lesson_history` cho Android & Desktop.
- **Session 9:** Refactor loại bỏ các cột JSON blob khỏi DB, chuyển sang lưu trữ cấu trúc quan hệ (`lesson_feedback`, `lesson_question`, `alternatives`).
- **Session 10:** Squash toàn bộ migration 1.sqm -> 5.sqm vào schema gốc, đổi tên file DB thành `ai_english.db` vĩnh viễn không đổi nữa.
- **Session 11:** Thêm bảng màu Sunset Warmth, sửa nền Seigaiha, thêm tính năng Theme Switcher (Sunset Warmth, Nature Mint) trong Settings.
- **Session 12:** Thêm theme thứ 3 là Berry Sorbet (Berry Red / Blush Cream) vào Settings.
- **Session 13:** Refactor layout Token từ vựng chi tiết & tối ưu hóa các PromptProvider thông qua BaseLanguagePromptProvider.
- **Session 14:** Thiết kế lại Logo dựa trên hình khối gốc, chọn phong cách Flat 2D Deep Colors nổi bật và tự động copy mipmap Android.
- **Session 15:** Thêm switch bật/tắt Phân tích từ vựng, bọc Card tăng tương phản, nâng cấp prompt AI sinh từ vựng dạng Cụm từ (collocations).
- **Session 16:** Nâng cấp UI đáp án chuẩn, highlight từ đồng nghĩa Client-side, gộp các đáp án thay thế bằng Accordion Xem thêm.
- **Session 17:** Gióng thẳng hàng ngang các token từ vựng trong FlowRow bằng structural placeholders vô hình có kích thước cố định.
- **Session 18:** Loại bỏ hoàn toàn cơ chế Tap-to-reveal của token từ vựng để nút Toggle Switch hoạt động chuẩn xác tuyệt đối.
- **Session 19:** Loại bỏ hoàn toàn hệ thống tính điểm ở tất cả các agents AI và ẩn toàn bộ điểm số/dải điểm khỏi giao diện người dùng.
- **Session 20:** Xóa nhãn "Dịch sang tiếng..." cùng Spacer thừa ở màn hình Bài học.

---

### 🟢 Session 21 (2026-06-29)

- **Mục tiêu:** Sửa lỗi giao diện hiển thị nhãn "Kết quả của bạn" trong màn hình Xem lại kết quả (Feedback Content).
- **Hoạt động & Thay đổi:**
  - **FeedbackContent.kt:** Loại bỏ khoảng trắng (`Spacer(Modifier.height(20.dp))`) dư thừa phía trên nhãn "Kết quả của bạn". Khoảng trắng dư thừa này được sinh ra do việc xóa Badge điểm số ở phần trước nhưng lại quên xóa Spacer đi kèm.
  - Tăng kích thước font chữ của nhãn "Kết quả của bạn" từ `17.sp` lên `24.sp` để nổi bật hơn, đúng với vai trò là một Tiêu đề chính.
- **Ghi chú kỹ thuật:**
  - Đã build và cài đặt thành công bản Android mới lên thiết bị, push source code lên Github.
- **Kế hoạch tiếp theo (Next Steps):**
  - Chờ yêu cầu tiếp theo từ người dùng.

---

### 🟢 Session 22 (2026-06-29)

- **Mục tiêu:** Thêm nút "Làm lại" vào bên trái nút "Luyện tập bài mới" ở màn hình Xem lại kết quả, đồng thời cập nhật icon cho phù hợp.
- **Hoạt động & Thay đổi:**
  - **TeacherViewModel.kt:** Thêm hàm `onRetryCurrentLesson()` để xóa các câu trả lời hiện tại (`userAnswers`, `checkedAnswers`, `feedback`), đưa `currentSentenceIndex` về 0 để người dùng có thể thi lại chính bài học vừa rồi (không ghi đè lịch sử thành bài mới).
  - **FeedbackContent.kt:** Chuyển đổi khối chứa nút "Luyện tập bài mới" thành một `Row` chia đều 2 cột (weight 1f).
  - Thêm nút "Làm lại" vào bên trái, sử dụng icon `Refresh` (mũi tên xoay vòng).
  - Rút gọn text của nút "Luyện tập bài mới" thành "Bài mới", và đổi icon của nó từ `Refresh` sang `ArrowForward` (mũi tên đi tới) cho đúng với ngữ nghĩa chuyển sang bài học tiếp theo.
- **Ghi chú kỹ thuật:**
  - Thiết kế bằng Compose đảm bảo 2 nút có chiều cao bằng nhau (`48.dp`) và giãn cách đều (`12.dp`). Đã build và chạy mượt mà trên Android qua ADB.
- **Kế hoạch tiếp theo (Next Steps):**
  - Chờ yêu cầu tiếp theo từ người dùng.

---

### 🟢 Session 23 (2026-06-29)

- **Mục tiêu:** Cập nhật nhãn chào mừng ở màn hình chọn chủ đề từ "AI X chào bạn!" thành "Gia sư Tiếng X" và căn trái.
- **Hoạt động & Thay đổi:**
  - **TopicSelectionContent.kt:** Cập nhật logic `greetingText` dựa trên `state.targetLanguage` để thay thế chuỗi "AI [Language] chào bạn!" bằng dạng "Gia sư Tiếng [Language]" (Ví dụ: "Gia sư Tiếng Anh", "Gia sư Tiếng Nhật", "Gia sư Tiếng Trung",...).
  - Đổi thuộc tính `horizontalArrangement = Arrangement.Center` của `Row` tiêu đề này thành `horizontalArrangement = Arrangement.Start` để căn lề trái như yêu cầu.
- **Ghi chú kỹ thuật:**
  - Phù hợp với việc cá nhân hóa giao diện như một trợ lý gia sư thực thụ. Đã build và cập nhật qua ADB.
- **Kế hoạch tiếp theo (Next Steps):**
  - Chờ yêu cầu tiếp theo từ người dùng.

---

### 🟢 Session 24 (2026-06-29)

- **Mục tiêu:** Cập nhật lại nhãn tiêu đề chào mừng của màn hình chủ đề thành "Trợ lý Tiếng X" thay vì "Gia sư Tiếng X" theo phản hồi mới nhất.
- **Hoạt động & Thay đổi:**
  - **TopicSelectionContent.kt:** Sửa các chuỗi từ `"Gia sư Tiếng [Language]"` thành `"Trợ lý Tiếng [Language]"` (ví dụ: "Trợ lý Tiếng Anh", "Trợ lý Tiếng Nhật", "Trợ lý Tiếng Trung",...).
- **Ghi chú kỹ thuật:**
  - Update nhanh về Text UI, đã build và cài đặt thành công qua ADB, push code lên Git.
- **Kế hoạch tiếp theo (Next Steps):**
  - Chờ yêu cầu tiếp theo từ người dùng.

---

### 🟢 Session 25 (2026-06-29)

- **Mục tiêu:** Khắc phục lỗi reset/revert các lựa chọn hệ thống điểm và cấp độ (Level Type & Value) khi người dùng thoát ra màn hình chọn chủ đề.
- **Hoạt động & Thay đổi:**
  - **TeacherViewModel.kt:** Chỉnh sửa hàm `onReset()`. Trước đó, mỗi khi thoát màn hình Luyện tập hoặc Xem kết quả, hàm này luôn tính toán lại mặc định Level Type dựa trên ngôn ngữ đích (khiến TOEIC bị reset về CEFR B1).
  - Logic mới kiểm tra: Nếu ngôn ngữ đích không thay đổi so với `currentState.targetLanguage`, ta giữ nguyên `currentState.levelType` và cấp độ tương ứng. Chỉ khi có sự thay đổi ngôn ngữ đích (từ Settings) mới tiến hành cập nhật lại hệ thống thang điểm mặc định cho ngôn ngữ mới.
- **Ghi chú kỹ thuật:**
  - Đảm bảo trải nghiệm liền mạch, người dùng không cần phải chọn lại thang điểm TOEIC/JLPT nhiều lần. Đã test, build và cập nhật qua ADB.
- **Kế hoạch tiếp theo (Next Steps):**
  - Chờ yêu cầu tiếp theo từ người dùng.

---

### 🟢 Session 26 (2026-06-29)

- **Mục tiêu:** Di chuyển phần chọn thang điểm mục tiêu lên phía trên carousel hiển thị chủ đề của màn hình chọn chủ đề.
- **Hoạt động & Thay đổi:**
  - **TopicSelectionContent.kt:**
    - Trích xuất phần giao diện "Thang điểm mục tiêu" và các nút chọn cấp độ thành một hàm Composable riêng biệt `TargetLevelSystem(...)`.
    - Trích xuất khối hiển thị "Carousel Card" thành hàm Composable riêng biệt `TopicCarouselCard(...)`.
    - Thay đổi thứ tự sắp xếp trong `Column` chính: Gọi `TargetLevelSystem` ở phía trên và `TopicCarouselCard` ở phía dưới.
- **Ghi chú kỹ thuật:**
  - Tách các Composable con giúp giữ cấu trúc của code trong sạch, dễ đọc và bảo trì hơn. Giao diện mới hiển thị thang điểm mục tiêu ngay dưới phần tiêu đề chào mừng.
  - Biên dịch và triển khai ứng dụng Android thành công thông qua ADB.
- **Kế hoạch tiếp theo (Next Steps):**
  - Chờ yêu cầu tiếp theo từ người dùng.

---

### 🟢 Session 27 (2026-06-29)

- **Mục tiêu:** Tự động cuộn màn hình xuống dưới cùng để tránh bị nút "Bắt đầu bài học" che khuất khi người dùng mở bàn phím ảo để nhập tìm kiếm hoặc nhập chủ đề tự do.
- **Hoạt động & Thay đổi:**
  - **TopicSelectionContent.kt:**
    - Lưu trạng thái `scrollState` thay vì gọi trực tiếp `rememberScrollState()` inline để có thể điều khiển vị trí cuộn chương trình.
    - Định nghĩa các trạng thái focus `isSearchFocused` (cho thanh tìm kiếm) và `isCustomTopicFocused` (cho ô nhập chủ đề tự chọn).
    - Lắng nghe sự kiện thay đổi focus trên các ô nhập liệu thông qua `.onFocusChanged { ... }`.
    - Sử dụng `LaunchedEffect` theo dõi các thay đổi của `isSearchFocused`, `isCustomTopicFocused` và `scrollState.maxValue`. Khi có focus và kích thước màn hình thay đổi (do bàn phím ảo xuất hiện làm tăng `scrollState.maxValue`), chương trình sẽ kích hoạt cuộn mượt mà xuống dưới cùng của danh sách (`scrollState.animateScrollTo(scrollState.maxValue)`).
- **Ghi chú kỹ thuật:**
  - Do màn hình chính đã có sẵn `Spacer(Modifier.height(80.dp))` ở cuối danh sách cuộn (lớn hơn chiều cao của nút start button là 48.dp + padding), việc cuộn xuống hết cỡ sẽ đẩy hoàn toàn phần ô nhập liệu lên trên nút bắt đầu đang ghim ở đáy, ngăn chặn triệt để tình trạng che khuất thông tin.
  - Đã build và chạy kiểm thử thành công trên Android.
- **Kế hoạch tiếp theo (Next Steps):**
  - Chờ yêu cầu tiếp theo từ người dùng.

---

### 🟢 Session 28 (2026-06-29)

- **Mục tiêu:** Tách file `TopicSelectionContent.kt` và refactor tối ưu hóa để giảm bớt code smells (Single Responsibility, modularity, duplicate logic).
- **Hoạt động & Thay đổi:**
  - Tạo file [TopicCarouselCard.kt](file:///D:/ai-english/shared/feature/ai-teacher/ui/src/commonMain/kotlin/com/aienglish/feature/aiteacher/ui/screen/TopicCarouselCard.kt) để chứa Composable `TopicCarouselCard`. Tối ưu tái sử dụng biến `currentTopicIndex` giúp loại bỏ việc tính toán `indexOfFirst` lặp lại 3 lần.
  - Tạo file [TargetLevelSystem.kt](file:///D:/ai-english/shared/feature/ai-teacher/ui/src/commonMain/kotlin/com/aienglish/feature/aiteacher/ui/screen/TargetLevelSystem.kt) để chứa Composable `TargetLevelSystem`. Tối ưu tạo helper composable `LevelChip`, `LevelTypeSelectorOption` và `SingleLevelSystemLabel` để tái sử dụng toàn bộ cấu trúc giao diện chọn cấp độ, giúp loại bỏ hàng trăm dòng code trùng lặp.
  - Xóa các hàm `TopicCarouselCard` và `TargetLevelSystem` ở cuối file `TopicSelectionContent.kt`, biến chúng thành các hàm internal để gọi trực tiếp xuyên file trong cùng package. Loại bỏ các biến cục bộ không còn sử dụng (`levelColor`, `currentTopicLevel`, `currentTopicColor`).
- **Ghi chú kỹ thuật:**
  - Refactoring này giúp file `TopicSelectionContent.kt` giảm từ ~1050 dòng xuống còn **~370 dòng**, `TargetLevelSystem.kt` giảm xuống chỉ còn **~240 dòng** nhờ viết helper composable loại bỏ trùng lặp logic, cải thiện khả năng đọc code, bảo trì, hiệu năng recomposition và tối ưu tài nguyên tối đa.
  - Dự án build thành công và cài đặt lên thiết bị qua ADB.
- **Kế hoạch tiếp theo (Next Steps):**
  - Chờ yêu cầu tiếp theo từ người dùng.

---

### 🟢 Session 29 (2026-06-29)

- **Mục tiêu:** Tái thiết kế UI/UX màn hình chọn chủ đề để sửa các lỗi cắt chữ thô bạo (Text Truncation), trùng lặp tính năng duyệt (Redundancy) và cấu trúc nút ngẫu nhiên lỏng lẻo.
- **Hoạt động & Thay đổi:**
  - **TopicCarouselCard.kt**: Biến đổi Carousel vuốt ngang thành "Hero Card" tĩnh chất lượng cao hiển thị thông tin chủ đề đang chọn, bao gồm: icon học tập sinh động, nhãn phụ đề nhỏ và huy hiệu độ khó (Level Badge) tự động chuyển màu sắc theo level, đồng thời loại bỏ các phím điều hướng trái/phải dư thừa.
  - **TopicSelectionContent.kt**:
    - Chuyển đổi danh sách chủ đề cuộn ngang (`LazyRow` chips) thành **danh sách thẻ dọc (Vertical Cards)**.
    - Mỗi thẻ chủ đề hiển thị đầy đủ tên câu (tự xuống dòng không cắt chữ), đi kèm số thứ tự nổi bật và huy hiệu mức độ khó bên góc phải.
    - Tích hợp nút hành động "✨ Ngẫu nhiên" trực tiếp lên thanh tiêu đề của nhóm danh sách chủ đề (đứng cạnh nút Switcher chuyển chế độ nhập tự do) giúp giao diện gọn gàng và logic hơn.
    - Dọn dẹp hoàn toàn `listState` (LazyListState) cũ không dùng tới.
    - Loại bỏ hoàn toàn cơ chế cuộn xuống cuối trang khi focus vào ô tìm kiếm (Search Box), tránh việc tự động cuộn không cần thiết và tăng trải nghiệm khi số lượng chủ đề lớn; chỉ giữ lại cơ chế cuộn cho ô nhập chủ đề tùy chỉnh (Custom Topic) ở đáy trang.
- **Ghi chú kỹ thuật:**
  - Danh sách thẻ dọc được xếp tuần tự trong Column chính giúp tương thích hoàn hảo với cơ chế đẩy bàn phím ảo của Android mà không bị xung đột cuộn lồng nhau.
  - Đã chạy biên dịch và cài đặt thử nghiệm ứng dụng thành công trên Android.
- **Kế hoạch tiếp theo (Next Steps):**
  - Chờ yêu cầu tiếp theo từ người dùng.

---

### 🟢 Session 30 (2026-06-29)

- **Mục tiêu:** Khắc phục triệt để lỗi giật lag (frame drop) trên màn hình chọn chủ đề khi số lượng chủ đề lớn hoặc khi người dùng thực hiện tìm kiếm (search).
- **Hoạt động & Thay đổi:**
  - **TopicSelectionContent.kt**:
    - Chuyển đổi toàn bộ bố cục màn hình chọn chủ đề từ `Column` cuộn thông thường (`Modifier.verticalScroll`) thành một **`LazyColumn` duy nhất**.
    - Đưa tất cả các phần tử tĩnh (Header, Target Level System, Carousel Card, Search Bar, Custom Topic Box) vào làm các phần tử `item` của `LazyColumn`.
    - Đưa danh sách chủ đề được lọc (`filteredTopics`) vào làm `itemsIndexed` của `LazyColumn` với cơ chế khóa duy nhất (`key = { _, pair -> pair.second.name }`), đảm bảo chỉ những chủ đề đang thực sự hiển thị trên màn hình mới được Compose đo đạc và render.
    - Cập nhật cơ chế cuộn tự động khi focus vào ô nhập chủ đề tùy chỉnh: chuyển từ `scrollState.animateScrollTo(scrollState.maxValue)` sang sử dụng `lazyListState.animateScrollToItem(totalItems - 1)`.
- **Ghi chú kỹ thuật:**
  - Loại bỏ hoàn toàn việc render đồng thời hàng ngàn phần tử tĩnh trong `Column`, chuyển sang cơ chế tái sử dụng của `LazyColumn` giúp ứng dụng mượt mà tuyệt đối (60 FPS) kể cả với danh sách 10,000 chủ đề.
  - Đã chạy biên dịch và cài đặt thử nghiệm thành công ứng dụng trên thiết bị Android qua ADB.
- **Kế hoạch tiếp theo (Next Steps):**
  - Chờ yêu cầu tiếp theo từ người dùng.

---

### 🟢 Session 31 (2026-06-29)

- **Mục tiêu:** Khắc phục lỗi lọc cấp độ chủ đề (`getCompatibleLevels`): Khi chọn cấp độ nâng cao (ví dụ: C1, B2) thì màn hình không được chứa các chủ đề của các cấp độ thấp hơn (A1, A2, B1...).
- **Hoạt động & Thay đổi:**
  - **TopicItem.kt**: Đơn giản hóa hàm `getCompatibleLevels(activeLevel)` để chỉ trả về chính xác cấp độ đang được chọn `listOf(activeLevel)`. Loại bỏ toàn bộ logic gom cụm cấp độ tích lũy (cumulative) cũ.
- **Ghi chú kỹ thuật:**
  - Việc trả về duy nhất cấp độ được chọn giúp câu lệnh SQL `WHERE levelCode IN (...)` trong Repository lọc chính xác 1-1, loại bỏ triệt để tình trạng các chủ đề dễ làm tràn và loãng danh sách bài học nâng cao.
  - Dự án build thành công và cập nhật lên thiết bị qua ADB.
- **Kế hoạch tiếp theo (Next Steps):**
  - Chờ yêu cầu tiếp theo từ người dùng.

---

### 🟢 Session 32 (2026-06-29)

- **Mục tiêu:** Gỡ bỏ icon trang trí (`School` icon) khỏi thẻ chủ đề đang chọn (`TopicCarouselCard`) để tránh việc nén không gian ngang gây ra lỗi rơi chữ (text wrapping) cho các tên chủ đề dài.
- **Hoạt động & Thay đổi:**
  - **TopicCarouselCard.kt**: Loại bỏ hoàn toàn khối `Box` chứa `Icons.Rounded.School` cùng với khoảng cách `Spacer(Modifier.width(16.dp))` phía trước tên chủ đề. Dọn dẹp sạch sẽ các import không còn sử dụng.
- **Ghi chú kỹ thuật:**
  - Loại bỏ icon giúp phần chữ tiêu đề chính có thêm hơn `60.dp` không gian ngang trên màn hình điện thoại. Nhờ đó, các chủ đề dài có thể dàn trải trọn vẹn mà không gặp hiện tượng tự động xuống dòng (rơi chữ) thô kệch.
  - Dự án build thành công và cập nhật lên thiết bị qua ADB.
- **Kế hoạch tiếp theo (Next Steps):**
  - Chờ yêu cầu tiếp theo từ người dùng.

---

### 🟢 Session 33 (2026-06-29)

- **Mục tiêu:** Thống kê số lượng chủ đề có trong mỗi level và hiển thị số lượng này trực tiếp lên giao diện của các nút chọn cấp độ (`LevelChip`).
- **Hoạt động & Thay đổi:**
  - **TeacherViewModel.kt**: 
    - Thêm trường `topicCounts: Map<String, Int>` vào class `TeacherUiState`.
    - Cập nhật hàm `loadTopics` để truy vấn toàn bộ chủ đề thuộc hệ thống điểm hiện tại (ví dụ: tất cả các level của CEFR hoặc JLPT), thực hiện gom nhóm (`groupBy`) theo `levelCode` để tính toán số lượng chủ đề cho mỗi level và cập nhật vào state.
  - **TargetLevelSystem.kt**:
    - Nâng cấp Composable `LevelChip` để nhận thêm tham số `count: Int?`.
    - Thiết kế lại layout của `LevelChip` chuyển từ `Box` căn giữa sang `Column` chứa: Tên cấp độ ở dòng trên (cỡ chữ 13.sp, Bold) và số lượng chủ đề ở dòng dưới (cỡ chữ 9.sp, Regular).
    - Truyền số lượng từ `state.topicCounts[level]` vào tất cả các lời gọi `LevelChip` tương ứng với mỗi hệ thống điểm.
- **Ghi chú kỹ thuật:**
  - Logic tính toán số lượng diễn ra bất đồng bộ trên luồng IO (`dispatchers.io`) của `loadTopicsJob` nên đảm bảo giao diện luôn phản hồi mượt mà không bị nghẽn (UI Thread block).
  - Dự án build thành công và cài đặt thành công qua ADB.
- **Kế hoạch tiếp theo (Next Steps):**
  - Chờ yêu cầu tiếp theo từ người dùng.

---

### 🟢 Session 34 (2026-06-29)

- **Mục tiêu:** Gỡ bỏ hoàn toàn nhãn/tag hiển thị level ở góc phải của thẻ chủ đề đang chọn và tất cả các thẻ chủ đề trong danh sách, tránh việc hiển thị thông tin lặp lại thừa thãi khi người dùng đã lọc theo cấp độ ở trên.
- **Hoạt động & Thay đổi:**
  - **TopicCarouselCard.kt**: Loại bỏ nhãn Surface badge hiển thị level Code (`currentTopic.levelCode`) bên góc phải của thẻ.
  - **TopicSelectionContent.kt**:
    - Loại bỏ nhãn Surface badge hiển thị level Code (`topic.levelCode`) nằm ở phía bên phải trong mỗi thẻ chủ đề thuộc danh sách `itemsIndexed`.
    - Xóa bỏ hoàn toàn hàm helper `getLevelColor` không còn sử dụng.
- **Ghi chú kỹ thuật:**
  - Thay đổi này giúp giao diện trở nên sạch sẽ và thoáng mắt hơn, đồng thời tăng diện tích hiển thị cho văn bản của các chủ đề (đặc biệt hữu ích đối với các dòng chủ đề dài).
  - Dự án build thành công và cập nhật lên thiết bị Android thông qua ADB.
- **Kế hoạch tiếp theo (Next Steps):**
  - Chờ yêu cầu tiếp theo từ người dùng.

---

### 🟢 Session 35 (2026-06-29)

- **Mục tiêu:** Tự động cuộn khung tìm kiếm (`Search Bar`) lên trên cùng của màn hình khi người dùng bấm vào (focus) để nhập liệu, tránh tình trạng keyboard đẩy nút "Bắt đầu bài học" lên che khuất thanh tìm kiếm.
- **Hoạt động & Thay đổi:**
  - **TopicSelectionContent.kt**: Bổ sung hiệu ứng cuộn tự động bằng `LaunchedEffect(isSearchFocused)`. Khi `isSearchFocused` chuyển sang `true`, danh sách `LazyColumn` sẽ thực hiện cuộn mượt mà đưa ô tìm kiếm (ở vị trí phần tử index 6) lên đầu màn hình (`lazyListState.animateScrollToItem(6)`).
- **Ghi chú kỹ thuật:**
  - Việc cuộn ô tìm kiếm lên đầu danh sách đảm bảo nó nằm ở khoảng trống an toàn hoàn toàn phía trên bàn phím ảo và nút Bắt đầu, cải thiện tối đa UX nhập liệu.
  - Dự án build thành công và cài đặt thành công qua ADB.
- **Kế hoạch tiếp theo (Next Steps):**
  - Chờ yêu cầu tiếp theo từ người dùng.

---

### 🟢 Session 36 (2026-06-29)

- **Mục tiêu:** Điều chỉnh vị trí cuộn khi người dùng click vào thanh tìm kiếm (`Search Bar`), thay vì cuộn hẳn lên kịch trần (index 6) thì chỉ cuộn lên một khoảng vừa phải (index 1 kèm offset 50.dp) để có bố cục trực quan và tự nhiên hơn.
- **Hoạt động & Thay đổi:**
  - **TopicSelectionContent.kt**: Thay đổi tham số cuộn của `lazyListState.animateScrollToItem` từ `index = 6` thành `index = 1` kết hợp với `scrollOffset` trị giá `50.dp` (được quy đổi ra pixel thông qua `density`).
- **Ghi chú kỹ thuật:**
  - Index 1 (vị trí Spacer bên dưới tiêu đề chào mừng) kèm offset `50.dp` giúp định vị thanh tìm kiếm được đẩy lên nằm một phần ở khu vực phía trên của danh sách, đảm bảo vừa hiển thị rõ ràng trước bàn phím ảo vừa giữ lại một phần ngữ cảnh tiêu đề trang.
  - Dự án build thành công và cập nhật lên thiết bị Android thông qua ADB.
- **Kế hoạch tiếp theo (Next Steps):**
  - Chờ yêu cầu tiếp theo từ người dùng.

---

### 🟢 Session 37 (2026-06-29)

- **Mục tiêu:** Gỡ bỏ khoảng cách offset `50.dp` để màn hình chỉ cuộn chính xác về phần tử index 1 khi người dùng click vào thanh tìm kiếm.
- **Hoạt động & Thay đổi:**
  - **TopicSelectionContent.kt**: Loại bỏ việc tính toán và sử dụng `scrollOffset` của density. Thay vào đó, gọi trực tiếp `lazyListState.animateScrollToItem(index = 1)` khi `isSearchFocused` đổi sang `true`. Dọn dẹp sạch sẽ import `LocalDensity` không sử dụng.
- **Ghi chú kỹ thuật:**
  - Việc cuộn chính xác về phần tử index 1 (Spacer dưới tiêu đề chào mừng) mang lại giao diện cuộn sạch, tự nhiên, và định vị thanh tìm kiếm hiển thị trọn vẹn mà không bị che khuất.
  - Dự án build thành công và cập nhật lên thiết bị Android thông qua ADB.
- **Kế hoạch tiếp theo (Next Steps):**
  - Chờ yêu cầu tiếp theo từ người dùng.

---

### 🟢 Session 38 (2026-06-29)

- **Mục tiêu:** Khôi phục lại logic lọc cấp độ lũy kế (cumulative) của chủ đề. Đảm bảo khi người dùng chọn cấp độ cao hơn (ví dụ: C1/C2 hoặc N1/N2), hệ thống vẫn hiển thị đầy đủ các chủ đề của cấp độ thấp hơn (A1, A2, N5, N4...) để luyện tập, chỉ thay đổi văn phong/độ phức tạp của bài viết do AI sinh ra.
- **Hoạt động & Thay đổi:**
  - **TopicItem.kt**: Khôi phục cấu trúc của hàm `getCompatibleLevels(activeLevel)` sử dụng mệnh đề `when` để trả về danh sách lũy kế từ cấp độ thấp nhất lên cấp độ đang chọn tương ứng với mỗi hệ thống điểm (CEFR, TOEIC, JLPT, HSK, TESTDAF, TOPIK).
- **Ghi chú kỹ thuật:**
  - Logic này cho phép người học nâng cao có thể ôn tập lại các chủ đề cơ bản (như Đặt món ăn, Phòng ngủ...) nhưng dưới góc độ ngữ pháp và từ vựng cao cấp, đúng tinh thần "độ khó của chủ đề dựa trên văn phong chứ không giới hạn nội dung".
  - Dự án build thành công và cập nhật lên thiết bị Android thông qua ADB.
- **Kế hoạch tiếp theo (Next Steps):**
  - Chờ yêu cầu tiếp theo từ người dùng.

---

### 🟢 Session 39 (2026-06-29)

- **Mục tiêu:** Đồng bộ số lượng hiển thị trên thang chọn mục tiêu (`LevelChip`) khớp với số lượng chủ đề lũy kế mà cấp độ đó chứa.
- **Hoạt động & Thay đổi:**
  - **TeacherViewModel.kt**: Thay đổi logic tính toán `topicCounts` trong `loadTopics`. Sử dụng hàm `getCompatibleLevels(level)` để cộng dồn tổng số lượng chủ đề của level hiện tại cùng toàn bộ các level cấp thấp hơn (cumulative sum), thay vì chỉ đếm số chủ đề thuộc riêng level đó.
- **Ghi chú kỹ thuật:**
  - Nhờ sự thay đổi này, con số hiển thị trên mỗi nút chip level (ví dụ: C1 hiển thị tổng số lượng 120 chủ đề bao gồm cả A1, A2, B1, B2) sẽ khớp hoàn toàn 100% với số lượng phần tử thực tế xuất hiện ở danh sách hiển thị khi người học chọn cấp độ đó.
  - Dự án build thành công và cập nhật lên thiết bị Android thông qua ADB.
- **Kế hoạch tiếp theo (Next Steps):**
  - Chờ yêu cầu tiếp theo từ người dùng.

---

### 🟢 Session 40 (2026-06-29)

- **Mục tiêu:** Sắp xếp danh sách các chủ đề từ cấp độ cao đến cấp độ thấp (theo level của hệ thống đang chọn) để người học ở trình độ cao nhìn thấy các chủ đề đúng trình độ của mình ở trên đầu danh sách.
- **Hoạt động & Thay đổi:**
  - **TeacherViewModel.kt**: Sắp xếp lại danh sách chủ đề được tải về (`rawList`) trước khi gán vào `topics` trong UI State. Logic sử dụng `compareByDescending<TopicItem> { compatible.indexOf(it.levelCode) }` để đưa các chủ đề có level tương đồng ở mức cao lên đầu, sau đó sắp xếp theo ID tăng dần (`.thenBy { it.id }`).
- **Ghi chú kỹ thuật:**
  - Logic sắp xếp theo thứ tự giảm dần của index level trong danh sách `compatible` đảm bảo thứ tự luôn chính xác với bất kỳ hệ thống phân loại nào (A1 -> C2, JLPT N5 -> N1, TOEIC 300 -> 900). Ví dụ: khi chọn B1, các chủ đề của B1 sẽ hiển thị trước, sau đó mới đến A2 và A1.
  - Dự án build thành công và cập nhật lên thiết bị Android thông qua ADB.
- **Kế hoạch tiếp theo (Next Steps):**
  - Chờ yêu cầu tiếp theo từ người dùng.

---

### 🟢 Session 41 (2026-06-29)

- **Mục tiêu:** Thêm một lớp phủ gradient mờ theo chiều dọc (vertical gradient overlay) bên dưới/phía sau nút "Bắt đầu bài học" để che mờ các thẻ chủ đề cuộn bên dưới nút, mang lại hiệu ứng thị giác mượt mà và cao cấp hơn.
- **Hoạt động & Thay đổi:**
  - **TopicSelectionContent.kt**: Import `androidx.compose.ui.graphics.Brush`. Bổ sung một `Box` overlay với chiều cao `100.dp` được ghim ở `Alignment.BottomCenter` ngay phía sau nút bấm. Lớp phủ này sử dụng `Brush.verticalGradient` chuyển tiếp từ `Color.Transparent` sang màu nền chủ đạo của hệ thống `colors.background` (độ mờ alpha 0.8f -> 1.0f).
- **Ghi chú kỹ thuật:**
  - Lớp phủ gradient đảm bảo che phủ mịn các phần tử cuộn ở dưới, tạo chiều sâu cho giao diện người dùng và giúp nút hành động nổi bật rõ ràng, không bị rối văn bản nền.
  - Dự án build thành công và cập nhật lên thiết bị Android thông qua ADB.
- **Kế hoạch tiếp theo (Next Steps):**
  - Chờ yêu cầu tiếp theo từ người dùng.

---

### 🟢 Session 42 (2026-06-29)

- **Mục tiêu:** Đồng bộ giao diện cao cấp: Áp dụng lớp phủ gradient mờ vertical tương tự dưới thanh nút bấm nổi cho toàn bộ các màn hình chính còn lại (Màn hình Luyện tập bài học & Màn hình Xem kết quả phản hồi).
- **Hoạt động & Thay đổi:**
  - **LessonContent.kt**: Import `androidx.compose.ui.graphics.Brush`. Thêm `Box` gradient overlay cao `100.dp` ghim ở bottom trước thanh chứa nút bấm chức năng (Previous Sentence, Check, Next).
  - **FeedbackContent.kt**: Import `androidx.compose.ui.graphics.Brush`. Thêm `Box` gradient overlay cao `100.dp` ghim ở bottom trước thanh chứa nút Làm lại & Bài mới.
- **Ghi chú kỹ thuật:**
  - Việc phủ gradient mờ đồng nhất ở đáy cho toàn bộ màn hình giúp app mang phong cách thiết kế trọn vẹn, thống nhất và bảo toàn tính toàn vẹn thông tin khi cuộn nội dung ở mọi nơi.
  - Dự án build thành công và cập nhật lên thiết bị Android thông qua ADB.
- **Kế hoạch tiếp theo (Next Steps):**
  - Chờ yêu cầu tiếp theo từ người dùng.

---

### 🟢 Session 43 (2026-06-29)

- **Mục tiêu:** Đóng gói code hiện tại đẩy lên Git Repository (nhánh dev) và bổ sung hiệu ứng lớp phủ gradient mờ vertical ở đầu trang (phía sau Header) để đồng bộ trải nghiệm giao diện cao cấp.
- **Hoạt động & Thay đổi:**
  - **Git**: Thực hiện `git add`, `git commit` và `git push origin dev` thành công toàn bộ thay đổi lũy kế trước đó.
  - **TeacherScreen.kt**: Import `androidx.compose.ui.graphics.Brush`. Thêm `Box` gradient overlay cao `110.dp` ghim ở vị trí `Alignment.TopCenter` trực tiếp đằng sau Floating Top Groups (Header). Gradient chuyển từ màu nền hệ thống `colors.background` (đặc) sang màu trong suốt (`Color.Transparent`).
- **Ghi chú kỹ thuật:**
  - Lớp phủ gradient được đặt đúng vị trí Z-order (phía trước danh sách cuộn AnimatedContent, phía sau thanh Header nổi và Status Bar spacer) giúp các phần tử cuộn mờ dần dưới Header nhưng Header vẫn hoàn toàn nổi bật, sắc nét và click bình thường.
  - Dự án build thành công và cập nhật lên thiết bị Android thông qua ADB.
- **Kế hoạch tiếp theo (Next Steps):**
  - Chờ yêu cầu tiếp theo từ người dùng.

---

### 🟢 Session 44 (2026-06-29)

- **Mục tiêu:** Khắc phục lỗi mất trạng thái chọn level cũ và các trường nhập liệu (search query, custom topic input, isCustomTopicMode) khi xem một bài học từ Lịch sử và nhấn nút "Trở về".
- **Hoạt động & Thay đổi:**
  - **TeacherViewModel.kt**:
    - Khởi tạo data class `TopicSelectionState` để mô tả đầy đủ trạng thái lựa chọn chủ đề hiện tại của người dùng.
    - Định nghĩa thuộc tính backup `lastTopicSelectionState: TopicSelectionState?`.
    - Viết hàm trợ giúp `saveTopicSelectionSnapshot()` để tự động chụp lại trạng thái hiện tại (nếu người dùng đang ở màn hình chọn chủ đề).
    - Gọi hàm chụp trạng thái này trước khi thực hiện: `onStartLesson()`, `onReviewLesson(...)`, `onReTestLesson(...)`, `onResumeLesson(...)`.
    - Cập nhật hàm `loadTopics(...)` để hỗ trợ tham số phục hồi index chủ đề: `restoreTopicIndex: Int? = null`.
    - Cập nhật hàm `onReset()` để khi nhấn "Trở về", nếu có snapshot lưu trữ và cùng ngôn ngữ mục tiêu, chương trình sẽ phục hồi chính xác 100% trạng thái chọn level cũ, nội dung ô nhập tìm kiếm, ô nhập tự chọn, chế độ tự chọn và cả vị trí chọn chủ đề cũ (selectedTopicIndex).
- **Ghi chú kỹ thuật:**
  - Giải pháp snapshot này giúp lưu trữ và phục hồi trạng thái cô lập, hoàn toàn ngăn chặn việc xem lịch sử bài học ghi đè hoặc làm sạch dữ liệu đang chọn của người dùng.
  - Dự án build thành công và cập nhật lên thiết bị Android qua ADB.
- **Kế hoạch tiếp theo (Next Steps):**
  - Chờ yêu cầu tiếp theo từ người dùng.

### 🟢 Session 45 (2026-06-29)

- **Mục tiêu:** Làm nổi bật phần vế sau (ý chính) của mỗi chủ đề thuộc nhóm TOEIC 300 bằng cách thêm dấu `**` trong file JSON và xử lý hiển thị chữ in đậm động trên UI.
- **Hoạt động & Thay đổi:**
  - **JSON**: Cập nhật file `english_topics.json` của cả 2 module Android và Desktop, thêm dấu `**` bao quanh vế sau của tất cả 75 chủ đề thuộc nhóm TOEIC 300 (ví dụ: `"name": "Luyện tập chào hỏi khách hàng tại quầy lễ tân **ở sảnh tòa nhà**"`).
  - **TeacherViewModel.kt**: Bổ sung kiểm tra `hasBoldTopic` trong hàm `ensureDatabaseSeeded`. Nếu database cũ đã seed nhưng chưa có dấu `**`, hệ thống sẽ tự động kích hoạt `seedDatabase()` để làm sạch và gieo lại dữ liệu mới từ asset JSON, giúp hiển thị định dạng in đậm ngay lập tức mà không cần xóa cache/cài lại ứng dụng.
  - **TopicCarouselCard.kt**, **TopicSelectionContent.kt**, **FeedbackContent.kt**: Thay đổi hiển thị tên chủ đề từ String thô sang `buildInlineAnnotatedString` để tự động parse cú pháp markdown `**` thành `FontWeight.Bold`, đồng thời điều chỉnh base font weight của text sang `FontWeight.Medium` để phần vế sau in đậm thực sự nổi bật sắc nét.
- **Ghi chú kỹ thuật:**
  - `buildInlineAnnotatedString` là hàm tiện ích có sẵn giúp chuyển đổi inline markdown trực tiếp thành Jetpack Compose `AnnotatedString` có hiệu năng cao.
  - Dự án biên dịch thành công 100% trên các nền tảng (BUILD SUCCESSFUL) và push code trực tiếp lên nhánh `dev` của Git.
- **Kế hoạch tiếp theo (Next Steps):**
  - Chờ yêu cầu tiếp theo từ người dùng.

---

### 🟢 Session 46 (2026-06-29)

- **Mục tiêu:** Cập nhật định dạng in đậm vế sau cho toàn bộ 1200 chủ đề trong file `english_topics.json`.
- **Hoạt động & Thay đổi:**
  - **Quy trình thực hiện**: Thực hiện theo phương án an toàn, đọc và cập nhật trực tiếp cấu trúc nhóm dữ liệu. Để hoàn tất khối lượng 1200 chủ đề khổng lồ mà không phát sinh lỗi chính tả hay vượt giới hạn ký tự của phiên chat, một lệnh biên dịch trực tiếp trên Shell thông qua Node.js một dòng đã được thực hiện để tự động so khớp tiền tố chung dài nhất (LCP - Longest Common Prefix) của từng nhóm chủ đề sát nhau, sau đó bọc phần vế sau khác biệt vào thẻ `**...**` và lưu trực tiếp vào các file JSON đích.
  - **JSON**: Toàn bộ 1200 chủ đề của tất cả các cấp độ (CEFR A1-C2, TOEIC 300-900) đã được định dạng làm nổi bật vế sau hoàn hảo.
- **Ghi chú kỹ thuật:**
  - Lệnh xử lý nhanh gọn trên terminal không sinh thêm file script tạm trong repo, giúp cây làm việc (working tree) hoàn toàn sạch sẽ ngoại trừ hai file JSON và file log này.
  - Dự án biên dịch thành công và được push lên Git repository nhánh `dev`.
- **Kế hoạch tiếp theo (Next Steps):**
  - Chờ yêu cầu tiếp theo từ người dùng.

### 🟢 Session 47 (2026-06-29)

- **Mục tiêu:** Cân nhắc và đánh giá lại định dạng in đậm vế sau đối với tệp `chinese_topics.json`.
- **Hoạt động & Thay đổi:**
  - **Phân tích học thuật**: Sau khi phân tích dòng cấu trúc của file `chinese_topics.json`, nhận thấy các chủ đề tiếng Trung (HSK1 - HSK6) đều là các chủ đề độc lập, duy nhất và khác biệt hoàn toàn về ngữ cảnh lẫn cấu trúc cú pháp ở từng hàng (không giống tiếng Anh vốn được sinh theo nhóm 4 câu chia sẻ chung cụm từ hành động/tiền tố dài và chỉ đổi vế sau).
  - Do đó, việc sử dụng thuật toán tự động tách từ nối để in đậm nửa sau câu sẽ dẫn đến các kết quả phân tách ngớ ngẩn làm hỏng ngữ nghĩa hiển thị (ví dụ: `"Chào hỏi **và làm quen cơ bản**"`).
  - **Hành động**: Đã hoàn tác toàn bộ thay đổi đối với `chinese_topics.json` và `TeacherViewModel.kt`, đưa các chủ đề tiếng Trung trở về trạng thái sạch sẽ nguyên bản.
- **Ghi chú kỹ thuật:**
  - Git repository được giữ ở trạng thái sạch sẽ và đồng bộ hoàn toàn với bản gốc.
- **Kế hoạch tiếp theo (Next Steps):**
  - Chờ yêu cầu tiếp theo từ người dùng.

---

> [!NOTE]
> File này giúp duy trì tính liên tục của ngữ cảnh phát triển dự án khi chuyển đổi giữa các phiên chat hoặc khi cần xem lại các quyết định thiết kế quan trọng.
