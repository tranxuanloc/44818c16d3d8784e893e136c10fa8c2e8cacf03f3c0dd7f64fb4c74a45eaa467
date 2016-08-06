package com.scsvn.whc_2016.retrofit;

import com.scsvn.whc_2016.login.LoginInfo;
import com.scsvn.whc_2016.main.VersionInfo;
import com.scsvn.whc_2016.main.chuyenhang.ListLocationInfo;
import com.scsvn.whc_2016.main.chuyenhang.ListPalletIDInfo;
import com.scsvn.whc_2016.main.chuyenhang.LocationInfo;
import com.scsvn.whc_2016.main.chuyenhang.lichsu.StockMovementHistoriesInfo;
import com.scsvn.whc_2016.main.containerandtruckinfor.ContainerAndTruckInfo;
import com.scsvn.whc_2016.main.detailphieu.OrderDetail;
import com.scsvn.whc_2016.main.detailphieu.RequirementInfo;
import com.scsvn.whc_2016.main.detailphieu.chuphinh.AttachmentInfo;
import com.scsvn.whc_2016.main.detailphieu.chuphinh.OrderInfo;
import com.scsvn.whc_2016.main.detailphieu.so_do_day.LoadingReportInfo;
import com.scsvn.whc_2016.main.detailphieu.worker.EmployeeWorkingTonPerHourInfo;
import com.scsvn.whc_2016.main.detailphieu.worker.WorkerInfo;
import com.scsvn.whc_2016.main.equipment.EquipmentInventoryInfo;
import com.scsvn.whc_2016.main.giaonhanhoso.DSCartonCategoriesInfo;
import com.scsvn.whc_2016.main.giaonhanhoso.DSDispatchingOrderDetailsInfo;
import com.scsvn.whc_2016.main.giaonhanhoso.DSDispatchingOrdersInfo;
import com.scsvn.whc_2016.main.giaonhanhoso.cartonreturn.DSReceivingCartonReturnListInfo;
import com.scsvn.whc_2016.main.gps.GPSUserInfo;
import com.scsvn.whc_2016.main.gps.GPSViewByUserParameter;
import com.scsvn.whc_2016.main.gps.listuser.UserInfo;
import com.scsvn.whc_2016.main.kiemcontainer.ContainerInfo;
import com.scsvn.whc_2016.main.kiemcontainer.HistoryCheckingInfo;
import com.scsvn.whc_2016.main.kiemcontainer.detail.ContainerDetailInfo;
import com.scsvn.whc_2016.main.kiemhoso.KiemHoSoInfo;
import com.scsvn.whc_2016.main.kiemqa.metroqacheckingcarton.MetroCartonInfo;
import com.scsvn.whc_2016.main.kiemqa.metroqacheckinglistproducts.QACheckingListProductsInfo;
import com.scsvn.whc_2016.main.kiemqa.metroqacheckingproduct.MetroCheckingProductInfo;
import com.scsvn.whc_2016.main.kiemqa.metroqacheckingsuppliers.MetroQAInfo;
import com.scsvn.whc_2016.main.kiemvitri.LocationCheckingInfo;
import com.scsvn.whc_2016.main.lichlamviec.MyCalendarInfo;
import com.scsvn.whc_2016.main.lichlamviec.WorkingSchedulesEmployeePlanInfo;
import com.scsvn.whc_2016.main.lichlamviec.WorkingSchedulesInfo;
import com.scsvn.whc_2016.main.lichsuravao.EmployeeInOutInfo;
import com.scsvn.whc_2016.main.nangsuat.EmployeePerformanceInfo;
import com.scsvn.whc_2016.main.nhaphoso.ReceivingOrderDetailsInfo;
import com.scsvn.whc_2016.main.nhapngoaigio.EmployeeIDFindInfo;
import com.scsvn.whc_2016.main.nhapngoaigio.OverTimeOrderDetailsInfo;
import com.scsvn.whc_2016.main.nhapngoaigio.detail.OverTimeViewInfo;
import com.scsvn.whc_2016.main.nhapngoaigio.detail.PayRollMonthIDInfo;
import com.scsvn.whc_2016.main.opportunity.Opportunity;
import com.scsvn.whc_2016.main.palletcartonchecking.MovementHistoryInfo;
import com.scsvn.whc_2016.main.palletcartonchecking.PalletCartonInfo;
import com.scsvn.whc_2016.main.phieucuatoi.PhieuCuaToiInfo;
import com.scsvn.whc_2016.main.phieuhomnay.HomNayInfo;
import com.scsvn.whc_2016.main.phieuhomnay.InOutToDayUnFinishInfo;
import com.scsvn.whc_2016.main.phieuhomnay.giaoviec.EmployeeInfo;
import com.scsvn.whc_2016.main.phieuhomnay.giaoviec.GiaoViecInfo;
import com.scsvn.whc_2016.main.services.NotificationInfo;
import com.scsvn.whc_2016.main.technical.assign.AssignWorkInfo;
import com.scsvn.whc_2016.main.technical.schedulejobplan.ScheduleJobPlanInfo;
import com.scsvn.whc_2016.main.tonkho.detailkhachhang.StockOnHandByCustomerInfo;
import com.scsvn.whc_2016.main.tonkho.detailproduct.StockOnHandDetailsInfo;
import com.scsvn.whc_2016.main.tonkho.khachhang.StockOnHandInfo;
import com.scsvn.whc_2016.main.vesinhantoan.CommentInfo;
import com.scsvn.whc_2016.main.vesinhantoan.QHSEInfo;
import com.scsvn.whc_2016.main.vitritrong.FreeLocationDetailsInfo;
import com.scsvn.whc_2016.main.vitritrong.FreeLocationInfo;
import com.squareup.okhttp.RequestBody;

