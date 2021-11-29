package sungjae;

	/**
	 * Java Scanner에서 사용할 Token의 Model 클래스
	 * 
	 */
	public class Token {
	    /**
	    * Java Scanner 객체에서 인식한 Token의 Symbol 타입 (대분류)
	     */
	    public enum SymbolType {
	        Operator, IDorReserved, Con, Dec, Oct, Hex, Real, NULL
	    }

	    /**
	     * Token의 키워드나 명칭, 연산자를 식별하기 위한 Symbol (소분류)
	     */
	    public enum TokenSymbol {
	    	NULL(-1),
	    	ID(3),Con(4), Dec(5), Oct(6), Hex(7), Real(8),
	    	
	    	// 연산자
	    	Plus(10), Minus(11), Mul(12), Div(13), Mod(14),
	    	Assign(15), Not(16), And(17), Or(18),
	    	Equal(19), NotEqu(20), Less(21), Great(22), Lesser(23), Greater(24),
	    	AddAssign(25), SubAssign(26), MulAssign(27), DivAssign(28), ModAssign(29),
	    	
	    	// 특수 기호
	    	LBracket(30), RBracket(31), LBrace(32), RBrace(33), LParen(34), RParen(35), Comma(36), 
	    	Semicolon(37), LQuotes(38), RQuotes(39),
	        
	        // 예약어
	        Const(43), Else(46), If(40), Return(47), Void(48), While(41), For(42), Break(49), 
	        Continue(50), Float(45), Char(51), Int(44);
	        
	        private final int value;
	    	TokenSymbol(int value) {this.value = value;}
	    	public int getValue() {return value;}
	    }

	   
	    
	    private TokenSymbol symbol; // Token이 가진 Symbol (Symbol에 상응하는 정수를 추출해낼 수 있다)
	    private String val; // 명칭 혹은 숫자일 경우 그 값을 저장
	    private String tokenString; // 인식한 토큰의 원시 String

	    
	    
	    /**
	     * 생성자
	     * 각각의 멤버변수들을 NULL로 초기화한다.
	     */
	    public Token() {
	        symbol = TokenSymbol.NULL;
	        val = "0";
	        tokenString = "NULL";
	        
	    }
	    
	    /**
	    * 상수를 10진수, 8진수, 16진수, 실수형으로 소분류 해주는 메소드
	    */
	     
	     
	     /** 키워드나 명칭을 입력받았을 때, 그 String이 키워드인지 명칭인지 구분하는 메소드
	     * 키워드라면 어떤 키워드인지 Symbol을 할당
	     *
	     * @param token - Symbol을 구분할 토큰 String
	     * @return 구분된 토큰 Symbol
	     */
	    private TokenSymbol getIDorReservedSymbol(String token) {
	        switch (token) {
	            // Reserved (예약어)
	            case "const":   return TokenSymbol.Const;
	            case "else":    return TokenSymbol.Else;
	            case "if":      return TokenSymbol.If;
	            case "int":     return TokenSymbol.Int;
	            case "float":   return TokenSymbol.Float;
	            case "char":   return TokenSymbol.Char;
	            case "return":  return TokenSymbol.Return;
	            case "void":    return TokenSymbol.Void;
	            case "while":   return TokenSymbol.While;
	            case "break":   return TokenSymbol.Break;
	            case "for":   return TokenSymbol.For;
	            case "continue":   return TokenSymbol.Continue;
	            

	            // ID
	            default:
	                return TokenSymbol.ID;
	        }
	    }

	    /**
	     * 입력받은 토큰 String이 연산자라면 어떤 연산자인지 구분하기 위한 메소드
	     *
	     * @param token - Symbol을 구분할 토큰 String
	     * @return 구분된 토큰 Symbol (연산자)
	     */
	    private TokenSymbol getOperatorSymbol(String token) {
	        switch (token) {
	            case "!":   return TokenSymbol.Not;
	            case "!=":  return TokenSymbol.NotEqu;
	            case "%":   return TokenSymbol.Mod;
	            case "%=":  return TokenSymbol.ModAssign;
	            case "&&":  return TokenSymbol.And;
	            case "(":   return TokenSymbol.LParen;
	            case ")":   return TokenSymbol.RParen;
	            case "*":   return TokenSymbol.Mul;
	            case "*=":  return TokenSymbol.MulAssign;
	            case "+":   return TokenSymbol.Plus;
	            case "+=":  return TokenSymbol.AddAssign;
	            case ",":   return TokenSymbol.Comma;
	            case "-":   return TokenSymbol.Minus;
	            case "-=":  return TokenSymbol.SubAssign;
	            case "/":   return TokenSymbol.Div;
	            case "/=":  return TokenSymbol.DivAssign;
	            case ";":   return TokenSymbol.Semicolon;
	            case "<":   return TokenSymbol.Less;
	            case "<=":  return TokenSymbol.Lesser;
	            case "=":   return TokenSymbol.Assign;
	            case "==":  return TokenSymbol.Equal;
	            case ">":   return TokenSymbol.Great;
	            case ">=":  return TokenSymbol.Greater;
	            case "[":   return TokenSymbol.LBracket;
	            case "]":   return TokenSymbol.RBracket;
	            case "{":   return TokenSymbol.LBrace;
	            case "||":  return TokenSymbol.Or;
	            case "}":   return TokenSymbol.RBrace;
	            case "'": return TokenSymbol.LQuotes;
	            case "&":
	                System.err.print(LexicalError.getErrorMessage(LexicalError.ErrorCode.SingleAmpersand));
	                break;
	            case "|":
	                System.err.print(LexicalError.getErrorMessage(LexicalError.ErrorCode.SingleBar));
	                break;
	            default: // 인식하지 못한 TokenSymbol
	                System.err.print(LexicalError.getErrorMessage(LexicalError.ErrorCode.InvalidChar));
	                break;
	        }
	        return TokenSymbol.NULL;
	    }

	    /**
	     * Token의 Symbol과 value를 설정하는 메소드
	     * JAVA Scanner 객체에서 잘라낸 토큰 String을 통해 그 String에 맞는 Symbol로 설정한다
	     *
	     * @param token - 잘라낸 토큰 String
	     * @param type - Java Scanner 객체에서 분류한 타입 (키워드나 명칭, 숫자, 연산자로 대분류)
	     */
	    public void setSymbol(String token, SymbolType type) {
	        tokenString = token;
	        switch (type) {
	        	case Con:
	        		symbol = TokenSymbol.Con;
	        		val = token;
	            case IDorReserved:
	                symbol = getIDorReservedSymbol(token);
	                val = token;
	                break;
	            case Dec:
	            	symbol = TokenSymbol.Dec;
	            	val = Integer.toString(parseInt(token));
	                break;
	            case Oct:
	            	symbol = TokenSymbol.Oct;
	            	val = Integer.toString(parseInt(token));
	                break;
	            case Hex:
	            	symbol = TokenSymbol.Hex;
	            	val = Integer.toString(parseInt(token));
	                break;
	            case Real:
	            	symbol = TokenSymbol.Real;
	            	val = token;
	                break;
	            case Operator:
	                symbol = getOperatorSymbol(token);
	                break;
	            case NULL:
	            default:
	                break;
	        }
	    }

	    /**
	     * 16진수, 8진수, 10진수에 상관없이 String을 정수형으로 추출해내는 메소드
	     *
	     * @param s - 정수로 변환할 String (eg. 0x1F, 047, 14)
	     * @return String의 정수
	     */
	    
	    private int parseInt(String s) {
	        int radix = 10; // default 진법은 10진수
	        if (s.startsWith("0x")) { // 16진수일 경우
	            radix = 16; // 진법을 16진수로 설정
	            s = s.substring(2); // prefix인 0x 제거
	        } else if (s.startsWith("0") && s.length() > 1) { // 8진수일 경우
	            radix = 8; // 진법을 8진수로 설정
	        }
	        return Integer.parseInt(s, radix); // 위에서 설정한 진법대로 진법 변환
	    }

	    /**
	     * Symbol에 해당하는 토큰 심볼을 숫자로 표현하는 메소드
	     *
	     * @return 토큰 심볼의 숫자 (-1 : NULL)
	     */
	    public int getSymbolOrdinal() {
	        return symbol.value;  
	    }

	    /**
	     * 토큰이 명칭이나 숫자일 경우 그 토큰 값을 얻는 메소드
	     *
	     * @return 토큰 값 (없을 경우 0 반환)
	     */
	    public String getSymbolValue() {
	        return val;
	    }

	    /**
	     * Token의 번호, 값을 출력하는 toString 메소드
	     *
	     * @return 토큰을 표현하기 위한 String
	     */
	    public String toString() {
	    	 
	        return tokenString + "\t : (" + getSymbolOrdinal() + ", "  + val + ")";
	    }
	}

