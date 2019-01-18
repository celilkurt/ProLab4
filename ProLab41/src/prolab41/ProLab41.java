

package prolab41;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;
//Bir kategoriden kullanıcı silme veya listeleme seçeneklerinde kategorinin alt kategorileri için de aynı işlemler gerçekleştirilecek.
//yani American kategorisi için aleq-wilson kullanıcısının silinmesi istenirse ve pizza American kategorisinin alt kategorisi ise pizzada da aleq-wilson silinecek.


public class ProLab41 {

    static Tree root = new Tree();
    static ArrayList<String> lines = new ArrayList<>();
    static Scanner scan = new Scanner(System.in);
    
    public static void main(String[] args) {
        
        fileReader();//Dosyayı satır satır okuyarak 'lines' dizisine atar.
        createTree();//Dosyadan okunan rezervasyonlarla ağacı oluşturur.
        heapifyUser2(root);//User ağacını heapify eder.operations();//Kullanıcı çıkana kadar istediği işlemleri yapar.
        operations();
    
    }
    
    static void operations(){
        
        while(true){
            
            System.out.println("\n1) Kategori ile ilgili işlemleri:\n"
                    + "Kategori bilgilerini yazdırmak için 1'e"
                    + "Yeni kategori eklemek için 2'e,\n"
                    + "Kategori silmek için 3'e,\n\n"
                    + "2) Kullanıcı ile ilgili işlemler:\n"
                    + "Kullanıcı eklemek için 4'e,\n"
                    + "Bir kategori altındaki bütün kullanıcıları silmek için 5'e,\n"
                    + "Bir kategorinin altındaki bir kullanıcıyı silmek için 6'ya,\n"
                    + "Bir kullanıcının bütün rezervasyonlarını silmek için 7'ye,\n\n"
                    + "3) Sorgu ve listeleme işlemleri:\n"
                    + "Bir kullanıcının rezervasyonlarının dahil olduğu kategorileri yazdırmak için 8'e,\n"
                    + "Bir kategoriden rezervasyon yaptıran bütün kullanıcıları listelemek için 9'a, \n"
                    + "Aynı yerde rezervasyon yaptırmış bütün kullanıcıları listelemek için 10'a, \n"
                    + "Bir kullanıcıya ait bütün rezervasyonları yazdırmak için 11'e basiniz\n");
            
            int choice = scan.nextInt();
            
            switch(choice){
                case 1://+
                    catOpe1();
                    break;
                case 2://+
                    catOpe2();//Belirtilen kategorinin altına yeni bir kategori ekler.
                    break;
                case 3://+
                    catOpe3();//Kategori silmek için
                    break;
                case 4://+
                    userOpe1();//Belirtilen kategorinin altınada yeni bir kişi oluşturur.
                    break;
                case 5://Bir kategori altındaki bütün kullanıcıları silmek için
                    userOpe21();//+
                    break;
                case 6://Bir kategorinin altındaki bir kullanıcıyı silmek için
                    userOpe22();
                    break;
                case 7://Bir kullanıcının bütün rezervasyonlarını silmek için
                    userOpe23();
                    break;
                case 8://+
                    printOpe1();//Bir kullanıcın bulunduğu kategorileri yazdırır.
                    break;
                case 9://+
                    printOpe2();//Bir kategorideki bütün kullanıcıları yazdırır.
                    break;
                case 10://+
                    printOpeRez();//Bir locationId için alınan bütün rezervasyonları yazdırır.
                    break;
                case 11://+
                    printOpeRez2();//Bir kullanıcının bütün kategorilerdeki rezervasyonlarını yazdırır.
                    break;
                
            }
            
            
            if(choice==0)
                break;
        }
        
    }
    
    static void fileReader(){
        
        String str;
            
        try{
            //File file = new File("rezervasyon.txt");
            FileInputStream fStream = new FileInputStream("rezervasyon.txt");
            DataInputStream dStream = new DataInputStream(fStream);
            BufferedReader bReader = new BufferedReader(new InputStreamReader(dStream));
            
            while((str = bReader.readLine()) != null){
                //Satırları arrayliste kaydeder.
                lines.add(str);
                //System.out.println(lines.get(lines.size()-1));
            }
            
            dStream.close();
        }catch(Exception e){
            System.err.println("Hata : " + e.getMessage());
        }
        
    }
    