import java.util.List;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;

/**
 * Created by Trần Xuân Lộc on 1/3/2016.
 */
public interface MyRequests {
    @POST("/api/login")
    Call<LoginInfo> signIn(@Body com.scsvn.whc_2016.retrofit.LoginInfo loginInfo);

    @POST("/api/LogOut")
    Call<String> signOut(@Body String userName);

    @POST("/api/inouttoday")
    Call<List<HomNayInfo>> getPhieuHomNay(@Body InOutToDayInfo toDayInfo);

    @POST("/api/inouttodayunfinish")
    Call<List<InOutToDayUnFinishInfo>> getPhieuCustomer(@Body InOutToDayUnFinishParameter unFinishInfo);

    @POST("/api/MyOrders")
    Call<List<PhieuCuaToiInfo>> getPhieuCuaToi(@Body MyOrderInfo myOrderInfo);

    @POST("/api/InOutAvailableForSupervisor")
    Call<List<PhieuCuaToiInfo>> getInOutAvailableForSupervisor(@Body InOutAvailableForSupervisorParameter myOrderInfo);

    @POST("/api/ordersresult")
    Call<List<OrderDetail>> getDetailPhieu(@Body OrdersInfo orderNumber);

    @POST("/api/LoadingReport")
    Call<List<LoadingReportInfo>> getLoadingReport(@Body LoadingReportParameter parameter);

    @POST("/api/LoadingReportInsert")
    Call<String> executeLoadingReportInsert(@Body LoadingReportInsertParameter parameter);

    @POST("/api/LoadingReportUpdate")
    Call<String> executeLoadingReportUpdate(@Body LoadingReportUpdateParameter parameter);

    @POST("/api/customerRequirement")
    Call<List<RequirementInfo>> getRequestPhieu(@Body String orderNumber);

    @POST("/api/employeeworkingassign")
    Call<List<GiaoViecInfo>> getGiaoViec(@Body GiaoViecParameter parameter);

    @POST("/api/EmployeePresent")
    Call<List<EmployeeInfo>> getEmployeeID(@Body EmployeePresentParameter parameter);

    @POST("/api/LocationList")
    Call<List<ListLocationInfo>> getLocation(@Body ListLocationParameter parameter);

    @POST("/api/employeepresent")
    Call<List<ListPalletIDInfo>> getPalletID();

