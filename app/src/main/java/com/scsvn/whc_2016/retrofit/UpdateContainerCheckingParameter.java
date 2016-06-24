package com.scsvn.whc_2016.retrofit;

/**
 * Created by tranxuanloc on 1/27/2016.
 */
public class UpdateContainerCheckingParameter {
    public int checkingID;
    public String userName;
    public String temperatureShow;
    public String temperatureSetup;
    public boolean running;
    public boolean thawing;
    public boolean stop;
    public boolean error;
    public boolean productEmpty;
    public boolean seal;
    public boolean lock;
    public String remark;
    public String dockNumber;
    public boolean noOperation;
    public boolean Electricity;

    public UpdateContainerCheckingParameter(int checkingID, String dockNumber, boolean error, boolean lock, boolean noOperation, boolean productEmpty, String remark, boolean running, boolean seal, boolean stop, String temperatureSetup, String temperatureShow, boolean thawing, boolean Electricity, String userName) {
        this.checkingID = checkingID;
        this.dockNumber = dockNumber;
        this.error = error;
        this.lock = lock;
        this.noOperation = noOperation;
        this.productEmpty = productEmpty;
        this.remark = remark;
        this.running = running;
        this.seal = seal;
        this.stop = stop;
        this.temperatureSetup = temperatureSetup;
        this.temperatureShow = temperatureShow;
        this.thawing = thawing;
        this.userName = userName;
        this.Electricity = Electricity;
    }
}
