package com.hldj.hmyg.bean;

public class OrderGood {
private String name;
private boolean bo;
public String getName() {
    return name;
}
public void setName(String name) {
    this.name = name;
}
public boolean getBo() {
    return bo;
}
public void setBo(boolean bo) {
    this.bo = bo;
}
@Override
public String toString() {
    return "Good [name=" + name + ", bo=" + bo + "]";
}
public OrderGood(String name, boolean bo) {
    super();
    this.name = name;
    this.bo = bo;
}
public OrderGood() {
    super();
}

}



