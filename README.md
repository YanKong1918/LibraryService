# LibraryService📖


## To Do

1. 도서 대출 기능
  - 요청 시: `Loan` 생성, `Book.isAvailable = false` 로 변경
  - 예외 처리: 대출 중인 책이면 오류 리턴

2. 도서 반납 기능
  - 요청 시: `Loan` 삭제 또는 종료 처리, `Book.isAvailable = true`

3. 사용자 도서 대출 기록 조회
  - `/users/{id}/loans` → 해당 사용자의 대출 목록

4. 연체 여부 자동 계산
  - `LocalDate.now().isAfter(dueDate)` 를 기준으로 `isOverdue` 갱신