    @POST("/api/employeeworkingdelete")
    Call<String> deleteEmployeeID(@Body DeleteEmployeeIDGiaoViecParameter parameter);

    @POST("/api/employeeworking")
    Call<List<WorkerInfo>> getEmployeeWorking(@Body EmployeeWorkingParameter parameter);

    @POST("/api/employeeworkinginsert")
    Call<String> insertEmployeeWorking(@Body InsertWorkerParameter parameter);

    @POST("/api/EmployeeWorkingTonPerHour")
    Call<List<EmployeeWorkingTonPerHourInfo>> getEmployeeWorkingTonPerHour(@Body String parameter);

    @POST("/api/dispatchingorderdetailupdate")
    Call<String> updateDispatchingOrderDetail(@Body UpdateDispatchingOrderDetailParameter parameter);

    @POST("/api/DispatchingOrderScannedDelete")
    Call<String> executeDispatchingOrderScannedDelete(@Body DispatchingOrderScannedDeleteParameter parameter);

    @Multipart
    @POST("/api/postfilefromandroid")
    Call<String> uploadFile(@Part("file") RequestBody photo, @Part("filename") RequestBody fileName, @Part("description") RequestBody description);

    @POST("/api/orderinfophoto")
    Call<List<OrderInfo>> getOrderInfo(@Body String parameter);

    @POST("/api/attachment")
    Call<String> setAttachment(@Body AttachmentParameter parameter);

    @POST("/api/attachmentview")
    Call<List<AttachmentInfo>> getAttachmentInfo(@Body String orderNumber);

    @POST("/api/containerchecking")
    Call<List<ContainerInfo>> getContainerChecking(@Body int gate);

    @POST("/api/containercheckinghistories")
    Call<List<HistoryCheckingInfo>> getHistoryChecking(@Body int ContInOutID);

    @POST("/api/containercheckingdetails")
    Call<List<ContainerDetailInfo>> getContainerInfo(@Body ContainerCheckingDetailParameter parameter);

    @POST("/api/containercheckingupdate")
    Call<String> updateContainerChecking(@Body UpdateContainerCheckingParameter parameter);

    @POST("/api/containercheckingcompleted")
    Call<String> completedChecking(@Body CompletedCheckingParameter parameter);

    @POST("/api/qhse")
    Call<List<QHSEInfo>> getQHSE(@Body QHSEParemeter parameter);

    @POST("/api/QSHEComment")
    Call<List<CommentInfo>> getQHSEComment(@Body CommentParameter parameter);

    @POST("/api/qhseinsert")
    Call<String> insertQHSE(@Body InsertQHSEParameter parameter);

    @POST("/api/qhse")
    Call<List<AssignWorkInfo>> getAssignWork(@Body AssignWorkParemeter parameter);

    @POST("/api/MMS_ScheduledJobWeekPlan")
    Call<List<ScheduleJobPlanInfo>> getScheduleJobPlanInfo(@Body ScheduleJobPlanParameter parameter);

    @POST("/api/QSHEComment")
    Call<List<com.scsvn.whc_2016.main.technical.assign.CommentInfo>> getAssignWorkComment(@Body CommentParameter parameter);

    @POST("/api/qhseinsert")
    Call<String> insertAssignWork(@Body InsertAssignWorkParameter parameter);

    @POST("/api/QHSEAssignmentInsert")
    Call<String> executeQHSEAssignmentInsert(@Body QHSEAssignmentInsertParameter parameter);

    @POST("/api/changepassword")
    Call<String> changePassword(@Body ChangePasswordParameter parameter);

    @POST("/api/receivingorderdetails")
    Call<List<ReceivingOrderDetailsInfo>> getReceivingOrderDetails(@Body ReceivingOrderDetailParameter parameter);

    @POST("/api/ReceivingLocationUpdate")
    Call<String> updateLocationReceivingOrderDetails(@Body UpdateLocationReceivingOrder parameter);

    @POST("/api/notification")
    Call<String> getNumberQHSENewAssign(@Body NotificationParameter parameter);

    @POST("/api/NotificationMainScreen")
    Call<List<NotificationInfo>> getNotification(@Body NotificationParameter parameter);

