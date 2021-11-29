package sungjae;

	/**
	 * Java Scanner���� ����� Token�� Model Ŭ����
	 * 
	 */
	public class Token {
	    /**
	    * Java Scanner ��ü���� �ν��� Token�� Symbol Ÿ�� (��з�)
	     */
	    public enum SymbolType {
	        Operator, IDorReserved, Con, Dec, Oct, Hex, Real, NULL
	    }

	    /**
	     * Token�� Ű���峪 ��Ī, �����ڸ� �ĺ��ϱ� ���� Symbol (�Һз�)
	     */
	    public enum TokenSymbol {
	    	NULL(-1),
	    	ID(3),Con(4), Dec(5), Oct(6), Hex(7), Real(8),
	    	
	    	// ������
	    	Plus(10), Minus(11), Mul(12), Div(13), Mod(14),
	    	Assign(15), Not(16), And(17), Or(18),
	    	Equal(19), NotEqu(20), Less(21), Great(22), Lesser(23), Greater(24),
	    	AddAssign(25), SubAssign(26), MulAssign(27), DivAssign(28), ModAssign(29),
	    	
	    	// Ư�� ��ȣ
	    	LBracket(30), RBracket(31), LBrace(32), RBrace(33), LParen(34), RParen(35), Comma(36), 
	    	Semicolon(37), LQuotes(38), RQuotes(39),
	        
	        // �����
	        Const(43), Else(46), If(40), Return(47), Void(48), While(41), For(42), Break(49), 
	        Continue(50), Float(45), Char(51), Int(44);
	        
	        private final int value;
	    	TokenSymbol(int value) {this.value = value;}
	    	public int getValue() {return value;}
	    }

	   
	    
	    private TokenSymbol symbol; // Token�� ���� Symbol (Symbol�� �����ϴ� ������ �����س� �� �ִ�)
	    private String val; // ��Ī Ȥ�� ������ ��� �� ���� ����
	    private String tokenString; // �ν��� ��ū�� ���� String

	    
	    
	    /**
	     * ������
	     * ������ ����������� NULL�� �ʱ�ȭ�Ѵ�.
	     */
	    public Token() {
	        symbol = TokenSymbol.NULL;
	        val = "0";
	        tokenString = "NULL";
	        
	    }
	    
	    /**
	    * ����� 10����, 8����, 16����, �Ǽ������� �Һз� ���ִ� �޼ҵ�
	    */
	     
	     
	     /** Ű���峪 ��Ī�� �Է¹޾��� ��, �� String�� Ű�������� ��Ī���� �����ϴ� �޼ҵ�
	     * Ű������ � Ű�������� Symbol�� �Ҵ�
	     *
	     * @param token - Symbol�� ������ ��ū String
	     * @return ���е� ��ū Symbol
	     */
	    private TokenSymbol getIDorReservedSymbol(String token) {
	        switch (token) {
	            // Reserved (�����)
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
	     * �Է¹��� ��ū String�� �����ڶ�� � ���������� �����ϱ� ���� �޼ҵ�
	     *
	     * @param token - Symbol�� ������ ��ū String
	     * @return ���е� ��ū Symbol (������)
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
	            default: // �ν����� ���� TokenSymbol
	                System.err.print(LexicalError.getErrorMessage(LexicalError.ErrorCode.InvalidChar));
	                break;
	        }
	        return TokenSymbol.NULL;
	    }

	    /**
	     * Token�� Symbol�� value�� �����ϴ� �޼ҵ�
	     * JAVA Scanner ��ü���� �߶� ��ū String�� ���� �� String�� �´� Symbol�� �����Ѵ�
	     *
	     * @param token - �߶� ��ū String
	     * @param type - Java Scanner ��ü���� �з��� Ÿ�� (Ű���峪 ��Ī, ����, �����ڷ� ��з�)
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
	     * 16����, 8����, 10������ ������� String�� ���������� �����س��� �޼ҵ�
	     *
	     * @param s - ������ ��ȯ�� String (eg. 0x1F, 047, 14)
	     * @return String�� ����
	     */
	    
	    private int parseInt(String s) {
	        int radix = 10; // default ������ 10����
	        if (s.startsWith("0x")) { // 16������ ���
	            radix = 16; // ������ 16������ ����
	            s = s.substring(2); // prefix�� 0x ����
	        } else if (s.startsWith("0") && s.length() > 1) { // 8������ ���
	            radix = 8; // ������ 8������ ����
	        }
	        return Integer.parseInt(s, radix); // ������ ������ ������� ���� ��ȯ
	    }

	    /**
	     * Symbol�� �ش��ϴ� ��ū �ɺ��� ���ڷ� ǥ���ϴ� �޼ҵ�
	     *
	     * @return ��ū �ɺ��� ���� (-1 : NULL)
	     */
	    public int getSymbolOrdinal() {
	        return symbol.value;  
	    }

	    /**
	     * ��ū�� ��Ī�̳� ������ ��� �� ��ū ���� ��� �޼ҵ�
	     *
	     * @return ��ū �� (���� ��� 0 ��ȯ)
	     */
	    public String getSymbolValue() {
	        return val;
	    }

	    /**
	     * Token�� ��ȣ, ���� ����ϴ� toString �޼ҵ�
	     *
	     * @return ��ū�� ǥ���ϱ� ���� String
	     */
	    public String toString() {
	    	 
	        return tokenString + "\t : (" + getSymbolOrdinal() + ", "  + val + ")";
	    }
	}

