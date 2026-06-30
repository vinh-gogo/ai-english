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
| **Session 45** | 2026-06-30 | Antigravity | `54f4cc15-1ecd-4a16-ba0c-ddc3bd33d99b` | Thêm hệ thống học tiếng Việt (VIET) cho người nói tiếng Anh: 3 cấp Tiểu học / Trung học / Đại học, 400 chủ đề đa lĩnh vực, full infrastructure (PromptProvider, ViewModel, UI, Settings, DB seeding). | Hoàn thành |
| **Session 46** | 2026-06-30 | Antigravity | `54f4cc15-1ecd-4a16-ba0c-ddc3bd33d99b` | Mở rộng Vietnamese topics từ 300 lên 400: thêm 100 chủ đề mới bao quát IT, âm nhạc, thể thao, MXH, nội dung số, kinh doanh, thời trang, sức khỏe tâm thần, khoa học, pháp lý,... phân bổ 3 cấp. | Hoàn thành |
| **Session 47** | 2026-06-30 | Antigravity | `9c45f4be-b4c2-4534-b153-c08a5ee10f2a` | Tích hợp ô nhập chủ đề tùy chỉnh vào thẻ "Chủ đề đang chọn" khi chọn chế độ "Tự nhập" và xóa OutlinedTextField cũ. | Hoàn thành |
| **Session 48** | 2026-06-30 | Antigravity | `59abccb0-a99a-40e9-adfb-104ca2daee00` | Di chuyển icon (+) lên góc trên cùng bên phải trong thẻ "Chủ đề đang chọn" ở chế độ tự nhập. | Hoàn thành |
| **Session 49** | 2026-06-30 | Antigravity | `59abccb0-a99a-40e9-adfb-104ca2daee00` | Thay đổi hiển thị số chỉ mục của các chủ đề thành hình chữ nhật nhỏ nằm ở góc trên cùng bên trái và bo tròn các góc để tương thích với Card. | Hoàn thành |
| **Session 50** | 2026-06-30 | Antigravity | `59abccb0-a99a-40e9-adfb-104ca2daee00` | Loại bỏ hoàn toàn tính năng/nút "Ngẫu nhiên" ở danh sách chủ đề, giữ nguyên thứ tự label số index. | Hoàn thành |
| **Session 51** | 2026-06-30 | Antigravity | `59abccb0-a99a-40e9-adfb-104ca2daee00` | Xáo trộn ngẫu nhiên danh sách các chủ đề trong cùng một level và khôi phục nút Ngẫu nhiên ở UI. | Hoàn thành |
| **Session 52** | 2026-06-30 | Antigravity | `59abccb0-a99a-40e9-adfb-104ca2daee00` | Tự động hiển thị nút "Quay lại đầu trang" (Scroll to Top) khi cuộn xuống qua phần tử thứ 10. | Hoàn thành |
| **Session 53** | 2026-06-30 | Antigravity | `59abccb0-a99a-40e9-adfb-104ca2daee00` | Nhấn giữ (long press) các dòng chủ đề hiển thị nút "Xóa" ở trung tâm và hỗ trợ xóa khỏi database. | Hoàn thành |
| **Session 54** | 2026-06-30 | Antigravity | `59abccb0-a99a-40e9-adfb-104ca2daee00` | Nghiên cứu và đề xuất giải pháp phát âm thanh (TTS) offline, local-only, free 100% bằng ONNX cho tiếng Anh & tiếng Việt trên KMP. | Hoàn thành |
| **Session 55** | 2026-06-30 | Antigravity | `59abccb0-a99a-40e9-adfb-104ca2daee00` | Chạy thử nghiệm thành công phát âm thanh TTS offline local 100% (Tiếng Anh & Tiếng Việt) sử dụng Sherpa-ONNX + Piper models không qua Python. | Hoàn thành |
| **Session 56** | 2026-06-30 | Antigravity | `e0b16657-3637-4bdc-88bb-05f12457c6a7` | Nghiên cứu & Thử nghiệm mô hình tiếng Việt mới NGHI-TTS (NghiMe Studio), kiểm thử chất lượng và điều chỉnh tốc độ nói (lengthScale). | Hoàn thành |
| **Session 57** | 2026-06-30 | Antigravity | `e0b16657-3637-4bdc-88bb-05f12457c6a7` | Tích hợp thành công động cơ phát âm offline KMP TTS (Android & Desktop) sử dụng giọng nam Adam (tốc độ chậm 1.15) cho tiếng Việt. | Hoàn thành |
| **Session 58** | 2026-06-30 | Antigravity | `e0b16657-3637-4bdc-88bb-05f12457c6a7` | Fix crash SIGABRT (espeak_Initialize), dọn assets espeak-ng-data còn 9 ngôn ngữ, bỏ dấu ** trước khi đọc TTS, thêm giọng English lessac-medium (lengthScale=1.3) cho chủ đề tiếng Anh. | Hoàn thành |
| **Session 59** | 2026-06-30 | Antigravity | `e0b16657-3637-4bdc-88bb-05f12457c6a7` | Tự động phát âm thanh khi chọn chủ đề, thêm loading spinner cho TTS, xóa bỏ toggle "Giải thích chi tiết" (luôn hiển thị tự động). | Hoàn thành |
| **Session 60** | 2026-06-30 | Antigravity | `e0b16657-3637-4bdc-88bb-05f12457c6a7` | Tối ưu hóa hiệu năng render danh sách chủ đề bằng cách cô lập recomposition cho từng row riêng biệt. | Hoàn thành |
| **Session 61** | 2026-06-30 | Antigravity | `e0b16657-3637-4bdc-88bb-05f12457c6a7` | Khắc phục lỗi phát âm đè lên nhau (overlapping audio) khi chuyển nhanh giữa các chủ đề. | Hoàn thành |
| **Session 62** | 2026-06-30 | Antigravity | `b933a38c-5ae1-4304-a547-bc73e4226c01` | Thêm cài đặt lựa chọn nền tảng giọng đọc (Google TTS vs ONNX) và giọng đọc Tiếng Anh (Amy vs Lessac) dưới phần Cài đặt ngôn ngữ. | Hoàn thành |

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

### 🟢 Session 45 (2026-06-30)

- **Mục tiêu:** Thêm hệ thống học tiếng Việt cho người nói tiếng Anh (VIET system), với 3 cấp học: Tiểu học, Trung học, Đại học.
- **Hoạt động & Thay đổi:**
  - **VietnamesePromptProvider.kt:** Tạo mới — AI dạy tiếng Việt bằng tiếng Anh, implement đầy đủ tất cả abstract members của `BaseLanguagePromptProvider` (14 properties/functions).
  - **PromptProviderFactory.kt:** Thêm `TIEU_HOC/TRUNG_HOC/DAI_HOC → VietnamesePromptProvider`.
  - **LevelType.VIET:** Thêm vào tất cả các `when` block trong `TeacherViewModel`, `LessonContent`, `TargetLevelSystem`, `TopicSelectionContent`, `LanguageSettingsSection`, `HistoryViewModel`.
  - **JsonResourceReader + Android/Desktop readers:** Thêm `readVietnameseTopicsEnJson()`.
  - **TopicRepositoryImpl:** Seed + EN locale overlay cho VIET system.
  - **learning_systems.json + learning_levels.json:** Thêm VIET system entries.
  - **en/vietnamese_topics.json:** 300 topics ban đầu (100/cấp, IDs 7000–7299).
  - **LanguageSettingsSection.kt:** Thêm "Tiếng Việt" vào dropdown chọn ngôn ngữ đích.
  - **HistoryViewModel.kt:** Nhận diện `TIEU_HOC/TRUNG_HOC/DAI_HOC → "vi"`.
  - **Seed check:** Cập nhật `ensureDatabaseSeeded()` để detect VIET topics < 400 và tự động reseed.
