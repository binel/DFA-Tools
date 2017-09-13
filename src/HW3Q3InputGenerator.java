import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;

public class HW3Q3InputGenerator {
	private String lastGenerated = "000"; 
	
	public HW3Q3InputGenerator() {
		
	}
	
	private String add1(String input, int pos) {
		char chars[] = input.toCharArray(); 
		if(chars[pos] == '0') {
			chars[pos] = '1';
			return String.valueOf(chars); 
		} else {
			if(pos - 1 == -1) {
				chars[pos] = '0';
				return add1("000" + String.valueOf(chars), pos + 2);
			} 
			chars[pos] = '0';
			return add1(String.valueOf(chars), pos - 1);
		}
	}
	
	private Boolean verifyString(String s) {
		String a = ""; 
		String b = ""; 
		String c = ""; 
		
		for(int i = s.length() - 1; i >= 0; i = i - 3) {
			 c += s.charAt(i); 
			 b += s.charAt(i - 1); 
			 a += s.charAt(i - 2); 
		}
		
		int a_n = Integer.parseInt(a, 2);
		int b_n = Integer.parseInt(b, 2);
		int c_n = Integer.parseInt(c, 2);
		
		if(a_n + b_n == c_n) {
			return true; 
		} else {
			return false; 
		}
	}
	
	public Entry<String, Boolean> generateNextInput() {
		String next = add1(this.lastGenerated, this.lastGenerated.length() - 1);
		this.lastGenerated = next; 
		
		return new SimpleEntry<String, Boolean>(next, verifyString(next));
	}
	
	public static void main(String args[]) {
		HW3Q3InputGenerator gen = new HW3Q3InputGenerator(); 
		
		for(int i = 0; i < 10000; i++) {
			Entry<String, Boolean> e = gen.generateNextInput(); 
			System.out.println(e.getKey() + "->" + e.getValue());
		}
	}
}
