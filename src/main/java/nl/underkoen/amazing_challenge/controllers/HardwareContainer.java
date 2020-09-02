package nl.underkoen.amazing_challenge.controllers;

/**
 * @author Under_Koen
 */
public class HardwareContainer {
    private final Context context;
    private boolean eyeEnabled = false;
    private boolean colorEyeEnabled = false;
    private boolean compassEnabled = false;

    public HardwareContainer(Context context) {
        this.context = context;
    }

    public void enableEye() {
        context.spendMoney(context.priceTable.getBuyEye());
        this.eyeEnabled = true;
    }

    public void enableColorEye() {
        context.spendMoney(context.priceTable.getBuyColorEye());
        this.colorEyeEnabled = true;
    }

    public void enableCompass() {
        context.spendMoney(context.priceTable.getBuyCompass());
        this.compassEnabled = true;
    }

    public boolean isEyeEnabled() {
        return eyeEnabled;
    }

    public boolean isColorEyeEnabled() {
        return colorEyeEnabled;
    }

    public boolean isCompassEnabled() {
        return compassEnabled;
    }
}
