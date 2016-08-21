import java.io.BufferedReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.CopyOnWriteArrayList;

public class Subtree {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		int fileCount = 0;
		String tempWork = null;
		File file 		= new File("C:\\Users\\sujee\\Downloads\\E-Books\\Alog\\B503_Graphs"
									+ "\\tree_25.txt");
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
			BufferedReader  br = new BufferedReader(fr);
			
						
			/***************************************************************************
			 *	Populating Temp LinkedHashSet										   *								
			 ***************************************************************************/
			
		//	String temp = br.readLine();
		//	tempHash.add(temp);
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
			
			for(Map.Entry<String,ArrayList<String>> entry : childMatrix.entrySet())	
			{
				System.out.println(entry.getKey()+" = "+entry.getValue());
				
			}
			
			System.out.println("==============");
			for(Map.Entry<String,ArrayList<String>> entry : parentMatrix.entrySet())	
			{
				System.out.println(entry.getKey()+" = "+entry.getValue());
				
			}
			
			HashMap<String,ArrayList<String>> consisParentMatrix = new HashMap<String,ArrayList<String>>();
//		    
			for(int i =0; i < noOfNodes; i++)
			{
				ArrayList<String> listOfParent = new ArrayList<String>();
				
				SendMeListOfChildren(workingArray[i],listOfParent,parentMatrix);
				
				consisParentMatrix.put(workingArray[i], listOfParent);
				
			}
			
			
			System.out.println("----------------");
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
			ArrayList<String> graphSoFar					= new ArrayList<String>();

			
			graphSoFar.add(workingArray[0]);
			
			for(int i =1; i< inOrdered.size();i++)
			{
				String tempIncoming = inOrdered.get(i);
				int tempSiz = graphSoFar.size();
				for(int j =0; j < tempSiz; j++)
				{	
					String tempGraphSoFar = graphSoFar.get(j);
					String[] tempGraphSize = tempGraphSoFar.split(" ");
					ArrayList<String> tempGraphArray = new ArrayList<String>();
					for(int d =0; d < tempGraphSize.length; d++)
					{
					tempGraphArray.add(tempGraphSize[d]);
					}
					ArrayList<String> listToBeSent = consisParentMatrix.get(tempIncoming);
					if(tempGraphSize.length < listToBeSent.size()  ||
							(tempGraphArray.containsAll(parentMatrix.get(tempIncoming))==false)){}
					else
					{
						if(checkConsistency(tempIncoming , tempGraphSize, listToBeSent, i ) == true)
						{
						String newPath =tempGraphSoFar+" "+tempIncoming;
						graphSoFar.add(newPath);
						System.out.println(graphSoFar.size());
						}
					}

					System.out.println("printing number  of nodes reached"+i);
					
				}
			}
			
			HashSet<String> finalSet = new HashSet<String>();

			finalSet.addAll(graphSoFar);
		
//			Iterator iterator = finalSet.iterator(); 
//		      
//			   while (iterator.hasNext()){
//			   System.out.println("Paths: "+iterator.next() + " ");  
//			   }
//			   

			
			fr.close();
			frr.close();
		}
		catch(IOException e)
		{
			System.out.println(" ex");
		}
		
		
		
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

	public static boolean checkConsistency(String IncomingNode, String[] SplitedIncomingPath, ArrayList<String> a2, int len)
	{
		//String[] SplitedIncomingPath = IncomingPath.split(" ");
		TreeSet<String> a1	 = new TreeSet<String>();
		TreeSet<String> a3	 = new TreeSet<String>();
	//	ArrayList<String> a2 = new ArrayList<String>();
		boolean returnValue  = false;
		
		/* populating a1 which contains individual member of graph so far and incoming node */
		a1.add(IncomingNode);
		for(int i=0; i < SplitedIncomingPath.length; i++ )
		{
			a1.add(SplitedIncomingPath[i]);
					
		}
		
		a3.addAll(a2);
		
		if(a1.containsAll(a3))
		{
			returnValue = true;
		}
		return returnValue;
		
	}
	
}