    static void createTree(){
        
        root.data = "rezervasyon";
        
        for(int i = 0;i < lines.size(); i++){
            
            String[] bilesenler = lines.get(i).split(",");//7 bileşenden oluşuyor
            
            //<kullanıcı id><,><yer id><,><zaman><,><enlem><,><boylam><,><şehir><,><kategori:altkategori>
            
            String[] categories = bilesenler[6].split(":");
            
            insertCat(categories);
            
            if(findCat(categories[categories.length-1],root).childUser==null){
                //kategorinin ilk userı atanmadıysa
                User tempUser= new User();
                tempUser.userId = bilesenler[0];
                tempUser.catName = categories[categories.length-1];
                tempUser.num = 1;//bulunduğu ağacın kaçıncı user'ı olduğunu gösterir.
                findCat(categories[categories.length-1],root).childUser = tempUser;
                //System.out.println(categories[categories.length-1] + " childUserı atandı " + findCat(categories[categories.length-1],root).childUser.userId);
                
                //findUser(bilesenler[0],findCat(categories[categories.length-1],root).childUser).numOfRez++;
                //userin rezervasyon sayısı güncellenir.
                
                insertRez(bilesenler,categories);
                
                //System.out.println(findUser(bilesenler[0],findCat(categories[categories.length-1],root).childUser).userId + " userinin "
                //        + findUser(bilesenler[0],findCat(categories[categories.length-1],root).childUser).numOfRez + ". rezervasyon eklendi.");
            
            }else{
                //kategorinin ilk userı atandıysa
                
                User tempUser = new User();
                tempUser.userId = bilesenler[0];
                tempUser.catName = categories[categories.length-1];
                
                if(findUser(bilesenler[0],findCat(categories[categories.length-1],root).childUser)==null){
                    tempUser.num = findCat(categories[categories.length-1],root).getNumOfUser()+1;
                    //daha önce bu user atanmadıysa ata.
                    insertUser(findCat(categories[categories.length-1],root).childUser,tempUser);
                    //System.out.println(findUser(bilesenler[0],findCat(categories[categories.length-1],root).childUser).userId + " kullanıcısı eklendi.");
                    
                }else{
                    //System.out.println("User daha önce atanmıştır.");
                }
                    
                    
                //findUser(bilesenler[0],findCat(categories[categories.length-1],root).childUser).numOfRez++;
                //userin rezervasyon sayısı güncellenir.
                
                insertRez(bilesenler,categories);
                
                //System.out.println(findUser(bilesenler[0],findCat(categories[categories.length-1],root).childUser).userId + " userinin "
                //        + findUser(bilesenler[0],findCat(categories[categories.length-1],root).childUser).numOfRez + ". rezervasyon eklendi.");

            }
            
            
            
            
        }
        
    }
    
    static void insertRez(String[] bilesenler,String[] categories){
        
        if(findUser(bilesenler[0],findCat(categories[categories.length-1],root).childUser).firstRez==null){
                //İlk rezervasyon eklenmemişse
                Rezervation tempRez = new Rezervation();
                tempRez.city = bilesenler[5];//şehir 
                tempRez.locationId = bilesenler[1];// yer id
                tempRez.date = bilesenler[2];
                tempRez.latitude = bilesenler[3];
                tempRez.longitude = bilesenler[4];
                
                findUser(bilesenler[0],findCat(categories[categories.length-1],root).childUser).firstRez = tempRez;
                //System.out.println(findUser(bilesenler[0],findCat(categories[categories.length-1],root).childUser).userId + 
                //        " userının " + findCat(categories[categories.length-1],root).data + " kategorisindeki İLK rezervasyonu eklendi.");
            }else{
                //ilk rezervasyon eklenmişse
                int sayac = 2;
                Rezervation tempRez = findUser(bilesenler[0],findCat(categories[categories.length-1],root).childUser).firstRez;
                
                while(tempRez.next!=null){
                    tempRez = tempRez.next;
                    sayac++;
                }       
                Rezervation tempRez2 = new Rezervation();
                tempRez2.city = bilesenler[5];//şehir 
                tempRez2.locationId =  bilesenler[1]; //yer id
                tempRez2.date = bilesenler[2];
                tempRez2.latitude = bilesenler[3];
                tempRez2.longitude = bilesenler[4];
                tempRez2.pre = tempRez;
                tempRez.next = tempRez2;
                //System.out.println(findUser(bilesenler[0],findCat(categories[categories.length-1],root).childUser).userId + 
                //        " userının " + findCat(categories[categories.length-1],root).data + " kategorisindeki " + sayac + ". rezervasyonu eklendi.");
                
            }
    }//Rezervasyon ekler.
    
