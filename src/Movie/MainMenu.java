package Movie;

import java.io.IOException;
import java.util.ArrayList;

class MainMenu extends AbstractMenu {
    private static final MainMenu instance = new MainMenu(null); // 싱글톤 인스턴스
    private static final String MAIN_MENU_TEXT = // 메인 메뉴 텍스트
            "1: 영화 예매하기\n" +
                    "2: 예매 확인하기\n" +
                    "3: 예매 취소하기\n" +
                    "4: 관리자 메뉴로 이동\n" +
                    "q: 종료\n\n" +
                    "메뉴를 선택하세요: ";

    // 생성자: 부모 클래스의 생성자를 호출
    private MainMenu(Menu prevMenu) {
        super(MAIN_MENU_TEXT, prevMenu); // 부모 생성자 호출 (메뉴 텍스트와 이전 메뉴 설정)
    }

    // 싱글톤 인스턴스를 반환하는 메소드
    public static MainMenu getInstance() {
        return instance; // 메인 메뉴 객체 반환
    }

    // 사용자가 선택한 메뉴에 따라 동작을 수행하는 메소드
    public Menu next() {
        switch (scanner.nextLine()) {
            case "1":
                reserve(); // 영화 예매 진행
                return this; // 예매 후 메인 메뉴로 돌아옴
            case "2":
                checkReservation(); // 예매 확인
                return this; // 예매 확인 후 메인 메뉴로 돌아옴
            case "3":
                cancelReservation(); // 예매 취소
                return this; // 예매 취소 후 메인 메뉴로 돌아옴
            case "4":
                if (!checkAdminPassword()) { // 관리자 비밀번호 확인
                    System.out.println(">> 비밀번호가 틀렸습니다.");
                    return this; // 비밀번호 틀리면 메인 메뉴로 돌아옴
                }
                AdminMenu adminMenu = AdminMenu.getInstance(); // 관리자 메뉴 인스턴스를 얻어옴
                adminMenu.setPrevMenu(this); // 메인 메뉴를 이전 메뉴로 설정
                return adminMenu; // 관리자 메뉴로 이동
            case "q":
                return prevMenu; // 'q' 입력 시 이전 메뉴(종료)로 이동
            default:
                return this; // 잘못된 입력일 경우 메인 메뉴로 다시 돌아옴
        }
    }

    // 관리자 비밀번호를 확인하는 메소드
    private boolean checkAdminPassword() {
        System.out.print("관리자 비밀번호를 입력하세요: ");
        return "admin1234".equals(scanner.nextLine()); // "admin1234"와 비교하여 일치 여부 확인
    }

    // 예매 확인을 위한 메소드
    private void checkReservation() {
        System.out.print("발급번호를 입력하세요: ");
        try {
            Reservation r = Reservation.findById(scanner.nextLine()); // 발급번호로 예매 내역 조회
            if (r != null) {
                System.out.printf(">> [확인 완료] %s\n", r.toString()); // 예매 내역 출력
            } else {
                System.out.println(">> 예매 내역이 없습니다."); // 예매가 없으면 안내 메시지 출력
            }
        } catch (IOException e) {
            System.out.println(">> 파일 입출력에 문제가 생겼습니다."); // 파일 처리 중 에러 발생 시 메시지 출력
        }
    }

    // 예매 취소를 위한 메소드
    private void cancelReservation() {
        System.out.print("발급번호를 입력하세요: ");
        try {
            Reservation canceled = Reservation.cancel(scanner.nextLine()); // 발급번호로 예매 취소 진행
            if (canceled != null) {
                System.out.printf(">> [취소 완료] %s의 예매가 취소되었습니다.\n", canceled.toString()); // 취소된 예매 정보 출력
            } else {
                System.out.println(">> 예매 내역이 없습니다."); // 예매가 없으면 안내 메시지 출력
            }
        } catch (IOException e) {
            System.out.println(">> 파일 입출력에 문제가 생겼습니다."); // 파일 처리 중 에러 발생 시 메시지 출력
        }
    }

    // 영화 예매를 진행하는 메소드
    private void reserve() {
        try {
            ArrayList<Movie> movies = Movie.findAll(); // 모든 영화 목록을 가져옴
            for (int i = 0; i < movies.size(); i++) {
                System.out.printf("%s\n", movies.get(i).toString()); // 영화 목록을 출력
            }
            System.out.print("예매할 영화를 선택하세요: ");
            String movieIdStr = scanner.nextLine(); // 예매할 영화 ID 입력
            Movie m = Movie.findById(movieIdStr); // 영화 정보 조회
            ArrayList<Reservation> reservations = Reservation.findByMovieId(movieIdStr); // 영화에 대한 예약 목록 조회
            Seats seats = new Seats(reservations); // 좌석 배치 생성
            seats.show(); // 좌석 배치도 출력
            System.out.print("좌석을 선택하세요(예: E-9): ");
            String seatName = scanner.nextLine(); // 좌석 선택
            seats.mark(seatName); // 선택된 좌석을 마크
            Reservation r = new Reservation(
                    Long.parseLong(movieIdStr), // 영화 대푯값
                    m.getTitle(), // 영화 제목
                    seatName // 좌석명
            );
            r.save(); // 예매 내역 저장
            System.out.println(">> 예매가 완료되었습니다.");
            System.out.printf(">> 발급번호: %d\n", r.getId()); // 발급된 예매 번호 출력
        } catch (IOException e) {
            System.out.println(">> 파일 입출력에 문제가 생겼습니다."); // 파일 처리 중 에러 발생 시 메시지 출력
        } catch (Exception e) {
            System.out.printf(">> 예매에 실패하였습니다: %s\n", e.getMessage()); // 예외 발생 시 에러 메시지 출력
        }
    }
}
