import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Set;


// Import any package as required


public class HuffmanSubmit implements Huffman {
  
	// Feel free to add more methods and variables as required. 
 
	public void encode(String inputFile, String outputFile, String freqFile){
		// TODO: Your code here
		BinaryIn in= new BinaryIn(inputFile);
		
		//putting things into a hashmap
		HashMap<Character, Integer> hm =new HashMap<Character, Integer>();
		while(!in.isEmpty()) {
			Character temp =in.readChar();
			if(hm.containsKey(temp)) {
				hm.put(temp, hm.get(temp)+1);
			}else {
				hm.put(temp, 1);
			}
		}
		
		//putting everything into a priority queue
		PriorityQueue<BTNode> pq =new PriorityQueue<BTNode>(11, new FreqComparator());
		Set<Character> keys= hm.keySet();
		for(Character c: keys) {
			pq.offer(new BTNode(c, hm.get(c)));
		}
		
		//building the huffman tree
		BTNode root = null;
		while(!pq.isEmpty()) {
			BTNode small = pq.poll();
			BTNode big = pq.poll();
			if(big==null) {
				root=small;
				break;
			}
			BTNode temp =new BTNode(small, big);
			pq.offer(temp);
		}
		//assign codes
		    //root.Hcode+="0";
			assignCode(root);
		   
			HashMap<Character, String> Huffhm = new HashMap<Character, String>();
		//create the original hash map
			assignHash(root, Huffhm);
	
		//write the freqFile
	 BinaryOut out= new BinaryOut(freqFile);

		Set<Character> keyo=Huffhm.keySet();
		
		    for(Character temp :keyo) {
		  //  System.out.println(Huffhm.get(temp)+" : "+hm.get(temp)+"  "+temp);
			 out.write(Integer.toBinaryString(temp)+":"+hm.get(temp)+"\n");
		    }
				out.close();
			//encoding
				BinaryIn ino= new BinaryIn(inputFile);
				BinaryOut outo=new BinaryOut(outputFile);
				
				while(!ino.isEmpty()) {
					Character temp = ino.readChar();
					
					String s = Huffhm.get(temp);
					char[] a = s.toCharArray();
					for(int i=0; i<a.length; i++) {
						if(a[i] == ('1')) {
							outo.write(true);
							//outo.flush();
						}else {
							outo.write(false);
							//outo.flush();
							//what is flush
						}
					}					
				}
				outo.close();
				Huffhm.clear();
	}

	public static void assignHash(BTNode root, HashMap<Character, String> Huffhm) {
		if(root==null) {
			return;
		}
		
		assignHash(root.left, Huffhm);
		if(!(root.data==null)) {
			Huffhm.put(root.data, root.Hcode);
		}
		assignHash(root.right, Huffhm);
		
	}
	
	
	public static void assignCode(BTNode root) {
		if(root.right==null&&root.left==null) {
			return;
		}
		if(!(root.left==null)) {
			root.left.Hcode+=root.Hcode+"0";
		}
		if(!(root.right==null)) {
			root.right.Hcode+=root.Hcode+"1";
		}
		assignCode(root.left);
		assignCode(root.right);
	}
	
	private class FreqComparator implements Comparator<BTNode>{

		@Override
		public int compare(BTNode arg0, BTNode arg1) {
			// TODO Auto-generated method stub
            if(arg0.freq-arg1.freq>0) {
				return 1;
			}else if(arg0.freq-arg1.freq<0) {
				return -1;
			}
			return 0;
			
		}
		
	}
	
	public static class BTNode{

		Integer freq;
		Character data;
		String Hcode;
		BTNode left;
		BTNode right;
		
		public BTNode(Character data, Integer freq) {
			this.data= data;
			this.freq=freq;
			left=null;
			right=null;
			Hcode="";
		}
		
		public BTNode(BTNode small, BTNode big) {
			Hcode="";
			this.data=null;
			left=small;
			right=big;
			freq =small.freq+big.freq;
		}

	}

	//decoding
   public void decode(String inputFile, String outputFile, String freqFile){
		// TODO: Your code here
	   HashMap<Character, Integer> hm =new HashMap<Character, Integer>();
	   String line = null;

       try {
           // FileReader reads text files in the default encoding.
           FileReader fileReader = 
               new FileReader(freqFile);

           // Always wrap FileReader in BufferedReader.
           BufferedReader bufferedReader = 
               new BufferedReader(fileReader);

           while((line = bufferedReader.readLine()) != null) {
        	   if(line.equals("")) {
        		   break;
        	   }
        	  int index = line.indexOf(":");
        	  String bin = line.substring(0, index);
        	  Integer c = Integer.parseInt(line.substring(index+1));
              // System.out.println(line);      
        	  char b = (char)Integer.parseInt(bin, 2);
               hm.put(b, c);
           }   
            bufferedReader.close();
       }catch(FileNotFoundException ex) {              
           }
           catch(IOException ex) {
           }
       
       PriorityQueue<BTNode> pq =new PriorityQueue<BTNode>(11, new FreqComparator());
		Set<Character> keys= hm.keySet();
		for(Character c: keys) {
			pq.offer(new BTNode(c, hm.get(c)));
		}
		
		//building the huffman tree
		BTNode root = null;
		while(!pq.isEmpty()) {
			BTNode small = pq.poll();
			BTNode big = pq.poll();
			if(big==null) {
				root=small;
				break;
			}
			BTNode temp =new BTNode(small, big);
			pq.offer(temp);
		}
		
		//assign codes
	    //root.Hcode+="0";
		assignCode(root);
		
		BinaryOut out = new BinaryOut(outputFile);
		BinaryIn inf =new BinaryIn(inputFile);
		
		
		//writing output file
		//String ino ="";
		BTNode pointer=root;
		while(!inf.isEmpty()) {
			boolean boo = inf.readBoolean();
			//System.out.println(boo);
			if(boo==true) {
			    pointer=pointer.right;
			}else {
				pointer=pointer.left;
			}
			
			if(pointer.data!=null) {
				out.write(pointer.data);
				pointer=root;
				out.flush();
			}
		}
            out.close();		
	   
   }




public static void main(String[] args) {
      Huffman  huffman = new HuffmanSubmit();
		huffman.encode("ur.jpg", "ur.enc", "freq.txt");
		System.out.println("pic encode done");
		huffman.decode("ur.enc", "ur_dec.jpg", "freq.txt");
		System.out.println("pic decode done");
		huffman.encode("alice30.txt", "alice_en.enc", "freqa.txt");
		System.out.println("alice encode done");
		huffman.decode("alice_en.enc", "alice30_dec.txt", "freqa.txt");
		System.out.println("alice decode done");
		System.out.println("Ready to view");
		// After decoding, both ur.jpg and ur_dec.jpg should be the same. 
		// On linux and mac, you can use `diff' command to check if they are the same. 
   }

}
