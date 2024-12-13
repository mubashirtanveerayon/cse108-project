package utils.exceptions;

import utils.attribute.Attribute;

public class AttributeKeyMismatchException extends RuntimeException{
    public AttributeKeyMismatchException(Attribute t1, Attribute t2){
        super("Comparison between attributes having different keys: "+t1.getKey()+", "+t2.getKey());
    }
}
