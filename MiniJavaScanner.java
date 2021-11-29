package sungjae;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Token�� �������� ���� ó���� ����ϴ� Scanner Ŭ����
 */

public class MiniJavaScanner {
    static public final char EOF = '\255'; // ������ ���� �ǹ��ϴ� EOF ���� ���
    static public final String SPECIAL_CHARS = "!=%&*+-/<>|"; // �� ���� �̻��� �ϳ��� ��ū�� �� �ִ� Ư�����ڵ�
    static public final int ID_LENGTH = 12; // �����Ϸ��� ������ ������ ��Ī�� ���̿� ������ �δ� ���� ����

    private String src; // Source Code�� ��ü ������ String���� �����ϱ� ���� ����
    private String sen;
    private Integer idx; // Source Code�� ���� �� cursor ������ �ϴ� ����

    /**
     * Token�� �����س� �� � ��ū�� �ν��ϰ� �ִ��� ��Ÿ���� ���� State
     */
    private enum State {
        Initial, Dec, Con, Oct, Hex, IDorReserved, Operator, Zero, PreHex, Real, Quotation, SingleOperator
    }

    /**
     *  Scanner ������
     * �ҽ� �ڵ��� ���� ��θ� �Է¹޾� src ������ String���� �����ϰ�, Ŀ���� �� ó������ �̵�
     *
     * @param filePath - �ҽ� �ڵ��� ���� ���
     */
    public MiniJavaScanner(String filePath) {
        src = parseFile(filePath);
        idx = 0;
    }

    /**
     * �ҽ��ڵ� ��θ� ���� �ҽ��ڵ� ������ String���� �о� ���̴� Method
     *
     * @param filePath - �о�� �ҽ��ڵ� ���
     * @return �ҽ��ڵ� ������ ���� (String)
     */
    private String parseFile(String filePath) {
        String src = "", readedString = ""; //  �ҽ��ڵ带 �����س��� String ����, readedString: �ҽ��ڵ��� ������ ��Ƴ��� String ����
        FileReader fileReader = null; // �ҽ��ڵ带 ���� File Reader
        try {
            fileReader = new FileReader(new File(filePath)); // ���� ��θ� �̿��� File Reader ����
        } catch (IOException e) {
            // ������ ���� �� ����
            System.err.print(LexicalError.getErrorMessage(LexicalError.ErrorCode.CannotOpenFile));
            return "";
        }

        BufferedReader reader = new BufferedReader(fileReader); // BufferedReader ��ü�� ����� �ҽ��ڵ� ������ ����
        try {
            while ((readedString = reader.readLine()) != null) // ���Ϸκ��� ���� ����
                src += readedString + "\n"; // ���� �ǵڿ� ���๮�� �߰�
            src += EOF;   // ������ ���� �ǹ��ϴ� EOF ���� �߰�
            reader.close();
        } catch (IOException e) {
            // ������ ���� �� ����
            System.err.print(LexicalError.getErrorMessage(LexicalError.ErrorCode.CannotOpenFile));
            return "";
        }
        return src;
    }

    /**
     * Core Method
     * Source code���� Token ������ String�� ������ Token ��ü�� ����� ��ȯ�ϴ� ó���� �Ѵ�
     *
     * @return �νĵ� ��ū ��ü ��ȯ
     */
    public Token getToken() {
        Token token = new Token();
        Token.SymbolType symType = Token.SymbolType.NULL; // Symbol Type�� NULL�� ����
        String tokenString = "";

        State state = State.Initial;

        // ���� Ŀ���κ��� Comment ����
        if (exceptComment()) {
            // Comment�� ����� ���߿� ERROR�� �߻����� ���
            System.err.print(LexicalError.getErrorMessage(LexicalError.ErrorCode.InvalidComment));
            return token;
        }

        sen = "";
        
        while (!isEOF(idx)) { // �ҽ��ڵ带 ���� ������ ���� ���
            char c = src.charAt(idx++); // Ŀ���κ��� ���� �ϳ��� �а� Ŀ���� ��ĭ �̵�
            
            if (Character.isWhitespace(c)) { // white space (needs trimming)
                if(state != State.Initial) break; // ���� ���ڵ��� �ν��ϰ� �־��ٸ� �״�� ����
                else continue;
            } else if (isSingleSpecialToken(c)) { // Single operator ( '(', ')', '{', '}', ',', '[', ']', ';', EOF )
                if (state == State.Initial) { // ó������ 1����¥��
                    state = State.SingleOperator;
                    tokenString = String.valueOf(c);
                } else { --idx; }
                break;
            } else if (isSpecialChar(c)) { // 1����¥�� �����ڰ� �ƴ� 2���� �̻��� �����ڰ� �� �� �ִ� �������� ���
                if (state != State.Initial && state != State.Operator) {
                    --idx; break;
                }
                state = State.Operator;
            } else if (state == State.Initial && c == '0') { // Zero�� �ν��� ���
                state = State.Zero;
            } else if(c == '\''){
            		state = State.Con;
        	} else if (Character.isDigit(c)) { // ���ڸ� �ν��� ���
        		if (state == State.Initial) // �ƹ��͵� �ν����� �ʾ��� ���, 10������ ���
                    state = State.Dec;
                else if (state == State.Zero) // ���� 0�� �ν��ϰ� �־��� ���, 8������ ���
                    state = State.Oct;
                else if (state == State.PreHex) // 0x ���� �ν��ϰ� �־��� ���, 16������ ���
                    state = State.Hex;
                else if (state == State.Operator) { // �����ڰ� ���� �� ���ڰ� ���ð�� while�� Ż��
                    --idx; break;
                }
            } else if (state == State.Zero && c == 'x') { // 0x���� �ν� ���� ���
                state = State.PreHex;
            } else if (Character.isAlphabetic(c) || c == '_') { // underscore Ȥ�� ���ĺ��� �ν����� ���
                if (state != State.Initial && state != State.IDorReserved) {
                    // ��Ī Ȥ�� ���� �ƴ� ��ū�� �ν��ϴ� ���� ��� while�� Ż��
                    --idx; break;
                }
                else if((c == '.' && Character.isDigit(src.charAt(idx-1))) || (c == 'e' && Character.isDigit(src.charAt(idx-1))))
                	state = State.Real;
                state = State.IDorReserved; // ��Ī Ȥ�� ����� �ν�
            } 

            tokenString += String.valueOf(c); // ��ū String�� ���� �߰�
        }
        symType = getSymbolType(state); // �ν��� state�κ��� ��ū�� � ���� �ǹ��ϴ��� ��з�
        if (symType == Token.SymbolType.IDorReserved && tokenString.length() > ID_LENGTH) {
            // ��Ī�� ���̰� ������ �Ѿ ��� ������ ó��
            // ERROR : ��Ī�� ���� �ʰ�
            System.err.print(LexicalError.getErrorMessage(LexicalError.ErrorCode.AboveIDLimit));
            return token;
        }
        token.setSymbol(tokenString, symType); // tokenString�� �Բ� ��з��� Ÿ���� �����Ͽ� token�� ����
         // �ν��� token�� ��ȯ
		return token;
		
        }

