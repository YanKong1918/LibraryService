# LibraryService📖


## To Do

[] 도서 대출 기능
  - 요청 시: `Loan` 생성, `Book.isAvailable = false` 로 변경
  - 예외 처리: 대출 중인 책이면 오류 리턴

[] 도서 반납 반납
  - 요청 시: `Loan` 삭제 또는 종료 처리, `Book.isAvailable = true`

[] 사용자 기준 대출 이력 조회
  - `/users/{id}/loans` → 해당 사용자의 대출 목록

[] 연체 여부 자동 계산
  - `LocalDate.now().isAfter(dueDate)` 를 기준으로 `isOverdue` 갱신
