import java.util.*;
import java.io.*;

public class BKTree
{
	private Node Root;
	public int hit;
	public HashMap<String,Integer> wordDictionary=new HashMap<String,Integer>();
    public void ConstructBKTree(String dictionaryName)throws FileNotFoundException,IOException
	{
		BufferedReader br=new BufferedReader(new FileReader(dictionaryName));
		String line;
		while((line=br.readLine())!=null)
		{
			String words[]=line.split(",");
			Add(words[0]);
			wordDictionary.put(words[0],1);
		}
	}
	public void Add(String word)
	{
		if(Root==null)
		{
			Root=new Node(word);
			return;
		}
		Node curNode = Root;
		DamerauLevenshteinAlgorithm dl = new DamerauLevenshteinAlgorithm(1, 1, 1, 1);
		int dist = dl.execute(curNode.Word,word);
		while(curNode.ContainsKey(dist))
		{
			if(dist==0)
			{
				return;
			}
			curNode=curNode.Children.get(dist);
			dist=dl.execute(curNode.Word, word);
		}
		curNode.AddChild(dist,word);
	}
	public ArrayList<String> Search(String word,int d)
	{
		this.hit=0;
		ArrayList<String> rtn = new ArrayList<String>();
		RecursiveSearch(Root,rtn,word,d);
		return rtn;
	}
	private void RecursiveSearch(Node node,ArrayList<String> rtn,String word,int d)
	{
		this.hit+=1;
		DamerauLevenshteinAlgorithm dl = new DamerauLevenshteinAlgorithm(1, 1, 1, 1);
		int curDist = dl.execute(node.Word,word);
		int minDist=curDist-d;
		int maxDist=curDist+d;
		if(curDist<=d)
		{
			rtn.add(node.Word);
		}
		if(node.Children==null)
		{
			return;
		}
		for(int key:node.Children.keySet())
		{
			if((key>=minDist)&&(key<=maxDist))
			{
				RecursiveSearch(node.Children.get(key),rtn,word,d);
			}
		}
	}
}
class Node
{
	String Word;
	HashMap<Integer,Node> Children;
    public Node(String word)
    {
        this.Word = word;
    }
    public void AddChild(int key,String word)
    {
    	if(this.Children==null)
    	{
    		Children = new HashMap<Integer,Node>();
    	}
    	this.Children.put(key,new Node(word));
    }
    public boolean ContainsKey(int key)
    {
        if(Children==null)
        {
        	return false;
        }
        else
        {
           if(Children.get(key)!=null)
           {
        	   return true;
           }
           else
           {
        	   return false;
           }
        }
    }
       
}
class test
{
	public static void main(String argv[]) throws FileNotFoundException,IOException
	{
		BKTree bktree=new BKTree();
		String typo= "fiscal";
		bktree.ConstructBKTree("cleaned_counts.txt");
		final long BeginTime=System.currentTimeMillis();
		System.out.println(bktree.Search(typo, 3));
		final long EndTime=System.currentTimeMillis();
		System.out.println(EndTime-BeginTime);
		System.out.println(bktree.hit);
		GetMetaPhone getmetaphone = new GetMetaPhone();
		getmetaphone.buildPhoneticIndex("cleaned_counts.txt");
		final long BeginPhoneticTime=System.currentTimeMillis();
		ArrayList<String> phoneticCandidates=getmetaphone.phoneticIndex.get(GetMetaPhone.encode(typo));
		final long EndPhoneticTime=System.currentTimeMillis();
		System.out.println(EndPhoneticTime-BeginPhoneticTime);
		System.out.println(phoneticCandidates);
	}
}