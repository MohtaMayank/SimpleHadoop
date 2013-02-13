package cmu.cs.distsystems.hw2.prototypes.example;

public class ZipCodeList
{
    String city;
    String ZipCode;
    ZipCodeList next;

    public ZipCodeList(String c, String z, ZipCodeList n)
    {
	city=c;
	ZipCode=z;
	next=n;
    }
}
