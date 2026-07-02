# 📘 Cẩm nang Tổng hợp: Thiết kế Hệ thống & Kỹ thuật Refactoring (Cheat Sheet)

Tài liệu này tổng hợp toàn bộ các khái niệm cốt lõi từ các tài liệu về **Design Patterns**, **Code Smells**, và **Refactoring Techniques**. Đây là cẩm nang tham khảo nhanh giúp bạn đưa ra các quyết định thiết kế hệ thống và làm sạch mã nguồn một cách chuyên nghiệp.

---

## Phần 1: DESIGN PATTERNS (Mẫu thiết kế)
Mẫu thiết kế là các giải pháp tổng thể cho các vấn đề lặp đi lặp lại trong thiết kế phần mềm.

### 1. Creational Patterns (Mẫu khởi tạo)
Giải quyết các vấn đề liên quan đến việc tạo object, giúp hệ thống linh hoạt và tái sử dụng tốt hơn.
- **Factory Method**: Cung cấp interface để tạo object trong class cha, nhưng cho phép subclass thay đổi kiểu object được tạo.
- **Abstract Factory**: Tạo ra các họ object liên quan với nhau mà không cần chỉ định class cụ thể.
- **Builder**: Xây dựng một object phức tạp theo từng bước. Cho phép tạo ra các biểu diễn khác nhau của cùng một quá trình khởi tạo.
- **Prototype**: Copy object hiện có mà không làm code phụ thuộc vào class của chúng.
- **Singleton**: Đảm bảo một class chỉ có duy nhất một instance, và cung cấp điểm truy cập toàn cục tới nó. (Lưu ý: Hạn chế dùng trong môi trường đa luồng nếu không quản lý tốt).

### 2. Structural Patterns (Mẫu cấu trúc)
Cách lắp ráp các object và class thành cấu trúc lớn hơn trong khi vẫn giữ được tính linh hoạt và hiệu quả.
- **Adapter**: Cho phép các class có interface không tương thích làm việc cùng nhau (Wrapper).
- **Bridge**: Tách một class lớn hoặc tập hợp các class thành hai hệ thống phân cấp độc lập (Abstraction và Implementation) có thể phát triển riêng biệt.
- **Composite**: Tổ chức các object thành cấu trúc cây để biểu diễn mô hình phần-toàn thể (part-whole).
- **Decorator**: Thêm các hành vi mới cho object động bằng cách bọc nó trong các wrapper object.
- **Facade**: Cung cấp interface đơn giản hóa tới một thư viện, framework hoặc tập hợp các class phức tạp.
- **Flyweight**: Tiết kiệm RAM bằng cách chia sẻ các phần trạng thái chung giữa nhiều object (thay vì lưu trữ tất cả dữ liệu ở mỗi object).
- **Proxy**: Cung cấp vật thay thế hoặc đại diện cho một object khác để kiểm soát quyền truy cập tới nó.

### 3. Behavioral Patterns (Mẫu hành vi)
Tập trung vào giao tiếp hiệu quả và phân công trách nhiệm giữa các object.
- **Chain of Responsibility**: Chuyển request dọc theo một chuỗi các handler. Mỗi handler quyết định tự xử lý hoặc chuyển cho handler tiếp theo.
- **Command**: Biến một request thành một object độc lập. Cho phép tham số hóa các phương thức, hàng đợi, ghi log request và hỗ trợ undo.
- **Iterator**: Duyệt qua các phần tử của một collection mà không để lộ cấu trúc bên dưới của nó (list, stack, tree...).
- **Mediator**: Giảm sự phụ thuộc hỗn loạn giữa các object. Bắt các object giao tiếp qua một object trung gian (Mediator).
- **Memento**: Lưu và khôi phục trạng thái cũ của một object mà không vi phạm tính đóng gói.
- **Observer**: Định nghĩa cơ chế đăng ký (subscribe) để thông báo cho nhiều object về bất kỳ sự kiện nào xảy ra với object mà chúng đang theo dõi.
- **State**: Cho phép một object thay đổi hành vi của nó khi trạng thái bên trong thay đổi (giống như object thay đổi class).
- **Strategy**: Định nghĩa một tập hợp các thuật toán, đóng gói từng thuật toán và làm cho chúng có thể hoán đổi cho nhau.
- **Template Method**: Định nghĩa khung của thuật toán trong class cha, nhưng cho phép subclass ghi đè các bước cụ thể.
- **Visitor**: Tách thuật toán khỏi cấu trúc object mà nó hoạt động.

---

## Phần 2: CODE SMELLS (Dấu hiệu Code xấu)
Code Smells là các dấu hiệu cho thấy mã nguồn đang có vấn đề sâu bên trong (về thiết kế hoặc chất lượng).

### 1. Bloaters (Phình to)
Code, class hoặc phương thức trở nên quá lớn và khó quản lý.
- **Long Method**: Phương thức quá dài. (Giải pháp: *Extract Method*).
- **Large Class**: Class làm quá nhiều việc. (Giải pháp: *Extract Class*, *Extract Subclass*).
- **Primitive Obsession**: Dùng kiểu nguyên thủy (int, string) thay vì object cho những dữ liệu có ý nghĩa (ví dụ: số điện thoại, tiền tệ).
- **Long Parameter List**: Hàm nhận quá nhiều tham số. (Giải pháp: *Introduce Parameter Object*).
- **Data Clumps**: Một nhóm các biến thường xuyên xuất hiện cùng nhau ở khắp nơi. (Giải pháp: *Extract Class* để gộp chúng lại).