    static void insertUser(User root,User tempUser){
        
        if(root.left==null){
            
            //System.out.println("rootun soluna atama yapıldı.");
            if(root.depth==0) 
                root.depth = 1;
            
            tempUser.parent= root;
            root.left=tempUser;
            
        }else if(root.right==null){
            
            //System.out.println("rootun sağına atama yapıldı.");
            if(root.depth==0) 
                root.depth = 1;
            
            tempUser.parent= root;
            root.right=tempUser;
            
        }else{
            
                if(root.parent!=null){
                    //önce rootun ağacı tamammı sonra parentın agacı tamam mı
                    if(root.maxNode(root.getDepth())>root.getNumOfNode()){
                        
                        root = root.left;
                        //System.out.println("rootun solu yeni root.");
                        insertUser(root,tempUser);
                        
                    }else if(root.parent.maxNode(root.parent.getDepth())>root.parent.getNumOfNode()){
                        if(root.parent.right!=root){
                           
                            root = root.parent.right;
                            //System.out.println("rootun parentının sağı yeni root.");
                            insertUser(root,tempUser); 
                            
                        }else{
                            
                            root = root.left;
                            //System.out.println("rootun solu yeni root.");
                            insertUser(root,tempUser);
                            
                        }
                        
                        
                    }else{
                        
                        root = root.left;
                        //System.out.println("rootun solu yeni root.");
                        insertUser(root,tempUser);
                        
                    }
                       
                    

                }else{
                    //Bir sonraki sol yeni parent.
                    root= root.left;
                    //System.out.println("rootun  solu yeni root.");
                    insertUser(root,tempUser);
                    
                }
                
            
            
            
        }
        
        
    }//tempUser kullanıcısını root'tan altına max-heap ağacına uygun şekilde ekler.
        
    static void insertCat(String[] categories){
                
            Tree tempTree = new Tree();
            tempTree.data = categories[0];
            tempTree.catWay = "root";
            tempTree.depth = 0;
            
            if(findCat(tempTree.data,root)==null){
                //kategori yoksa.
                root.childList.add(tempTree);
                
                //System.out.println("Eklenen kategori : " + root.childList.get(root.childList.size()-1).data);
                
            }else{
                //System.out.println("Bu kategori daha önce atanmıştır. " + tempTree.data);
            }
                
            
            
            //alt kategoriler
            for(int j = 1;j < categories.length;j++){
                
                tempTree = new Tree();
                tempTree.data = categories[j];
                tempTree.catWay = findCat(categories[j-1],root).catWay + ":" +  categories[j-1];
                tempTree.depth = j;
                
                if(findCat(tempTree.data,root)==null){
                    //atama yapılsın
                    
                    findCat(categories[j-1],root).childList.add(tempTree);
                    //System.out.println(tempTree.data+ "alt kategorisi eklendi.");
                    //System.out.println("alt kategorinin parentı : " + findCat(categories[j-1],root).data );
                    
                }else{
                    //System.out.println(tempTree.data + " Alt kategori daha önce atanmıştır.");
                }
                    
                
                
            }
            
            }//categories dizisindeki kategorileri ar arda ekler.
    
    static void printCat(Tree tempRoot){
        
        if(!tempRoot.data.equals("rezervasyon"))
            System.out.println("kategori : " + tempRoot.data);
        
        for(int i = 0;i < tempRoot.childList.size();i++){
                 
            printCat(tempRoot.childList.get(i));
            
        }
        
    }//tempRoot'tan itibaren kategorileri yazdırmak için
    
    static Tree findCat(String data,Tree tempRoot){
        //Genelde rezervasyon düğümünden itibaren arama yapılır.
        
        if(!tempRoot.childList.isEmpty()){
            
            for(int i = 0;i < tempRoot.childList.size();i++){
                    
                if(findCat(data,tempRoot.childList.get(i))!=null)
                    return findCat(data,tempRoot.childList.get(i));
                    
                    
            }
            
            if(tempRoot.data.equals(data))    
                return tempRoot;
            else
                return null;
                        
        }else if(tempRoot.data.equals(data))
            return tempRoot;
        else 
            return null;
        
    }//data adında kategoriyi tempRoot'tan itibaren arar.
    
    static User findUser(String userId,User tempUser){//rootUser'dan itibaren userId'li kullanıcı varsa adresini döndürür.
    
    if(tempUser != null){
        if(tempUser.left!=null){
            
            if(findUser(userId,tempUser.left)!=null){
                //null'a eşit olmaması böyle bir kullanıcının var olduğunu gösterir.
                return findUser(userId,tempUser.left);
                
            }else{
                
                if(tempUser.right!=null&&findUser(userId,tempUser.right)!=null){
                    
                    return findUser(userId,tempUser.right);
                    
                }else{
                    
                    if(tempUser.userId.equals(userId))
                        return tempUser;
                    else
                        return null;
                }
                    
            }
                
        }else{
            
            if(tempUser.userId.equals(userId))
                return tempUser;
            else
                return null;
           
        }
    }else
        return null;
        
        
    }//Kullanıcı adresini bulmak için
    
