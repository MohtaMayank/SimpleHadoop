package cmu.cs.distsystems.hw2.prototypes.example;

public interface ZipCodeRList // extends YourRemote or whatever
{
    public String find(String city);
    public ZipCodeRList add(String city, String zipcode);
    public ZipCodeRList next();   
}
