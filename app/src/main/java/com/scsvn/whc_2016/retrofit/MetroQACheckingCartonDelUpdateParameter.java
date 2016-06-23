package com.scsvn.whc_2016.retrofit;

/**
 * Created by tranxuanloc on 4/25/2016.
 */
public class MetroQACheckingCartonDelUpdateParameter {
    private int ReceivingCartonCheckingID;
    private int Flag;
    private float CheckingWeighPerCarton;
    private float DamageWeighPerCarton;
    private String UserName;

    public MetroQACheckingCartonDelUpdateParameter(float checkingWeighPerCarton, float damageWeighPerCarton, int flag, int receivingCartonCheckingID, String userName) {
        CheckingWeighPerCarton = checkingWeighPerCarton;
        DamageWeighPerCarton = damageWeighPerCarton;
        Flag = flag;
        ReceivingCartonCheckingID = receivingCartonCheckingID;
        UserName = userName;
    }

    public float getDamageWeighPerCarton() {
        return DamageWeighPerCarton;
    }

    public float getCheckingWeighPerCarton() {
        return CheckingWeighPerCarton;
    }
}
