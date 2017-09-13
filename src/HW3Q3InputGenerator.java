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
		
		String DFANodes = "1 [s,a];2 [];3 [];4 [];5 [];6 [];7 [];8 [];9 [];10 [];11 [];12 [];13 [];14 [];15 [];trap [];";
		
		String DFAEdges = "edge 1 2 0;edge 1 4 1;"
				+ "edge 2 5 1;edge 2 3 0;"
				+ "edge 3 1 0;edge 3 trap 1;"
				+ "edge 4 6 1;edge 4 5 0;"
				+ "edge 5 trap 0;edge 5 1 1;"
				+ "edge 6 trap 1;edge 6 7 0;"
				+ "edge 7 8 0;edge 7 10 1;"
				+ "edge 8 9 0;edge 8 11 1;"
				+ "edge 9 1 1;edge 9 trap 0;"
				+ "edge 10 12 1;edge 10 11 0;"
				+ "edge 11 7 0;edge 11 trap 1;"
				+ "edge 12 13 1;edge 12 trap 0;"
				+ "edge 13 8 0;edge 13 14 1;"
				+ "edge 14 15 0;edge 14 12 1;"
				+ "edge 15 7 0;edge 15 trap 1;"
				+ "edge trap trap 1;edge trap trap 0";
		
		String DFAString = DFANodes + DFAEdges; 
		char[] alphabet = {'0', '1'};
		DFA dfa = new DFA(alphabet); 
		
		try {
			dfa.readTableToDFA(DFAString);
		} catch (DFA.NodeNotInListException e) {
			System.out.println(e.message);
			e.printStackTrace();
			return; 
		} catch (DFA.InvalidDFATableStringException e) { 
			e.printStackTrace();
		}
		
		double passed = 0; 
		double failed = 0; 
		
		for(int i = 0; i < 100000000; i++) {
			Entry<String, Boolean> e = gen.generateNextInput(); 
			Boolean result = dfa.processString(e.getKey());
			if(result != e.getValue()) {
				System.out.println("FAILED: " + e.getKey() + " expected " + e.getValue() + ", got " + result);
				failed++; 
			} else {
				//System.out.println("PASSED: " + e.getKey() + " expected " + e.getValue() + ", got " + result);
				passed++; 
			}
		}
		
		System.out.println("Accuracy: " + Double.toString(passed / (failed + passed)));
		
		System.out.println(dfa.toString());
	}
}