    static void catOpe1(){//Bir kategorinin bilgilerini yazdırmak için
        
        String str;
        
        System.out.println("Kategoriyi giriniz:");
        scan.nextLine();
        str = scan.nextLine();
        if(findCat(str,root)!=null){
            
            System.out.println("Kategori adı: " + findCat(str,root).data);
            System.out.println("Kategori derinliği: " + findCat(str,root).getDepth());
            System.out.println("Kategori yolu: " + findCat(str,root).catWay);
            System.out.println("Alt kategori sayısı: " + findCat(str,root).getNumOfChild());
            System.out.println("Kullanıcı sayısı: " + findCat(str,root).getNumOfUser());
            System.out.println("Rezervasyon sayısı: " + findCat(str,root).getNumOfRez());
            
        }else
            System.out.println("Kategori bulunamadı.");
        
    }//Bir kategorinin bilgilerini yazdırmak için
    
    static void catOpe2(){//Kategori eklemek için
        
        System.out.println("Eklemek istediğiniz kategoriyi giriniz:");
        scan.nextLine();
        String str = scan.nextLine();
        if(findCat(str,root)==null){
            
            System.out.println("Altına ekleme yapmak istediğiniz kategoriyi giriniz.");
            String str2 = scan.nextLine();
            if(findCat(str2,root)!=null){
                printCat(root);
                Tree tempTree = new Tree();
                tempTree.data = str;
                tempTree.catWay = str2;
                findCat(str2,root).childList.add(tempTree);
                printCat(findCat(str2,root));//eklenen kategorinin parent'ından itibaren.
                
            }else
                System.out.println("Girdiğiniz kategori bulunamadı.");
            
        }else
            System.out.println("Eklemek istediğiniz kategori zaten var.");
        
        System.out.println(findCat(str,root).data);
        System.out.println(findCat(str,root).catWay);
        System.out.println(findCat(str,root).depth);
        System.out.println(findCat(str,root).getNumOfUser());
        System.out.println(findCat(str,root).getNumOfRez());
        System.out.println(findCat(str,root).getNumOfChild());
        
    }//Kategori eklemek için

    static void catOpe3(){//kategori silme
        String str;
        
        System.out.println("Silmek istediğiniz kategoriyi giriniz:");
        scan.nextLine();
        str = scan.nextLine();
        
        if(findCat(str,root)!=null){
            String categories[] = findCat(str,root).catWay.split(":");
            Tree parent = findCat(categories[categories.length-1],root);
                    
            for(int i = 0; i < findCat(str,root).childList.size();i++)//silmek istenen kategori food ise food'un childlarını parentının childelarına atar.
                parent.childList.add(findCat(str,root).childList.get(i));
                        
            if(parent.childList.remove(findCat(str,root))){
                System.out.println(str + " kategorisi silindi.");
                printCat(parent);//Silinmek istenen kategorinin parentının childlerı tekrar yazdırılıyor.
            }
        
        }else
            System.out.println("Girdiğiniz kategori bulunamadı.");
        
        
    }//kategori silme
    
    static void userOpe1(){
        
        System.out.println("Ekleme yapmak istediğiniz kategoriyi giriniz.");
        scan.nextLine();
        String str = scan.nextLine();
        
        if(findCat(str,root)!=null){
            
            System.out.println("Eklemek istediğiniz kullanıcı adını giriniz.(örnek: aleq-wilson)");
            String str2 = scan.nextLine();
            if(findUser(str2,findCat(str,root).childUser)==null&&findCat(str,root).childUser==null){
                
                User tempUser = new User();
                tempUser.userId = str2;
                findCat(str,root).childUser = tempUser;
                System.out.println(findCat(str,root).data + " kategorisine " + findUser(str2,findCat(str,root).childUser).userId + " useri atandı." );
            
            }else if(findUser(str2,findCat(str,root).childUser)==null){
                
                User tempUser = new User();
                tempUser.userId = str2;
                insertUser(findCat(str,root).childUser,tempUser);
                System.out.println(findCat(str,root).data + " kategorisine " + findUser(str2,findCat(str,root).childUser).userId + " useri atandı." );
                
                
            }else
                System.out.println("Eklemek istediğiniz kullanıcı varolduğundan bu işlem gerçekleştirilemiyor.");
            
            
        }else
            System.out.println("Girdiğiniz kategori bulunamadı.");
        
    }//Bir kategori altına bir kullanıcı eklemek için
    
    static void userOpe21(){//kategori altındaki bütün kullanıcıları silme
        
        System.out.println("Hangi kategori için işlem yapmak istiyorsunuz.");
        scan.nextLine();
        String str = scan.nextLine();
        
        if(findCat(str,root)!=null){
            
            if(findCat(str,root).childUser!=null){
                
                findCat(str,root).childUser = null;
                System.out.println("Girdiğiniz kategorinin altındaki bütün kullanıcılar silindi.");
                
            }else
                System.out.println("Girdiğiniz kategorinin altında hiç kullanıcı yok.");
            
            
        }else
            System.out.println("Girdiğiniz kategori bulunamadı.");
        
    }//kategori altındaki bütün kullanıcıları silme
    
