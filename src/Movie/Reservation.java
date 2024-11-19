package Movie;

import java.io.*;
import java.time.Instant;
import java.util.ArrayList;

public class Reservation {
    private long id; // 발급번호
    private long movieId; // 영화 대푯값
    private String movieTitle; // 영화 제목
    private String seatName; // 좌석명
    private static final File file = new File("reservations.txt"); // 예매 정보가 저장될 파일 객체

    // 생성자: 발급번호, 영화 대푯값, 영화 제목, 좌석명 인수로 받음
    public Reservation(long id, long movieId, String movieTitle, String seatName) {
        this.id = id;
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.seatName = seatName;
    }

    // 예약 ID를 이용해 예약을 찾는 메소드
    public static Reservation findById(String reservationId) throws IOException {
        Reservation r = null;
        BufferedReader br = new BufferedReader(new FileReader(file)); // 파일에서 읽기 위한 BufferedReader 생성
        String line = null;

        // 파일의 각 라인을 읽어서 예약 ID가 일치하는 예약을 찾음
        while ((line = br.readLine()) != null) {
            String[] temp = line.split(","); // 쉼표로 구분된 문자열을 배열로 분리
            if (reservationId.equals(temp[0])) { // 예매 ID와 일치하는 예약 찾음
                r = new Reservation( // Reservation 객체 생성
                        Long.parseLong(temp[0]), // 발급번호
                        Long.parseLong(temp[1]), // 영화 대푯값
                        temp[2], // 영화 제목
                        temp[3] // 좌석명
                );
                break; // 예약을 찾으면 더 이상 반복할 필요 없음
            }
        }
        br.close(); // BufferedReader 자원 해제
        return r; // 찾은 예약 객체 반환
    }

    // 예약 정보를 읽기 좋게 출력하는 메소드 (예: "영화: 영화제목, 좌석: 좌석명")
    public String toString() {
        return String.format("영화: %s, 좌석: %s", movieTitle, seatName);
    }

    // 예약 ID를 이용해 예약을 취소하는 메소드
    public static Reservation cancel(String reservationId) throws IOException {
        Reservation canceled = null;
        BufferedReader br = new BufferedReader(new FileReader(file)); // 파일에서 읽기 위한 BufferedReader 생성
        String text = ""; // 취소된 예약을 제외한 나머지 예약을 저장할 문자열 변수
        String line = null;

        // 파일을 한 줄씩 읽으며 예약을 찾고 취소된 예약을 제외한 나머지를 text에 저장
        while ((line = br.readLine()) != null) {
            String[] temp = line.split(","); // 쉼표로 구분된 문자열을 배열로 분리
            if (reservationId.equals(temp[0])) { // 예약 ID가 일치하는 경우
                canceled = new Reservation( // 취소된 예약 객체 생성
                        Long.parseLong(temp[0]), // 발급번호
                        Long.parseLong(temp[1]), // 영화 대푯값
                        temp[2], // 영화 제목
                        temp[3] // 좌석명
                );
                continue; // 취소된 예약은 text에 추가하지 않음
            }
            text += line + "\n"; // 취소되지 않은 예약 정보는 text에 추가
        }
        br.close(); // BufferedReader 자원 해제

        // 파일을 덮어쓰기 모드로 열어서 취소된 예약을 제외한 예약을 다시 저장
        FileWriter fw = new FileWriter(file);
        fw.write(text); // 수정된 내용을 파일에 기록
        fw.close(); // FileWriter 자원 해제
        return canceled; // 취소된 예약 객체 반환
    }

    // 영화 ID를 이용해 해당 영화에 대한 모든 예약을 찾는 메소드
    public static ArrayList<Reservation> findByMovieId(String movieIdStr)
            throws IOException {
        ArrayList<Reservation> reservations = new ArrayList<Reservation>(); // 예매 정보를 저장할 ArrayList
        BufferedReader br = new BufferedReader(new FileReader(file)); // 파일에서 읽기 위한 BufferedReader 생성
        String line = null;

        // 파일을 한 줄씩 읽으며 주어진 영화 ID에 해당하는 예약을 찾음
        while ((line = br.readLine()) != null) {
            String[] temp = line.split(","); // 쉼표로 구분된 문자열을 배열로 분리
            if (movieIdStr.equals(temp[1])) { // 영화 ID가 일치하는 예약 찾기
                long id = Long.parseLong(temp[0]); // 예매 발급번호
                long movieId = Long.parseLong(temp[1]); // 영화 대푯값
                String movieTitle = temp[2]; // 영화 제목
                String seatName = temp[3]; // 좌석명
                Reservation r = new Reservation(id, movieId, movieTitle, seatName); // 예약 객체 생성
                reservations.add(r); // ArrayList에 추가
            }
        }
        br.close(); // BufferedReader 자원 해제
        return reservations; // 해당 영화에 대한 모든 예약 객체를 담은 ArrayList 반환
    }

    // 예약을 새로 생성할 때 사용하는 생성자 (발급번호는 현재 밀리초를 기준으로 자동 생성)
    public Reservation(long movieId, String movieTitle, String seatName) {
        this.id = Instant.now().toEpochMilli(); // 밀리초 단위 타임스탬프 생성 (현재 시간)
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.seatName = seatName;
    }

    // 좌석명을 반환하는 getter 메소드
    public String getSeatName() {
        return seatName;
    }

    // 예약 객체를 파일에 저장하는 메소드
    public void save() throws IOException {
        FileWriter fw = new FileWriter(file, true); // 파일에 이어쓰기 모드로 FileWriter 객체 생성
        fw.write(this.toFileString() + "\n"); // 객체를 파일에 저장할 수 있는 문자열로 변환하여 저장
        fw.close(); // FileWriter 자원 해제
    }

    // 객체의 정보를 파일 저장 형식에 맞는 문자열로 반환하는 메소드
    private String toFileString() {
        return String.format("%d,%d,%s,%s", id, movieId, movieTitle, seatName); // 파일에 저장할 형식으로 포맷팅
    }

    // 발급번호를 반환하는 getter 메소드
    public long getId() {
        return id;
    }
}
