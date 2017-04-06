package com.scsvn.whc_2016.main;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.main.booking.BookingActivity;
import com.scsvn.whc_2016.main.capnhatphienban.CapNhatUngDungActivity;
import com.scsvn.whc_2016.main.changepassword.ChangePasswordActivity;
import com.scsvn.whc_2016.main.chuyenhang.ChuyenHangActivity;
import com.scsvn.whc_2016.main.containerandtruckinfor.ContainerAndTruckInfoActivity;
import com.scsvn.whc_2016.main.crm.CRMActivity;
import com.scsvn.whc_2016.main.equipment.EquipmentInventoryActivity;
import com.scsvn.whc_2016.main.equipment.EquipmentInventoryNoEMDKActivity;
import com.scsvn.whc_2016.main.giaonhanhoso.GiaoHoSoActivity;
import com.scsvn.whc_2016.main.gps.listuser.ListUserActivity;
import com.scsvn.whc_2016.main.kiemcontainer.KiemContainerActivity;
import com.scsvn.whc_2016.main.kiemhoso.KiemHoSoActivity;
import com.scsvn.whc_2016.main.kiemhoso.KiemHoSoNoEMDKActivity;
import com.scsvn.whc_2016.main.kiemqa.metroqacheckingsuppliers.MetroQACheckingSuppliersActivity;
import com.scsvn.whc_2016.main.kiemvesinh.KiemVeSinhActivity;
import com.scsvn.whc_2016.main.kiemvesinh.KiemVeSinhEMDKActivity;
import com.scsvn.whc_2016.main.kiemvitri.KiemViTriActivity;
import com.scsvn.whc_2016.main.kiemvitri.KiemViTriNoEMDKActivity;
import com.scsvn.whc_2016.main.lichlamviec.LichLamViecActivity;
import com.scsvn.whc_2016.main.lichsuravao.LichSuRaVaoActivity;
import com.scsvn.whc_2016.main.mms.MaintenanceActivity;
import com.scsvn.whc_2016.main.nangsuat.NangSuatActivity;
import com.scsvn.whc_2016.main.nangsuatnhanvien.NangSuatNhanVienActivity;
import com.scsvn.whc_2016.main.nhaphoso.NhapHoSoActivity;
import com.scsvn.whc_2016.main.nhaphoso.NhapHoSoNoEMDKActivity;
import com.scsvn.whc_2016.main.nhapngoaigio.detail.ListOverTimeEntryActivity;
import com.scsvn.whc_2016.main.opportunity.ListOpportunityActivity;
import com.scsvn.whc_2016.main.palletcartonchecking.KiemPalletCartonEMDKActivity;
import com.scsvn.whc_2016.main.palletcartonchecking.KiemPalletCartonActivity;
import com.scsvn.whc_2016.main.phieucuatoi.PhieuCuaToiActivity;
import com.scsvn.whc_2016.main.phieuhomnay.KhachHangActivity;
import com.scsvn.whc_2016.main.register.RegisterActivity;
import com.scsvn.whc_2016.main.technical.assign.AssignWorkActivity;
import com.scsvn.whc_2016.main.technical.schedulejobplan.ScheduleJobActivity;
import com.scsvn.whc_2016.main.tonkho.khachhang.StockOnHandActivity;
import com.scsvn.whc_2016.main.vesinhantoan.QHSEActivity;
import com.scsvn.whc_2016.main.vitritrong.FreeLocationActivity;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Trần Xuân Lộc on 12/26/2015.
 */
public class MenuAdapter extends ArrayAdapter<MenuInfo> {
    private LayoutInflater layoutInflater;

