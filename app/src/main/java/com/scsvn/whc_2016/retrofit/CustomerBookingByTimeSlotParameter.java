package com.scsvn.whc_2016.retrofit;

/**
 * Created by xuanloc on 12/12/2016.
 */
public class CustomerBookingByTimeSlotParameter {
    public String BookingDate;
    public Byte WarehouseID;

    public CustomerBookingByTimeSlotParameter(String bookingDate, Byte warehouseID) {
        BookingDate = bookingDate;
        WarehouseID = warehouseID;
    }
}
