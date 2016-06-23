package com.scsvn.whc_2016.main;

import android.content.Context;

import com.scsvn.whc_2016.R;

import java.util.ArrayList;


public class DataUser {
    public static ArrayList<MenuInfo> data = new ArrayList<>();
    private Context context;
    private int max = 25;

    public DataUser(Context context) {
        this.context = context;
    }

    public void manager() {
        data.clear();
        data.add(new MenuInfo(0, context.getString(R.string.phieu_hom_nay), R.drawable.ic_hom_nay));
        data.add(new MenuInfo(1, context.getString(R.string.phieu_cua_toi), R.drawable.ic_cua_toi));
        data.add(new MenuInfo(25, context.getString(R.string.phieu_da_xong), R.drawable.ic_pending_confirm));
        data.add(new MenuInfo(2, context.getString(R.string.nhap_ho_so), R.drawable.ic_scan_ho_so));
        data.add(new MenuInfo(3, context.getString(R.string.giao_ho_so), R.drawable.ic_scan_xuat));
        data.add(new MenuInfo(19, context.getString(R.string.ve_sinh_an_toan), R.drawable.ic_beach));
        data.add(new MenuInfo(4, context.getString(R.string.qa_kiem), R.drawable.ic_chup_hinh));
        data.add(new MenuInfo(5, context.getString(R.string.kiem_container), R.drawable.ic_container));
        data.add(new MenuInfo(13, context.getString(R.string.container_information), R.drawable.ic_kiem_xe));
        data.add(new MenuInfo(6, context.getString(R.string.kiem_vi_tri), R.drawable.ic_kiem_vi_tri));
        data.add(new MenuInfo(7, context.getString(R.string.keim_pallet), R.drawable.ic_kiem_pallet));
        data.add(new MenuInfo(8, context.getString(R.string.keim_ho_so), R.drawable.ic_kiem_ho_so));
        data.add(new MenuInfo(9, context.getString(R.string.vi_tri_trong), R.drawable.ic_vi_tri_trong));
        data.add(new MenuInfo(20, context.getString(R.string.ton_kho), R.drawable.ic_ton_kho));
        data.add(new MenuInfo(11, context.getString(R.string.lich_su_chuyen_hang), R.drawable.ic_lich_su_chuyen));
        data.add(new MenuInfo(10, context.getString(R.string.nang_suat), R.drawable.ic_nang_suat));
        data.add(new MenuInfo(18, context.getString(R.string.lich_su_ra_vao), R.drawable.ic_lich_su_ra_vao));
        data.add(new MenuInfo(14, context.getString(R.string.nhap_ngoai_gio), R.drawable.ic_ngoai_gio));
        data.add(new MenuInfo(22, context.getString(R.string.giao_viec), R.drawable.ic_giao_viec));
        data.add(new MenuInfo(16, context.getString(R.string.lich_lam_viec), R.drawable.ic_lich_lam_viec));
        data.add(new MenuInfo(21, context.getString(R.string.gps), R.drawable.ic_place));
        data.add(new MenuInfo(24, context.getString(R.string.fixed_asset), R.drawable.ic_equipment_inventory));
        data.add(new MenuInfo(12, context.getString(R.string.doi_mat_khau), R.drawable.ic_mat_khau));
        data.add(new MenuInfo(17, context.getString(R.string.cap_nhat_moi), R.drawable.ic_cap_nhat_moi));
    }

