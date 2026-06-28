# AI English Teacher

> Ứng dụng luyện dịch câu tiếng Anh theo chủ đề, tích hợp AI (Gemini), theo chuẩn CEFR — được xây dựng bằng Kotlin Multiplatform + Compose Multiplatform.

---

## 🚀 Bắt đầu nhanh

### 1. Cấu hình API Key

Tạo file `local.properties` ở thư mục gốc (đã có sẵn template):

```properties
GEMINI_API_KEY=your_actual_gemini_api_key_here
```

### 2. Chạy desktop

```sh
./gradlew :composeApp:run
```

---

## 🏗️ Kiến trúc

```
ai-english/
├── androidApp/              ← Android entry point & Google Auth configuration
├── composeApp/              ← Desktop entry point
├── shared/
│   ├── core/
│   │   ├── common/          ← BaseViewModel, AppPreferences, AppDispatchers
│   │   ├── database/        ← SQLDelight Database and LessonHistory queries
│   │   ├── network/         ← GeminiClient (Ktor)
│   │   └── designsystem/    ← Theme, Color (Royal Blue dark mode)
│   └── feature/
│       ├── ai-teacher/
│       │   ├── domain/      ← Entities, Repository interfaces, Topics
│       │   ├── application/ ← UseCases (Generate, Evaluate, Explain)
│       │   ├── data/        ← TeacherRepositoryImpl (Gemini prompts)
│       │   └── ui/          ← TeacherScreen, TeacherViewModel (MVI)
│       ├── profile/
│       │   ├── domain/      ← ProfileRepository interface
│       │   ├── data/        ← ProfileRepositoryImpl
│       │   └── ui/          ← ProfileSheet, ProfileViewModel
│       └── history/
│           ├── domain/      ← LessonHistory entity, HistoryRepository interface
│           ├── data/        ← HistoryRepositoryImpl
│           └── ui/          ← HistorySheet, HistoryViewModel
```

**Dependency flow:** UI → Application/UI → Domain ← Data

---

## 🎯 Tính năng

| Tính năng | Mô tả |
|---|---|
| 🎯 Chọn chủ đề | 200+ chủ đề từ cuộc sống, công việc, TikTok, game, thể thao... |
| 🔍 Tìm kiếm chủ đề | Search filter tức thì |
| 📊 Chọn cấp độ CEFR | A1 → A2 → B1 → B2 → C1 → C2 |
| 📝 Luyện dịch câu | 10 câu Việt → Anh mỗi bài, có gợi ý từ vựng IPA |
| 🤖 AI đánh giá | Chấm điểm + nhận xét theo chuẩn CEFR đã chọn |
| 💡 Giải thích từng câu | AI phân tích ngữ pháp + từ vựng (Markdown) |

---

## 📋 Chuẩn CEFR

| Cấp | Mô tả | Nội dung |
|---|---|---|
| A1 | Beginner | Thông tin cá nhân, gia đình, thói quen, đồ vật, thời tiết, thức ăn |
| A2 | Elementary | Du lịch, mua sắm, khách sạn, sức khỏe, lễ hội, so sánh |
| B1 | Intermediate | Thì phức tạp, câu điều kiện: Ý kiến cá nhân, giáo dục, mạng xã hội, du lịch, tài |
| B2 | Upper-Intermediate | Cụm động từ, thành ngữ, từ vựng nâng cao: Tranh luận xã hội, môi trường, AI, tin giả, quyền riêng tư |
| C1 | Advanced | Cấu trúc phức tạp, văn phong học thuật: Doanh nghiệp, đàm phán quốc tế, ESG, M&A, logistics |
| C2 | Mastery | Gần giống người bản ngữ, diễn đạt tinh tế: Pháp lý quốc tế, kinh tế vĩ mô, AGI, Blockchain, triết học |

### Cấu trúc mới dựa trên nghiên cứu thực tế:

| Level | Chủ đề chính (theo CEFR/TOEIC chuẩn) |
|---|---|
| TOEIC 300 | Chào hỏi văn phòng, văn phòng phẩm, email đơn giản |
| TOEIC 500 | Đặt phòng công tác, hợp đồng, tuyển dụng, đào tạo |
| TOEIC 700 | Báo cáo doanh thu, đàm phán, kiểm toán, chiến dịch marketing |
| TOEIC 900 | Kiểm toán tập đoàn, M&A, IPO, hedging, FTA, C-level |

---

## 🛠️ Tech Stack

- **Kotlin 2.3.0** + **Compose Multiplatform 1.10.2**
- **Koin 4.0.0** — Dependency Injection
- **Ktor 3.0.0** — Networking (Gemini API)
- **Arrow 1.2.0** — Either/AppResult type safety
- **Gemini API** — AI models

---

## 🤝 Contributing

We welcome contributions to improve **AI English Teacher**! To contribute:

1. Fork the project.
2. Create a feature branch (`git checkout -b feature/amazing-feature`).
3. Commit your changes (`git commit -m 'Add some amazing feature'`).
4. Push to the branch (`git push origin feature/amazing-feature`).
5. Open a Pull Request.

Please make sure to follow the existing code style, ensure that the app builds successfully on all targets, and write/update tests where appropriate.

---

## 📄 License

This project is licensed under the **GNU General Public License v3.0** (GPL-3.0). See the [LICENSE](LICENSE) file for the full text.
