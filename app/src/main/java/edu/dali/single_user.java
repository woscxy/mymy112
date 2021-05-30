package edu.dali;

public class single_user {
    private String username;
    private String infomation;
    public single_user(){

    }
    public single_user(String username,String infomation){
        this.username=username;
        this.infomation=infomation;
    }
    public void setUsername(String username){
       this.username= username;
    }
    public void setInfomation(String infomation){
        this.infomation= infomation;
    }
    public String getUsername(){
        return username;
    }
    public String getInfomation(){
        return infomation;
    }
}
