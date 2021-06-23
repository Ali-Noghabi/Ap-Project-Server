import java.util.ArrayList;

public class User {
    private String email;
    private String name;
    private String password;
    private String phoneNum;
    private static int loginCounter=0;
    private ArrayList<Integer> soldItems;
    private ArrayList<Integer>  broughtItems;
    private ArrayList<Integer>  availableItems; ///products that are available to sell

    public User(String email,String name,String password,String phoneNum){
        this.email=email;
        this.name=name;
        this.password=password;
        this.phoneNum=phoneNum;
        loginCounter=loginCounter++;
        soldItems=new ArrayList<>();
        broughtItems=new ArrayList<>();
        availableItems=new ArrayList<>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public static int getLoginCounter() {
        return loginCounter;
    }

    private void addSoldItems(Integer soldId){
        soldItems.add(soldId);
    }

    private void deleteSoldItems(Integer soldId){
        soldItems.remove(soldId);
    }
    private void addBroughtItems(Integer broughtId){
        broughtItems.add(broughtId);
    }
    private void deleteBroughtItems(Integer broughtId){
        broughtItems.remove(broughtId);
    }
    private void addAvailableItems(Integer availableId){
        availableItems.add(availableId);
    }
    private void deleteAvailableItems(Integer availableId){
        availableItems.remove(availableId);
    }

    public ArrayList<Integer> getSoldItems() {
        return soldItems;
    }

    public ArrayList<Integer> getBroughtItems() {
        return broughtItems;
    }

    public ArrayList<Integer> getAvailableItems() {
        return availableItems;
    }
}
