package utils.entity;

import utils.enums.AttributeKey;
import utils.attribute.NumericAttribute;
import utils.attribute.Attribute;
import utils.exceptions.AttributeKeyMismatchException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Entity implements Serializable {


    // protected final List<Attribute> attributes;

    protected final Map<AttributeKey,Attribute> attributes;

    private final int hashCode;
    public Entity(Attribute... attributes) {
        this.attributes = new HashMap<>();
        for(Attribute attribute : attributes)
            this.attributes.put(attribute.getKey(), attribute);
        this.hashCode = attributes[0].hashCode();
    }

    public final Entity addAttribute(Attribute attribute){
         attributes.put(attribute.getKey(), attribute);
         return this;
    }


    public boolean attributeInRange(NumericAttribute low, NumericAttribute high)throws AttributeKeyMismatchException {
        if(!Attribute.hasSameKeys(low,high))throw new AttributeKeyMismatchException(low,high);
        Attribute target=attributes.get(low.getKey());

        if(target==null)return false;
        double targetValue = (Double) target.getContent();
        return targetValue>=(Double)low.getContent() && targetValue<=(Double)high.getContent();
    }

    public boolean equals(Object o){
        if(o==this)return true;
        if(!(o instanceof Entity))return false;
        return hashCode()==o.hashCode();
    }

    public int hashCode(){
        return this.hashCode;
    }


    public boolean hasAttribute(Attribute attribute){
//        int count=0;
//        for (Attribute attribute : attributes) {
//            if (this.attributes.containsKey(attribute.getKey()))count++;
//        }
//        return count == attributes.length;

        return attributes.containsKey(attribute.getKey());
    }

    public abstract boolean containsAttribute(Attribute ...attributes);

    public Attribute getAttribute(AttributeKey key){

        return attributes.get(key);
    }

    public int getAttributeCount(){
        return attributes.size();
    }

    public abstract String encode();

    public void removeAttribute(AttributeKey attributeKey){
        attributes.remove(attributeKey);

    }
}
