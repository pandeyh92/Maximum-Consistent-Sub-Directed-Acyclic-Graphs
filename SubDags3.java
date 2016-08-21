package algoProject;

import java.io.BufferedReader;

import java.io.BufferedWriter;
import java.io.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.*;
import java.util.ArrayList;
import java.util.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;


public class SubDags3 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		int fileCount = 0;
		String tempWork = null;
		File file 		= new File("C:\\Users\\sujee\\Downloads\\E-Books\\Alog\\B503_Graphs"
									+ "\\tree_25.txt");
		File writeFile 		= new File("C:\\Users\\sujee\\Downloads\\E-Books\\Alog\\B503_Graphs"
				+ "\\testing.txt");
		//ArrayList<String> graphSoFar					= new ArrayList<String>();
		
		
		
		LinkedHashSet<String> tempHash 					= new LinkedHashSet<String>();
		HashMap<String,ArrayList<String>> parentMatrix	= new HashMap<String,ArrayList<String>>();
		HashMap<String,ArrayList<String>> childMatrix 	= new HashMap<String,ArrayList<String>>();
		
		
		System.out.println(file.exists());
		System.out.println(file.exists());
		
		try 
		{
		    
			FileReader fr	= new FileReader(file);
			FileReader frr	= new FileReader(file);
			FileWriter wr	= new FileWriter(writeFile);
			BufferedReader  br = new BufferedReader(fr);
			BufferedWriter  bw = new BufferedWriter(wr);
			
			if(writeFile.exists()==false)
			{
				writeFile.createNewFile();
			}
						
			/***************************************************************************
			 *	Populating Temp LinkedHashSet										   *								
			 ***************************************************************************/
			
			int rootNumber =0;
			ArrayList<String> nul = new ArrayList<String>();
			parentMatrix.put("GO:"+0,nul );
			while(( tempWork = br.readLine()) != null)
				{
				    ArrayList<String> tempArray 					= new ArrayList<String>();
					fileCount++;
					
					String tempSplit[] = tempWork.split(" ");
					for(int i =0; i < tempSplit.length;i++)
					{
						if(tempSplit[i].equals("1"))
						{
							int nodeNumber=i;
							tempArray.add("GO:"+nodeNumber);
							ArrayList<String> temp = new ArrayList<String>();
							temp.add("GO:"+rootNumber);
							parentMatrix.put("GO:"+nodeNumber,temp );
		
						}
					}
					
					childMatrix.put("GO:"+rootNumber,tempArray);

					rootNumber++;
				}
			
			int noOfNodes = parentMatrix.size();
			String[] workingArray = new String[noOfNodes];
			for(int i =0; i < noOfNodes; i++)
				{
					workingArray[i]="GO:"+i;
				}
			
			System.out.println(" children ");
			for(Map.Entry<String,ArrayList<String>> entry : childMatrix.entrySet())	
			{
				System.out.println(entry.getKey()+" = "+entry.getValue());
				
			}
			
			System.out.println(" perent ");
			for(Map.Entry<String,ArrayList<String>> entry : parentMatrix.entrySet())	
			{
				System.out.println(entry.getKey()+" = "+entry.getValue());
				
			}
			
			HashMap<String,ArrayList<String>> consisParentMatrix = new HashMap<String,ArrayList<String>>();
//		    
			for(int i =0; i < noOfNodes; i++)
			{
				ArrayList<String> listOfParent = new ArrayList<String>();
				listOfParent.add(workingArray[i]);
				
				SendMeListOfChildren(workingArray[i],listOfParent,parentMatrix);
				
				consisParentMatrix.put(workingArray[i], listOfParent);
				
			}
			
			
			System.out.println("consistenr parent");
			for(Map.Entry<String,ArrayList<String>> entry : consisParentMatrix.entrySet())	
			{
				System.out.println(entry.getKey()+" = "+entry.getValue());
				
			}
			
			ArrayList<String> inOrdered = new ArrayList<String>();
			ArrayList<String> inOrderedCheck = new ArrayList<String>();
			inOrdered.add(workingArray[0]);
			int tempCount = 0;
			int incrNoOfNodes = noOfNodes;
			int two = 2;
			int forceOut = 0;
			String Back=null;
			while(tempCount < noOfNodes)
			{
				System.out.println(tempCount+" "+inOrdered.size());
				String Temp1 					= inOrdered.get(tempCount);
				inOrderedCheck.add(Temp1);
				if(Back == Temp1)
					{
					   tempCount++;
					   continue;
					}
				ArrayList<String> tempArrayList = childMatrix.get(Temp1);
				Iterator<String> iterator       = tempArrayList.iterator();
			
				while(iterator.hasNext())
				{	
					
					String tempV = iterator.next();
					if(inOrdered.contains(tempV)==false)
					{
						inOrdered.add(tempV);
					}
					else
					{
						inOrdered.remove(tempV);
						inOrdered.add(tempV);
						if(inOrderedCheck.contains(tempV)==true)
						{
							incrNoOfNodes++;
							tempCount--;
						}
					}
					
				}
				tempCount++;
				Back = Temp1;
			}
				
				
				/***************************************************************************
				 *	Generating nodes for 1st level distribution 						   *								*
				 ***************************************************************************/
				
				ArrayList<String>  firstLevelNodes = new ArrayList<String>();
//				for(int i =0; i< noOfNodes;i++)
//				{
					String tempItem    			= workingArray[0];
					ArrayList<String> listOfRootsChild = childMatrix.get(tempItem);
					for(int j =0; j< listOfRootsChild.size(); j++)
				    {
				    		firstLevelNodes.add(listOfRootsChild.get(j));
				    	
				     }
				//}
				
				/***************************************************************************
				 *	Generating All children of any given  node  		   				   *								
				 ***************************************************************************/
				
				HashMap<String,ArrayList<String>> allChildren = new HashMap<String,ArrayList<String>>();
				
				for(int i =0; i < noOfNodes; i++)
				{
					ArrayList<String> listOfChildren = new ArrayList<String>();
					
					SendMeListOfChildren(workingArray[i],listOfChildren,childMatrix);
					
					allChildren.put(workingArray[i], listOfChildren);
					
				}
				

				/***************************************************************************
				 *	Building Double Hash Function		   				                   *					
				 ***************************************************************************/

				
				HashMap<String,HashMap<String, ArrayList<String>>> fisrtHashing = 
						new HashMap<String,HashMap<String, ArrayList<String>>>();
				int[] array = new int[firstLevelNodes.size()];
				int[] tempSize = new int[firstLevelNodes.size()];
				for (int i = 0; i < firstLevelNodes.size(); i++)
				{
					String currentNode = firstLevelNodes.get(i);
					
//					ArrayList<String> childsOfCurrentNode = new ArrayList<String>();
//					childsOfCurrentNode.add(currentNode);
					HashMap<String, ArrayList<String>> secondHashing = new HashMap<String, ArrayList<String>>();
				    ArrayList<String> childsOfFirstHashing 			= allChildren.get(currentNode);
				    ArrayList<ArrayList<String>> temp1	= new ArrayList<ArrayList<String>>();
				    secondHashing.put(currentNode, consisParentMatrix.get(currentNode));
				    for(int j =0; j < childsOfFirstHashing.size(); j++)
				    {
				    	secondHashing.put(childsOfFirstHashing.get(j), consisParentMatrix.get(childsOfFirstHashing.get(j)));
				    
				    }
				    populateMore(currentNode,secondHashing);
				    
				    
				  //  wr.newLine();
				    System.out.println("currentNode = "+currentNode+" secondHashing = "+secondHashing.size());
				 fisrtHashing.put(firstLevelNodes.get(i), secondHashing); 
				 array[i] = secondHashing.size();
				 tempSize[i] = secondHashing.size();
					
				}  
						    
				int finalSizeCount = 1;
				
				for(int i =0; i < firstLevelNodes.size();i++)
				{
					for(int j =i+1; j < firstLevelNodes.size();j++)
					{
						int temporo = array[i]*array[j];
						finalSizeCount += temporo;
						tempSize[j] +=temporo;
					}
				}
			
				for(int i = 0;  i < firstLevelNodes.size();i++)
				{
					finalSizeCount +=array[i];
					
				}
				
				System.out.println(" FinalSizeCount"+finalSizeCount);
				System.out.println(" END 1");
				
				
				System.out.println(" END 2");	
				wr.close();
		}catch(IOException e)
		{}
				
	}
	
	public static void SendMeListOfChildren(String node , ArrayList<String> listTobeAdded , 
			HashMap<String, ArrayList<String>> matrix )
	{
			ArrayList<String> list = matrix.get(node);
			
			if (list.size()== 0) return;
			else 
			{
				for(int i = 0; i < list.size(); i++)
				{
					listTobeAdded.add(list.get(i));
					SendMeListOfChildren(list.get(i),listTobeAdded,matrix);
					
				}
			}
			
			
		
	}
	
	public static void populateMore(String cNode,HashMap<String, ArrayList<String>> secondHashing)
	{
		
		int size 			= secondHashing.size();
		String[] prefix 	= new String[size];
		prefix[0]			= "0";
		String[] retrivalKeys = new String[size];

		TreeSet<String> addCheckSet = new TreeSet<String>();		
		ArrayList<ArrayList<String>> arrList = new ArrayList<ArrayList<String>>(size);
		
		int temp 			= 0;
		for(Map.Entry<String,ArrayList<String>> entry : secondHashing.entrySet())	
		{
			
			ArrayList<String> l1 = entry.getValue();
			ArrayList<String> l2 	= new ArrayList<String>();
			
			l2.addAll(l1);
			
			TreeSet<String> setOrder = new TreeSet<String>();
			for(int p = 0; p < l1.size();p++ )
			{
				setOrder.add(l1.get(p));
			}
			String kk = setOrder.toString();
			kk=kk.substring(1, kk.length()-1).replaceAll(",", "");
			retrivalKeys[temp++] = entry.getKey();
			arrList.add(l2);
			addCheckSet.add(kk);
			
			
		}

		
		
		for(int i =1; i < size; i++)
		{
			prefix[i]       ="0"+prefix[i-1];
		}
		
		int pow 			= (int)Math.pow(2, size);
		
		for(int i =1; i < pow; i++)
		{
			String binaryRepre = Integer.toBinaryString(i);
			int len1 		   = binaryRepre.length();
			if(len1 != size)
			{
				int sub 	   = size - len1;
				binaryRepre   = prefix[sub-1]+binaryRepre;
			}
			String[] binaryRepreArray = binaryRepre.split("");
			int[] index = new int[binaryRepreArray.length];
			int indexCount = 0;
			Arrays.fill(index, -99);
			for(int j =0; j < index.length; j++)
			{
				if (binaryRepreArray[j].equals("1"))
				{
					index[indexCount++] = j;
				}
			}
			
		
			int loppingCount =0;
		//	String newKeyToBeAdded ="NO";
			TreeSet<String> tempHash    = new TreeSet<String>();
			while(loppingCount < size && index[loppingCount] != -99)
			{
				int posIndex = index[loppingCount++];
			//	newKeyToBeAdded = newKeyToBeAdded.concat(" ").concat(retrivalKeys[posIndex]);
				tempHash.addAll(secondHashing.get(retrivalKeys[posIndex]));
			}
			
			if(loppingCount > 1)
			{
				boolean flag = false;
				
				
				ArrayList<String> tempp = new ArrayList<String>();
				tempp.addAll(tempHash);
				
				String kk1 = tempp.toString();
				kk1=kk1.substring(1, kk1.length()-1).replaceAll(",", "");
				
				for(int z = 0; z < size; z++)
				{	
					if(arrList.get(z).containsAll(tempp) == true)
					{
						flag = true;
					}
				}
//				secondHashing.put(newKeyToBeAdded.substring(2),tempp);
//				if(flag == false)
//				{
//				secondHashing.put(cNode+" "+i,tempp);
//				}
				if(addCheckSet.add(kk1)==true)
				{
					secondHashing.put(cNode+" "+i,tempp);
				}
				
			//	System.out.println();
			}
			
		}
		
	}
	
}
