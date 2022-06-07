package common;

public enum CommandList {
    HELP("help"),
    INFO("info"),
    SHOW("show"),
    ADD("add"),
    UPDATE("update"),
    REMOVE_BY_ID("remove_by_id"),
    CLEAR("clear"),
    EXECUTE("execute_script"),
    EXIT("exit"),
    ADD_IF_MAX("add_if_max"),
    REMOVE_GREATER("remove_greater"),
    HISTORY("history"),
    MAX_BY_REAL_HERO("max_by_real_hero"),
    FILTER("filter_contains_name"),
    SIGN_IN("sign_in"),
    SIGN_UP("sign_up"),
    PRINT_DESCENDING("print_descending");


    private final String name;
    private CommandList(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }
}