    /**
     * Ŀ���� ������ ���� ����Ű�� �ִ��� Ȯ���ϴ� Method
     * ��, ���̻� ���� ���ڰ� ������ Ȯ���ϴ� Method
     *
     * @param idx - Ȯ���� ��ġ
     * @return ���̻� ���� ���ڰ� ������ true, �ƴϸ� false
     */
    private boolean isEOF(int idx) {
        return idx >= src.length();
    }

    /**
     * ���ڰ� Ư������(1���� ������ ����)���� Ȯ���ϴ� Method
     *
     * @param c - Ȯ���� ��� ����
     * @return Ư������(1���� ������ ����)�� ��� true ��ȯ, �ƴҰ�� false ��ȯ
     */
    private boolean isSpecialChar(char c) {
        for (int i = 0; i < SPECIAL_CHARS.length(); ++i)
            if (SPECIAL_CHARS.charAt(i) == c)
                return true;
        return false;
    }

    /**
     * ���ڰ� 1���� ���������� Ȯ���ϴ� Method
     *
     * @param c - Ȯ���� ��� ����
     * @return 1���� �������� ��� true ��ȯ, �ƴҰ�� false ��ȯ
     */
    private boolean isSingleSpecialToken(char c) {
        switch (c) {
            case '(': case ')': case ',': case ';':
            case '[': case ']': case '{': case '}':
            case EOF:
                return true;
            default:
                return false;
        }
    }

    /**
     * ������ token�� Ÿ���� � Ÿ������ state�� ���� �з�
     *
     * @param s - �νĵ� state
     * @return state�� �ش��ϴ� �ɺ� Ÿ��
     */
    private Token.SymbolType getSymbolType(State s) {
        switch (s) {
            // ������ ��� (10����, 8����, 16����, 0, �Ǽ���)
            case Dec:
            	return Token.SymbolType.Dec;
            case Oct:
            	return Token.SymbolType.Oct;
            case Hex:
            	return Token.SymbolType.Hex;
            case Zero:
                return Token.SymbolType.Dec;
            case Real:
            	return Token.SymbolType.Real;
            // ��Ī Ȥ�� ������� ���
            case IDorReserved:
                return Token.SymbolType.IDorReserved;
            // �������� ���
            case Operator:
            case SingleOperator:
                return Token.SymbolType.Operator;
            // ������°� �ƴ� State�� ��� NULL Type�� ��ȯ (�νĽ���)
            case Initial:
            case PreHex:
            default:
                return Token.SymbolType.NULL;
        }
    }

    /**
     * ���� �ҽ��ڵ� ���Ͽ� ���� Ŀ���κ��� ��ȿ�� ��ū�� ���� ������ �ּ��� �����ϴ� Method
     *
     * @return block comment�� ������ ���� ��� true, �ƴϸ� false ��ȯ
     */
    private boolean exceptComment() {
        char c;
        // Ŀ���κ��� whitespace ���ڵ� ��� ����
        while (!isEOF(idx) && Character.isWhitespace(src.charAt(idx))) idx++;
        if (isEOF(idx)) return false; // whitespace ���ڵ��� ��� �����ߴµ� ������ ���� ��� ���������� �ּ� ���Ÿ� �ߴٰ� ��ȯ

        if (src.charAt(idx) == '/') { // 
            if (src.charAt(idx+1) == '*') {    // Block Comment
                idx += 2; // "//" ���� ���ڷ� Ŀ�� �̵�
                while (!isEOF(idx) && src.charAt(idx) != '\n') idx++; // ���๮�� Ȥ�� EOF ���ڰ� ���� ������ Ŀ�� �̵�
                if (!isEOF(idx)) idx += 1; // ���๮�� ������ ���ڰ� ���� ���, �� ���ڷ� Ŀ�� �̵�
            } 
        }
        return false; // ������ ���� ���������� �ּ� ����
    }
}