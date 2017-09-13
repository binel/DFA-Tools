import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Map.Entry;

public class DFA {
	private ArrayList<DFA_Node> nodes; 
	private DFA_Node start; 
	private char[] alphabet; 
	
	public DFA(char[] alphabet) {
		this.alphabet = alphabet; 
		nodes = new ArrayList<DFA_Node>(); 
	}
	
	public void addNode(String name, boolean isAccepting, boolean isStarting) {
		
		for(DFA_Node n : nodes) {
			if(n.getName().equals(name)) {
				return; 
			}
		}
		
		DFA_Node newNode = new DFA_Node(name, isAccepting, isStarting);
		nodes.add(newNode);
		if(isStarting) {
			this.start = newNode; 
		}
	}
	
	//assumes both nodes are already in the DFA, otherwise throws exception
	public void addConnection(String fromNodeName, String toNodeName, Character symbol) throws NodeNotInListException {
		DFA_Node fromNode = null; 
		DFA_Node toNode = null; 
		
		for(DFA_Node n : nodes) {
			if(n.getName().equals(fromNodeName)) {
				fromNode = n; 
			}
			if(n.getName().equals(toNodeName)) {
				toNode = n; 
			}
		}
		
		if(fromNode == null || toNode == null) {
			throw new NodeNotInListException(fromNodeName + "or" + toNodeName); 
		} else {
			fromNode.addConnection(toNode, symbol);
		}
	}
	
	public class NodeNotInListException extends Exception {
		public String message; 
		public NodeNotInListException(String string) {
			this.message = string; 
		}}
	
	//reads a string and converts it to a DFA. The String is formatted as follows: 
	//(name1) [{a, s}];
	//(name2) [{a}];
	// ..
	//edge (name1) (name2) (symbol);
	public void readTableToDFA(String table) throws NodeNotInListException, InvalidDFATableStringException {
		String[] lines = table.split(";"); 
		
		for(String l : lines) {
			String[] tokens = l.split(" ");
			if(tokens.length == 0) {
				continue; 
			} 
			if(tokens[0].equals("edge")) {
				Character symbol = null; 
				if(tokens[3].length() > 1) {
					throw new InvalidDFATableStringException(); 
				} else {
					symbol = (Character) tokens[3].charAt(0);
				}
				this.addConnection(tokens[1], tokens[2], symbol);
			} else {
				if(tokens[1].equals("[a]")) {
					this.addNode(tokens[0], true, false); 
				} else if (tokens[1].equals("[a,s]") || tokens[1].equals("[s,a]")) {
					this.addNode(tokens[0], true, true); 
				} else if (tokens[1].equals("[s]")) {
					this.addNode(tokens[0], false, true); 
				} else {
					this.addNode(tokens[0], false, false);
				}
			}
		}
	}
	
	public boolean processString(String s) {
		DFA_Node current = this.start; 
		for(int i = 0; i < s.length(); i++) {
			current = current.recieveSymbol(s.charAt(i));
			if(current == null) {
				return false; 
			}
		}
		
		if(current.isAccepting()) {
			return true; 
		} else {
			return false; 
		}
	}
	
	@Override
	public String toString() {
		String nodes = "";
		String edges = "";
		for(DFA_Node n : this.nodes) {
			String extras = "";
			if(n.isAccepting() && n.isStarting()) {
				extras += "s,a";
			} else if (n.isAccepting()) {
				extras += "a";
			} else if (n.isStarting()) {
				extras += "s";
			}
			
			nodes += n.getName() + " [" + extras + "];";
			
			for(Entry<Character, DFA_Node> e : n.connections) {
				edges += "edge " + n.getName() + " " + e.getValue().getName() + " " + e.getKey() +";";
			}
		}
		
		return nodes + edges;
	}
	
	public class InvalidDFATableStringException extends Exception {}
	
	private class DFA_Node {

		private String name; 
		private boolean accepting; 
		private boolean starting; 
		private ArrayList<Entry<Character, DFA_Node>> connections;
		
		public DFA_Node(String name, boolean isAccepting, boolean isStarting) {
			connections = new ArrayList<Entry<Character, DFA_Node>>(); 
			
			this.name = name; 
			this.accepting = isAccepting; 
			this.starting = isStarting; 
		}
		
		public void setName(String name) {
			this.name = name; 
		}
		
		public String getName() { 
			return name; 
		}
		
		public void setAccepting(boolean isAccepting) {
			this.accepting = isAccepting; 
		}
		
		public boolean isAccepting() {
			return this.accepting; 
		}
		
		public boolean isStarting() {
			return this.starting; 
		}
		
		public void setStarting(boolean isStarting) {
			this.starting = isStarting; 
		}
		
		public void addConnection(DFA_Node node, Character onSymbol) {
			if (!this.connections.contains(new SimpleEntry<Character, DFA_Node>(onSymbol, node))) {
				this.connections.add(new SimpleEntry<Character, DFA_Node>(onSymbol, node));
			}
		}
		
		//assumes that the DFA_Node does not have any invalid connections
		//(i.e. one symbol produces two nodes)
		public DFA_Node recieveSymbol(Character symbol) {
			for(Entry<Character, DFA_Node> e : this.connections) {
				if(e.getKey() == symbol) {
					return e.getValue(); 
				}
			}
			return null; 
		}
		
	}

	public static void main(String args[]) {
		String DFAString = "start [s];trap [];good [a];edge start trap b;edge start good a;edge good trap b;edge good good a;edge trap trap a;edge trap trap b";
		char[] alphabet = {'a', 'b'};
		DFA dfa = new DFA(alphabet); 
		
		try {
			dfa.readTableToDFA(DFAString);
		} catch (NodeNotInListException | InvalidDFATableStringException e) {
			e.printStackTrace();
		}
		
		String testString = "aabaa";
		Boolean result;
		
		result = dfa.processString(testString);
		System.out.println(testString + " -> " + result);
	}
}
