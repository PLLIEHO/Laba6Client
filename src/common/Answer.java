package common;

import java.io.Serializable;

public class Answer implements Serializable {
    private static final long serialVersionUID = 7489018723428732092L;
    private Object str;
    public Answer(Object answer){
        this.str = answer;
    }

    public Object getStr() {
        return str;
    }
}