    @POST("/api/dsdispatchingorders")
    Call<List<DSDispatchingOrdersInfo>> getDSDispatchingOrders(@Body NotificationParameter parameter);

    @POST("/api/DSReceivingCartonReturnList")
    Call<List<DSReceivingCartonReturnListInfo>> getDSReceivingCartonReturnList(@Body DSCartonCategoriesParameter parameter);

    @POST("/api/DSROCartonReturnAddNew")
    Call<String> executeDSROCartonReturnAddNew(@Body DSROCartonReturnAddNewParameter parameter);

    @POST("/api/dsdispatchingorderdetails")
    Call<List<DSDispatchingOrderDetailsInfo>> getDSDispatchingOrderDetails(@Body DSOrderDetailParameter parameter);

    @POST("/api/DSRODOCartonUpdate")
    Call<String> executeDSRODOCartonUpdate(@Body DSRODOCartonUpdateParameter parameter);

    @POST("/api/SendMailDSNote")
    Call<String> sendMail(@Body SendMailParameter parameter);

    @POST("/api/DSCartonCategories")
    Call<List<DSCartonCategoriesInfo>> getDSCartonCategories(@Body DSCartonCategoriesParameter parameter);

    @POST("/api/DSCreateNewCarton")
    Call<String> executeDSCreateNewCarton(@Body DSCreateNewCartonParameter parameter);

    @POST("/api/DSROCartonDelete")
    Call<String> executeDSROCartonDelete(@Body DSROCartonDeleteParameter parameter);

    @POST("/api/whcversion")
    Call<List<VersionInfo>> getWHCVersion();

    @POST("/api/FreeLocation")
    Call<List<FreeLocationInfo>> getFreeLocation();

    @POST("/api/FreeLocationDetails")
    Call<List<FreeLocationDetailsInfo>> getFreeLocationDetails(@Body String roomID);


    @POST("/api/FreeLocationUpdate")
    Call<String> executeFreeLocationUpdate();

    @POST("/api/pallet_cartonchecking")
    Call<List<PalletCartonInfo>> getPalletCarton(@Body PalletCartonParameter parameter);

    @POST("/api/MovementsHistories")
    Call<List<MovementHistoryInfo>> getMovementHistory(@Body String parameter);

    @POST("/api/EmployeePerformance")
    Call<List<EmployeePerformanceInfo>> getEmployeePerformance(@Body EmployeePerformanceParameter parameter);

    @POST("/api/EmployeeInOut")
    Call<List<EmployeeInOutInfo>> getEmployeeInOut(@Body EmployeeInOutParameter parameter);

    @POST("/api/GPSInsert")
    Call<String> executeGPSInsert(@Body GPSInsertParameter parameter);

    @POST("/api/InventoryChecking")
    Call<List<KiemHoSoInfo>> getDSInventoryChecking(@Body DSInventoryCheckingParameter parameter);

    @POST("/api/InventoryCheckingDelete")
    Call<String> executeInventoryCheckingDelete(@Body int idInventoryCheckingDelete);

    @POST("/api/WorkingSchedules")
    Call<List<WorkingSchedulesInfo>> getWorkingSchedules(@Body WorkingSchedulesParameter parameter);

    @POST("/api/MyCalendar")
    Call<List<MyCalendarInfo>> getMyCalendar(@Body MyCalendarParameter parameter);

    @POST("/api/WorkingSchedulesEmployeePlan")
    Call<List<WorkingSchedulesEmployeePlanInfo>> getWorkingSchedulesEmployeePlan(@Body WorkingSchedulesParameter parameter);

    @POST("/api/LocationChecking")
    Call<List<LocationCheckingInfo>> getLocationChecking(@Body LocationCheckingParameter parameter);

    @POST("/api/StockMovement")
    Call<List<LocationInfo>> getStockMovement(@Body StockMovementParameter parameter);

    @POST("/api/StockMovementInsert")
    Call<String> executeStockMovementInsert(@Body StockMovementInsertParameter parameter);

