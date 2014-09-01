import java.util.ArrayList;
import java.util.HashMap;

public class BKTree
{
	private Node Root;
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
		ArrayList<String> rtn = new ArrayList<String>();
		RecursiveSearch(Root,rtn,word,d);
		return rtn;
	}
	private void RecursiveSearch(Node node,ArrayList<String> rtn,String word,int d)
	{
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
	public static void main(String argv[])
	{
		BKTree bktree=new BKTree();
		bktree.Add("book");
		bktree.Add("books");
		bktree.Add("cake");
		bktree.Add("boo");
		bktree.Add("cape");
		bktree.Add("boon");
		bktree.Add("cook");
		bktree.Add("cart");
		System.out.println(bktree.Search("caqe", 2));
	}	
}