    static void userOpe22(){//Belirtilen kategorinin altındaki bir kullanıcının silinmesi.
        
        System.out.println("İşlem yapmak istediğiniz kategoriyi giriniz.");
        scan.nextLine();
        String str = scan.nextLine();
        
        if(findCat(str,root)!=null){
            
            System.out.println("Kullanıcı id'sini giriniz.(örnek: aleq-wilson)");
            String str2 = scan.nextLine();
            if(findUser(str2,findCat(str,root).childUser)!=null){
                
                changeUser(findUser(str2,findCat(str,root).childUser),findLastUser(findCat(str,root).childUser,findCat(str,root)));
                //silinmesi istenen ve son eleman yer değiştirdi
           
                System.out.println(findUser(str2,findCat(str,root).childUser).userId + " kullanıcısı " + findCat(str,root).data + " kategorisinin altından silindi.");

                if(findUser(str2,findCat(str,root).childUser)==findUser(str2,findCat(str,root).childUser).parent.right)
                    //silinmesi istenen user, parentının sağında ise
                    findUser(str2,findCat(str,root).childUser).parent.right=null;
                else if(findUser(str2,findCat(str,root).childUser)==findUser(str2,findCat(str,root).childUser).parent.left)
                    //silinmesi istenen user, parentının solunda ise
                    findUser(str2,findCat(str,root).childUser).parent.left=null;
                else//parentının sağı veya soluna eşit değilse childuser'dır.
                    findCat(str,root).childUser=null;
            
                if(findCat(str,root).childUser!=null)//heapify edilebilmesi için kategorinin altında en az bir user bulunmalı.
                    heapifyUser(findCat(str,root).childUser);
            }else
                System.out.println(findCat(str,root).data + " kategorisinin altında aradığınız user bulunamadı.");
            
            
        }else
            System.out.println("Girdiğiniz kategori bulunamadı.");
        
        
        
    }//Belirtilen kategorinin altındaki bir kullanıcının silinmesi.
    
    static void userOpe23(){//Belirtilen kullanıcı bütün kategorilerden sillinir.
        
        System.out.println("İşlem yapmak istediğiniz kullanıcıyı giriniz.");
        scan.nextLine();
        String str = scan.nextLine();
        userOpe231(str,root);//rezervasyon düğümünden itibaren kullanıcı aranır ve silinir.
        
    }//Belirtilen kullanıcı bütün kategorilerden sillinir.
    
    static void userOpe231(String userId, Tree tempRoot){//Bir kullanıcıyı bütün kategorilerin altında arar ve siler.
        
        if(!tempRoot.childList.isEmpty()){
            
            for(int i = 0;i < tempRoot.childList.size();i++){
                    
                userOpe231(userId,tempRoot.childList.get(i));
                    
            }
            
            if(tempRoot.childUser!=null){
                
                if(findUser(userId,tempRoot.childUser)!=null){//userlardan biri aranan user ise
                       
                       changeUser(findUser(userId,tempRoot.childUser),findLastUser(tempRoot.childUser,tempRoot));
                       if(findUser(userId,tempRoot.childUser).parent!=null){//parent'ı varsa
                           
                            if(findUser(userId,tempRoot.childUser)==findUser(userId,tempRoot.childUser).parent.right)
                                //user parentının sağında ise parentının sağı null olarak değiştiriliyor.
                                findUser(userId,tempRoot.childUser).parent.right=null;
                            else if(findUser(userId,tempRoot.childUser)==findUser(userId,tempRoot.childUser).parent.left)
                                //user parentının solunda ise parentının solu null olarak değiştiriliyor.
                                findUser(userId,tempRoot.childUser).parent.left=null;
                       }else//user parentının sağı ve soluna eşit değilse user child user'dır.
                           tempRoot.childUser=null;
                        
                       System.out.println("Silme işlemi gerçekletirildi.");
                   }
                
            }
                
        
        }else{
            
               if(tempRoot.childUser!=null){//kategoriye bağlı en az bir user var
                   
                   if(findUser(userId,tempRoot.childUser)!=null){//userlardan biri aranan user ise
                       
                       changeUser(findUser(userId,tempRoot.childUser),findLastUser(tempRoot.childUser,tempRoot));
                       if(findUser(userId,tempRoot.childUser).parent!=null){//parent'ı varsa
                           
                            if(findUser(userId,tempRoot.childUser)==findUser(userId,tempRoot.childUser).parent.right)
                                //user parentının sağında ise parentının sağı null olarak değiştiriliyor.
                                findUser(userId,tempRoot.childUser).parent.right=null;
                            else if(findUser(userId,tempRoot.childUser)==findUser(userId,tempRoot.childUser).parent.left)
                                //user parentının solunda ise parentının solu null olarak değiştiriliyor.
                                findUser(userId,tempRoot.childUser).parent.left=null;
                       }else//user parentının sağı ve soluna eşit değilse user child user'dır.
                           tempRoot.childUser=null;
                        
                       System.out.println("Silme işlemi gerçekletirildi.");
                   }
                   
               }
                
                
               
            }
        
        
    }//Bir kullanıcıyı bütün kategorilerin altında arar ve siler.
    
