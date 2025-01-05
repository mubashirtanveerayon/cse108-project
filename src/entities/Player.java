package entities;

import utils.attribute.Attribute;
import utils.attribute.NumericAttribute;
import utils.attribute.RangeAttribute;
import utils.attribute.StringAttribute;
import utils.entity.Entity;
import utils.enums.AttributeKey;

import java.io.*;
import java.util.ArrayList;

public class Player extends Entity implements Serializable{
    private boolean forSale=false;

    public Player(String name, String country,int age,float height, String clubName, String position, int number, long salary) {
        super(new StringAttribute(name, AttributeKey.NAME),new StringAttribute(country,AttributeKey.COUNTRY),new StringAttribute(clubName,AttributeKey.CLUB),new StringAttribute(position,AttributeKey.POSITION),new NumericAttribute(number,AttributeKey.NUMBER),new NumericAttribute(salary,AttributeKey.SALARY),new NumericAttribute(age,AttributeKey.AGE),new NumericAttribute(height,AttributeKey.HEIGHT));
    }

    public Player(String name, String country,int age,float height, String clubName, String position,long salary) {
        super(new StringAttribute(name, AttributeKey.NAME), new StringAttribute(country, AttributeKey.COUNTRY), new StringAttribute(clubName, AttributeKey.CLUB), new StringAttribute(position, AttributeKey.POSITION), new NumericAttribute(salary, AttributeKey.SALARY), new NumericAttribute(age, AttributeKey.AGE), new NumericAttribute(height, AttributeKey.HEIGHT));
    }

    public Player(String ...info) {
        super(new StringAttribute(info[0], AttributeKey.NAME), new StringAttribute(info[1], AttributeKey.COUNTRY), new NumericAttribute(Float.parseFloat(info[2]), AttributeKey.AGE), new NumericAttribute(Float.parseFloat(info[3]), AttributeKey.HEIGHT), new StringAttribute(info[4], AttributeKey.CLUB), new StringAttribute(info[5], AttributeKey.POSITION));
        if (info[6].isEmpty()) {
            addAttribute(new NumericAttribute(Float.parseFloat(info[7]), AttributeKey.SALARY));
        } else {
            addAttribute(new NumericAttribute(Float.parseFloat(info[6]), AttributeKey.NUMBER)).addAttribute(new NumericAttribute(Float.parseFloat(info[7]), AttributeKey.SALARY));
        }
    }

    public void toggleForSale(){
        forSale=!forSale;
    }

    public boolean isForSale(){
        return forSale;
    }

    public String getName() {
        Attribute attr = getAttribute(AttributeKey.NAME);
        if (attr==null)return null;
        return attr.getContent().toString();
    }

    public String getCountry() {
        Attribute attr = getAttribute(AttributeKey.COUNTRY);
        if (attr==null)return null;
        return attr.getContent().toString();
    }

    public String getPosition() {
        Attribute attr = getAttribute(AttributeKey.POSITION);
        if (attr==null)return null;
        return attr.getContent().toString();
    }

    public long getSalary() {
        Attribute attr = getAttribute(AttributeKey.SALARY);
        if(attr == null)return -1;
        return (long)(float)attr.getContent();
    }

    public int getAge() {
        Attribute attr = getAttribute(AttributeKey.AGE);
        if(attr == null)return -1;
        return (int)(float)attr.getContent();
    }

    public float getHeight() {
        Attribute attr = getAttribute(AttributeKey.HEIGHT);
        if(attr == null)return -1;
        return (float)attr.getContent();
    }



    public String getClub() {
        Attribute attr = getAttribute(AttributeKey.CLUB);
        if (attr==null)return null;
        return attr.getContent().toString();
    }

    public int getNumber() {
        Attribute attr = getAttribute(AttributeKey.NUMBER);
        if(attr == null)return -1;
        return (int)((float)attr.getContent());
    }


    public String toString(){
        return encode();
    }

    @Override
    public boolean containsAttribute(Attribute... attributes) {

        int uniqueAttributes = 0;
        int matches = 0;
        ArrayList<AttributeKey>checked = new ArrayList<>();

        for(Attribute attr : attributes) {
            if(!checked.contains(attr.getKey())){

                checked.add(attr.getKey());
                uniqueAttributes++;
            }
            if( this.attributes.containsKey(attr.getKey()) ){
                if( attr instanceof RangeAttribute ){
                    if( ((RangeAttribute)attr).inRange(this.attributes.get(attr.getKey())) )matches++;
                }else{
                    if( attr.equals(this.attributes.get(attr.getKey())) )
                        if(attr.getKey() !=AttributeKey.CLUB || ! forSale)matches++;
                }
            }else return false;
        }
        return uniqueAttributes==matches;

//        for(Attribute attr : attributes) {
//            if(this.attributes.containsKey(attr.getKey())){
//                if(attr instanceof RangeAttribute){
//                    if( !((RangeAttribute)attr).inRange(this.attributes.get(attr.getKey())) )return false;
//                }else {
//                    if(attr.equals(this.attributes.get(attr.getKey()))){
//
//                        if(attr.getKey()==AttributeKey.CLUB && forSale)return false;
//                    }else return false;
//
//                }
//
//            }else return false;
//
//        }


//        for(Attribute attribute:attributes)
//
//            if(!this.attributes.containsKey(attribute.getKey()) || !this.attributes.get(attribute.getKey()).equals(attribute))return false;
//            else if(attribute.getKey() == AttributeKey.CLUB && forSale)return false;
//        return true;

    }

    @Override
    public String encode() {
        if(getNumber()>=0)return "\tName: "+getName()+"\n\tCountry: "+getCountry()+"\n\tClub: "+getClub()+"\n\tPosition: "+getPosition()+"\n\tHeight: "+getHeight()+"\n\tSalary: "+getSalary()+"\n\tNumber: "+getNumber()+"\n\tAge: "+getAge();

        return "\tName: "+getName()+"\n\tCountry: "+getCountry()+"\n\tClub: "+getClub()+"\n\tPosition: "+getPosition()+"\n\tHeight: "+getHeight()+"\n\tSalary: "+getSalary()+"\n\tAge: "+getAge();
    }



}
