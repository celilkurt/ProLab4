
package prolab41;



public class User {
  
    String userId;
    String catName;
    int num=0;
    int numOfRez=0;
    int depth=0;//derinlik
    Rezervation firstRez=null;
    User left=null;
    User right=null;
    User parent=null;
    
    
    public int maxNode(int t){
        
        return (int)Math.pow(2,t+1)-1;
        
    }
    
    public int getNumOfRez(){
        
        Rezervation tempRez=firstRez;
        numOfRez=0;
            
        while(tempRez!=null){
            tempRez=tempRez.next;
            numOfRez++;
        }    
        
        return numOfRez;
       
    }
    
    public int getNumOfNode(){
        
        if(left!=null&&right!=null){
            
            return 1+left.getNumOfNode()+right.getNumOfNode();
            
        }else if(left!=null){
            
            return 1+left.getNumOfNode();
        }
        return 1;
    }
    
    public int getDepth(){
        
        if(left==null)
            return depth;
        else
            return depth+left.getDepth();
        
    }
    
}