    static User findLastUser(User tempRoot, Tree category){//Bir user ağacındaki en son elemanı bulur.
        
        if(tempRoot.left!=null){
            
           if(findLastUser(tempRoot.left,category)!=null)
                return findLastUser(tempRoot.left,category);
           
           if(tempRoot.right!=null){
               
                if(findLastUser(tempRoot.right,category)!=null)
                    return findLastUser(tempRoot.right,category);

           }
               
        }
        
        if(tempRoot.num==category.getNumOfUser())
            return tempRoot;
        else
            return null;
           
        
        
    }//Bir user ağacındaki en son elemanı bulur.
    
    static void printOpe1(){//kullanıcıya göre kategori yazdırma
        
        System.out.println("hangi kullanıcı için işlem yapmak istersiniz.(örnek:aleq-wilson)");
        scan.nextLine();
        String str = scan.nextLine();
        
        printOpe12(str,root);
        
    }//kullanıcıya göre kategori yazdırma
    
    static void printOpe12(String userId ,Tree tempRoot){//Bir userın bulunduğu kategorileri yazdırır.
        
        if(!tempRoot.childList.isEmpty()){
            
            for(int i = 0;i < tempRoot.childList.size();i++){
                    
                printOpe12(userId,tempRoot.childList.get(i));
                    
            }
            
            if(tempRoot.childUser!=null&&findUser(userId,tempRoot.childUser)!=null)
                //Eğer kategorinin en az bir userı varsa ve userlardan bir aranan usere eşitse
                System.out.println(tempRoot.data + " kategorisinde bulundu.");
        
        }else{
            
               if(tempRoot.childUser!=null&&findUser(userId,tempRoot.childUser)!=null)
                //Eğer kategorinin en az bir userı varsa ve userlardan bir aranan usere eşitse
                System.out.println(tempRoot.data + " kategorisinde bulundu.");
               
            }
                
                    
        
        
    }

    static void printOpe2(){//kategoriye göre kullanıcı listeleme. Kategori altından bulunan bütün kullanıcıları yazdırır.
        
        System.out.println("Hangi kategori için işlem yapmak istersiniz.");
        scan.nextLine();
        String str = scan.nextLine();
        
        if(findCat(str,root)!=null){
            
            if(findCat(str,root).childUser==null){
                System.out.println("Kategorinin hiç kullanıcısı yok.");    
            }else
                printUser(findCat(str,root).childUser);
        }else
            System.out.println("Girdiğiniz kategori bulunamadı.");
        
    }//kategoriye göre kullanıcı listeleme. Kategori altından bulunan bütün kullanıcıları yazdırır.
    
    static void printUser(User rootUser){//rootUser dan itibaren kullanıcıları yazdırır.
        
        if(rootUser.left!=null){
            
            printUser(rootUser.left);
            System.out.println(rootUser.userId);
            
            if(rootUser.right!=null)
                printUser(rootUser.right);
               
        }else
            System.out.println(rootUser.userId);
           
        
        
    }//rootUser'ın  kullanıcılarını yazdırır.
    
    static void printOpeRez(){//Belli bir yerde rezervasyon yaptırmış bütün kullanıcıların userId'si listelenir.
        
        System.out.println("İşlem yapmak istediğiniz yerId'sini giriniz.");
        scan.nextLine();
        String str = scan.nextLine();
        printOpeRez2(str,root);
       
    }
    
    static void printOpeRez2(String locationId, Tree tempRoot){//Her bir kategori için printRez3 fonksiyonunu çalıştırır.
        
         if(!tempRoot.childList.isEmpty()){
            
            for(int i = 0;i < tempRoot.childList.size();i++){
                
                printOpeRez2(locationId,tempRoot.childList.get(i));
                    
            }
            
            //System.out.println(tempRoot.data + " kategorisi için sorgulanıyor.");
            printOpeRez3(locationId,tempRoot.childUser);
        
        }else{
             //System.out.println(tempRoot.data + " kategorisi için sorgulanıyor.");
             printOpeRez3(locationId,tempRoot.childUser);
             
         }
            
               
            
        
    }//Her bir kategori için printRez3 fonksiyonunu çalıştırır.
    
