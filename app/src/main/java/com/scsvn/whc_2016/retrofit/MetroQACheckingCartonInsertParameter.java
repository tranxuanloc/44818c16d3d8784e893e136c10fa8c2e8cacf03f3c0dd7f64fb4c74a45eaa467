package com.scsvn.whc_2016.retrofit;

/**
 * Created by tranxuanloc on 4/20/2016.
 */
public class MetroQACheckingCartonInsertParameter {
    private int ReceivingOrderDetailID;
    private float CheckingWeighPerCarton;
    private float DamageWeighPerCarton;
    private String UserName;

    public MetroQACheckingCartonInsertParameter(float checkingWeighPerCarton, float damageWeighPerCarton, int receivingOrderDetailID, String userName) {
        CheckingWeighPerCarton = checkingWeighPerCarton;
        DamageWeighPerCarton = damageWeighPerCarton;
        ReceivingOrderDetailID = receivingOrderDetailID;
        UserName = userName;
    }

    public float getCheckingWeighPerCarton() {
        return CheckingWeighPerCarton;
    }

    public float getDamageWeighPerCarton() {
        return DamageWeighPerCarton;
    }

    public int getReceivingOrderDetailID() {
        return ReceivingOrderDetailID;
    }

    public String getUserName() {
        return UserName;
    }
}
