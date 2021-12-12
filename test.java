import java.io.*;
import java.util.*;

public class test{
    private String ipAddress;
    private String dateTime;
    private String request;
    private int requestStatus;
    private int ports;
    private String userAgent;
    
    public static void main(String[] arg){

        // ArrayList for restoring item objects
        ArrayList<test> items=new ArrayList<test>();

        // Read File
        try{
            FileInputStream fstream = new FileInputStream("test_data.log");
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String strLine;

            /* read log line by line */

            while ((strLine = br.readLine()) != null) {
                String current_string = strLine;
                test current_item = new test();
                String remaining = current_item.setipAddress(current_string);
                remaining = current_item.setdateTime(current_string);
                remaining = current_item.setRequest(remaining);
                remaining = current_item.setRequestStatus(remaining);
                remaining = current_item.setPorts(remaining);
                current_item.setUserAgent(remaining);
                items.add(current_item);
            }

            fstream.close();

        } catch (Exception e) {

            System.err.println("Error: " + e.getMessage());

            }
        
        // One sample unit test
        assert items.size()==23:" Not valid";  
        System.out.println("Number of Objects are "+items.size());  
        
        // Check the number of unique IP addresses
        HashSet<String> item_set = new HashSet<>();
        for (test item: items){
            item_set.add(item.ipAddress);
        }
        int num_unique_ip = item_set.size();
        System.out.println("The number of unique IP addresses are: " + num_unique_ip);

        // Top 3 most visited URLs
        ArrayList<String> top3URL = getTop3URL(items);
        System.out.println("TOP 3 most visited urls are: " + top3URL);

        // Top 3 most active IP addresses
        ArrayList<String> top3IP = getTop3IP(items);
        System.out.println("TOP 3 most visited urls are: " + top3IP);






    }

    //Method for getting top 3 URL
    public static ArrayList<String> getTop3URL(ArrayList<test> items){
        HashMap<String,Integer> count_map = new HashMap<>();
        int start_index = 0;
        int end_index = 0;
        for (test item:items){
            String current_string = item.request;
            for (int i = 0; i < current_string.length();i++){
                if (current_string.charAt(i)=='/' && (current_string.charAt(i-1)!='/' && current_string.charAt(i+1)!='/')){
                    start_index = i+1;
                    break;
                }
            }
            for (int i = start_index; i < current_string.length();i++){
                if (current_string.charAt(i)=='/'){
                    end_index = i;
                    break;
                }
            }
            String current_site = current_string.substring(start_index,end_index);
            count_map.put(current_site, count_map.getOrDefault(current_site, 1)+1);
        }
        LinkedHashMap<String, Integer> reverseSortedMap = new LinkedHashMap<>();
        count_map.entrySet()
        .stream()
        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())) 
        .forEachOrdered(x -> reverseSortedMap.put(x.getKey(), x.getValue()));
        int count = 0;
        ArrayList<String> top3 = new ArrayList<>();
        for (String key : reverseSortedMap.keySet()) {
            count++;
            top3.add(key);
            if (count == 3) break;
        }
        return top3;
    }

    //Method for getting top 3 most activate IP address
    public static ArrayList<String> getTop3IP(ArrayList<test> items){
        HashMap<String,Integer> count_map = new HashMap<>();
        for (test item:items){
            count_map.put(item.ipAddress, count_map.getOrDefault(item.ipAddress, 1)+1);
            
        }
        LinkedHashMap<String, Integer> reverseSortedMap = new LinkedHashMap<>();
        count_map.entrySet()
        .stream()
        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())) 
        .forEachOrdered(x -> reverseSortedMap.put(x.getKey(), x.getValue()));
        ArrayList<String> top3 = new ArrayList<>();
        int count = 0;
        for (String key : reverseSortedMap.keySet()) {
            count++;
            top3.add(key);
            if (count == 3) break;
        }
        return top3;
    }




    // Getters and Setters for all the attributes
    public String getipAddress(){
        return this.ipAddress;
      }
      
    public String setipAddress(String current_string){
        //max digits 12 + 3 dots - 1 because it's index
        int check_point = 14;
        String ip = "";
        while(check_point > 0){
            if(!Character.isDigit(current_string.charAt(check_point))){
                check_point--;
            }
            else{
                ip += current_string.substring(0, check_point+1);
                break;
            }
        }
        this.ipAddress = ip;
        return current_string.substring(check_point+1, current_string.length());
    }


    public String getdateTime(){
        return this.dateTime;
      }
      
    public String setdateTime(String current_string){
        int start_index = 0;
        int end_index = 0;
        for (int i = 0; i < current_string.length();i++){
            if (current_string.charAt(i)=='[') start_index = i+1;
            if (current_string.charAt(i)==']'){
                end_index = i;
                break;
            }
        }
        this.dateTime = current_string.substring(start_index, end_index);
        return current_string.substring(end_index+1, current_string.length());
    }

    public String getRequest(){
        return this.request;
      }
      
    public String setRequest(String current_string){
        int count = 2;
        int start_index = 0;
        int end_index = 0;
        for (int i = 0 ; i < current_string.length();i++){
            
            if (current_string.charAt(i)=='"'){
                if (count == 2){
                    start_index = i+1;
                    count--;
                }
                else if (count == 1){
                    end_index = i;
                    count--;
                }
            }
            if (count < 1) break;
        }
        this.request = current_string.substring(start_index, end_index);
        return current_string.substring(end_index+1, current_string.length());
    }

    public int getRequestStatus(){
        return this.requestStatus;
      }
      
    public String setRequestStatus(String current_string){
        int start_index = 0;
        for (int i = 0 ; i < current_string.length();i++){
            if (Character.isDigit(current_string.charAt(i))){
                start_index = i;
                break;
            }
        }
        this.requestStatus = Integer.parseInt(current_string.substring(start_index, start_index+3));
        return current_string.substring(start_index+3, current_string.length());
    }
    public int getPorts(){
        return this.ports;
      }
      
    public String setPorts(String current_string){
        int start_index = 0;
        for (int i = 0 ; i < current_string.length();i++){
            if (Character.isDigit(current_string.charAt(i))){
                start_index = i;
                break;
            }
        }
        this.ports = Integer.parseInt(current_string.substring(start_index, start_index+4));
        return current_string.substring(start_index+3, current_string.length());
    }
    public String getUserAgent(){
        return this.userAgent;
      }
      
    public void setUserAgent(String current_string){
        int count = 0;
        int start_index = 0;
        for (int i = 0 ; i < current_string.length();i++){
            
            if (current_string.charAt(i)=='"'){
                count++;
            }
            if (count == 3){
                start_index = i+1;
                break;
            }
        }
        this.userAgent = current_string.substring(start_index, current_string.length()-1);
    }
    

}