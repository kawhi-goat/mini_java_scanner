package sungjae;

/**
 * 프로그램의 흐름을 담당하는 Main 클래스
 */
public class Main {
    /**
     * Entry Point 메소드
     *
     * @param args - 디버그 인자 (0번째 원소로 입력 파일 경로를 받음)
     */
    public static void main(String[] args) {
        
    	if (args[0] == null) { // 스캐너가 분석할 파일의 경로를 받지 못했을 경우 에러 출력
            System.err.print("Please enter the file path (by debug argument)");
            return;
        }
        MiniJavaScanner sc = new MiniJavaScanner(args[0]); // Java Scanner 객체
        Token tok = null; // Java Scanner에서 얻어낸 token을 저장하기 위한 Token 변수
        while ((tok = sc.getToken()).getSymbolOrdinal() != -1) // Java Scanner에서 다 읽을 때까지 token을 얻어 출력
            System.out.println(tok);
    }
}
