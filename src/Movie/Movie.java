package Movie;

import java.io.*;
import java.time.Instant;
import java.util.ArrayList;

public class Movie {
    private long id; // 영화 대푯값 (영화 고유 ID)
    private String title; // 영화 제목
    private String genre; // 영화 장르
    private static final File file = new File("movies.txt"); // 영화 정보를 저장할 파일 객체

    // 생성자: 영화 대푯값, 제목, 장르 인수로 받음
    public Movie(long id, String title, String genre) {
        this.id = id;
        this.title = title;
        this.genre = genre;
    }

    // 모든 영화를 파일에서 읽어와 ArrayList에 저장하는 메소드
    public static ArrayList<Movie> findAll() throws IOException {
        ArrayList<Movie> movies = new ArrayList<Movie>(); // 영화 객체를 저장할 ArrayList 생성
        BufferedReader br = new BufferedReader(new FileReader(file)); // 파일에서 읽기 위한 BufferedReader 생성
        String line = null;

        // 파일의 각 라인을 읽으며 영화 정보를 추출하여 객체로 저장
        while ((line = br.readLine()) != null) { // 파일을 한 줄씩 읽음
            String[] temp = line.split(","); // 쉼표로 구분된 문자열을 배열로 분리
            Movie m = new Movie( // 영화 객체 생성
                    Long.parseLong(temp[0]), // 영화 대푯값
                    temp[1], // 영화 제목
                    temp[2] // 영화 장르
            );
            movies.add(m); // 생성된 영화 객체를 ArrayList에 추가
        }
        br.close(); // BufferedReader 자원 해제
        return movies; // 모든 영화 객체가 담긴 ArrayList 반환
    }

    // 영화 객체의 정보를 사람이 읽기 쉬운 형태로 반환하는 메소드 (예: "[1]: 영화 제목(장르)")
    public String toString() {
        return String.format("[%d]: %s(%s)", id, title, genre);
    }

    // 새 영화 객체를 생성할 때 사용하는 생성자 (ID는 현재 시간의 타임스탬프 값으로 자동 생성)
    public Movie(String title, String genre) {
        this.id = Instant.now().getEpochSecond(); // 타임스탬프 값을 영화 ID로 사용 (초 단위)
        this.title = title;
        this.genre = genre;
    }

    // 영화 객체를 파일에 저장하는 메소드
    public void save() throws IOException {
        FileWriter fw = new FileWriter(file, true); // 이어쓰기 모드로 FileWriter 객체 생성
        fw.write(this.toFileString() + "\n"); // 객체를 파일 저장 형식의 문자열로 변환하여 저장
        fw.close(); // FileWriter 자원 해제
    }

    // 객체 정보를 파일에 저장할 형식의 문자열로 변환하는 메소드
    private String toFileString() {
        return String.format("%d,%s,%s", id, title, genre); // 파일에 저장할 형식으로 포맷팅
    }

    // 영화 ID를 이용해 해당 영화를 삭제하는 메소드
    public static void delete(String movieIdStr) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file)); // 파일에서 읽기 위한 BufferedReader 생성
        String text = ""; // 파일 복사를 위한 빈 문자열 변수
        String line = null;

        // 파일을 한 줄씩 읽으며 삭제할 영화 ID를 찾고, 해당 ID를 제외한 다른 영화 정보는 text에 저장
        while ((line = br.readLine()) != null) {
            String[] temp = line.split(","); // 쉼표로 구분된 문자열을 배열로 분리
            if (movieIdStr.equals(temp[0])) { // 삭제 대상 영화 ID를 찾으면
                continue; // 해당 영화는 text에 추가하지 않음
            }
            text += line + "\n"; // 삭제되지 않은 영화는 text에 추가
        }
        br.close(); // BufferedReader 자원 해제

        // 덮어쓰기 모드로 파일을 열어서 수정된 내용(text)을 파일에 기록
        FileWriter fw = new FileWriter(file);
        fw.write(text); // 수정된 내용을 파일에 저장
        fw.close(); // FileWriter 자원 해제
    }

    // 영화 ID를 이용해 특정 영화를 찾는 메소드
    public static Movie findById(String movieIdStr) throws IOException {
        Movie movie = null;
        BufferedReader br = new BufferedReader(new FileReader(file)); // 파일에서 읽기 위한 BufferedReader 생성
        String line = null;

        // 파일의 각 라인을 읽으며 영화 ID가 일치하는 영화를 찾음
        while ((line = br.readLine()) != null) {
            String[] temp = line.split(","); // 쉼표로 구분된 문자열을 배열로 분리
            if (movieIdStr.equals(temp[0])) { // 영화 대푯값이 일치하면
                movie = new Movie(Long.parseLong(temp[0]), temp[1], temp[2]); // 영화 객체 생성
                break; // 일치하는 영화가 있으면 반복문 탈출
            }
        }
        br.close(); // BufferedReader 자원 해제
        return movie; // 찾은 영화 객체 반환
    }

    // 영화 제목을 반환하는 getter 메소드
    public String getTitle() {
        return title;
    }
}
