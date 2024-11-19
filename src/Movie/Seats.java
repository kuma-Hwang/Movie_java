package Movie;

import java.util.ArrayList;

public class Seats {
    public static final int MAX_ROW = 5; // 좌석의 최대 행 수
    public static final int MAX_COL = 9; // 좌석의 최대 열 수
    private String[][] map = new String[MAX_ROW][MAX_COL]; // 좌석의 예매 현황을 나타내는 2D 배열

    // 생성자: 예매된 좌석들을 받아서 좌석 맵을 초기화하고 예매된 좌석을 마크함
    public Seats(ArrayList<Reservation> reservations) throws Exception {
        // 좌석 상태를 초기화(모든 좌석은 "o"로 설정하여 예매 가능 상태로 시작)
        for (int i = 0; i < MAX_ROW; i++) {
            for (int j = 0; j < MAX_COL; j++) {
                map[i][j] = "o"; // 좌석은 예매 가능 상태로 초기화
            }
        }

        // 예매된 좌석을 "x"로 표기
        for (int i = 0; i < reservations.size(); i++) {
            Reservation r = reservations.get(i); // 예약 정보를 하나씩 가져옴
            String seatName = r.getSeatName(); // 예약된 좌석 이름
            mark(seatName); // 예매된 좌석을 "x"로 표기
        }
    }

    // 좌석 배치도와 예매 현황을 출력하는 메소드
    public void show() {
        System.out.println("--------------------");
        System.out.println(" S C R E E N");
        System.out.println("--------------------");
        // 좌석의 행과 열을 출력
        for (int i = 0; i < MAX_ROW; i++) {
            System.out.printf("%c ", 'A' + i); // 각 행의 시작 문자(예: A, B, C, ...) 출력
            for (int j = 0; j < MAX_COL; j++) {
                System.out.printf(" %s", map[i][j]); // 각 좌석의 상태("o" 또는 "x") 출력
            }
            System.out.println(); // 각 행마다 출력 후 줄바꿈
        }
        System.out.println("   1 2 3 4 5 6 7 8 9"); // 열 번호를 출력
    }

    // 주어진 좌석 이름을 예매 상태로 마크하는 메소드
    public void mark(String seatName) throws Exception {
        char[] temp = seatName.toCharArray(); // 좌석명을 문자 배열로 변환 (예: "A-1" -> ['A', '-', '1'])
        int row = temp[0] - 'A'; // 좌석의 행을 계산 ('A'는 0, 'B'는 1, ..., 'E'는 4)
        int col = temp[2] - '1'; // 좌석의 열을 계산 ('1'은 0, '2'는 1, ..., '9'는 8)

        // 이미 예매된 좌석이라면 예외를 발생시킴
        if ("x".equals(map[row][col])) {
            throw new Exception("이미 예매된 좌석입니다.."); // 좌석이 이미 예매되었을 때 예외 처리
        }

        // 예매되지 않은 좌석이라면 예매된 상태로 표기
        map[row][col] = "x"; // 해당 좌석을 "x"로 마크하여 예매된 상태로 표시
    }
}