    static void printOpeRez3(String locationId, User tempRoot){//tempTree'nin her bir userı için findRez fonksiyonunu çalştırır.
        
        if(tempRoot!=null){
            
            if(tempRoot.left!=null){
            
                printOpeRez3(locationId,tempRoot.left);
                
                //System.out.println(tempRoot.userId + " kullanıcısı için bakılıyor.");
                findRez(locationId,tempRoot.firstRez,tempRoot);
                
                if(tempRoot.right!=null)
                    printOpeRez3(locationId,tempRoot.right);
               
            }else{
                //System.out.println(tempRoot.userId + " kullanıcısı için bakılıyor.");
                findRez(locationId,tempRoot.firstRez,tempRoot);
            }
                
            
        }
         
          
    }//tempTree'nin her bir userı için findRez fonksiyonunu çalştırır.
    
    static void findRez(String locationId, Rezervation tempRoot,User parent){//tempRoot rezervasyonundan itibaren  rezervasyonların locationId'sini karşılaştırır.
    
        if(tempRoot!=null){
        
            if(tempRoot.next!=null){

                findRez(locationId,tempRoot.next,parent);

                if(tempRoot.locationId.equals(locationId))
                    System.out.println(parent.userId + " kullanıcısının rezervasyonu bulundu.");
                

            }else{

                if(tempRoot.locationId.equals(locationId))
                    System.out.println(parent.userId + " kullanıcısının rezervasyonu bulundu.");
                
            }

        }
        
        
        
        
        
    }
    
    static void printOpeRez2(){//Bir kullanıcının bütün kategorilerdeki randuvuları yazdırır.
        
        System.out.println("İşlem yapmak istediğiniz kullanıcıyı giriniz.");
        scan.nextLine();
        String str = scan.nextLine();
        printOpeRez22(str,root);
    }//Bir kullanıcının bütün kategorilerdeki randuvuları yazdırır.
    
    static void printOpeRez22(String userId, Tree tempRoot){//Bütün kategoriler için printOpeRez23'u çalıştırır.
        
        if(!tempRoot.childList.isEmpty()){
            
            for(int i = 0;i < tempRoot.childList.size();i++)
                printOpeRez22(userId,tempRoot.childList.get(i));
                    
            printOpeRez23(userId,tempRoot.childUser);
        
        }else
            printOpeRez23(userId,tempRoot.childUser);
             
         
        
    }//Bütün kategoriler için printOpeRez23'u çalıştırır.
    
    static void printOpeRez23(String userId, User tempRoot){//tempRoot'tan itibaren userları dolaşır. userId ye eşit olan varsa printRez fonksiyonunu çalıştırır.
        
        if(tempRoot!=null){
            
            if(tempRoot.left!=null){
            
                printOpeRez23(userId,tempRoot.left);
                
                //System.out.println(tempRoot.userId + " kullanıcısı için bakılıyor.");
                if(tempRoot.userId.equals(userId))
                    printRez(tempRoot.firstRez);//rezervasyonları yazdır.
                
                if(tempRoot.right!=null)
                    printOpeRez23(userId,tempRoot.right);
               
            }else{
                //System.out.println(tempRoot.userId + " kullanıcısı için bakılıyor.");
                if(tempRoot.userId.equals(userId))
                    printRez(tempRoot.firstRez);
            }
               
        }
        
    }//tempRoot'tan itibaren userları dolaşır. userId ye eşit olan varsa printRez fonksiyonunu çalıştırır.
    
    static void printRez(Rezervation tempRoot){//tempRoot'tan itibaren rezervasyonları yazdırır.
        
        while(tempRoot!=null){
            
            System.out.println("\n\nyerId : " + tempRoot.locationId +
                                "\nenlem : " + tempRoot.latitude + 
                                "\nboylam : " + tempRoot.longitude + 
                                "\nşehir : " + tempRoot.city + 
                                "\ntarih : " + tempRoot.date );
            
            tempRoot = tempRoot.next;
        }
        
    }//tempRoot'tan itibaren rezervasyonları yazdırır.
    