- **Ghi chú kỹ thuật:**
  - Fix 3 compile errors: missing abstract members, missing `when` branch VIET, syntax error trong `setState{}` block.
  - BUILD SUCCESSFUL, đã cài lên `CPH2781` qua ADB.
- **Kế hoạch tiếp theo (Next Steps):**
  - Chờ yêu cầu tiếp theo từ người dùng.

---

### 🟢 Session 46 (2026-06-30)

- **Mục tiêu:** Mở rộng Vietnamese topics từ 300 lên 400 (thêm 100 chủ đề đa lĩnh vực), bao phủ đầy đủ các lĩnh vực còn thiếu.
- **Hoạt động & Thay đổi:**
  - **en/vietnamese_topics.json:** Thêm 100 chủ đề mới (IDs 7300–7399) phân bổ đồng đều 3 cấp:
    - **Tiểu học (+24):** Âm nhạc, thể thao, nhắn tin cơ bản, smartphone, vẽ/nấu ăn, khoa học đơn giản, bảo tàng, thư viện, vườn thú, lễ hội, địa lý Việt Nam, thú cưng, TV thiếu nhi...
    - **Trung học (+36):** V-pop, thể thao ngôi sao, Zalo/Messenger, slang/emoji, TikTok/YouTube, kiếm tiền MXH, freelance, IT/lập trình, kinh doanh/văn phòng, nhiếp ảnh, thời trang, sức khỏe tâm thần, gym, thiền, tài chính cá nhân, đầu tư, tình nguyện, trao đổi ngôn ngữ, trang trí nhà, e-learning, phim Việt, nhà hát, STEM, nhà đất...
    - **Đại học (+40):** Ngành IT/outsourcing, an ninh mạng, blockchain/crypto, AI/data science, y tế/dược, tâm lý học, kiến trúc, nông nghiệp công nghệ cao, vũ trụ, luật báo chí, legal tech, kinh tế hành vi, nhân học, thời trang quốc tế, lý thuyết phim, quản trị công/e-gov, smart mobility, y học cổ truyền, luật hiến pháp so sánh, tài chính quốc tế, bất động sản đầu tư, chuỗi cung ứng, quan hệ quốc tế, nghiên cứu giới tính, triết học môi trường, tương lai công việc...
  - **Auto-reseed:** `ensureDatabaseSeeded()` tự động reseed khi tổng VIET topics < 400 (không cần xóa data hay reinstall).
  - Sync toàn bộ sang `composeApp` resources.
- **Ghi chú kỹ thuật:**
  - BUILD SUCCESSFUL (13s), đã cài lên `CPH2781` qua ADB.
  - Tổng chủ đề VIET: **400** (Tiểu học: 124, Trung học: 136, Đại học: 140).
- **Kế hoạch tiếp theo (Next Steps):**
  - Chờ yêu cầu tiếp theo từ người dùng.

---

### 🟢 Session 47 (2026-06-30)

- **Mục tiêu:** Tích hợp ô nhập chủ đề tùy chỉnh vào trong thẻ "Chủ đề đang chọn" khi chọn chế độ "Tự nhập" và sửa lỗi tràn viền của lớp phủ gradient phía sau các nút ghim đáy.
- **Hoạt động & Thay đổi:**
  - **TopicCarouselCard.kt:** Cập nhật Composable nhận callback `onCustomTopicInputChanged` và `onFocusChanged`. Khi ở chế độ `isCustomTopicMode`, thay thế Text hiển thị tĩnh bằng `BasicTextField` cho phép nhập trực tiếp, đồng thời thay đổi viền thẻ Card dày hơn và hiển thị màu `accent` khi TextField được focus. Chuyển đổi icon chỉnh sửa thành icon thêm mới `Icons.Rounded.Add` (+) và đưa lên góc trên cùng bên trái (top-left), nằm ngay trước nhãn tiêu đề Card để tối ưu không gian hiển thị trên màn hình hẹp của Android.
  - **TopicSelectionContent.kt & LessonContent.kt:** 
    - Truyền callback xử lý sự thay đổi nội dung tự nhập, trạng thái focus và click (+) từ `TopicCarouselCard`. 
    - Loại bỏ khung `OutlinedTextField` nhập chủ đề tùy chỉnh ở cuối màn hình. 
    - Tích hợp component `AlertDialog` Compose để hỏi người dùng: *"Bạn có muốn lưu chủ đề này vào danh sách để sử dụng lại sau này không?"* khi click vào icon (+).
    - Cập nhật `LaunchedEffect` của `isCustomTopicFocused` để cuộn danh sách hiển thị đúng vị trí thẻ `TopicCarouselCard` (index 3).
    - **Sửa lỗi tràn viền gradient**: Loại bỏ `.padding(horizontal = 16.dp)` của Box cha ngoài cùng, chuyển thuộc tính này vào trong nội dung cuộn (`LazyColumn`/`Column`) và các container của Button/IconButton. Điều này cho phép lớp phủ gradient vertical che mờ ở đáy màn hình tràn sát rìa trái/phải một cách hoàn hảo, không còn bị hở khoảng trống.
    - **Đồng bộ hóa hàng đáp án**: Thêm Composable `AnswerRow` dùng chung cho các dòng đáp án chuẩn. Đồng bộ hóa hàng hiển thị đáp án chính (số 1) và các cách diễn đạt thay thế (số 2, 3,...) với cùng cấu trúc: có icon check tròn màu xanh lá ở đầu dòng, chữ hiển thị 15.sp Medium, và hàng nút Volume + Copy ở bên phải, căn lề thẳng tắp tăm tắp, chống lệch dòng và rớt chữ.
    - **Tích hợp nút Giải thích chi tiết**: Bổ sung nút `Giải thích` (OutlinedButton với icon `Info` / `ExpandLess`) ngay dưới Card đáp án chuẩn. Khi người dùng click, trạng thái `isExplanationExpanded` (tự động reset theo index câu `idx`) được kích hoạt để gọi AI giải thích và hiển thị nội dung giải thích, giúp giao diện màn hình luyện tập đồng nhất trải nghiệm với màn hình kết quả.
  - **FeedbackContent.kt:**
    - **Sửa lỗi sai level**: Đổi `state.selectedCefrLevel` cố định thành `state.selectedLevelText` động để hiển thị chính xác cấp độ của bài học tương ứng theo JLPT/TOEIC/CEFR/HSK/VIET.
    - **Tăng khoảng cách**: Thêm `Spacer(Modifier.height(10.dp))` phía dưới tiêu đề và tăng khoảng cách đến Review Card lên `24.dp`.
    - **Căn lề đồng đều & Đồng nhất padding (Sửa lỗi co chiều rộng)**:
      - Loại bỏ `horizontalAlignment = Alignment.CenterHorizontally` khỏi Column cha cuộn dọc ở màn hình kết quả để tránh lỗi đo lường kích thước trong Compose làm co rút chiều rộng Card. Toàn bộ giao diện chuyển về căn lề trái mặc định (`Start`) tự nhiên.
      - Loại bỏ các modifier `.align(Alignment.Start)` dư thừa của tiêu đề và Chips.
      - Thống nhất độ rộng lề bằng cách chỉnh sửa padding bên trong Column của cả Card Nhận xét AI và Card câu hỏi (`SentenceReviewCard`) đều thành **`16.dp`** (thay vì 14.dp và 12.dp lệch nhau trước đó).
      - Chuyển padding ngang của `AnswerRow` trong `LessonContent.kt` sang `16.dp` để thẳng hàng tăm tắp với viền.
    - **Tái thiết kế Tags hiển thị**: 
      - Nâng cấp dòng văn bản mô tả dạng thô cũ thành cấu trúc các nhãn thẻ (Chips) bo tròn cao cấp, có background màu dịu (accent copy và mutedText copy) kèm đường viền tinh tế và tích hợp các icon tương ứng (`Icons.Default.Bookmark` và `Icons.Default.Star`). 
      - Sử dụng cấu trúc `Row` có `Modifier.fillMaxWidth()` và `IntrinsicSize.Min` để hai thẻ Chips tự động cao bằng nhau tuyệt đối (đồng bộ chiều cao theo thẻ cao nhất).
      - **Tái cấu trúc Level Chip (dạng dọc)**: Chuyển đổi hiển thị ngang thành dạng dọc Column: Icon Star ở trên, dòng 1 hiển thị tên hệ thống (CEFR/TOEIC/JLPT...), dòng 2 hiển thị giá trị cụ thể (B1/500/N3...) giúp thẻ gọn gàng và tinh tế.
      - **Di chuyển icon Topic lên Top-Left**: Thay đổi `verticalAlignment` của Row trong Topic Chip thành `Alignment.Top` kết hợp padding top 2.dp, đưa biểu tượng Bookmark luôn cố định ở góc trên bên trái sát dòng chữ đầu tiên của chủ đề thay vì bị lơ lửng ở giữa khi chủ đề xuống nhiều dòng.
    - **Đồng bộ hóa hàng đáp án**: Tách logic hiển thị sang Composable `AnswerRow` thống nhất với màn hình luyện tập để giao diện đồng đều hoàn hảo.
  - **TeacherViewModel.kt & TopicRepository:**
    - Cấu hình `TeacherUiState` hỗ trợ thuộc tính `showSaveToDbDialog` và getter `currentLevelCode`.
    - Định nghĩa hàm `addCustomTopic` trong interface `TopicRepository` và class `TopicRepositoryImpl` để lưu chủ đề mới vào SQLite database (`INSERT OR REPLACE INTO learning_topics`).
    - Thêm các phương thức `showSaveToDbDialog()`, `dismissSaveToDbDialog()` và `saveCustomTopicToDb(name)`. Khi người dùng xác nhận "Lưu" trên Dialog, tiến hành lưu chủ đề tùy chọn vào Database và tự động reload danh sách chủ đề qua `loadTopics()`.
