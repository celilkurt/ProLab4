
package prolab41;

import java.util.ArrayList;

public class Tree {
    
    String data;//örnek american
    ArrayList<Tree> childList = new ArrayList<>();
    User childUser=null;
    private int numOfChild;//düğümün child sayısı
    private int numOfUser=0;//toplam user sayısını döndürür.
    private int numOfRez=0;
    String catWay;
    int depth=1;
    
    public int getNumOfRez(){
        
        numOfRez=0;
        
        if(childUser!=null)
            numOfRez += getNumOfRez2(childUser);
        
        return numOfRez;
        
    }
    
    private int getNumOfRez2(User tempRoot){
        
        if(tempRoot.right!=null&&tempRoot.left!=null){
            
            return tempRoot.getNumOfRez() + getNumOfRez2(tempRoot.right)+getNumOfRez2(tempRoot.left);
            
        }else if(tempRoot.left!=null){
            
            return tempRoot.getNumOfRez() + getNumOfRez2(tempRoot.left);
        }
        
        return tempRoot.getNumOfRez();
        
    }
    
    public int getNumOfUser(){
        
        if(childUser!=null)
            return childUser.getNumOfNode();//getNumOfNode childUser'dan itibaren agaçta kaç düğüm olduğunu döndürür.
        else
            return 0;
        
    }
    
    public int getNumOfChild(){
        
        numOfChild = childList.size();
        
        return numOfChild;
    }
    
    public int getDepth(){
        
        String[] str = catWay.split(":");
        depth = str.length;
        
        return depth;
    }
    
    
    
}