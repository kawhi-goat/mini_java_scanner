package sungjae;

/**
 * ���α׷��� �帧�� ����ϴ� Main Ŭ����
 */
public class Main {
    /**
     * Entry Point �޼ҵ�
     *
     * @param args - ����� ���� (0��° ���ҷ� �Է� ���� ��θ� ����)
     */
    public static void main(String[] args) {
        
    	if (args[0] == null) { // ��ĳ�ʰ� �м��� ������ ��θ� ���� ������ ��� ���� ���
            System.err.print("Please enter the file path (by debug argument)");
            return;
        }
        MiniJavaScanner sc = new MiniJavaScanner(args[0]); // Java Scanner ��ü
        Token tok = null; // Java Scanner���� �� token�� �����ϱ� ���� Token ����
        while ((tok = sc.getToken()).getSymbolOrdinal() != -1) // Java Scanner���� �� ���� ������ token�� ��� ���
            System.out.println(tok);
    }
}