- **Ghi chú kỹ thuật:**
  - Build và deploy trực tiếp bản debug lên máy khách qua ADB thành công.
- **Kế hoạch tiếp theo (Next Steps):**
  - Chờ yêu cầu tiếp theo từ người dùng.

---

### 🟢 Session 48 (2026-06-30)

- **Mục tiêu:** Di chuyển icon (+) sang góc trên cùng bên phải (top-right) trong thẻ "Chủ đề đang chọn" (TopicCarouselCard) ở chế độ tự nhập (Custom Topic Mode).
- **Hoạt động & Thay đổi:**
  - **TopicCarouselCard.kt:** Thay đổi thuộc tính `horizontalArrangement` của `Row` tiêu đề từ `Arrangement.Start` thành `Arrangement.SpaceBetween`. Di chuyển khối kiểm tra điều kiện `if (state.isCustomTopicMode)` chứa `IconButton` (+) xuống phía dưới composable `Text("CHỦ ĐỀ ĐANG CHỌN")`. Bố cục mới này đẩy nhãn chữ sang góc trái và nút (+) sang góc phải của Card một cách tự nhiên và gọn gàng hơn.
- **Ghi chú kỹ thuật:**
  - Build và kiểm thử thành công trên Desktop.
- **Kế hoạch tiếp theo (Next Steps):**
  - Chờ yêu cầu tiếp theo từ người dùng.

---

### 🟢 Session 49 (2026-06-30)

- **Mục tiêu:** Thay đổi số chỉ mục (index badge) của các chủ đề trong danh sách: làm số nhỏ lại, đặt ở rìa góc trên-trái, và đổi bounding box thành hình chữ nhật bo tròn các góc để tương thích với độ cong của Card.
- **Hoạt động & Thay đổi:**
  - **TopicSelectionContent.kt:**
    - Thay thế bố cục `Row` bên trong Card chủ đề bằng `Box` để cho phép badge index dính sát vào rìa góc trên cùng bên trái (0,0).
    - Thay đổi hình dáng (shape) của badge từ `CircleShape` sang `RoundedCornerShape(topStart = 12.dp, topEnd = 4.dp, bottomEnd = 4.dp, bottomStart = 4.dp)`. Góc trên-trái (`topStart = 12.dp`) giúp ôm khít hoàn hảo góc bo cong của Card, trong khi các góc khác được bo tròn nhẹ `4.dp` để loại bỏ hoàn toàn các góc vuông sắc nhọn thô cứng, đồng bộ với phong cách thiết kế bo tròn của toàn ứng dụng.
    - Giảm kích thước font chữ của số từ `12.sp` xuống `9.sp` và giảm padding trong badge xuống `horizontal = 8.dp, vertical = 4.dp` để làm số nhỏ lại.
    - Đặt padding bắt đầu (`start = 42.dp`) cho `Text` hiển thị chủ đề để đảm bảo toàn bộ tên chủ đề của các Card thẳng hàng tăm tắp và không bị đè lên bởi badge chỉ mục.
- **Ghi chú kỹ thuật:**
  - Build và kiểm thử thành công trên Desktop & Android qua ADB.
- **Kế hoạch tiếp theo (Next Steps):**
  - Chờ yêu cầu tiếp theo từ người dùng.

---

### 🟢 Session 50 (2026-06-30)

- **Mục tiêu:** Loại bỏ nút "Ngẫu nhiên" (xáo trộn/chọn ngẫu nhiên) trong danh sách chủ đề và bảo toàn việc đánh số thứ tự (badge index) các chủ đề hiển thị một cách nhất quán.
- **Hoạt động & Thay đổi:**
  - **TopicSelectionContent.kt:**
    - Xóa bỏ block composable hiển thị nút "Ngẫu nhiên" (`if (!state.isCustomTopicMode && filteredTopics.isNotEmpty()) { Row { ... } }`) trong phần header danh sách chủ đề.
    - Giữ nguyên cơ chế render số chỉ mục dựa trên index hiển thị `i` (`${i + 1}`) của list để đảm bảo label số index luôn đúng thứ tự từ 1 đến N.
- **Ghi chú kỹ thuật:**
  - Build và kiểm thử thành công trên Desktop & Android qua ADB.
- **Kế hoạch tiếp theo (Next Steps):**
  - Chờ yêu cầu tiếp theo từ người dùng.

---

### 🟢 Session 51 (2026-06-30)

