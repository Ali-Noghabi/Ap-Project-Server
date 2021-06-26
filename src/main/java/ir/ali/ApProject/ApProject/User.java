package ir.ali.ApProject.ApProject;

import java.util.ArrayList;

public class User {
    public String email;
    public String name;
    public String password;
    public String phoneNum;
    public static int loginCounter=0;

    public User(String email,String name,String password,String phoneNum){
        this.email=email;
        this.name=name;
        this.password=password;
        this.phoneNum=phoneNum;
        loginCounter=loginCounter++;
    }



}
