package Movie;

import java.io.IOException;
import java.util.ArrayList;

public class AdminMenu extends AbstractMenu {
    private static final AdminMenu instance = new AdminMenu(null); // AdminMenu의 싱글톤 인스턴스
    private static final String ADMIN_MENU_TEXT = // 관리자 메뉴의 기본 문구
            "1: 영화 등록하기\n" +
                    "2: 영화 목록 보기\n" +
                    "3: 영화 삭제하기\n" +
                    "b: 메인 메뉴로 이동\n\n" +
                    "메뉴를 선택하세요: ";

    // 생성자: 메뉴의 기본 문구와 이전 메뉴를 인자로 받아 부모 클래스의 생성자를 호출
    private AdminMenu(Menu prevMenu) {
        super(ADMIN_MENU_TEXT, prevMenu); // 부모 클래스인 AbstractMenu의 생성자 호출
    }

    // 싱글톤 인스턴스를 반환하는 메소드
    public static AdminMenu getInstance() {
        return instance; // AdminMenu 객체 반환
    }

    // 사용자가 입력한 메뉴에 따라 다음 메뉴를 반환
    public Menu next() {
        switch (scanner.nextLine()) {
            case "1":
                createMovie(); // 영화 등록 진행
                return this; // 관리자 메뉴로 다시 돌아옴
            case "2":
                printAllMovies(); // 영화 목록 출력
                return this; // 관리자 메뉴로 다시 돌아옴
            case "3":
                deleteMovie(); // 영화 삭제 진행
                return this; // 관리자 메뉴로 다시 돌아옴
            case "b":
                return prevMenu; // 메인 메뉴로 돌아감
            default:
                return this; // 잘못된 입력일 경우 관리자 메뉴로 돌아옴
        }
    }

    // 모든 영화를 출력하는 메소드
    private void printAllMovies() {
        try {
            ArrayList<Movie> movies = Movie.findAll(); // Movie 클래스에서 모든 영화 목록을 가져옴
            System.out.println();
            // 영화 목록을 한 줄씩 출력
            for (int i = 0; i < movies.size(); i++) {
                System.out.printf("%s\n", movies.get(i).toString()); // 영화 객체의 toString()을 호출하여 출력
            }
        } catch (IOException e) {
            System.out.println("데이터 접근에 실패하였습니다."); // 영화 목록을 가져오는 데 실패한 경우 예외 처리
        }
    }

    // 새 영화를 등록하는 메소드
    private void createMovie() {
        System.out.print("제목: ");
        String title = scanner.nextLine(); // 영화 제목 입력 받음
        System.out.print("장르: ");
        String genre = scanner.nextLine(); // 영화 장르 입력 받음
        Movie movie = new Movie(title, genre); // 입력된 제목과 장르로 새로운 영화 객체 생성
        try {
            movie.save(); // 영화 객체를 파일에 저장
            System.out.println(">> 등록되었습니다."); // 등록 성공 메시지
        } catch (IOException e) { // 저장 실패 시 예외 처리
            System.out.println(">> 실패하였습니다."); // 등록 실패 메시지
        }
    }

    // 영화를 삭제하는 메소드
    private void deleteMovie() {
        printAllMovies(); // 먼저 모든 영화 목록을 출력
        System.out.print("삭제할 영화를 선택하세요: ");
        try {
            Movie.delete(scanner.nextLine()); // 사용자로부터 삭제할 영화 ID를 입력받아 삭제
            System.out.println(">> 삭제되었습니다."); // 삭제 성공 메시지
        } catch (IOException e) {
            System.out.println(">> 삭제에 실패하였습니다."); // 삭제 실패 시 예외 처리
        }
    }
}