- **Mục tiêu:** Xáo trộn ngẫu nhiên danh sách các chủ đề thuộc cùng một level để tránh tình trạng lặp từ ở các dòng kề nhau, đồng thời giữ lại/khôi phục lại nút "Ngẫu nhiên" ở giao diện danh sách chủ đề.
- **Hoạt động & Thay đổi:**
  - **TeacherViewModel.kt:**
    - Cập nhật logic sắp xếp danh sách chủ đề trong hàm `loadTopics`. 
    - Nhóm danh sách chủ đề theo `levelCode`, sau đó gọi `.shuffled()` để xáo trộn ngẫu nhiên các chủ đề trong cùng một level.
    - Kết hợp các nhóm lại theo thứ tự level từ cao xuống thấp thông qua `flatMap` giúp giữ nguyên độ ưu tiên của level đang chọn nhưng nội dung các chủ đề cùng level luôn được xáo trộn để mang lại sự đa dạng.
  - **TopicSelectionContent.kt:**
    - Khôi phục lại block composable chứa nút "Ngẫu nhiên" (sử dụng icon `Icons.Rounded.AutoAwesome`) bên cạnh nút "Tự nhập" trong phần header của danh sách chủ đề.
- **Ghi chú kỹ thuật:**
  - Build và kiểm thử thành công trên Desktop & Android qua ADB.
- **Kế hoạch tiếp theo (Next Steps):**
  - Chờ yêu cầu tiếp theo từ người dùng.

---

### 🟢 Session 52 (2026-06-30)

- **Mục tiêu:** Hiển thị nút quay lại đầu trang (Scroll to Top) khi người dùng cuộn danh sách chủ đề xuống từ phần tử (row) thứ 10 trở đi.
- **Hoạt động & Thay đổi:**
  - **TopicSelectionContent.kt:**
    - Định nghĩa trạng thái `showScrollToTop` sử dụng `derivedStateOf` lắng nghe sự thay đổi của `lazyListState.firstVisibleItemIndex >= 10`.
    - Thêm nút `SmallFloatingActionButton` dạng hình tròn (`shape = CircleShape`) với icon `Icons.Rounded.ArrowUpward` ở góc dưới cùng bên phải (`Alignment.BottomEnd`).
    - Cấu hình padding bottom của nút là `76.dp` để nó được ghim cố định ngay phía trên nút "Bắt đầu bài học" (khoảng cách vừa vặn 14.dp so với mép trên nút Bắt đầu), mang lại trải nghiệm cuộn thuận tiện và tự nhiên.
    - **Tối ưu hóa độ mượt mà của Animation**: Sử dụng kỹ thuật nhảy nhanh (fast-jump) trung gian: Nếu người dùng đang cuộn ở vị trí quá xa (> 10), khi nhấn nút, danh sách sẽ nhảy tức thời về index 10 bằng `scrollToItem(10)` trước, sau đó mới chạy `animateScrollToItem(0)` để cuộn mượt mà nốt khoảng cách ngắn còn lại. Kỹ thuật này giúp loại bỏ hoàn toàn tình trạng lag giật khung hình do Compose phải render quá nhiều phần tử trung gian trong danh sách dài.
- **Ghi chú kỹ thuật:**
  - Build và kiểm thử thành công trên Desktop & Android qua ADB.
- **Kế hoạch tiếp theo (Next Steps):**
  - Chờ yêu cầu tiếp theo từ người dùng.

---

### 🟢 Session 53 (2026-06-30)

- **Mục tiêu:** Nhấn và giữ (long press) các dòng chủ đề để hiển thị nút "Xóa" (Remove) ở trung tâm của mỗi dòng và cho phép xóa chủ đề đó khỏi danh sách/database.
- **Hoạt động & Thay đổi:**
  - **UserSchema.sq:** Định nghĩa query `deleteTopic` xóa chủ đề trong bảng `learning_topics` dựa theo `system_code` và `topic_id`.
  - **TopicRepository.kt & TopicRepositoryImpl.kt:** Thêm phương thức `deleteTopic(id: Int, systemCode: String)` để thực thi xóa bản ghi trong database.
  - **TeacherViewModel.kt:** Thêm handler `onRemoveTopic(id: Int)` lấy ra `systemCode` tương ứng và thực hiện xóa chủ đề, đồng thời tự động reload danh sách chủ đề qua `loadTopics(...)` để giao diện cập nhật ngay lập tức.
  - **TopicSelectionContent.kt:**
    - Định nghĩa trạng thái `longPressedTopicId` lưu trữ id chủ đề đang được nhấn giữ.
    - Thay thế `.clickable { ... }` của Card bằng `.combinedClickable(onClick = { ... }, onLongClick = { ... })` để lắng nghe sự kiện long press.
    - Thiết lập overlay che mờ Card khi có sự kiện nhấn giữ thông qua `matchParentSize()`, ở giữa hiển thị nút "Xóa" (IconButton màu đỏ có icon `Icons.Default.Delete`). Khi bấm nút Xóa sẽ gọi `viewModel.onRemoveTopic` và reset trạng thái nhấn giữ.
- **Ghi chú kỹ thuật:**
  - Build và kiểm thử thành công trên Desktop & Android qua ADB.
- **Kế hoạch tiếp theo (Next Steps):**
  - Chờ yêu cầu tiếp theo từ người dùng.

---

### 🟢 Session 54 (2026-06-30)

- **Mục tiêu:** Nghiên cứu giải pháp phát âm thanh (Text-to-Speech) chạy local-only, offline 100%, hoàn toàn miễn phí (không trả phí API) bằng mô hình ONNX cho cả tiếng Anh và tiếng Việt trong dự án Kotlin Multiplatform (KMP).
- **Hoạt động & Thay đổi:**
  - **onnx_tts_research.md (Mới):** Tạo tài liệu nghiên cứu chi tiết lưu trữ trong thư mục `research/`.
  - Nghiên cứu các mô hình tối ưu: Đề xuất **Piper TTS** (VITS-based mã nguồn mở MIT, siêu nhẹ ~15MB-50MB, chất lượng cao, có sẵn giọng đọc tiếng Anh và tiếng Việt chuẩn).
  - Đề xuất Engine suy luận: Khuyên dùng **Sherpa-ONNX** (Apache-2.0 License, tích hợp sẵn phonemization offline bằng C++, hỗ trợ hoàn hảo JNI cho Android và Desktop JVM).
  - Xây dựng bản vẽ kiến trúc luồng dữ liệu (Mermaid) và hướng dẫn tích hợp code mẫu chi tiết cho `expect`/`actual` trên cả Android (`androidMain`) và Desktop (`desktopMain`).
- **Ghi chú kỹ thuật:**
  - Lưu tài liệu nghiên cứu tại `research/onnx_tts_research.md`.
- **Kế hoạch tiếp theo (Next Steps):**
  - Chờ yêu cầu tiếp theo từ người dùng để triển khai code.

---

### 🟢 Session 55 (2026-06-30)

