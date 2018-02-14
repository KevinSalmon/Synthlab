package sauvegarde;

public class SavedOscillo extends SavedModule{

    private Integer value;
    private Integer spinnerAxisXValue;
    private Integer spinnerAxisYValue;

    public SavedOscillo() {
    }

    public SavedOscillo(double xPos, double yPos, Integer value, Integer spinnerAxisXValue, Integer spinnerAxisYValue) {
        super(xPos, yPos);
        this.value = value;
        this.spinnerAxisXValue = spinnerAxisXValue;
        this.spinnerAxisYValue = spinnerAxisYValue;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Integer getSpinnerAxisXValue() {
        return spinnerAxisXValue;
    }

    public void setSpinnerAxisXValue(Integer spinnerAxisXValue) {
        this.spinnerAxisXValue = spinnerAxisXValue;
    }

    public Integer getSpinnerAxisYValue() {
        return spinnerAxisYValue;
    }

    public void setSpinnerAxisYValue(Integer spinnerAxisYValue) {
        this.spinnerAxisYValue = spinnerAxisYValue;
    }
}