    public MenuAdapter(Context context, ArrayList<MenuInfo> objects) {
        super(context, 0, objects);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.items_menu_main, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else holder = (ViewHolder) convertView.getTag();

        final MenuInfo info = getItem(position);
        holder.title.setText(info.getName());
        holder.title.setTag(info.getId());
        holder.title.setCompoundDrawablesWithIntrinsicBounds(0, info.getDrawable(), 0, 0);
        holder.title.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                    v.setAlpha(0.5f);
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    int tag = (int) holder.title.getTag();
                    navigateMenu(getContext(), tag, info.getNumber());
                    v.setAlpha(1f);
                } else
                    v.setAlpha(1f);
                return true;
            }
        });
        holder.tvNumber.setText(String.format(Locale.US, "%d", info.getNumber()));
        if (info.getNumber() == 0)
            holder.tvNumber.setVisibility(View.GONE);
        else
            holder.tvNumber.setVisibility(View.VISIBLE);
        return convertView;
    }

    private void navigateMenu(Context context, int tag, short numberQHSE) {
        if (tag == 0)
            context.startActivity(new Intent(context, KhachHangActivity.class));
        else if (tag == 1) {
            Intent intent = new Intent(context, PhieuCuaToiActivity.class);
            intent.putExtra("FUNCTION", "NhanVien");
            context.startActivity(intent);
        } else if (tag == 25) {
            Intent intent = new Intent(context, PhieuCuaToiActivity.class);
            intent.putExtra("FUNCTION", "GiamSat");
            context.startActivity(intent);
        } else if (tag == 4)
            context.startActivity(new Intent(context, MetroQACheckingSuppliersActivity.class));
        else if (tag == 5)
            context.startActivity(new Intent(context, KiemContainerActivity.class));
        else if (tag == 7) {
            try {
                Class.forName("com.symbol.emdk.EMDKManager");
                context.startActivity(new Intent(context, KiemPalletCartonEMDKActivity.class));
            } catch (ClassNotFoundException e) {
                context.startActivity(new Intent(context, KiemPalletCartonActivity.class));
            }
        } else if (tag == 8) {
            try {
                Class.forName("com.symbol.emdk.EMDKManager");
                context.startActivity(new Intent(context, KiemHoSoActivity.class));
            } catch (ClassNotFoundException e) {
                context.startActivity(new Intent(context, KiemHoSoNoEMDKActivity.class));
            }
        } else if (tag == 10)
            context.startActivity(new Intent(context, NangSuatActivity.class));
        else if (tag == 29)
            context.startActivity(new Intent(context, NangSuatNhanVienActivity.class));
        else if (tag == 19) {
            Intent intentQHSE = new Intent(context, QHSEActivity.class);
            intentQHSE.putExtra("numberQHSE", "0");
            context.startActivity(intentQHSE);
        } else if (tag == 9)
            context.startActivity(new Intent(context, FreeLocationActivity.class));
        else if (tag == 2) {
            try {
                Class.forName("com.symbol.emdk.EMDKManager");
                context.startActivity(new Intent(context, NhapHoSoActivity.class));
            } catch (ClassNotFoundException e) {
                context.startActivity(new Intent(context, NhapHoSoNoEMDKActivity.class));
            }
        } else if (tag == 3)
            context.startActivity(new Intent(context, GiaoHoSoActivity.class));
        else if (tag == 6) {
            try {
                Class.forName("com.symbol.emdk.EMDKManager");
                context.startActivity(new Intent(context, KiemViTriActivity.class));
            } catch (ClassNotFoundException e) {
                context.startActivity(new Intent(context, KiemViTriNoEMDKActivity.class));
            }
        } else if (tag == 11)
            context.startActivity(new Intent(context, ChuyenHangActivity.class));
        else if (tag == 16)
            context.startActivity(new Intent(context, LichLamViecActivity.class));
        else if (tag == 17)
            context.startActivity(new Intent(context, CapNhatUngDungActivity.class));
        else if (tag == 12)
            context.startActivity(new Intent(context, ChangePasswordActivity.class));
        else if (tag == 18)
            context.startActivity(new Intent(context, LichSuRaVaoActivity.class));
        else if (tag == 20)
            context.startActivity(new Intent(context, StockOnHandActivity.class));
        else if (tag == 14)
            context.startActivity(new Intent(context, ListOverTimeEntryActivity.class));
        else if (tag == 21) {
            if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS)
                context.startActivity(new Intent(context, ListUserActivity.class));
            else
                Toast.makeText(context, "Google Play Services Not Available", Toast.LENGTH_LONG).show();

        } else if (tag == 22)
            context.startActivity(new Intent(context, AssignWorkActivity.class));
        else if (tag == 23)
            context.startActivity(new Intent(context, ScheduleJobActivity.class));
        else if (tag == 13)
            context.startActivity(new Intent(context, ContainerAndTruckInfoActivity.class));
        else if (tag == 24) {
            try {
                Class.forName("com.symbol.emdk.EMDKManager");
                context.startActivity(new Intent(context, EquipmentInventoryActivity.class));
            } catch (ClassNotFoundException e) {
                context.startActivity(new Intent(context, EquipmentInventoryNoEMDKActivity.class));
            }
        } else if (tag == 15)
            context.startActivity(new Intent(context, ListOpportunityActivity.class));
        else if (tag == 26)
            context.startActivity(new Intent(context, CRMActivity.class));
        else if (tag == 30)
            context.startActivity(new Intent(context, BookingActivity.class));
        else if (tag == 27)
            context.startActivity(new Intent(context, MaintenanceActivity.class));
        else if (tag == 28)
            context.startActivity(new Intent(context, RegisterActivity.class));
        else if (tag == 31)
            context.startActivity(new Intent(context, DeviceInfoActivity.class));
        else if (tag == 32)
            try {
                Class.forName("com.symbol.emdk.EMDKManager");
                context.startActivity(new Intent(context, KiemVeSinhEMDKActivity.class));
            } catch (ClassNotFoundException e) {
                context.startActivity(new Intent(context, KiemVeSinhActivity.class));
            }
    }

    static class ViewHolder {
        @Bind(R.id.title)
        TextView title;
        @Bind(R.id.tv_number)
        TextView tvNumber;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
