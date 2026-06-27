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
├── composeApp/              ← Desktop + Android entry point
├── shared/
│   ├── core/
│   │   ├── common/          ← BaseViewModel, AppDispatchers, AppResult
│   │   ├── network/         ← GeminiClient (Ktor)
│   │   └── designsystem/    ← Theme, Color (Royal Blue dark mode)
│   └── feature/
│       └── ai-teacher/
│           ├── domain/      ← Entities, Repository interfaces, Topics
│           ├── application/ ← UseCases (Generate, Evaluate, Explain)
│           ├── data/        ← TeacherRepositoryImpl (Gemini prompts)
│           └── ui/          ← TeacherScreen, TeacherViewModel (MVI)
```

**Dependency flow:** UI → Application → Domain ← Data

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
| A1 | Beginner | Câu đơn giản, hiện tại đơn, từ vựng cơ bản |
| A2 | Elementary | Câu ngắn, thì đơn giản, giao tiếp hàng ngày |
| B1 | Intermediate | Thì phức tạp, câu điều kiện, từ vựng trung bình |
| B2 | Upper-Intermediate | Cụm động từ, thành ngữ, từ vựng nâng cao |
| C1 | Advanced | Cấu trúc phức tạp, văn phong học thuật |
| C2 | Mastery | Gần giống người bản ngữ, diễn đạt tinh tế |

---

## 🛠️ Tech Stack

- **Kotlin 2.3.0** + **Compose Multiplatform 1.10.2**
- **Koin 4.0.0** — Dependency Injection
- **Ktor 3.0.0** — Networking (Gemini API)
- **Arrow 1.2.0** — Either/AppResult type safety
- **Gemini 2.5 Flash** — AI model