    public void supervisor() {
        data.clear();
        data.add(new MenuInfo(0, context.getString(R.string.phieu_hom_nay), R.drawable.ic_hom_nay));
        data.add(new MenuInfo(1, context.getString(R.string.phieu_cua_toi), R.drawable.ic_cua_toi));
        data.add(new MenuInfo(25, context.getString(R.string.phieu_da_xong), R.drawable.ic_pending_confirm));
        data.add(new MenuInfo(2, context.getString(R.string.nhap_ho_so), R.drawable.ic_scan_ho_so));
        data.add(new MenuInfo(3, context.getString(R.string.giao_ho_so), R.drawable.ic_scan_xuat));
        data.add(new MenuInfo(19, context.getString(R.string.ve_sinh_an_toan), R.drawable.ic_beach));
        data.add(new MenuInfo(4, context.getString(R.string.qa_kiem), R.drawable.ic_chup_hinh));
        data.add(new MenuInfo(5, context.getString(R.string.kiem_container), R.drawable.ic_container));
        data.add(new MenuInfo(13, context.getString(R.string.container_information), R.drawable.ic_kiem_xe));
        data.add(new MenuInfo(6, context.getString(R.string.kiem_vi_tri), R.drawable.ic_kiem_vi_tri));
        data.add(new MenuInfo(7, context.getString(R.string.keim_pallet), R.drawable.ic_kiem_pallet));
        data.add(new MenuInfo(8, context.getString(R.string.keim_ho_so), R.drawable.ic_kiem_ho_so));
        data.add(new MenuInfo(9, context.getString(R.string.vi_tri_trong), R.drawable.ic_vi_tri_trong));
        data.add(new MenuInfo(20, context.getString(R.string.ton_kho), R.drawable.ic_ton_kho));
        data.add(new MenuInfo(11, context.getString(R.string.lich_su_chuyen_hang), R.drawable.ic_lich_su_chuyen));
        data.add(new MenuInfo(10, context.getString(R.string.nang_suat), R.drawable.ic_nang_suat));
        data.add(new MenuInfo(18, context.getString(R.string.lich_su_ra_vao), R.drawable.ic_lich_su_ra_vao));
        data.add(new MenuInfo(14, context.getString(R.string.nhap_ngoai_gio), R.drawable.ic_ngoai_gio));
        data.add(new MenuInfo(16, context.getString(R.string.lich_lam_viec), R.drawable.ic_lich_lam_viec));
        data.add(new MenuInfo(21, context.getString(R.string.gps), R.drawable.ic_place));
        data.add(new MenuInfo(24, context.getString(R.string.fixed_asset), R.drawable.ic_equipment_inventory));
        data.add(new MenuInfo(12, context.getString(R.string.doi_mat_khau), R.drawable.ic_mat_khau));
        data.add(new MenuInfo(17, context.getString(R.string.cap_nhat_moi), R.drawable.ic_cap_nhat_moi));

    }

    public void productChecker() {
        data.clear();
        data.add(new MenuInfo(0, context.getString(R.string.phieu_hom_nay), R.drawable.ic_hom_nay));
        data.add(new MenuInfo(1, context.getString(R.string.phieu_cua_toi), R.drawable.ic_cua_toi));
        data.add(new MenuInfo(25, context.getString(R.string.phieu_da_xong), R.drawable.ic_pending_confirm));
        data.add(new MenuInfo(2, context.getString(R.string.nhap_ho_so), R.drawable.ic_scan_ho_so));
        data.add(new MenuInfo(3, context.getString(R.string.giao_ho_so), R.drawable.ic_scan_xuat));
        data.add(new MenuInfo(19, context.getString(R.string.ve_sinh_an_toan), R.drawable.ic_beach));
        data.add(new MenuInfo(4, context.getString(R.string.qa_kiem), R.drawable.ic_chup_hinh));
        data.add(new MenuInfo(5, context.getString(R.string.kiem_container), R.drawable.ic_container));
        data.add(new MenuInfo(13, context.getString(R.string.container_information), R.drawable.ic_kiem_xe));
        data.add(new MenuInfo(6, context.getString(R.string.kiem_vi_tri), R.drawable.ic_kiem_vi_tri));
        data.add(new MenuInfo(7, context.getString(R.string.keim_pallet), R.drawable.ic_kiem_pallet));
        data.add(new MenuInfo(8, context.getString(R.string.keim_ho_so), R.drawable.ic_kiem_ho_so));
        data.add(new MenuInfo(9, context.getString(R.string.vi_tri_trong), R.drawable.ic_vi_tri_trong));
        data.add(new MenuInfo(10, context.getString(R.string.nang_suat), R.drawable.ic_nang_suat));
        data.add(new MenuInfo(18, context.getString(R.string.lich_su_ra_vao), R.drawable.ic_lich_su_ra_vao));
        data.add(new MenuInfo(16, context.getString(R.string.lich_lam_viec), R.drawable.ic_lich_lam_viec));
        data.add(new MenuInfo(17, context.getString(R.string.cap_nhat_moi), R.drawable.ic_cap_nhat_moi));
        data.add(new MenuInfo(12, context.getString(R.string.doi_mat_khau), R.drawable.ic_mat_khau));
    }

