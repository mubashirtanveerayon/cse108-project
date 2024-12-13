package utils.attribute;

import utils.enums.AttributeKey;

import java.io.Serializable;
import java.util.Objects;

public abstract class Attribute implements Serializable {
    private final Object content;
    private final int hashCode;
    private final AttributeKey key;
    public Attribute(Object o, AttributeKey key){
        hashCode = Objects.hash(key,o);
        this.key = key;
        this.content = o;
    }

    public Object getContent(){
        return content;
    }

    public int hashCode(){
        return hashCode;
    }
    public boolean equals(Object o){
        if(o == null)return false;
        if(o==this)return true;
        if(!(o instanceof Attribute))return false;
        return hashCode() == o.hashCode();
    }

    public static boolean hasSameKeys(Attribute t1, Attribute t2){
        return t1.key==t2.key;
    }

    public AttributeKey getKey(){
        return key;
    }

}