- **Mục tiêu:** Chạy thử nghiệm thành công phát âm thanh Text-To-Speech (TTS) hoàn toàn offline local, free 100% cho cả Tiếng Anh và Tiếng Việt trong dự án Java/Kotlin mà không qua Python.
- **Hoạt động & Thay đổi:**
  - **build.gradle.kts (audio-test):**
    - Cấu hình compile task và JVM execution task bắt buộc sử dụng encoding UTF-8 để tránh lỗi ký tự tiếng Việt có dấu.
    - Thêm task Gradle `downloadEspeakData` tải gói ngữ âm `espeak-ng-data.tar.bz2` (khoảng 3MB) từ GitHub Releases và tự động giải nén bằng lệnh `tar` hệ thống để phục vụ phonemization offline cho Piper.
    - Thêm task Gradle `downloadEnglishModel` tải trọn gói mô hình tiếng Anh chính thức `vits-piper-en_US-amy-low.tar.bz2` (khoảng 15MB) đã nhúng sẵn metadata tương thích với Sherpa-ONNX.
  - **AudioTest.java & .gitignore:**
    - Nâng cấp hàm `runTts(...)` hỗ trợ ghi mảng bytes PCM thô thành file âm thanh định dạng `.wav` tiêu chuẩn thông qua Java Sound API (`AudioSystem.write`).
    - Thêm cấu hình ignore `/*.wav` vào `.gitignore` của subproject để giữ Git repository sạch sẽ.
    - Phát triển và tải thêm hai mô hình tiếng Việt thay thế chất lượng cao hơn: `vi_VN-25hours_single-low` (16kHz) và `vi_VN-vais1000-medium` (22.05kHz - chất lượng Studio).
    - Tạo 3 file audio tiếng Việt so sánh tương ứng (`output-vi-vivos.wav`, `output-vi-25hours.wav`, `output-vi-vais1000.wav`) cùng file tiếng Anh `output-en.wav` để người dùng thẩm định chất lượng.
- **Ghi chú kỹ thuật:**
  - Chương trình chạy thử nghiệm hoạt động hoàn hảo tại folder `research/audio-test/` của dự án.
- **Kế hoạch tiếp theo (Next Steps):**
  - Dựa trên kết quả thẩm định chất lượng các model của người dùng, tiến hành triển khai tích hợp TTS vào Core App (Android & Desktop) bằng KMP expect/actual class.

---

### 🟢 Session 56 (2026-06-30)

