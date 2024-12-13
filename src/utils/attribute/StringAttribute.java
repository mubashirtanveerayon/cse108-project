package utils.attribute;

import utils.enums.AttributeKey;

public class StringAttribute extends Attribute {
    public StringAttribute(String content, AttributeKey key){
        super(content,key);
    }

    public boolean equals(Object o){
        if(o==null)return false;
        if(o==this)return true;
        if(!(o instanceof StringAttribute))return false;
        StringAttribute sa = (StringAttribute)o;
        return getKey().equals(sa.getKey()) && toString().equalsIgnoreCase(sa.toString());
    }

    public int hashCode(){
        return toString().toLowerCase().hashCode();
    }


    public String toString(){
        return (String)getContent();
    }
}
