package algoProject;
import java.io.*;
import java.util.*;
public class trySubSet2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int fileCount = 0;
		String tempWork = null;
		File file 		= new File("C:\\Users\\sujee\\Downloads\\E-Books\\Alog\\B503_Graphs"
									+ "\\TESTEXTRA.txt");
		ArrayList<String> graphSoFar					= new ArrayList<String>();
		LinkedHashSet<String> tempHash 					= new LinkedHashSet<String>();
		HashMap<String,ArrayList<String>> parentMatrix	= new HashMap<String,ArrayList<String>>();
		HashMap<String,ArrayList<String>> edgeMatrix	= new HashMap<String,ArrayList<String>>();
		HashMap<String,ArrayList<String>> childMatrix 	= new HashMap<String,ArrayList<String>>();
		
		
		System.out.println(file.exists());
		System.out.println(file.exists());
		try 
		{
		    
			FileReader fr	= new FileReader(file);
			FileReader frr	= new FileReader(file);
			BufferedReader  br = new BufferedReader(fr);
			
			/* Populating Temp LinkedHashSet*/
			
			String temp = br.readLine();
			tempHash.add(temp);
				
			while(( tempWork = br.readLine()) != null)
				{
					fileCount++;
					String tempSplit[] = tempWork.split("\\t");
					for(int i =0; i < tempSplit.length;i++)
					{
						tempHash.add(tempSplit[i]);
					}
				}
			
			/* Populating InContent which contains input data  */
			
				String [][] InContent = new String[fileCount][2];
				BufferedReader  brr = new BufferedReader(frr);
				temp = brr.readLine();
				int z = 0;
				while((tempWork = brr.readLine()) != null)
				{
					String tempSplit[] = tempWork.split("\\t");
					for(int i =0; i< tempSplit.length;i++)
					{
						if(i==0)
						{
							InContent[z][0] = tempSplit[i]; 
						}
						else
						{
							InContent[z][1] = tempSplit[i]; 
						}
					}
							z++;				
				}
				
				for(int i =0; i < fileCount; i++)
				{
					for( int j =0; j< 2; j++)
					{
						System.out.print(" "+InContent[i][j]);
					}
					System.out.println();
				}

			/* Copying tempHash to workingArray  */
				int noOfNodes        = tempHash.size(); 
				String[] workingArray = new String[noOfNodes];
				workingArray          = tempHash.toArray(workingArray);
				
				//tempHash.forEach(a -> System.out.println(a));
				
				br.close();
				brr.close();

				/* Generating ParentMatrix for all node in wokringArray */
				
				for(int i =0; i< noOfNodes;i++)
				{
					String tempItem    			= workingArray[i];
					ArrayList<String>  tempList = new ArrayList<String>();
					ArrayList<String>  edgeList = new ArrayList<String>();
				    for(int j =0; j< fileCount; j++)
				    {
				    	
				    	if(InContent[j][0].equals(tempItem) )
				    	{
				    		tempList.add(InContent[j][1]);
				    		edgeList.add("From_"+InContent[j][1]+"_To_"+InContent[j][0]);
				    	}
				    }
				  
				  	parentMatrix.put(tempItem, tempList);
					edgeMatrix.put(tempItem, edgeList); 
				}
				
				/* Generating childMatrix for all node in wokringArray */
				
				for(int i =0; i< noOfNodes;i++)
				{
					String tempItem    			= workingArray[i];
					ArrayList<String>  tempList = new ArrayList<String>();
				    for(int j =0; j< fileCount; j++)
				    {
				    	if(InContent[j][1].equals(tempItem) )
				    	{
				    		tempList.add(InContent[j][0]);
				    	}
				    }
				  	childMatrix.put(tempItem, tempList);
				}
				
//				Set<String> keyset = parentMatrix.keySet();
//				for(int i =0; i<keyset.size(); i++)
//				{
//				}
			System.out.println("Parent details");
			for(Map.Entry<String,ArrayList<String>> entry : parentMatrix.entrySet())	
			{
				System.out.println("Key = "+entry.getKey()+" value = "+entry.getValue());
			}

			System.out.println("Child details");
			for(Map.Entry<String,ArrayList<String>> entry : childMatrix.entrySet())	
			{
				System.out.println("Key = "+entry.getKey()+" value = "+entry.getValue());
			}
			
			/* Generating Array to hold incoming nodes level wise */
				
				//				int nextIndex 		= 0;
//				int movingPosition 	= 0;
//				Arrays.fill(inOrder, "UNUSED");
				
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

				
			/* Generating ConsistentPartentMatrix for all nodes*/
				HashMap<String,ArrayList<String>> consisParentMatrix = new HashMap<String,ArrayList<String>>();
				HashMap<String,ArrayList<String>> consisParentEdge = new HashMap<String,ArrayList<String>>();
				for (int i = 0; i < noOfNodes; i++)
				{
					String node = inOrdered.get(i);
					ArrayList<String> nodesOriginalParent = parentMatrix.get(node);
					ArrayList<String> nodesOriginalEdge = edgeMatrix.get(node);
					ArrayList<String> tem = new ArrayList<String>();
					HashSet<String> tempo = new HashSet<String>();
					ArrayList<String> temEdge = new ArrayList<String>();
					HashSet<String> tempoEdge = new HashSet<String>();
					tem.addAll(nodesOriginalParent);
					temEdge.addAll(nodesOriginalEdge);
					for(int j = 0 ; j < nodesOriginalParent.size(); j++)
					{
						ArrayList<String> nodesGrandEdge = edgeMatrix.get(nodesOriginalParent.get(j));
						ArrayList<String> nodesGrandParent = parentMatrix.get(nodesOriginalParent.get(j));
						tem.addAll(nodesGrandParent);
						temEdge.addAll(nodesGrandEdge);
						//nodesGrandParent.addAll(nodesOriginalParent);
						System.out.println(" node "+i+" parent"+j);
					}
					tempo.addAll(tem);
					tempoEdge.addAll(temEdge);
					tem.clear();
					temEdge.clear();
					tem.addAll(tempo);
					temEdge.addAll(tempoEdge);
					consisParentMatrix.put(node, tem);
					consisParentEdge.put(node, temEdge);
					
				}
			
				System.out.println("Parent details after");
				for(Map.Entry<String,ArrayList<String>> entry : consisParentMatrix.entrySet())	
				{
					System.out.println("Key = "+entry.getKey()+" value = "+entry.getValue());
				}
				
				
				System.out.println("Edge details after");
				for(Map.Entry<String,ArrayList<String>> entry : consisParentEdge.entrySet())	
				{
					System.out.println("Key = "+entry.getKey()+" value = "+entry.getValue());
				}
				
		
		/* Implementing Our Algo on Graph we have build using workingArray , parrentMatrix and childMatrix*/
			HashMap<String,ArrayList<String>> pathWithEdge					= new HashMap<String,ArrayList<String>>();
			graphSoFar.add(workingArray[0]);
			pathWithEdge.put(workingArray[0], new ArrayList<String>());
				
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
							String newPath =tempGraphSoFar+"  "+tempIncoming;
							HashSet<String> tm = new HashSet<String>();
							tm.addAll(pathWithEdge.get(tempGraphSoFar));
							tm.addAll(edgeMatrix.get(tempIncoming));
							ArrayList<String> tm1 = new ArrayList<String>();
							tm1.addAll(tm);
							pathWithEdge.put(newPath,tm1);
							
							ArrayList<String> parentEdge	= new ArrayList<String>();
							parentEdge = edgeMatrix.get(tempIncoming);
							String temp99 = parentEdge.toString();
							String[] temp2 = temp99.split(",");
							if(temp2.length > 1)
							{
								addEdge(tempGraphSoFar,tempIncoming,pathWithEdge,edgeMatrix);
							}
							graphSoFar.add(newPath);
							System.out.println(graphSoFar.size());
							}
						}

						System.out.println("printing number  of nodes reached"+i);
						
					}
				}
				
				HashSet<String> finalSet = new HashSet<String>();

				
				finalSet.addAll(graphSoFar);
				System.out.println("final set "+finalSet.size());
				System.out.println(" finalGraph "+graphSoFar.size());
				System.out.println(" PathWithEdge "+pathWithEdge.size());

				System.out.println(" pathWithEdge ");
				for(Map.Entry<String,ArrayList<String>> entry : pathWithEdge.entrySet())	
				{
					System.out.println("Key = "+entry.getKey()+" value = "+entry.getValue());
				}
			
		
		}catch(IOException e)
		{}

		
	}

	public static boolean checkConsistency(String IncomingNode, String[] SplitedIncomingPath, ArrayList<String> a2, int len)
	{
		//String[] SplitedIncomingPath = IncomingPath.split(" ");
		TreeSet<String> a1	 = new TreeSet<String>();
		TreeSet<String> a3	 = new TreeSet<String>();
	
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
	
	public static void addEdge(String tempGraphSoFar,String tempIncoming,
			HashMap<String,ArrayList<String>> pathWithEdge, HashMap<String,ArrayList<String>> edgeMatrix)
	{
		
		ArrayList<String> pathSofar = pathWithEdge.get(tempGraphSoFar);
		
		ArrayList<String> parentEdge	= new ArrayList<String>();
		parentEdge = edgeMatrix.get(tempIncoming);
		String temp = parentEdge.toString();
		String[] temp2 = temp.split(",");
		if(temp2.length > 1)
		{
			for(int i = 0; i < temp2.length; i ++)
			{
				String newPath =tempGraphSoFar+"  "+tempIncoming+"-"+i;
				HashSet<String> tm = new HashSet<String>();
				tm.addAll(pathSofar);
				tm.add(temp2[i]);
				ArrayList<String> tm1 = new ArrayList<String>();
				tm1.addAll(tm);
			//	addEdge(tempGraphSoFar,tempIncoming,pathWithEdge,edgeMatrix);
				
				pathWithEdge.put(newPath,tm1);
				
			}
		}
		
		
		
		
	}
	
}