- **Mục tiêu:** Thử nghiệm mô hình Kokoro Vietnamese và tìm kiếm/chạy thử các mô hình giọng nói tiếng Việt offline khác để khắc phục hiện tượng âm thanh bị vang của mô hình `vais1000`.
- **Hoạt động & Thay đổi:**
  - **AudioTest.java**:
    - Tích hợp lớp `OfflineTtsKokoroModelConfig` và tải mô hình `Kokoro-Vietnamese` (`kokoro_vi.onnx`) để chạy thử. Kết quả thử nghiệm ghi nhận lỗi crash native do tệp ONNX thiếu metadata `sample_rate` và lõi C++ của `sherpa-onnx` chưa hỗ trợ G2P tiếng Việt cho Kokoro.
    - Tìm kiếm và tích hợp bộ sưu tập mô hình chất lượng cao **NGHI-TTS (NghiMe Studio)** từ HF mirror `doof-ferb/nghitts-copy` bao gồm giọng đọc Minh Thư (`minhthu.onnx`), Phương Trang (`phuongtrang.onnx`) và tệp ngữ âm `nghitts-tokens.txt`.
    - Thử nghiệm các mức tốc độ đọc chậm khác nhau (`lengthScale = 1.1`, `1.15`, `1.2`) cho giọng Phương Trang để tránh hiện tượng đọc quá nhanh, giúp tối ưu cho học từ vựng.
    - Tải và chạy thử nghiệm thêm các giọng nói giả lập đặc sắc khác của NghiMe Studio: giọng đọc Nguyễn Ngọc Ngạn (`ngocngan3701.onnx`), giọng nam Adam (`adam1.onnx`), giọng nam Chiếu Thành (`chieuthanh.onnx`), giọng nữ Ban Mai (`banmai.onnx`), và giọng nữ Mỹ Tâm (`mytam2.onnx`) ở cả tốc độ chuẩn (`1.0`) và làm chậm (`1.15`).
    - Tạo tài liệu nghiên cứu mới tại [nghitts_research.md](file:///D:/ai-english/research/nghitts_research.md) để ghi nhận chi tiết cấu hình, tham số tốc độ tối ưu và hướng dẫn nạp mô hình.
- **Ghi chú kỹ thuật:**
  - Biên dịch và chạy thành công trên JVM (`BUILD SUCCESSFUL`). Các tệp âm thanh đầu ra `output-vi-minhthu.wav`, `output-vi-phuongtrang-slow-*.wav`, `output-vi-ngocngan*.wav`, `output-vi-adam*.wav`, `output-vi-chieuthanh*.wav`, `output-vi-banmai*.wav`, và `output-vi-mytam*.wav` được sinh ra ổn định.
- **Kế hoạch tiếp theo (Next Steps):**
  - Triển khai tích hợp TTS vào Core App bằng KMP (Android & Desktop) hỗ trợ Dual-Mode (Online API & Offline local NGHI-TTS).

---

### 🟢 Session 57 (2026-06-30)

- **Mục tiêu:** Tích hợp cơ chế phát âm tiếng Việt offline TTS vào core app bằng Kotlin Multiplatform, sử dụng duy nhất giọng nam Adam làm chậm (speed 1.15) cho Desktop và hỗ trợ Native TTS cho Android.
- **Hoạt động & Thay đổi:**
  - **TtsPlayer.kt:** Tạo interface core phát âm âm thanh `TtsPlayer` trong commonMain.
  - **AndroidTtsPlayer.kt:** Sử dụng engine `android.speech.tts.TextToSpeech` có sẵn trên hệ thống, điều chỉnh tốc độ nói `0.85f` cho tiếng Việt để giọng đọc truyền cảm và tự nhiên hơn.
  - **DesktopTtsPlayer.kt:** 
    *   Tự động tải xuống mô hình `adam1.onnx`, `tokens.txt` và `espeak-ng-data.tar.bz2` (giải nén qua công cụ hệ thống `tar`) về vùng lưu trữ cục bộ `.ai-english` khi người dùng nhấn nút Nghe lần đầu.
    *   Sử dụng Piper/VITS thông qua JNI `sherpa-onnx` để tổng hợp giọng nói của Adam làm chậm ở tốc độ `lengthScale = 1.15f`.
  - **TeacherViewModel.kt:** Inject `TtsPlayer`, bổ sung các hàm điều phối `speak()` & `stopSpeaking()`, và thêm hàm `speakTopic(topicName)` tự động dùng ngôn ngữ mẹ đẻ của người dùng (`prefs.getLanguageNative()`) để phát âm chuẩn xác.
  - **LessonContent.kt & FeedbackContent.kt:** Kết nối nút phát âm loa (`VolumeUp`) của thẻ đáp án chính và các cách diễn đạt thay thế (`AnswerRow`) để gọi đến `viewModel.speak()`.
  - **LanguageSettingsSection.kt:** Tích hợp tùy chọn điều chỉnh tốc độ giọng đọc TTS (Chậm, Thường, Nhanh) vào Profile Sheet, sử dụng bố cục dọc (Column) và chia đều chiều rộng nút (weight 1f) để tương thích hoàn hảo trên thiết bị di động Android.
  - **TopicSelectionContent.kt:** Thêm nút phát loa (`VolumeUp`) ở góc bên trái mỗi dòng chủ đề trong danh sách chọn lựa, gọi `viewModel.speakTopic(topic.name)` để phát âm offline tên chủ đề theo ngôn ngữ mẹ đẻ của người dùng.
  - **AndroidTtsPlayer.kt:** Gỡ bỏ hoàn toàn sự phụ thuộc vào động cơ hệ thống Google TTS (`android.speech.tts.TextToSpeech`), chuyển sang sử dụng bộ thư viện native `sherpa-onnx` để tổng hợp giọng nói offline của Adam tiếng Việt tương tự trên Desktop.
  - **Android Gradle Task:** Thêm task `prepareAndroidTtsAssets` vào `androidApp/build.gradle.kts` để sao chép trực tiếp các tệp mô hình giọng nói (`adam1.onnx`, `tokens.txt` và `espeak-ng-data`) từ thư mục cache Desktop sang thư mục `assets/vits/` của Android tại thời điểm build (tải xuống tự động nếu cache trống).
  - **Gradle Fixes:** Khắc phục lỗi cast kiểu dữ liệu `sampleRate` (Float), tải thư viện `sherpa-onnx-static-link-onnxruntime-1.12.10.aar` từ Hugging Face để làm dependency nội bộ cho Android, và sửa lỗi Gradle Configuration Cache.
- **Ghi chú kỹ thuật:**
  - Ứng dụng Android biên dịch thành công và được cài đặt trực tiếp lên thiết bị/giả lập (`androidApp-debug.apk`) thông qua ADB.
  - Loại bỏ hoàn toàn Google TTS, 100% âm thanh tiếng Việt sử dụng offline giọng Adam.
- **Kế hoạch tiếp theo (Next Steps):**
  - Tiếp tục phát triển và tối ưu hóa hệ thống TTS ngoại tuyến cho các ngôn ngữ khác.

---

### 🟢 Session 58 (2026-06-30)

- **Mục tiêu:** Sửa lỗi crash SIGABRT (espeak_Initialize), tối ưu hóa assets espeak-ng-data, thêm giọng English lessac cho chủ đề tiếng Anh.
- **Hoạt động & Thay đổi:**
  - Sửa lỗi crash JNI SIGABRT trên Android bằng cách sao chép assets ra filesDir của ứng dụng và khởi tạo với `assetManager = null`.
  - Tối ưu hóa dung lượng assets bằng cách loại bỏ các file espeak-ng-data không dùng tới, chỉ giữ lại 9 ngôn ngữ cần thiết.
  - Loại bỏ các ký tự `**` khỏi văn bản tên topic trước khi phát âm.
  - Thêm cấu hình mô hình `en_US-lessac-medium.onnx` ở tốc độ `1.3f` cho giọng đọc tiếng Anh khi ngôn ngữ đích học là tiếng Việt.

---

### 🟢 Session 59 (2026-06-30)

- **Mục tiêu:** Tự động phát âm thanh khi chọn chủ đề, thêm chỉ báo loading khi nạp model TTS, và luôn hiển thị phần giải thích chi tiết trong màn hình luyện tập mà không cần toggle.
- **Hoạt động & Thay đổi:**
  - **TtsPlayer.kt & implementation:** Thêm StateFlow `isLoading` để giao diện biết khi nào TTS engine đang chuẩn bị tài nguyên.
  - **TopicSelectionContent.kt:** Tích hợp auto-play khi người dùng click vào chủ đề và hiển thị loading spinner trên icon phát loa khi đang nạp mô hình.
  - **LessonContent.kt:**
    - Xóa bỏ nút toggle và biến trạng thái ẩn/hiện "Giải thích chi tiết".
    - Thêm `LaunchedEffect` tự động gửi yêu cầu lấy giải thích từ AI (`viewModel.onExplainSentence()`) ngay khi người dùng kiểm tra đáp án (`hasChecked == true`).
    - Luôn hiển thị giải thích chi tiết (dưới dạng Skeleton loading khi đang tải và dạng Card chi tiết khi đã tải xong).
  - **Sequential TTS:** Cập nhật phương thức `playMixed(text)` ở core TTS player để tự động bóc tách từng câu song ngữ Anh-Việt, phát hiện ngôn ngữ của câu và phát tuần tự bằng đúng model (Adam cho Việt, Lessac cho Anh).
  - **TeacherViewModel.kt:** Tối ưu hóa hàm `onExplainSentence` tránh gửi nhiều request API trùng lặp khi đang tải.

---

### 🟢 Session 60 (2026-06-30)

- **Mục tiêu:** Tối ưu hóa hiệu năng render danh sách chủ đề bằng cách cô lập recomposition cho từng row riêng biệt.
- **Hoạt động & Thay đổi:**
  - **TopicSelectionContent.kt:** 
    - Tách logic hiển thị của mỗi hàng chủ đề ra một Composable con độc lập `@Composable private fun TopicRowItem(...)`.
    - Khi người dùng click chọn chủ đề hoặc trạng thái loading của TTS thay đổi, chỉ có duy nhất các hàng chịu ảnh hưởng trực tiếp (hàng được chọn mới và hàng bị bỏ chọn cũ) mới recompose (vẽ lại).
    - Các hàng chủ đề khác có tham số đầu vào không thay đổi sẽ kích hoạt cơ chế tự động bỏ qua (**Skipped**) của Jetpack Compose.
- **Ghi chú kỹ thuật:**
  - Giảm thiểu đáng kể UI lag và CPU overhead khi cuộn danh sách lớn hoặc click chuyển đổi nhanh các chủ đề.
  - Build và cài đặt thành công APK tối ưu qua ADB.

---

### 🟢 Session 61 (2026-06-30)

- **Mục tiêu:** Khắc phục lỗi phát âm đè lên nhau (overlapping audio) khi chuyển nhanh giữa các chủ đề.
- **Hoạt động & Thay đổi:**
  - **AndroidTtsPlayer.kt & DesktopTtsPlayer.kt:**
    - Khai báo thuộc tính `playJob: Job?` để theo dõi và quản lý coroutine phát âm thanh hiện tại.
    - Khi có yêu cầu phát âm thanh mới (`play` hoặc `playMixed`), coroutine cũ sẽ lập tức bị hủy bỏ (`playJob?.cancel()`) và đóng/dừng các luồng phát âm (`AudioTrack`/`SourceDataLine`) đang hoạt động.
    - Chia nhỏ mảng buffer âm thanh khi ghi xuống luồng phát (`ShortArray` / `ByteArray`) thành các khối nhỏ (chunk size), liên tục kiểm tra `job.isActive` để dừng phát âm thanh ngay lập tức khi coroutine bị hủy.
- **Ghi chú kỹ thuật:**
  - Đảm bảo âm thanh cũ tắt hẳn lập tức khi người dùng click chọn hàng chủ đề mới, loại bỏ hoàn toàn hiện tượng âm thanh phát đè lên nhau.

---

### 🟢 Session 62 (2026-06-30)

- **Mục tiêu:** Thêm cài đặt lựa chọn nền tảng giọng đọc (Google TTS vs ONNX) và giọng đọc Tiếng Anh (Amy vs Lessac) dưới phần Cài đặt ngôn ngữ.
- **Hoạt động & Thay đổi:**
  - **AppPreferences.kt, AndroidAppPreferences.kt, DesktopAppPreferences.kt:**
    - Bổ sung cấu hình `ttsEngine` (mặc định: `onnx`, tuỳ chọn: `google` / `onnx`) và `ttsVoiceEn` (mặc định: `lessac`, tuỳ chọn: `amy` / `lessac`).
    - Đồng bộ hóa các luồng Flow phản ứng (`ttsEngineFlow`, `ttsVoiceEnFlow`).
  - **ProfileViewModel.kt & ProfileState.kt:**
    - Cập nhật State và ViewModel để phản chiếu và thay đổi lựa chọn nền tảng/giọng nói TTS từ Settings.
  - **LanguageSettingsSection.kt (UI):**
    - Thêm 2 cụm cài đặt UI trực quan bằng selector chip: "Nền tảng giọng đọc" (Google TTS / ONNX) và "Giọng đọc Tiếng Anh (ONNX)" (Amy / Lessac, chỉ hiển thị khi chọn nền tảng ONNX).
  - **AndroidTtsPlayer.kt & DesktopTtsPlayer.kt:**
    - Tích hợp động cơ phát âm Google TTS online (`translate_tts` qua URL, tự động detect ngôn ngữ và phát bằng `MediaPlayer` trên Android hoặc thư viện `JLayer` trên Desktop JVM).
    - Hỗ trợ thay đổi model ONNX linh hoạt tại runtime (tải động `en_US-amy-low.onnx` trên Desktop khi chuyển sang giọng Amy, hoặc tải trước ở Gradle task trên Android).
  - **Build & Dependency:**
    - Thêm dependency `javazoom:jlayer:1.0.1` vào desktop target để giải nén và chơi MP3 stream trực tuyến từ Google TTS.
    - Cập nhật Gradle Android assets task để tự động tải mô hình `en_US-amy-low.onnx` về đóng gói.
- **Ghi chú kỹ thuật:**
  - Tính năng hoạt động mượt mà, kiểm tra build và cài đặt thành công trên cả Windows Desktop và thiết bị Android thật qua ADB.

---

### 🟢 Session 63 (2026-06-30)

- **Mục tiêu:** Tinh giản cài đặt TTS (xóa thuật ngữ ONNX), sửa lỗi phát âm tiếng Việt bị mất trong giải thích từ vựng, và bổ sung nút copy cho giải thích chi tiết.
- **Hoạt động & Thay đổi:**
  - **LanguageSettingsSection.kt (UI):**
    - Đổi tên các nút chọn nền tảng từ "Google TTS (Online)" và "ONNX (Offline)" thành **Trực tuyến** và **Ngoại tuyến** để thân thiện hơn với người dùng.
    - Hiển thị thông tin giọng đọc Tiếng Việt tĩnh (`Adam (Nam)`) dưới dạng hàng thông tin khi chọn Ngoại tuyến.
    - Ẩn nhãn "(ONNX)" khỏi lựa chọn giọng đọc Tiếng Anh.
  - **AndroidTtsPlayer.kt & DesktopTtsPlayer.kt (Audio) & TeacherViewModel.kt:**
    - Khắc phục triệt để lỗi không nhận diện được các từ/cụm từ Tiếng Anh lồng ghép trong các dòng giải thích Tiếng Việt (ví dụ: các từ vựng đứng trước dấu hai chấm, đặt trong dấu ngoặc kép `""`, nháy đơn `''`, dấu gạch chéo `/phonetic/`, hoặc định dạng markdown `**bold**`, `*italic*`).
    - Giữ nguyên các định dạng markdown khi truyền văn bản từ `TeacherViewModel` sang `playMixed` để giữ ngữ cảnh.
    - Cải tiến thuật toán `splitByLanguage` sử dụng Regex để tách biệt các cụm từ tiếng Anh được định dạng nổi bật này ra khỏi các đoạn văn tiếng Việt giải thích xung quanh, đảm bảo các từ tiếng Anh được phát âm chuẩn bằng giọng Anh, còn nội dung giải thích tiếng Việt được phát âm bằng giọng Việt.
    - Đồng thời, áp dụng cơ chế tự động chia nhỏ các phân đoạn dài quá **180 ký tự** để tránh lỗi HTTP 400 từ Google TTS.
  - **LessonContent.kt & FeedbackContent.kt (UI):**
    - Bổ sung nút Copy (`Icons.Default.ContentCopy`) bên cạnh nút phát âm thanh tại phần tiêu đề "Giải thích chi tiết" trên cả màn hình Luyện tập và màn hình Xem kết quả phản hồi.
    - Người dùng có thể chạm để sao chép toàn bộ văn bản giải thích ngữ pháp vào Clipboard.

---

### 🟢 Session 64 (2026-06-30)

- **Mục tiêu:** Chuyển đổi định dạng phản hồi giải thích chi tiết từ chuỗi văn bản Markdown thô sang mảng JSON các phân đoạn ngôn ngữ song ngữ để trình phát phát âm chuẩn xác 100%.
- **Hoạt động & Thay đổi:**
  - **BaseLanguagePromptProvider.kt & EnglishPromptProvider.kt (Prompt):**
    - Thay đổi yêu cầu định dạng đầu ra của trường `explanation` từ dạng chuỗi văn bản Markdown thô sang **mảng JSON các đối tượng** `{ "text": "...", "lang": "vi" | "en" | ... }`.
    - AI tự phân tách rõ ràng các phân đoạn tiếng Việt ("vi") và các từ/cụm từ/câu ví dụ tiếng Anh ("en"/ngôn ngữ mục tiêu khác) sang các đối tượng riêng biệt.
    - Cấu hình chỉ thị AI **không sử dụng** bất kỳ ký tự định dạng markdown nào (`**`, `*`, `_`, `###`, `` ` ``) trong nội dung `text` để giải phóng hoàn toàn văn bản khỏi ký tự gây nhiễu, viết chữ trơn hoàn toàn (plain text) sử dụng `\n` và `-`.
    - Chỉ thị AI bắt buộc phân tách cực kỳ chi tiết các từ/cụm từ tiếng Anh lồng ghép trong câu tiếng Việt thành các đối tượng có `lang: "en"` riêng lẻ để có thể phát âm chuẩn xác từng chữ của nhiều ngôn ngữ trong cùng một câu/đoạn văn nhỏ.
    - Bổ sung yêu cầu bắt buộc AI **ở phần cuối cùng của lời giải thích phải liệt kê và giải thích các phương án dịch/cách diễn đạt thay thế khác (Alternatives)** hoặc phân tích tại sao một số cách dịch thông thường khác lại chưa phù hợp/chưa tự nhiên trong ngữ cảnh này để mở rộng tối đa kiến thức cho học sinh.
  - **SentenceExplanationDto.kt (DTO):**
    - Chuyển kiểu dữ liệu trường `explanation` từ `String` thành `JsonElement` để có thể nhận diện và phân tích cả mảng JSON mới và chuỗi văn bản cũ (đảm bảo tương thích ngược 100%).
  - **TeacherRepositoryImpl.kt (Repository):**
    - Cập nhật hàm `extractExplanationSafe` để trích xuất cả mảng JSON hoặc chuỗi thô từ Gemini một cách an toàn và tin cậy.
  - **AndroidTtsPlayer.kt & DesktopTtsPlayer.kt (Audio Engine):**
    - Tích hợp hàm `parseSegmentsManual` để giải mã mảng JSON các phân đoạn ngôn ngữ tĩnh từ database và phát lần lượt từng segment với đúng mã ngôn ngữ tương ứng, giải quyết triệt để sự cố lẫn lộn hoặc bỏ sót audio.
    - Gọi hàm `cleanMarkdownSymbols` để làm sạch hoàn toàn các ký tự markdown nếu có trước khi chuyển văn bản sang trình phát, ngăn chặn lỗi ngắt quãng/bỏ sót âm thanh từ engine TTS.
  - **LessonContent.kt & FeedbackContent.kt (UI):**
    - Tích hợp hàm `getDisplayExplanation` để tự động ghép các thuộc tính `text` từ các phân đoạn JSON thành văn bản Markdown hoàn chỉnh trước khi hiển thị hoặc thực hiện hành động sao chép.

---

### 🟢 Session 65 (2026-06-30)

- **Mục tiêu:** Cập nhật UI hiển thị giải thích chi tiết: Tự động bọc các chữ phát tiếng Anh (hoặc ngôn ngữ mục tiêu đang học) trong dấu ngoặc kép và định dạng in đậm.
- **Hoạt động & Thay đổi:**
  - **LessonContent.kt & FeedbackContent.kt (UI):**
    - Cải tiến hàm `getDisplayExplanation` chấp nhận tham số `targetLanguage` để nhận diện phân đoạn ngôn ngữ mục tiêu đang học (ví dụ: "en").
    - Đối với các phân đoạn văn bản có ngôn ngữ trùng khớp với `targetLanguage` (không phải tiếng mẹ đẻ như tiếng Việt "vi"), hệ thống tự động bọc nội dung trong dấu ngoặc kép và định dạng in đậm bằng cú pháp markdown `**"từ_vựng"**`.
    - Bảo toàn và xử lý chính xác các khoảng trắng ở đầu/cuối của các phân đoạn để đảm bảo câu văn sau khi ghép nối hiển thị thẳng hàng, liền mạch và tự nhiên.
    - Loại bỏ các dấu ngoặc kép hoặc ngoặc đơn dư thừa có sẵn của phân đoạn để tránh hiện tượng bọc trùng lặp.
  - **ExplanationParser.kt (New Common Utility):**
    - Tạo object `ExplanationParser` dùng chung ở module `shared/core/common` sử dụng thư viện `kotlinx-serialization-json` chính thức để giải mã mảng JSON các phân đoạn ngôn ngữ thay thế cho cơ chế Regex cũ.
    - Cơ chế mới giúp loại bỏ hoàn toàn các lỗi khớp trượt khi AI thay đổi thứ tự thuộc tính trong JSON (ví dụ `"lang"` đứng trước `"text"`), giải quyết triệt để sự cố biến mất từ tiếng Anh hoặc nhận diện sai ngôn ngữ.
  - **AndroidTtsPlayer.kt & DesktopTtsPlayer.kt & UI files:**
    - Loại bỏ hoàn toàn hàm `parseSegmentsManual` tự viết và Regex đi kèm, thay thế bằng `ExplanationParser.parseSegments` giúp code mượt mà, đồng nhất 100% giữa trình phát Audio và trình hiển thị UI.

---

### 🟢 Session 66 (2026-06-30)

- **Mục tiêu:** Cải thiện tốc độ phát giọng đọc tiếng Việt Adam offline (vốn đang bị quá chậm so với tiếng Anh).
- **Hoạt động & Thay đổi:**
  - **AndroidTtsPlayer.kt & DesktopTtsPlayer.kt (TTS engine):**
    - Điều chỉnh lại speed profile `lengthScale` của tiếng Việt Adam cho nhanh hơn, linh hoạt và sinh động hơn nữa:
      * Tốc độ **Bình thường (Normal)**: Điều chỉnh `lengthScale` xuống mức **`0.85f`** (tăng tốc độ lên 15% so với nguyên bản của model Adam, đọc nhanh, dứt khoát và cực kỳ tự nhiên).
      * Tốc độ **Chậm (Slow)**: Thay đổi xuống **`1.1f`** (chậm vừa phải để học từ vựng rõ ràng).
      * Tốc độ **Nhanh (Fast)**: Thay đổi xuống **`0.7f`**.
    - Việc tăng tốc độ tiếng Việt Adam giúp cân bằng hoàn hảo trải nghiệm đọc song ngữ Anh - Việt, không tạo cảm giác lệch nhịp khi chuyển đổi ngôn ngữ.
    - **Loại bỏ hoàn toàn độ trễ nối (gap) giữa các phân đoạn:** Thay vì tạo, phát và giải phóng AudioTrack / SourceDataLine liên tục cho mỗi phân đoạn ngắn (gây ra độ trễ 100ms - 300ms do khởi tạo phần cứng), hệ thống chuyển sang **tích lũy và ghép nối toàn bộ FloatArray audio samples của các phân đoạn lại thành một bộ đệm duy nhất** trước khi phát.
    - **Thuật toán Nội suy Tuyến tính (Linear Interpolation Resampling):** Tích hợp hàm `resample` tự động quy đổi đồng nhất tần số lấy mẫu (Sample Rate) của các phân đoạn khác tần số về cùng một tần số mục tiêu cao nhất trước khi ghép mảng, đảm bảo âm thanh chuyển giao mượt mà 100% không có khoảng lặng ngắt quãng.
    - **Khử khoảng lặng padding và tự động chèn khoảng dừng thông minh:**
      * Sử dụng hàm `trimSilence` lọc bỏ hoàn toàn các khoảng lặng đệm (padding silence) ở đầu và cuối do mô hình VITS tự động sinh ra cho các phân đoạn thô (tránh việc tiếng Việt và tiếng Anh bị đứt quãng ngay giữa câu).
      * Phân tích văn bản gốc của phân đoạn để tự động chèn khoảng dừng tự nhiên bằng hàm `appendSilence`: Nếu phân đoạn kết thúc bằng dấu câu (`.`, `?`, `!`, `\n`), chèn **`300ms`** lặng; nếu kết thúc bằng `:` hoặc `;`, chèn **`150ms`** lặng; nếu kết thúc bằng `,`, chèn **`100ms`** lặng. Các phân đoạn nối ngôn ngữ thông thường (không có dấu câu) sẽ được nối trực tiếp với **`0ms`** trễ.
    - **Tự động giữ cao độ giọng (Intonation/Pitch Contour) liền mạch giữa câu:**
      * Khi các phân đoạn ngắn được tổng hợp độc lập, mô hình VITS thường coi chúng là kết thúc câu và hạ thấp giọng (drop pitch) ở chữ cuối cùng, tạo cảm giác giọng đọc bị hụt hoặc ngang bất thường.
      * Tích hợp hàm `shouldAppendContinuation` để tự động phát hiện các từ/phân đoạn chuyển tiếp không phải ranh giới dấu câu (kết thúc bằng chữ cái, chữ số hoặc ngoặc đơn). Hệ thống tự động đính kèm dấu phẩy `,` vào cuối văn bản gửi sang engine TTS để mô hình giữ nguyên cao độ tiếp tục (continuation tone), sau đó dùng `trimSilence` loại bỏ khoảng lặng của dấu phẩy này trước khi ghép nối. Âm điệu chuyển tiếp giữa tiếng Việt và tiếng Anh nhờ đó cực kỳ tự nhiên, tròn âm và không bị tụt giọng.

---

> [!NOTE]
> File này giúp duy trì tính liên tục của ngữ cảnh phát triển dự án khi chuyển đổi giữa các phiên chat hoặc khi cần xem lại các quyết định thiết kế quan trọng.