    @POST("/api/StockMovementReversed")
    Call<String> executeStockMovementReversed(@Body StockMovementReversedParameter parameter);

    @POST("/api/StockMovementHistories")
    Call<List<StockMovementHistoriesInfo>> getStockMovementHistories(@Body StockMovementHistoriesParameter parameter);

    @POST("/api/StockOnHand")
    Call<List<StockOnHandInfo>> getStockOnHand();

    @POST("/api/StockOnHandByCustomer")
    Call<List<StockOnHandByCustomerInfo>> getStockOnHandByCustomer(@Body StockOnHandByCustomerParameter parameter);

    @POST("/api/StockOnHandDetails")
    Call<List<StockOnHandDetailsInfo>> getStockOnHandDetails(@Body StockOnHandDetailsParameter parameter);

    @POST("/api/EmployeeIDFind")
    Call<List<EmployeeIDFindInfo>> getEmployeeID(@Body EmployeeIDFindParameter parameter);

    @POST("/api/OverTimeEntry")
    Call<String> executeOverTimeEntry(@Body OverTimeEntryParameter parameter);

    @POST("/api/OverTimeDelUpdate")
    Call<String> executeOverTimeDelUpdate(@Body OverTimeDelUpdateParameter parameter);

    @POST("/api/PayRollMonthIDList")
    Call<List<PayRollMonthIDInfo>> getPayRollMonthIDList();

    @POST("/api/OverTimeView")
    Call<List<OverTimeViewInfo>> getOverTimeView(@Body OverTimeViewParameter parameter);

    @POST("/api/OverTimeOrderDetails")
    Call<List<OverTimeOrderDetailsInfo>> getOverTimeOrderDetails(@Body OverTimeOrderDetailsParameter parameter);

    @POST("/api/MetroQACheckingSuppliers")
    Call<List<MetroQAInfo>> getMetroQACheckingSuppliers(@Body String date);

    @POST("/api/MetroQACheckingProductList")
    Call<List<QACheckingListProductsInfo>> getMetroQACheckingListProducts(@Body MetroQACheckingListProductsParameter parameter);

    @POST("/api/MetroQACheckingProduct")
    Call<List<MetroCheckingProductInfo>> getMetroQACheckingProducts(@Body MetroQACheckingProductsParameter parameter);

    @POST("/api/MetroQACheckingCarton")
    Call<List<MetroCartonInfo>> getMetroQACheckingCarton(@Body MetroQACheckingCartonParameter parameter);

    @POST("/api/MetroQACheckingCartonInsert")
    Call<String> executeMetroQACheckingCartonInsert(@Body MetroQACheckingCartonInsertParameter parameter);

    @POST("/api/MetroQACheckingCartonDelUpdate")
    Call<String> executeMetroQACheckingCartonDelUpdate(@Body MetroQACheckingCartonDelUpdateParameter parameter);

    @POST("/api/UserManagement")
    Call<List<UserInfo>> getListUser(@Body short Flag);

    @POST("/api/GPSViewByUser")
    Call<List<GPSUserInfo>> getGPSViewByUser(@Body GPSViewByUserParameter parameter);

    @POST("/api/EquipmentInventory")
    Call<List<EquipmentInventoryInfo>> getEquipmentInventory(@Body EquipmentInventoryParameter parameter);

    @POST("/api/ContainerAndTruckInfor")
    Call<List<ContainerAndTruckInfo>> getContainerAndTruckInfor(@Body int gate);

    @POST("/api/CRMOpportunities")
    Call<List<Opportunity>> getOpportunities();

    @POST("/api/CRMOpportunitiesDetail")
    Call<List<Opportunity>> getOpportunity(@Body int opportunityId);

    @POST("/api/CRMOpportunitiesUpdate")
    Call<String> updateOpportunity(@Body OpportunityParameter parameter);

    @POST("/api/CRMOpportunitiesDelete")
    Call<String> deleteOpportunity(@Body OpportunityDeleteParameter parameter);

    @POST("/api/CRMOpportunitiesInsert")
    Call<String> addOpportunity(@Body OpportunityParameter parameter);

}
