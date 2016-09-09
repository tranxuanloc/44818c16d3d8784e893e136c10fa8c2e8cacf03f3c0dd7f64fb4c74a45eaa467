package com.scsvn.whc_2016.main.mms.add;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.main.BaseActivity;
import com.scsvn.whc_2016.main.MyAutoCompleteTextView;
import com.scsvn.whc_2016.main.mms.MaintenanceActivity;
import com.scsvn.whc_2016.main.mms.MaintenanceJob;
import com.scsvn.whc_2016.main.mms.employee.EmployeeAbstract;
import com.scsvn.whc_2016.main.mms.employee.MaintenanceEmployee;
import com.scsvn.whc_2016.main.mms.equipment.Equipment;
import com.scsvn.whc_2016.main.mms.equipment.EquipmentAdapter;
import com.scsvn.whc_2016.main.mms.job.JobDaily;
import com.scsvn.whc_2016.main.mms.job.JobDailyAbstract;
import com.scsvn.whc_2016.main.mms.job.JobDefinition;
import com.scsvn.whc_2016.main.mms.job.JobDefinitionAdapter;
import com.scsvn.whc_2016.main.mms.part.PartAbstract;
import com.scsvn.whc_2016.main.mms.part.PartRemain;
import com.scsvn.whc_2016.main.mms.part.PartRemainAdapter;
import com.scsvn.whc_2016.main.mms.part.WriteOff;
import com.scsvn.whc_2016.main.phieuhomnay.giaoviec.EmployeeInfo;
import com.scsvn.whc_2016.main.technical.assign.EmployeePresentAdapter;
import com.scsvn.whc_2016.preferences.LoginPref;
import com.scsvn.whc_2016.retrofit.EmployeePresentParameter;
import com.scsvn.whc_2016.retrofit.EquipmentParameter;
import com.scsvn.whc_2016.retrofit.JobDailyParameter;
import com.scsvn.whc_2016.retrofit.JobDefinitionParameter;
import com.scsvn.whc_2016.retrofit.MMSEmployeeParameter;
import com.scsvn.whc_2016.retrofit.MaintenanceJobEmployeeParameter;
import com.scsvn.whc_2016.retrofit.MaintenanceJobParameter;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.retrofit.NoInternet;
import com.scsvn.whc_2016.retrofit.PartRemainParameter;
import com.scsvn.whc_2016.retrofit.RetrofitError;
import com.scsvn.whc_2016.retrofit.WriteOffParameter;
import com.scsvn.whc_2016.utilities.Utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class CreateMaintenanceActivity extends BaseActivity implements View.OnClickListener, View.OnFocusChangeListener {
    private final static String TAG = CreateMaintenanceActivity.class.getSimpleName();
    private Toolbar toolbar;
    private String userName;
    private AutoCompleteTextView equipmentEditText;
    private EquipmentAdapter equipmentAdapter;
    private EditText equipmentIdView;
    private EditText equipmentNameView;
    private EditText equipmentDeptView;
    private EditText equipmentSerialView;
    private EditText maintenanceDateEditText;
    private AutoCompleteTextView partRemainEditText;
    private AutoCompleteTextView jobDefinitionEditText;
    private AutoCompleteTextView employeeEditText;
    private PartRemainAdapter partRemainAdapter;
    private JobDefinitionAdapter jobDefinitionAdapter;
    private EmployeePresentAdapter employeeAdapter;
    private LinearLayout containerPart;
    private LinearLayout containerJob;
    private LinearLayout containerEmployee;
    private EditText maintenanceDesET;
    private EditText runningHourET;
    private EditText h1ET;
    private EditText h2ET;
    private EditText h3ET;
    private EditText h4ET;
    private String username;
    private int id;
    private ProgressDialog dialog;
    private boolean isDetail;
    private ArrayList<JobDefinitionViewHolder> listJob = new ArrayList<>();
    private ArrayList<PartRemainViewHolder> listPart = new ArrayList<>();
    private ArrayList<EmployeeViewHolder> listEmployee = new ArrayList<>();
    private AdapterView.OnItemClickListener partRemainItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            PartRemain item = partRemainAdapter.getItem(position);
            addPartView(item, listPart.size());
        }
    };
    private AdapterView.OnItemClickListener employeeItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            EmployeeInfo item = employeeAdapter.getItem(position);
            addEmployeeView(item, listEmployee.size());
        }
    };
    private AdapterView.OnItemClickListener jobDefinitionItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            JobDefinition item = jobDefinitionAdapter.getItem(position);
            addJobDailyView(item, listJob.size());
        }
    };
    private MenuItem itemDone;
    private MenuItem itemEdit;
    private MenuItem itemDelete;
    private RelativeLayout containerView;
    private int indexJobDaily;
    private Integer idMaintenanceJob;
    private int indexPart;
    private int indexEmployee;
    private Calendar calendar;
    private String dept;
    private boolean isConfirm;
    private TextView maintenanceInfo;
    private TextView maintenanceTitleCreateView;
    private EditText equipmentModel;
    private AdapterView.OnItemClickListener equipmentItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Equipment item = equipmentAdapter.getItem(position);
            String dept = item.getDept();
            getListJobDefinition(dept);
            getListPartRemain(dept);
            updateUIEquipment(item);
        }
    };
    private MenuItem itemCreate;
    private TextView actionBarTitleView;
    private View viewEmployee;
    private View viewJob;
    private boolean isUpdated;

    private void addEmployeeView(EmployeeAbstract item, int position) {
        EmployeeViewHolder holder = new EmployeeViewHolder();
        holder.isDetail = item.isDetail();
        holder.viewEmployee = LayoutInflater.from(CreateMaintenanceActivity.this).inflate(R.layout.item_mms_employee, null);
        holder.idView = (EditText) holder.viewEmployee.findViewById(R.id.item_mms_employee_info);
        holder.durationET = (EditText) holder.viewEmployee.findViewById(R.id.item_mms_employee_duration);
        holder.otET = (EditText) holder.viewEmployee.findViewById(R.id.item_mms_employee_ot);
        holder.noteET = (EditText) holder.viewEmployee.findViewById(R.id.item_mms_employee_note);
        holder.evaluationCB = (AppCompatCheckBox) holder.viewEmployee.findViewById(R.id.item_mms_employee_evaluation);
        holder.employeeRemoveView = (ImageView) holder.viewEmployee.findViewById(R.id.item_mms_employee_remove);
        holder.employeeRemoveView.setTag(holder);
        holder.employeeRemoveView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmployeeViewHolder holder = (EmployeeViewHolder) v.getTag();
                containerEmployee.removeView(holder.viewEmployee);
                listEmployee.remove(holder);
                if (holder.isDetail)
                    deleteEmployee(parserInt(holder.idView.getTag().toString()));
            }
        });

        if (isDetail) {
            holder.employeeRemoveView.setVisibility(View.GONE);
            holder.noteET.setText(((MaintenanceEmployee) item).getRemark());
            holder.durationET.setText(String.format(Locale.getDefault(), "%.1f", ((MaintenanceEmployee) item).getDuration()));
            holder.otET.setText(String.format(Locale.getDefault(), "%.1f", ((MaintenanceEmployee) item).getOverTime()));
            holder.evaluationCB.setChecked(((MaintenanceEmployee) item).getEvaluation());
            holder.evaluationCB.setEnabled(false);
        }
        holder.idView.setTag(holder.isDetail ? ((MaintenanceEmployee) item).getWorkingId() : item.getEmployeeID());
        holder.idView.setText(String.format(Locale.getDefault(), "%d %s", item.getEmployeeID(), item.getEmployeeName()));
        containerEmployee.addView(holder.viewEmployee);
        listEmployee.add(holder);
        employeeEditText.setText("");
        if (position % 2 != 0)
            holder.viewEmployee.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAlternativeRow));
    }

    private void addPartView(PartAbstract item, int position) {
        PartRemainViewHolder holder = new PartRemainViewHolder();
        holder.isDetail = item.isDetail();
        holder.viewPartRemain = LayoutInflater.from(CreateMaintenanceActivity.this).inflate(R.layout.item_part_remain, null);
        holder.quantityView = (EditText) holder.viewPartRemain.findViewById(R.id.part_remain_quantity);
        holder.descriptionView = (EditText) holder.viewPartRemain.findViewById(R.id.part_remain_description);
        holder.noteView = (EditText) holder.viewPartRemain.findViewById(R.id.part_remain_note);
        holder.partRemoveView = (ImageView) holder.viewPartRemain.findViewById(R.id.part_remain_remove);
        holder.partRemoveView.setTag(holder);

        holder.partRemoveView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PartRemainViewHolder holder = (PartRemainViewHolder) v.getTag();
                containerPart.removeView(holder.viewPartRemain);
                listPart.remove(holder);
                if (holder.isDetail)
                    deletePart(holder.descriptionView.getTag().toString());
            }
        });
        if (isDetail) {
            holder.partRemoveView.setVisibility(View.GONE);
            holder.noteView.setText(((WriteOff) item).getRemark());
            holder.quantityView.setText(String.format(Locale.getDefault(), "%d", ((WriteOff) item).getQuantity()));

        }
        holder.descriptionView.setTag(item.getId());
        holder.descriptionView.setText(String.format("%s ~ %s", item.getOriginal(), item.getName()));
        containerPart.addView(holder.viewPartRemain);
        listPart.add(holder);
        if (position % 2 == 0)
            holder.viewPartRemain.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAlternativeRow));
    }

    private void addJobDailyView(JobDailyAbstract item, int position) {
        final JobDefinitionViewHolder holder = new JobDefinitionViewHolder();
        holder.isDetail = item.isDetail();
        holder.viewJobDefinition = LayoutInflater.from(CreateMaintenanceActivity.this).inflate(R.layout.item_job_definition, null);
        holder.accidentView = (EditText) holder.viewJobDefinition.findViewById(R.id.job_definition_accident);
        holder.descriptionView = (EditText) holder.viewJobDefinition.findViewById(R.id.job_definition_description);
        holder.noteView = (EditText) holder.viewJobDefinition.findViewById(R.id.job_definition_note);
        holder.jobRemoveView = (ImageView) holder.viewJobDefinition.findViewById(R.id.job_definition_remove);
        holder.accidentView.setInputType(InputType.TYPE_NULL);
        holder.jobRemoveView.setTag(holder);

        holder.jobRemoveView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JobDefinitionViewHolder holder = (JobDefinitionViewHolder) v.getTag();
                containerJob.removeView(holder.viewJobDefinition);
                listJob.remove(holder);
                if (holder.isDetail)
                    deleteJobDaily(holder.descriptionView.getTag().toString());
            }
        });

        final PopupMenu menu = new PopupMenu(CreateMaintenanceActivity.this, holder.accidentView);
        menu.inflate(R.menu.job_definition_accident);
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                holder.accidentView.setText(item.getTitle());
                return true;
            }
        });
        holder.accidentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isDetail)
                    menu.show();
            }
        });
        holder.accidentView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Utilities.hideKeyboard(CreateMaintenanceActivity.this);
                    menu.show();
                }
            }
        });
        if (isDetail) {
            holder.jobRemoveView.setVisibility(View.GONE);
            holder.noteView.setText(((JobDaily) item).getRemark());
            holder.accidentView.setText(((JobDaily) item).getReason());
        }
        holder.descriptionView.setTag(item.getId());
        holder.descriptionView.setText(item.getName());
        containerJob.addView(holder.viewJobDefinition);
        listJob.add(holder);
        if (position % 2 != 0)
            holder.viewJobDefinition.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAlternativeRow));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_maintenance);

        initial();
    }

    private void mapView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        actionBarTitleView = (TextView) toolbar.findViewById(R.id.title);
        equipmentEditText = (MyAutoCompleteTextView) findViewById(R.id.create_maintenance_equipment);
        partRemainEditText = (MyAutoCompleteTextView) findViewById(R.id.create_maintenance_part_remain);
        jobDefinitionEditText = (MyAutoCompleteTextView) findViewById(R.id.create_maintenance_job_definition);
        employeeEditText = (MyAutoCompleteTextView) findViewById(R.id.create_maintenance_employee);
        equipmentIdView = (EditText) findViewById(R.id.create_maintenance_id);
        equipmentNameView = (EditText) findViewById(R.id.create_maintenance_name);
        equipmentDeptView = (EditText) findViewById(R.id.create_maintenance_dept);
        equipmentSerialView = (EditText) findViewById(R.id.create_maintenance_serial);
        maintenanceDateEditText = (EditText) findViewById(R.id.create_maintenance_date_create);
        containerJob = (LinearLayout) findViewById(R.id.container_job_definition);
        containerPart = (LinearLayout) findViewById(R.id.container_part_remain);
        containerEmployee = (LinearLayout) findViewById(R.id.container_employee);
        maintenanceDesET = (EditText) findViewById(R.id.create_maintenance_description);
        runningHourET = (EditText) findViewById(R.id.create_maintenance_running_hour);
        equipmentModel = (EditText) findViewById(R.id.create_maintenance_model);
        maintenanceInfo = (TextView) findViewById(R.id.create_maintenance_info);
        maintenanceTitleCreateView = (TextView) findViewById(R.id.create_maintenance_title_created);
        h1ET = (EditText) findViewById(R.id.create_maintenance_h1);
        h2ET = (EditText) findViewById(R.id.create_maintenance_h2);
        h3ET = (EditText) findViewById(R.id.create_maintenance_h3);
        h4ET = (EditText) findViewById(R.id.create_maintenance_h4);
        containerView = (RelativeLayout) findViewById(R.id.create_maintenance_container);
        viewEmployee = findViewById(R.id.viewEmployee);
        viewJob = findViewById(R.id.viewJob);
    }

    private void setListener() {
        equipmentEditText.setOnItemClickListener(equipmentItemClickListener);
        jobDefinitionEditText.setOnItemClickListener(jobDefinitionItemClickListener);
        partRemainEditText.setOnItemClickListener(partRemainItemClickListener);
        employeeEditText.setOnItemClickListener(employeeItemClickListener);
        maintenanceDateEditText.setOnClickListener(this);
        equipmentEditText.setOnFocusChangeListener(this);
        partRemainEditText.setOnFocusChangeListener(this);
        jobDefinitionEditText.setOnFocusChangeListener(this);
        employeeEditText.setOnFocusChangeListener(this);
        equipmentEditText.setOnClickListener(this);
        partRemainEditText.setOnClickListener(this);
        jobDefinitionEditText.setOnClickListener(this);
        employeeEditText.setOnClickListener(this);
    }

    private void initial() {
        mapView();
        setListener();
        snackBarView = toolbar;
        calendar = Calendar.getInstance();
        setSupportActionBar(toolbar);
        Utilities.showBackIcon(getSupportActionBar());
        dialog = Utilities.getProgressDialog(this, getString(R.string.loading_data));
        dialog.show();
        userName = LoginPref.getUsername(this);
        equipmentAdapter = new EquipmentAdapter(CreateMaintenanceActivity.this, new ArrayList<Equipment>());
        equipmentEditText.setAdapter(equipmentAdapter);

        partRemainAdapter = new PartRemainAdapter(this, new ArrayList<PartRemain>());
        partRemainEditText.setAdapter(partRemainAdapter);

        jobDefinitionAdapter = new JobDefinitionAdapter(this, new ArrayList<JobDefinition>());
        jobDefinitionEditText.setAdapter(jobDefinitionAdapter);

        employeeAdapter = new EmployeePresentAdapter(this, new ArrayList<EmployeeInfo>());
        employeeEditText.setAdapter(employeeAdapter);

        getListEquipment();

        Intent intent = getIntent();
        isDetail = intent.getIntExtra("TYPE", MaintenanceActivity.TYPE_DETAIL) == MaintenanceActivity.TYPE_DETAIL;
        if (isDetail) {
            updateUITypeDetail(intent);
        }
    }

    private void updateUITypeDetail(Intent intent) {
        switchMode();
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        username = LoginPref.getUsername(this);
        id = intent.getIntExtra(MaintenanceJob.MAINTENANCE_JOB_ID, 0);
        isConfirm = intent.getBooleanExtra(MaintenanceJob.MAINTENANCE_JOB_CONFIRM, false);
        String equipmentId = intent.getStringExtra(MaintenanceJob.EQUIPMENT_ID);
        String equipName = intent.getStringExtra(MaintenanceJob.EQUIPMENT_NAME);
        String serialNumber = intent.getStringExtra(MaintenanceJob.SERIAL_NUMBER);
        String info = intent.getStringExtra(MaintenanceJob.MAINTENANCE_JOB_CREARED_BY);
        dept = intent.getStringExtra(MaintenanceJob.DEPT);
        String remark = intent.getStringExtra(MaintenanceJob.REMARK);
        String jobDate = intent.getStringExtra(MaintenanceJob.MAINTENANCE_JOB_DATE);
        float runningHour = intent.getFloatExtra(MaintenanceJob.RUNNING_HOUR, 0);
        String h1 = intent.getStringExtra(MaintenanceJob.TRUCK_KH);
        int h2 = intent.getIntExtra(MaintenanceJob.TRUCK_HL, 0);
        int h3 = intent.getIntExtra(MaintenanceJob.TRUCK_HD, 0);
        int h4 = intent.getIntExtra(MaintenanceJob.TRUCK_TM, 0);
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            calendar.setTimeInMillis(formatter.parse(jobDate).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        actionBarTitleView.setText(String.format(Locale.getDefault(), "MJ-%d~%s", id, jobDate));
        maintenanceInfo.setVisibility(View.VISIBLE);
        maintenanceInfo.setText(info);
        equipmentIdView.setText(equipmentId);
        equipmentNameView.setText(equipName);
        equipmentDeptView.setText(dept);
        equipmentSerialView.setText(serialNumber);
        maintenanceDateEditText.setText(jobDate);
        maintenanceDateEditText.setTag(Utilities.formatDateTime_ddMMyyHHmmFromMili(calendar.getTimeInMillis()));
        maintenanceDesET.setText(remark);
        runningHourET.setText(String.format(Locale.getDefault(), "%.1f", runningHour));
        h1ET.setText(h1);
        h2ET.setText(String.format(Locale.getDefault(), "%d", h2));
        h3ET.setText(String.format(Locale.getDefault(), "%d", h3));
        h4ET.setText(String.format(Locale.getDefault(), "%d", h4));

        getMaintenanceJobDaily();
    }

    private void getMaintenanceJobDaily() {

        if (!Utilities.isConnected(this)) {
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, snackBarView);
            dialog.dismiss();
            return;
        }
        MyRetrofit.initRequest(this)
                .getMaintenanceJobDaily(id)
                .enqueue(new Callback<List<JobDaily>>() {
                    @Override
                    public void onResponse(Response<List<JobDaily>> response, Retrofit retrofit) {
                        List<JobDaily> body = response.body();
                        if (response.isSuccess() && body != null) {
                            updateUIJobDaily(body);
                            getMaintenanceJobWriteOffs();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        dialog.dismiss();
                        RetrofitError.errorNoAction(getApplicationContext(), t, TAG, snackBarView);
                    }
                });

    }

    private void updateUIJobDaily(List<JobDaily> body) {
        int size = body.size();
        for (int i = 0; i < size; i++) {
            JobDaily item = body.get(i);
            addJobDailyView(item, i);
        }

    }

    private void getMaintenanceJobWriteOffs() {

        if (!Utilities.isConnected(this)) {
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, snackBarView);
            dialog.dismiss();
            return;
        }
        MyRetrofit.initRequest(this)
                .getMaintenanceJobWriteOffs(id)
                .enqueue(new Callback<List<WriteOff>>() {
                    @Override
                    public void onResponse(Response<List<WriteOff>> response, Retrofit retrofit) {
                        List<WriteOff> body = response.body();
                        if (response.isSuccess() && body != null) {
                            updateUIWriteOff(body);

                            getMaintenanceEmployee();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        dialog.dismiss();
                        RetrofitError.errorNoAction(getApplicationContext(), t, TAG, snackBarView);
                    }
                });

    }

    private void updateUIWriteOff(List<WriteOff> body) {
        int size = body.size();
        for (int i = 0; i < size; i++) {
            WriteOff item = body.get(i);
            addPartView(item, i);
        }
    }

    private void getMaintenanceEmployee() {

        if (!Utilities.isConnected(this)) {
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, snackBarView);
            dialog.dismiss();
            return;
        }
        MyRetrofit.initRequest(this)
                .getMaintenanceEmployee(new MaintenanceJobEmployeeParameter(id, username))
                .enqueue(new Callback<List<MaintenanceEmployee>>() {
                    @Override
                    public void onResponse(Response<List<MaintenanceEmployee>> response, Retrofit retrofit) {
                        List<MaintenanceEmployee> body = response.body();
                        if (response.isSuccess() && body != null) {
                            updateUIEmployee(body);
                            getListJobDefinition(dept);
                            getListPartRemain(dept);
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        dialog.dismiss();
                        RetrofitError.errorNoAction(getApplicationContext(), t, TAG, snackBarView);
                    }
                });

    }

    private void updateUIEmployee(List<MaintenanceEmployee> body) {
        int size = body.size();
        for (int i = 0; i < size; i++) {
            MaintenanceEmployee item = body.get(i);
            addEmployeeView(item, i);
        }
    }

    private void getListEquipment() {

        if (!Utilities.isConnected(this)) {
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, snackBarView);
            dialog.dismiss();
            return;
        }
        MyRetrofit.initRequest(this)
                .getListEquipment(new EquipmentParameter(userName))
                .enqueue(new Callback<List<Equipment>>() {
                    @Override
                    public void onResponse(Response<List<Equipment>> response, Retrofit retrofit) {
                        if (response.isSuccess() && response.body() != null) {
                            equipmentAdapter.clear();
                            equipmentAdapter.addAll(response.body());

                            getListEmployee();
                            if (!isDetail)
                                dialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        dialog.dismiss();
                        RetrofitError.errorNoAction(getApplicationContext(), t, TAG, snackBarView);
                    }
                });

    }

    private void getListJobDefinition(String departmentId) {

        if (!Utilities.isConnected(this)) {
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, snackBarView);
            return;
        }
        MyRetrofit.initRequest(this)
                .getListJobDefinition(new JobDefinitionParameter(userName, departmentId))
                .enqueue(new Callback<List<JobDefinition>>() {
                    @Override
                    public void onResponse(Response<List<JobDefinition>> response, Retrofit retrofit) {
                        if (response.isSuccess() && response.body() != null) {
                            jobDefinitionAdapter.clear();
                            jobDefinitionAdapter.addAll(response.body());

                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        RetrofitError.errorNoAction(getApplicationContext(), t, TAG, snackBarView);
                    }
                });

    }

    private void getListPartRemain(String departmentId) {

        if (!Utilities.isConnected(this)) {
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, snackBarView);
            return;
        }
        MyRetrofit.initRequest(this)
                .getPartRemain(new PartRemainParameter(departmentId))
                .enqueue(new Callback<List<PartRemain>>() {
                    @Override
                    public void onResponse(Response<List<PartRemain>> response, Retrofit retrofit) {
                        if (response.isSuccess() && response.body() != null) {
                            partRemainAdapter.clear();
                            partRemainAdapter.addAll(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        RetrofitError.errorNoAction(getApplicationContext(), t, TAG, snackBarView);
                    }
                });

    }

    private void getListEmployee() {

        if (!Utilities.isConnected(this)) {
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, snackBarView);
            return;
        }
        MyRetrofit.initRequest(this)
                .getEmployeeID(new EmployeePresentParameter(4, ""))
                .enqueue(new Callback<List<EmployeeInfo>>() {
                    @Override
                    public void onResponse(Response<List<EmployeeInfo>> response, Retrofit retrofit) {
                        if (response.isSuccess() && response.body() != null) {
                            employeeAdapter.clear();
                            employeeAdapter.addAll(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        RetrofitError.errorNoAction(getApplicationContext(), t, TAG, snackBarView);
                    }
                });

    }

    private void updateUIEquipment(Equipment item) {
        equipmentIdView.setText(item.getId());
        equipmentNameView.setText(item.getName());
        equipmentDeptView.setText(item.getDept());
        equipmentSerialView.setText(item.getSerialNumber());
        equipmentModel.setText(item.getModel());
    }

    private void insertMaintenanceJob() {
        if (equipmentIdView.getText().length() == 0) {
            Snackbar.make(snackBarView, getString(R.string.field_not_empty, getString(R.string.id)), Snackbar.LENGTH_SHORT).show();
            return;
        }
        if (maintenanceDateEditText.getText().length() == 0) {
            Snackbar.make(snackBarView, getString(R.string.field_not_empty, getString(R.string.label_maintenance_date)), Snackbar.LENGTH_SHORT).show();
            return;
        }
        for (JobDefinitionViewHolder holder : listJob) {
            if (holder.accidentView.getText().length() == 0) {
                Snackbar.make(snackBarView, getString(R.string.field_not_empty, "Accident"), Snackbar.LENGTH_SHORT).show();
                return;
            }
        }

        MaintenanceJobParameter parameter = new MaintenanceJobParameter(
                maintenanceDateEditText.getTag().toString(),
                equipmentIdView.getText().toString(),
                h1ET.getText().toString(),
                parserInt(h2ET.getText().toString()),
                parserInt(h3ET.getText().toString()),
                parserInt(h4ET.getText().toString()),
                0f,
                userName,
                parserFloat(runningHourET.getText().toString()),
                maintenanceDesET.getText().toString(),
                (float) calendar.get(Calendar.WEEK_OF_YEAR)
        );
        dialog.setMessage(getString(R.string.saving));
        dialog.show();
        indexEmployee = 0;
        indexPart = 0;
        indexJobDaily = 0;
        if (id == 0) {
            insertMaintenanceJob(parameter);
        } else {
            idMaintenanceJob = id;
            if (listJob.size() > 0)
                insertListJob();
            else if (listPart.size() > 0)
                insertPart();
            else if (listEmployee.size() > 0)
                insertEmployee();
            else {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), getString(R.string.success), Toast.LENGTH_SHORT).show();
                updateMode();
            }
        }

    }

    private void insertMaintenanceJob(MaintenanceJobParameter parameter) {

        if (!Utilities.isConnected(this)) {
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, snackBarView);
            return;
        }
        MyRetrofit.initRequest(this)
                .insertMaintenanceJob(parameter)
                .enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Response<Integer> response, Retrofit retrofit) {
                        if (response.isSuccess() && response.body() != null) {
                            idMaintenanceJob = response.body();
                            if (listJob.size() > 0)
                                insertListJob();
                            else if (listPart.size() > 0)
                                insertPart();
                            else if (listEmployee.size() > 0)
                                insertEmployee();
                            else {
                                dialog.dismiss();
                                Toast.makeText(getApplicationContext(), getString(R.string.success), Toast.LENGTH_SHORT).show();
                                updateMode();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        RetrofitError.errorNoAction(getApplicationContext(), t, TAG, snackBarView);
                    }
                });

    }

    private void insertListJob() {

        JobDefinitionViewHolder holder = listJob.get(indexJobDaily);
        JobDailyParameter parameter = new JobDailyParameter(
                holder.noteView.getText().toString(),
                holder.accidentView.getText().toString()
        );
        if (holder.isDetail) {
            parameter.setMaintenanceJobDailyID(parserInt(holder.descriptionView.getTag().toString()));
            updateMaintenanceJobDaily(parameter);
        } else {
            parameter.setJobDefinitionID(holder.descriptionView.getTag().toString());
            parameter.setMaintenanceJobID(idMaintenanceJob);
            holder.isDetail = true;
            insertMaintenanceJobDaily(parameter);
        }

    }

    private void insertMaintenanceJobDaily(JobDailyParameter parameter) {

        if (!Utilities.isConnected(this)) {
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, snackBarView);
            return;
        }
        MyRetrofit.initRequest(this)
                .insertMaintenanceJobDaily(parameter)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Response<String> response, Retrofit retrofit) {
                        if (response.isSuccess() && response.body() != null) {
                            if (listJob.size() - 1 > indexJobDaily) {
                                indexJobDaily++;
                                insertListJob();
                            } else if (listPart.size() > 0)
                                insertPart();
                            else if (listEmployee.size() > 0)
                                insertEmployee();
                            else {
                                dialog.dismiss();
                                Toast.makeText(getApplicationContext(), getString(R.string.success), Toast.LENGTH_SHORT).show();
                                updateMode();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        RetrofitError.errorNoAction(getApplicationContext(), t, TAG, snackBarView);
                    }
                });

    }

    private void updateMaintenanceJobDaily(JobDailyParameter parameter) {

        if (!Utilities.isConnected(this)) {
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, snackBarView);
            return;
        }
        MyRetrofit.initRequest(this)
                .updateMaintenanceJobDaily(parameter)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Response<String> response, Retrofit retrofit) {
                        if (response.isSuccess() && response.body() != null) {
                            if (listJob.size() - 1 > indexJobDaily) {
                                indexJobDaily++;
                                insertListJob();
                            } else if (listPart.size() > 0)
                                insertPart();
                            else if (listEmployee.size() > 0)
                                insertEmployee();
                            else {
                                dialog.dismiss();
                                Toast.makeText(getApplicationContext(), getString(R.string.success), Toast.LENGTH_SHORT).show();
                                updateMode();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        RetrofitError.errorNoAction(getApplicationContext(), t, TAG, snackBarView);
                    }
                });

    }

    private void insertPart() {
        PartRemainViewHolder holder = listPart.get(indexPart);
        WriteOffParameter parameter = new WriteOffParameter(
                parserInt(holder.quantityView.getText().toString()),
                holder.noteView.getText().toString()
        );
        if (holder.isDetail) {
            parameter.setMaintenanceJobWriteOffID((Integer) holder.descriptionView.getTag());
            updateMaintenanceJobWriteOffs(parameter);
        } else {
            parameter.setPartID((Integer) holder.descriptionView.getTag());
            parameter.setMaintenanceJobID(idMaintenanceJob);
            holder.isDetail = true;
            insertMaintenanceJobWriteOffs(parameter);
        }

    }

    private void insertMaintenanceJobWriteOffs(WriteOffParameter parameter) {

        if (!Utilities.isConnected(this)) {
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, snackBarView);
            return;
        }
        MyRetrofit.initRequest(this)
                .insertMaintenanceJobWriteOffs(parameter)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Response<String> response, Retrofit retrofit) {
                        if (response.isSuccess() && response.body() != null) {
                            if (listPart.size() - 1 > indexPart) {
                                indexPart++;
                                insertPart();
                            } else if (listEmployee.size() > 0)
                                insertEmployee();
                            else {
                                dialog.dismiss();
                                Toast.makeText(getApplicationContext(), getString(R.string.success), Toast.LENGTH_SHORT).show();
                                updateMode();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        RetrofitError.errorNoAction(getApplicationContext(), t, TAG, snackBarView);
                    }
                });

    }

    private void updateMaintenanceJobWriteOffs(WriteOffParameter parameter) {

        if (!Utilities.isConnected(this)) {
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, snackBarView);
            return;
        }
        MyRetrofit.initRequest(this)
                .updateMaintenanceJobWriteOffs(parameter)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Response<String> response, Retrofit retrofit) {
                        if (response.isSuccess() && response.body() != null) {
                            if (listPart.size() - 1 > indexPart) {
                                indexPart++;
                                insertPart();
                            } else if (listEmployee.size() > 0)
                                insertEmployee();
                            else {
                                dialog.dismiss();
                                Toast.makeText(getApplicationContext(), getString(R.string.success), Toast.LENGTH_SHORT).show();
                                updateMode();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        RetrofitError.errorNoAction(getApplicationContext(), t, TAG, snackBarView);
                    }
                });

    }

    private void insertEmployee() {
        EmployeeViewHolder holder = listEmployee.get(indexEmployee);
        MMSEmployeeParameter parameter = new MMSEmployeeParameter(
                holder.evaluationCB.isChecked(),
                holder.noteET.getText().toString(),
                parserFloat(holder.durationET.getText().toString()),
                parserFloat(holder.otET.getText().toString()),
                userName
        );
        if (holder.isDetail) {
            parameter.setEmployeeWorkingID(parserInt(holder.idView.getTag().toString()));
            updateMaintenanceEmployee(parameter);
        } else {
            parameter.setEmployeeID(parserInt(holder.idView.getTag().toString()));
            parameter.setMaintenanceJobID(idMaintenanceJob);
            holder.isDetail = true;
            insertMaintenanceEmployee(parameter);
        }

    }

    private void insertMaintenanceEmployee(MMSEmployeeParameter parameter) {

        if (!Utilities.isConnected(this)) {
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, snackBarView);
            return;
        }
        MyRetrofit.initRequest(this)
                .insertMaintenanceEmployee(parameter)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Response<String> response, Retrofit retrofit) {
                        if (response.isSuccess() && response.body() != null) {
                            if (listEmployee.size() - 1 > indexEmployee) {
                                indexEmployee++;
                                insertEmployee();
                            } else {
                                dialog.dismiss();
                                Toast.makeText(getApplicationContext(), getString(R.string.success), Toast.LENGTH_SHORT).show();
                                updateMode();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        RetrofitError.errorNoAction(getApplicationContext(), t, TAG, snackBarView);
                    }
                });

    }

    private void updateMaintenanceEmployee(MMSEmployeeParameter parameter) {

        if (!Utilities.isConnected(this)) {
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, snackBarView);
            return;
        }
        MyRetrofit.initRequest(this)
                .updateMaintenanceEmployee(parameter)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Response<String> response, Retrofit retrofit) {
                        if (response.isSuccess() && response.body() != null) {
                            if (listEmployee.size() - 1 > indexEmployee) {
                                indexEmployee++;
                                insertEmployee();
                            } else {
                                dialog.dismiss();
                                Toast.makeText(getApplicationContext(), getString(R.string.success), Toast.LENGTH_SHORT).show();
                                updateMode();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        RetrofitError.errorNoAction(getApplicationContext(), t, TAG, snackBarView);
                    }
                });

    }

    private void deleteJobDaily(String id) {
        if (!Utilities.isConnected(this)) {
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, snackBarView);
            return;
        }
        MyRetrofit.initRequest(this)
                .deleteMaintenanceJobDaily(parserInt(id))
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Response<String> response, Retrofit retrofit) {

                    }

                    @Override
                    public void onFailure(Throwable t) {
                        RetrofitError.errorNoAction(getApplicationContext(), t, TAG, snackBarView);
                    }
                });
    }

    private void deletePart(String id) {
        if (!Utilities.isConnected(this)) {
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, snackBarView);
            return;
        }
        MyRetrofit.initRequest(this)
                .deleteMaintenanceJobWriteOffs(parserInt(id))
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Response<String> response, Retrofit retrofit) {
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        RetrofitError.errorNoAction(getApplicationContext(), t, TAG, snackBarView);
                    }
                });

    }

    private void deleteEmployee(int id) {
        MMSEmployeeParameter parameter = new MMSEmployeeParameter();
        parameter.setEmployeeWorkingID(id);
        parameter.setUserName(userName);
        if (!Utilities.isConnected(this)) {
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, snackBarView);
            return;
        }
        MyRetrofit.initRequest(this)
                .deleteMaintenanceEmployee(parameter)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Response<String> response, Retrofit retrofit) {
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        RetrofitError.errorNoAction(getApplicationContext(), t, TAG, snackBarView);
                    }
                });

    }

    @Override
    public void onClick(View v) {
        if (v == maintenanceDateEditText) {
            if (!isDetail)
                pickDate();
        } else if (v == equipmentEditText || v == jobDefinitionEditText || v == partRemainEditText || v == employeeEditText)
            ((AutoCompleteTextView) v).showDropDown();

    }

    private void pickDate() {
        int yearNow = calendar.get(Calendar.YEAR);
        int monthOfYearNow = calendar.get(Calendar.MONTH);
        int dayOfMonthNow = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(year, monthOfYear, dayOfMonth);
                maintenanceDateEditText.setTag(Utilities.formatDateTime_ddMMyyHHmmFromMili(calendar.getTimeInMillis()));
                maintenanceDateEditText.setText(String.format(Locale.getDefault(), "%d/%d/%d", dayOfMonth, monthOfYear + 1, year));

            }
        }, yearNow, monthOfYearNow, dayOfMonthNow);
        datePicker.show();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus)
            ((AutoCompleteTextView) v).setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cud, menu);
        itemDone = menu.findItem(R.id.action_done);
        itemEdit = menu.findItem(R.id.action_edit);
        itemDelete = menu.findItem(R.id.action_delete);
        itemCreate = menu.findItem(R.id.action_create);

        if (isDetail) {
            itemDone.setVisible(false);
            if (isConfirm)
                itemDelete.setVisible(false);
        } else {
            itemEdit.setVisible(false);
            itemDelete.setVisible(false);
            itemCreate.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item == itemDone) {
            insertMaintenanceJob();
        } else if (item == itemEdit) {
            itemEdit.setVisible(false);
            itemDone.setVisible(true);
            isDetail = false;
            switchMode();
        } else if (item == itemDelete) {
            if (listJob.size() > 0 || listPart.size() > 0 || listEmployee.size() > 0) {
                Toast.makeText(getApplicationContext(), getString(R.string.alert_delete_all_detail_before), Toast.LENGTH_LONG).show();
            } else {
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setMessage(getString(R.string.alert_delete))
                        .setPositiveButton(getString(R.string.label_delete), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteMaintenanceJob();
                            }
                        })
                        .setNegativeButton(getString(R.string.label_cancel), null)
                        .create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }

        } else if (item == itemCreate) {
            Intent intent = new Intent(getApplicationContext(), CreateMaintenanceActivity.class);
            intent.putExtra("TYPE", MaintenanceActivity.TYPE_CREATE);
            startActivity(intent);
        }

        return true;
    }

    private void deleteMaintenanceJob() {
        dialog.setMessage(getString(R.string.deleting));
        dialog.show();
        if (!Utilities.isConnected(this)) {
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, snackBarView);
            return;
        }
        MyRetrofit.initRequest(this)
                .deleteMaintenanceJob(id)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Response<String> response, Retrofit retrofit) {
                        if (response.isSuccess() && response.body() != null) {
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(), getString(R.string.success), Toast.LENGTH_SHORT).show();
                            updateMode();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        RetrofitError.errorNoAction(getApplicationContext(), t, TAG, snackBarView);
                    }
                });

    }

    private void switchMode() {
        containerView.setDescendantFocusability(isDetail ? ViewGroup.FOCUS_BLOCK_DESCENDANTS : ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        equipmentEditText.setVisibility(isDetail ? View.GONE : View.VISIBLE);
        partRemainEditText.setVisibility(isDetail ? View.GONE : View.VISIBLE);
        jobDefinitionEditText.setVisibility(isDetail ? View.GONE : View.VISIBLE);
        employeeEditText.setVisibility(isDetail ? View.GONE : View.VISIBLE);
        maintenanceTitleCreateView.setText(isDetail ? getString(R.string.label_created) : getString(R.string.label_maintenance_date));
        maintenanceInfo.setVisibility(isDetail ? View.VISIBLE : View.GONE);
        maintenanceDateEditText.setVisibility(isDetail ? View.GONE : View.VISIBLE);
        viewEmployee.setVisibility(isDetail ? View.GONE : View.VISIBLE);
        viewJob.setVisibility(isDetail ? View.GONE : View.VISIBLE);

        for (JobDefinitionViewHolder holder : listJob)
            holder.jobRemoveView.setVisibility(isDetail ? View.GONE : View.VISIBLE);
        for (PartRemainViewHolder holder : listPart)
            holder.partRemoveView.setVisibility(isDetail ? View.GONE : View.VISIBLE);
        for (EmployeeViewHolder holder : listEmployee) {
            holder.employeeRemoveView.setVisibility(isDetail ? View.GONE : View.VISIBLE);
            holder.evaluationCB.setEnabled(!isDetail);
        }

    }

    private void updateMode() {
        isDetail = true;
        isUpdated = true;
        switchMode();
    }

    @Override
    public void onBackPressed() {
        if (isUpdated)
            setResult(RESULT_OK, new Intent());
        super.onBackPressed();
    }

    private class JobDefinitionViewHolder {
        View viewJobDefinition;
        EditText accidentView;
        EditText descriptionView;
        EditText noteView;
        ImageView jobRemoveView;
        boolean isDetail;
    }

    private class PartRemainViewHolder {
        View viewPartRemain;
        EditText quantityView;
        EditText descriptionView;
        EditText noteView;
        ImageView partRemoveView;
        boolean isDetail;
    }

    private class EmployeeViewHolder {
        View viewEmployee;
        EditText idView;
        EditText durationET;
        EditText otET;
        EditText noteET;
        AppCompatCheckBox evaluationCB;
        ImageView employeeRemoveView;
        boolean isDetail;
    }
}