    static void heapifyUser(User tempRoot){//Rezervasyon sayısı büyük olan userı üste taşır.
        
        if(tempRoot.left!=null){
            //System.out.println("tempRoot'un solundan itibaren tekrar başlatılıyor.");
            heapifyUser(tempRoot.left);
        }else{
            //System.out.println("Ağacın solunda eleman yok.");
        }
            
            
        
        if(tempRoot.parent!=null&&tempRoot.parent.right!=null&&tempRoot!=tempRoot.parent.right){
        //tempRoot parent'ı var ve parent'ının sağı boş değil ve tempRoot parentın sağındaki eleman değilse
            
            if(tempRoot.getNumOfRez()>tempRoot.parent.getNumOfRez()||tempRoot.parent.right.getNumOfRez()>tempRoot.parent.getNumOfRez()){
                //tempRoot'un rez sayısı veya parent'ının sağındaki elemanın rez sayısı parentından büyükse
                
                if(tempRoot.getNumOfRez()>tempRoot.parent.right.getNumOfRez()){
                    //tempRoot ile parentın yeri değişir(soldaki ile parent)
                    changeUser(tempRoot,tempRoot.parent);
                    
                    /*System.out.println("1.userId : " + tempRoot.userId + 
                            "1.numOfRez : " + tempRoot.getNumOfRez());
                    System.out.println("2.userId : " + tempRoot.parent.userId + 
                            "2.numOfRez : " + tempRoot.parent.getNumOfRez());
                    
                    changeUser(tempRoot,tempRoot.parent);
                    
                    System.out.println("1.userId : " + tempRoot.userId + 
                            "1.numOfRez : " + tempRoot.getNumOfRez());
                    System.out.println("2.userId : " + tempRoot.parent.userId + 
                            "2.numOfRez : " + tempRoot.parent.getNumOfRez());*/
                    
                }else{
                    //tempRoot'un parent'ının sağındaki eleman ile parent yer değiştirir.
                    
                    changeUser(tempRoot.parent.right,tempRoot.parent);
                    /*
                    System.out.println("1.userId : " + tempRoot.parent.right.userId + 
                            "1.numOfRez : " + tempRoot.parent.right.getNumOfRez());
                    
                    System.out.println("2.userId : " + tempRoot.parent.userId + 
                            "2.numOfRez : " + tempRoot.parent.getNumOfRez());
                    
                    changeUser(tempRoot.parent.right,tempRoot.parent);
                    
                    System.out.println("1.userId : " + tempRoot.parent.right.userId + 
                            "1.numOfRez : " + tempRoot.parent.right.getNumOfRez());
                    
                    System.out.println("2.userId : " + tempRoot.parent.userId + 
                            "2.numOfRez : " + tempRoot.parent.getNumOfRez());*/
                    
                }
                //System.out.println("Ağacın kökünden itibaren tekrar başlatılıyor.");
                //heapifyUser(root,root);//Ağacın gereçek kökünden ititbaren tekrar çalıştırıyor.
                
            }
                //System.out.println("tempRoot'un parentının sağından itibaren tekrar başlatılıyor.");
                heapifyUser(tempRoot.parent.right);
            
            
            
            
        }else if(tempRoot.parent!=null&&tempRoot!=tempRoot.parent.right){
            
            //System.out.println("Ağacın sağında eleman olmadığından.");

            if(tempRoot.getNumOfRez()>tempRoot.parent.getNumOfRez()){
                //tempRoot ile parentın yeri değişir(soldaki ile parent)
                
                changeUser(tempRoot,tempRoot.parent);
                
                /*System.out.println("1.userId : " + tempRoot.userId + 
                        "1.numOfRez : " + tempRoot.getNumOfRez());
                System.out.println("2.userId : " + tempRoot.parent.userId + 
                        "2.numOfRez : " + tempRoot.parent.getNumOfRez());
                    
                changeUser(tempRoot,tempRoot.parent);
                    
                System.out.println("1.userId : " + tempRoot.userId + 
                        "1.numOfRez : " + tempRoot.getNumOfRez());
                System.out.println("2.userId : " + tempRoot.parent.userId + 
                        "2.numOfRez : " + tempRoot.parent.getNumOfRez());*/
                
            }
            
        }
        
        
    }//Rezervasyon sayısı büyük olan userı üste taşır.
    
    static void heapifyUser2(Tree tempRoot){//Her kategori için heapifyUser fonksiyonunu çalıştırır.
        
        if(!tempRoot.childList.isEmpty()){
            
            for(int i = 0;i < tempRoot.childList.size();i++){
                    
                heapifyUser2(tempRoot.childList.get(i));
                //System.out.println(tempRoot.childList.get(i).data + " kategorisi için bakılıyor.");   
                    
            }
            
            if(tempRoot.childUser!=null)
                heapifyUser(tempRoot.childUser);  
               
                 
            
                
                        
        }else if(tempRoot.childUser!=null)
            heapifyUser(tempRoot.childUser);
            
        
    }//Her kategori için heapifyUser fonksiyonunu çalıştırır.
    
    static void changeUser(User user1, User user2){
        
        String tempUserId = user1.userId;
        int tempNumOfRez = user1.getNumOfRez();
        Rezervation tempFirstRez = user1.firstRez;
        
        user1.userId = user2.userId;
        user1.numOfRez = user2.numOfRez;
        user1.firstRez = user2.firstRez;
        
        user2.userId = tempUserId;
        user2.numOfRez = tempNumOfRez;
        user2.firstRez = tempFirstRez;
        
    }
    
}