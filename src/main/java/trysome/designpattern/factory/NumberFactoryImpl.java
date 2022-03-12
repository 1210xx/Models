package trysome.designpattern.factory;

import java.math.BigDecimal;

public class NumberFactoryImpl implements NumberFactory{
    public Number parse(String s){
        return new BigDecimal(s);
    }
}
