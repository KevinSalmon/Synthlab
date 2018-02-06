package module;

public enum PortType {
    OUTPUT ("output"),
    INPUT ("input"),
    AM("am");


    private String type = "";

    PortType(String type){
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