    public void forkliftDriver() {
        data.clear();
        data.add(new MenuInfo(0, context.getString(R.string.phieu_hom_nay), R.drawable.ic_hom_nay));
        data.add(new MenuInfo(1, context.getString(R.string.phieu_cua_toi), R.drawable.ic_cua_toi));
        data.add(new MenuInfo(25, context.getString(R.string.phieu_da_xong), R.drawable.ic_pending_confirm));
        data.add(new MenuInfo(2, context.getString(R.string.nhap_ho_so), R.drawable.ic_scan_ho_so));
        data.add(new MenuInfo(3, context.getString(R.string.giao_ho_so), R.drawable.ic_scan_xuat));
        data.add(new MenuInfo(19, context.getString(R.string.ve_sinh_an_toan), R.drawable.ic_beach));
        data.add(new MenuInfo(4, context.getString(R.string.qa_kiem), R.drawable.ic_chup_hinh));
        data.add(new MenuInfo(5, context.getString(R.string.kiem_container), R.drawable.ic_container));
        data.add(new MenuInfo(13, context.getString(R.string.container_information), R.drawable.ic_kiem_xe));
        data.add(new MenuInfo(6, context.getString(R.string.kiem_vi_tri), R.drawable.ic_kiem_vi_tri));
        data.add(new MenuInfo(7, context.getString(R.string.keim_pallet), R.drawable.ic_kiem_pallet));
        data.add(new MenuInfo(8, context.getString(R.string.keim_ho_so), R.drawable.ic_kiem_ho_so));
        data.add(new MenuInfo(9, context.getString(R.string.vi_tri_trong), R.drawable.ic_vi_tri_trong));
        data.add(new MenuInfo(20, context.getString(R.string.ton_kho), R.drawable.ic_ton_kho));
        data.add(new MenuInfo(11, context.getString(R.string.lich_su_chuyen_hang), R.drawable.ic_lich_su_chuyen));
        data.add(new MenuInfo(10, context.getString(R.string.nang_suat), R.drawable.ic_nang_suat));
        data.add(new MenuInfo(18, context.getString(R.string.lich_su_ra_vao), R.drawable.ic_lich_su_ra_vao));
        data.add(new MenuInfo(16, context.getString(R.string.lich_lam_viec), R.drawable.ic_lich_lam_viec));
        data.add(new MenuInfo(17, context.getString(R.string.cap_nhat_moi), R.drawable.ic_cap_nhat_moi));
        data.add(new MenuInfo(12, context.getString(R.string.doi_mat_khau), R.drawable.ic_mat_khau));
    }

    public void technical() {
        data.clear();
        data.add(new MenuInfo(19, context.getString(R.string.ve_sinh_an_toan), R.drawable.ic_beach));
        data.add(new MenuInfo(22, context.getString(R.string.giao_viec), R.drawable.ic_giao_viec));
        data.add(new MenuInfo(23, context.getString(R.string.lich_trinh), R.drawable.ic_kiem_pallet));
        data.add(new MenuInfo(5, context.getString(R.string.kiem_container), R.drawable.ic_container));
        data.add(new MenuInfo(10, context.getString(R.string.nang_suat), R.drawable.ic_nang_suat));
        data.add(new MenuInfo(18, context.getString(R.string.lich_su_ra_vao), R.drawable.ic_lich_su_ra_vao));
        data.add(new MenuInfo(14, context.getString(R.string.nhap_ngoai_gio), R.drawable.ic_ngoai_gio));
        data.add(new MenuInfo(17, context.getString(R.string.cap_nhat_moi), R.drawable.ic_cap_nhat_moi));
        data.add(new MenuInfo(12, context.getString(R.string.doi_mat_khau), R.drawable.ic_mat_khau));
    }

    public void user() {
        data.clear();
        data.add(new MenuInfo(5, context.getString(R.string.kiem_container), R.drawable.ic_container));
        data.add(new MenuInfo(10, context.getString(R.string.nang_suat), R.drawable.ic_nang_suat));
        data.add(new MenuInfo(18, context.getString(R.string.lich_su_ra_vao), R.drawable.ic_lich_su_ra_vao));
        data.add(new MenuInfo(17, context.getString(R.string.cap_nhat_moi), R.drawable.ic_cap_nhat_moi));
        data.add(new MenuInfo(12, context.getString(R.string.doi_mat_khau), R.drawable.ic_mat_khau));

    }

    public void lowerUser() {
        data.clear();
        data.add(new MenuInfo(17, context.getString(R.string.cap_nhat_moi), R.drawable.ic_cap_nhat_moi));
        data.add(new MenuInfo(12, context.getString(R.string.doi_mat_khau), R.drawable.ic_mat_khau));
    }

}