### 2. Object-Orientation Abusers (Lạm dụng OOP)
Áp dụng OOP không đúng hoặc chưa triệt để.
- **Switch Statements**: Dùng `switch/case` phức tạp thay vì đa hình (polymorphism).
- **Temporary Field**: Biến chỉ được gán giá trị ở một vài tình huống cụ thể (các lúc khác thì null).
- **Refused Bequest**: Subclass kế thừa phương thức/dữ liệu từ cha nhưng không dùng đến.
- **Alternative Classes with Different Interfaces**: Hai class làm việc giống hệt nhau nhưng tên method khác nhau.

### 3. Change Preventers (Cản trở thay đổi)
Sửa một chỗ nhưng phải đi tìm và sửa ở rất nhiều chỗ khác.
- **Divergent Change**: Một class phải bị sửa đổi vì nhiều lý do khác nhau.
- **Shotgun Surgery**: Thêm một tính năng mới buộc phải sửa nhỏ giọt ở hàng loạt class khác nhau.
- **Parallel Inheritance Hierarchies**: Mỗi lần tạo subclass ở một class, lại phải tạo một subclass tương ứng ở class khác.

### 4. Dispensables (Đồ dư thừa)
Thành phần vô dụng, không mang lại giá trị.
- **Comments**: Comment bù đắp cho code khó hiểu. (Thay vì viết comment, hãy refactor code cho dễ đọc).
- **Duplicate Code**: Các đoạn code giống nhau rải rác.
- **Lazy Class**: Class không làm đủ việc để tồn tại.
- **Data Class**: Class chỉ có thuộc tính và getter/setter mà không có hành vi.
- **Dead Code**: Biến, parameter, method hoặc class không bao giờ được gọi.
- **Speculative Generality**: Thiết kế tổng quát hóa cho một tương lai chưa từng xảy ra ("nhỡ đâu cần").

### 5. Couplers (Kết dính quá mức)
Các class phụ thuộc lẫn nhau một cách thái quá.
- **Feature Envy**: Một method quan tâm đến dữ liệu của class khác nhiều hơn class của chính nó.
- **Inappropriate Intimacy**: Hai class "sờ" vào các thành phần internal (private/protected) của nhau quá nhiều.
- **Message Chains**: Gọi hàm chuỗi kiểu `a.getB().getC().doSomething()`. (Biến client bị phụ thuộc cứng vào cấu trúc chuỗi).
- **Middle Man**: Class chỉ tồn tại để delegate công việc cho class khác.

---

## Phần 3: REFACTORING TECHNIQUES (Kỹ thuật Refactoring)
Các công cụ/hành động để giải quyết các Code Smells ở trên.

### 1. Composing Methods (Cấu trúc lại phương thức)
- **Extract Method**: Tách đoạn code thành một hàm riêng có tên rõ ràng.
- **Inline Method / Temp**: Gộp hàm/biến tạm ngược lại nếu nó không mang lại ý nghĩa rõ ràng.
- **Extract Variable**: Đặt tên biến cho một biểu thức phức tạp.
- **Replace Temp with Query**: Biến cục bộ trở thành một method nội bộ.

### 2. Moving Features between Objects (Di chuyển tính năng)
- **Move Method / Field**: Đưa phương thức hoặc trường sang class mà nó thuộc về hoặc sử dụng nhiều nhất.
- **Extract Class**: Tách một class lớn thành hai class nhỏ.
- **Inline Class**: Gộp một class (nếu nó vô dụng) vào class khác.
- **Hide Delegate / Remove Middle Man**: Ẩn/hiện cấu trúc bên trong bằng cách delegate.

### 3. Organizing Data (Tổ chức dữ liệu)
- **Encapsulate Field**: Ẩn biến public và thay bằng getter/setter.
- **Replace Magic Number with Symbolic Constant**: Đổi các số cứng (hard-code) thành các biến số hằng (constant).
- **Replace Type Code with Class / Subclasses / State/Strategy**: Chuyển các hằng số kiểm tra loại (type code) thành đa hình.

### 4. Simplifying Conditional Expressions (Đơn giản hóa biểu thức điều kiện)
- **Decompose Conditional**: Tách điều kiện if-else phức tạp thành các phương thức dễ đọc.
- **Consolidate Conditional Expression**: Gộp nhiều điều kiện rải rác có cùng kết quả thành một biểu thức if.
- **Replace Conditional with Polymorphism**: Đưa if-else/switch logic vào các class đa hình.
- **Introduce Null Object**: Thay các khối check null lặp đi lặp lại bằng một Null Object xử lý logic mặc định.

### 5. Dealing with Generalization (Xử lý kế thừa/Trừu tượng hóa)
- **Pull Up Field / Method**: Đưa field/method chung lên class cha.
- **Push Down Field / Method**: Chuyển field/method đặc thù xuống class con.
- **Extract Interface / Superclass**: Nhóm các hành vi chung thành một interface hoặc class cha.
- **Collapse Hierarchy**: Gộp cha và con nếu chúng không khác nhau nhiều.
- **Replace Inheritance with Delegation** (và ngược lại): Đổi kế thừa bằng uỷ quyền để linh hoạt hơn khi không thỏa mãn quy tắc kế thừa nguyên bản (Liskov Substitution Principle).

---
*Lưu ý khi ứng dụng:* 
- Khi thiết kế một tính năng mới (Design), hãy rà soát lại xem có Design Pattern nào phù hợp (Phần 1) hoặc có vi phạm nguyên lý nào sinh ra Smell (Phần 2) hay không.
- Quá trình Refactoring (Phần 3) nên diễn ra liên tục, kết hợp với Unit Tests để không làm thay đổi luồng nghiệp vụ cũ.*
