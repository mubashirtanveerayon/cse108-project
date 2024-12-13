package utils.attribute;

import utils.entity.Entity;
import utils.enums.AttributeKey;
import utils.exceptions.AttributeKeyMismatchException;

import java.io.Serializable;

public class RangeAttribute extends NumericAttribute {

    // private final NumericAttribute min,max;
    private final float min,max;
    public RangeAttribute(float v1, float v2, AttributeKey key){
        super(v1,key);
        // NumericAttribute other = new NumericAttribute(v2,key);
        if(v1 > v2){
            max = v1;
            min = v2;
        }else{
            max = v2;
            min = v1;
        }
    }


    public boolean inRange(Attribute attribute){

        if(!attribute.getKey().equals(getKey()))throw new AttributeKeyMismatchException(this,attribute);
        if(!(attribute instanceof NumericAttribute))return false;
        float value =(Float) attribute.getContent();
        return value>=min && value<=max;
    }
}
