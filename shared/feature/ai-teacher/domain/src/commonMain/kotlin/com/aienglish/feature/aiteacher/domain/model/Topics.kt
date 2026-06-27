package com.aienglish.feature.aiteacher.domain.model

data class TopicItem(
    val id: Int,
    val name: String,
    val levelCode: String // "A1", "A2", "B1", "B2", "C1", "C2", "300", "500", "700", ">900"
)

object Topics {
    val list = listOf(
        // ── CEFR A1 (10 chủ đề) ──
        TopicItem(1, "Giới thiệu bản thân & gia đình", "A1"),
        TopicItem(2, "Thời gian biểu và sinh hoạt hàng ngày", "A1"),
        TopicItem(3, "Miêu tả phòng ngủ và các đồ vật trong nhà", "A1"),
        TopicItem(4, "Món ăn và đồ uống yêu thích", "A1"),
        TopicItem(5, "Thời tiết hôm nay và lựa chọn quần áo phù hợp", "A1"),
        TopicItem(6, "Các bộ phận trên cơ thể người", "A1"),
        TopicItem(7, "Sở thích cá nhân và trò chơi giải trí đơn giản", "A1"),
        TopicItem(8, "Chỉ đường đơn giản trong nhà hoặc trường học", "A1"),
        TopicItem(9, "Mua sắm trái cây và rau củ tại cửa hàng", "A1"),
        TopicItem(10, "Chào hỏi xã giao và tạm biệt lịch sự", "A1"),

        // ── CEFR A2 (10 chủ đề) ──
        TopicItem(11, "Kể lại một chuyến du lịch nghỉ mát đã qua", "A2"),
        TopicItem(12, "Gọi món ăn nhanh tại quầy dịch vụ", "A2"),
        TopicItem(13, "Mua sắm quần áo tại trung tâm thương mại", "A2"),
        TopicItem(14, "Hỏi đường đi đến các địa điểm công cộng", "A2"),
        TopicItem(15, "Sử dụng phương tiện xe buýt và tàu điện ngầm", "A2"),
        TopicItem(16, "Làm thủ tục nhận phòng và trả phòng khách sạn", "A2"),
        TopicItem(17, "Mô tả công việc và các nghề nghiệp phổ biến", "A2"),
        TopicItem(18, "Lập kế hoạch tổ chức tiệc sinh nhật", "A2"),
        TopicItem(19, "Hỏi thăm sức khỏe và đưa ra lời khuyên đơn giản", "A2"),
        TopicItem(20, "Các công việc dọn dẹp và trang hoàng nhà cửa", "A2"),

        // ── CEFR B1 (10 chủ đề) ──
        TopicItem(21, "Lên lịch hẹn và đi chơi cùng nhóm bạn", "B1"),
        TopicItem(22, "Hoạch định kế hoạch nghề nghiệp trong tương lai", "B1"),
        TopicItem(23, "Xây dựng lối sống lành mạnh và dinh dưỡng cân bằng", "B1"),
        TopicItem(24, "Kể lại một câu chuyện hoặc trải nghiệm đáng nhớ", "B1"),
        TopicItem(25, "Bàn luận về một bộ phim hoặc bài hát yêu thích", "B1"),
        TopicItem(26, "Tác động của mạng xã hội tới việc kết nối bạn bè", "B1"),
        TopicItem(27, "Giới thiệu về các ngày lễ Tết truyền thống", "B1"),
        TopicItem(28, "Đời sống học đường và các kỳ thi cử", "B1"),
        TopicItem(29, "Trao đổi ý kiến về một cuốn sách hay đã đọc", "B1"),
        TopicItem(30, "Những kỷ niệm thời ấu thơ khó quên", "B1"),

        // ── CEFR B2 (10 chủ đề) ──
        TopicItem(31, "Ứng dụng các thiết bị thông minh trong đời sống", "B2"),
        TopicItem(32, "Bảo vệ môi trường và quy trình phân loại rác", "B2"),
        TopicItem(33, "Sử dụng mạng xã hội quá mức và ảnh hưởng tâm lý", "B2"),
        TopicItem(34, "Trải nghiệm đi phượt và cắm trại ngoài trời", "B2"),
        TopicItem(35, "Phân tích xu hướng các video ngắn trên TikTok", "B2"),
        TopicItem(36, "Văn hóa trượt ván và thể thao đường phố", "B2"),
        TopicItem(37, "Sức hút của nhạc nền game và văn hóa Anime", "B2"),
        TopicItem(38, "Chế độ tập luyện thể hình và ăn kiêng khoa học", "B2"),
        TopicItem(39, "Rèn luyện sự tự tin khi nói trước công chúng", "B2"),
        TopicItem(40, "Tranh luận về xu hướng thời trang nhanh (Fast Fashion)", "B2"),

        // ── CEFR C1 (10 chủ đề) ──
        TopicItem(41, "Kỹ năng trả lời phỏng vấn tuyển dụng chuyên sâu", "C1"),
        TopicItem(42, "Thuyết trình kế hoạch dự án trước ban điều hành", "C1"),
        TopicItem(43, "Viết báo cáo phân tích và thư điện tử thương mại", "C1"),
        TopicItem(44, "Giải quyết xung đột nội bộ và bất đồng quan điểm", "C1"),
        TopicItem(45, "Thương lượng đề xuất tăng lương với cấp trên", "C1"),
        TopicItem(46, "Đánh giá hiệu suất và đóng góp ý kiến cho đồng nghiệp", "C1"),
        TopicItem(47, "Chủ trì cuộc họp trực tuyến đa quốc gia", "C1"),
        TopicItem(48, "Xây dựng văn hóa doanh nghiệp và giá trị cốt lõi", "C1"),
        TopicItem(49, "Quản lý thời gian hiệu quả dưới áp lực công việc lớn", "C1"),
        TopicItem(50, "Phương pháp nhận diện tin tức giả mạo trên truyền thông", "C1"),

        // ── CEFR C2 (10 chủ đề) ──
        TopicItem(51, "Soạn thảo và thẩm định các điều khoản hợp đồng kinh tế", "C2"),
        TopicItem(52, "Tư duy phản biện, triết học ứng dụng và đạo đức học", "C2"),
        TopicItem(53, "Biến đổi khí hậu toàn cầu và các chính sách năng lượng xanh", "C2"),
        TopicItem(54, "Ứng dụng thực tiễn của công nghệ chuỗi khối (Blockchain)", "C2"),
        TopicItem(55, "Viễn cảnh tương lai của xe tự hành và trí tuệ nhân tạo", "C2"),
        TopicItem(56, "Nền kinh tế chia sẻ và sự trỗi dậy của Freelancer", "C2"),
        TopicItem(57, "Chiến lược phân tích kỹ thuật trên thị trường chứng khoán", "C2"),
        TopicItem(58, "Thuyết trình kế hoạch tái cấu trúc chiến lược 5 năm", "C2"),
        TopicItem(59, "Đàm phán thương vụ sáp nhập và mua lại (M&A) doanh nghiệp", "C2"),
        TopicItem(60, "Tranh chấp sở hữu trí tuệ và quyền tác giả pháp lý", "C2"),

        // ── TOEIC 300 (10 chủ đề) ──
        TopicItem(61, "Chào đón đối tác tại quầy lễ tân văn phòng", "300"),
        TopicItem(62, "Đặt hàng văn phòng phẩm cơ bản cho phòng ban", "300"),
        TopicItem(63, "Thông báo lịch trình làm việc nội bộ trong ngày", "300"),
        TopicItem(64, "Chỉ đường đến phòng họp và các khu vực dịch vụ", "300"),
        TopicItem(65, "Nhận gửi bưu phẩm và thư tín tại phòng hành chính", "300"),
        TopicItem(66, "Hỏi giá và thanh toán đồ dùng văn phòng tại đại lý", "300"),
        TopicItem(67, "Hướng dẫn vận hành máy in và máy photocopy cơ bản", "300"),
        TopicItem(68, "Đặt suất ăn trưa văn phòng cho nhân viên", "300"),
        TopicItem(69, "Kiểm tra lịch trình phương tiện công cộng đi làm", "300"),
        TopicItem(70, "Báo cáo lý do đi làm muộn do sự cố giao thông", "300"),

        // ── TOEIC 500 (10 chủ đề) ──
        TopicItem(71, "Đặt phòng khách sạn và chuẩn bị công tác phí", "500"),
        TopicItem(72, "Soạn thảo email triệu tập cuộc họp phòng ban khẩn cấp", "500"),
        TopicItem(73, "Đọc hiểu bản mô tả công việc tuyển dụng nhân sự", "500"),
        TopicItem(74, "Yêu cầu bảng báo giá thiết bị văn phòng từ nhà thầu", "500"),
        TopicItem(75, "Mô tả bảng phân công công việc hàng ngày của cá nhân", "500"),
        TopicItem(76, "Đăng ký tham gia khóa đào tạo nội bộ của doanh nghiệp", "500"),
        TopicItem(77, "Thay đổi thời gian chuyến bay công tác tại đại lý vé", "500"),
        TopicItem(78, "Báo cáo sự cố thiết bị máy tính bị hỏng lên IT Support", "500"),
        TopicItem(79, "Hướng dẫn đối tác điền các thông tin vào biểu mẫu mẫu", "500"),
        TopicItem(80, "Soạn thảo biên nhận thanh toán dịch vụ văn phòng đơn giản", "500"),

        // ── TOEIC 700 (10 chủ đề) ──
        TopicItem(81, "Báo cáo tóm tắt doanh số bán hàng định kỳ theo quý", "700"),
        TopicItem(82, "Thống nhất các điều khoản hợp đồng mua bán thiết bị tiêu chuẩn", "700"),
        TopicItem(83, "Lập kế hoạch tổ chức sự kiện tri ân khách hàng cuối năm", "700"),
        TopicItem(84, "Soạn thư trả lời khiếu nại của khách hàng về lỗi sản phẩm", "700"),
        TopicItem(85, "Đọc hiểu báo cáo phân tích xu hướng thị trường tiêu dùng", "700"),
        TopicItem(86, "Phân bổ phân khúc công việc cho đội ngũ nhân sự dự án", "700"),
        TopicItem(87, "Thực hiện buổi đánh giá năng lực thử việc của nhân viên mới", "700"),
        TopicItem(88, "Lập ngân sách dự kiến cho chiến dịch quảng bá trực tuyến", "700"),
        TopicItem(89, "Đàm phán chiết khấu thương mại với nhà cung ứng vật tư", "700"),
        TopicItem(90, "Truyền đạt quy trình bảo hộ an toàn lao động mới tại nhà xưởng", "700"),

        // ── TOEIC >900 (10 chủ đề) ──
        TopicItem(91, "Phân tích báo cáo kiểm toán tài chính hàng năm của tập đoàn", "900"),
        TopicItem(92, "Hoạch định chiến lược thâm nhập thị trường quốc tế mới", "900"),
        TopicItem(93, "Thuyết trình trước Hội đồng quản trị về kế hoạch gọi vốn", "900"),
        TopicItem(94, "Đàm phán các điều khoản liên doanh thương mại đa quốc gia", "900"),
        TopicItem(95, "Giải quyết tranh chấp thương mại và pháp lý phức tạp với đối tác", "900"),
        TopicItem(96, "Tối ưu hóa chuỗi cung ứng toàn cầu nhằm giảm thiểu rủi ro", "900"),
        TopicItem(97, "Quyết định phê duyệt chính sách nhân sự cấp cao của tập đoàn", "900"),
        TopicItem(98, "Đánh giá mức độ rủi ro đầu tư dự án phát triển hạ tầng", "900"),
        TopicItem(99, "Phân tích tác động của chính sách thuế quan xuất nhập khẩu mới", "900"),
        TopicItem(100, "Đàm phán chi tiết các thương vụ sáp nhập và mua lại (M&A)", "900")
    )
}
