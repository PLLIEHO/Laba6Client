package common;

import java.io.Serializable;

public class Request implements Serializable {
    private static final long serialVersionUID = -1069157352143096370L;
    private CommandList command;
    private Pack argument;
    public Request(CommandList command, Pack argument){
        this.command = command;
        this.argument = argument;
    }
}
