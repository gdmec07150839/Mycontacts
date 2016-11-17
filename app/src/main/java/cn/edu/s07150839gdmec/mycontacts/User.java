package cn.edu.s07150839gdmec.mycontacts;

/**
 * Created by WU on 2016/11/17.
 */
public class User  {
    public final static String NAME="name";
    public final static String MOBILE="mobile";
    public final static String DANWEI="danwei";
    public static final String QQ="qq";
    public static final  String ADDRESS="address";

    private String name;
    private String mobile;
    private String danwei;
    private String qq;
    private String address;
    private int id_DB=-1;

    public String getName() { return name; }
    public String getMobile() { return mobile; }
    public String getDanwei() { return danwei; }
    public String getQq() {return qq; }
    public String getAddress() {return address; }
    public int getId_DB() {return id_DB; }
    public void setName(String name){this.name=name; }
    public void setMobile(String mobile) {this.mobile=mobile;}
    public void setDanwei(String danwei){this.danwei=danwei;}
    public void setQq(String qq){this.qq=qq;}
    public void setAddress(String address){this.address=address;}
    public void setId_DB(int id_DB){this.id_DB=id_DB;}
}