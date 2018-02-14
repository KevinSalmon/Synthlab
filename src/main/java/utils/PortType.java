package utils;

public enum PortType {
    OUTPUT ("output"),
    OUTPUT1 ("output1"),
    OUTPUT2 ("output2"),
    OUTPUT3 ("output3"),
    GATE ("outputgate"),
    INPUT ("input"),
    INPUT1 ("input1"),
    INPUT2 ("input2"),
    INPUT3 ("input3"),
    INPUT4 ("input4"),
    INPUTAM ("inputam"),
    INPUTFM ("inputfm");


    private String type = "";

    PortType(String type){
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
