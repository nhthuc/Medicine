package com.example.medicine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.medicine.R.menu.add_medicine;

public class MainActivity extends AppCompatActivity {

    Database database;
    ListView lvMedicine;
    EditText edtFind;
    TextView txtStatus;
    ArrayList<Medicine> arrMedicines;
    MedicineAdapter adapter;
    String selectAll = "SELECT * FROM Medicine_v1 ORDER BY TenM ASC";
    RadioGroup radioGroup;

    MainActivity c = MainActivity.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtFind = findViewById(R.id.editTextFind);
        lvMedicine = findViewById(R.id.listMedicine);
        txtStatus = findViewById(R.id.txtStatus);
        database = new Database(c, "Medicine_v1.SQLite", null, 1);
        database.QueryData("CREATE TABLE IF NOT EXISTS Medicine_v1(Id INTEGER PRIMARY KEY AUTOINCREMENT, TenM VARCHAR(200), Status INTEGER)");

        arrMedicines = new ArrayList<>();
        adapter = new MedicineAdapter(c, R.layout.item_medicine, arrMedicines);
        lvMedicine.setAdapter(adapter);

        Cursor dataMedicine_1 = database.GetData(selectAll);
        if (dataMedicine_1.getCount() == 0){
            InsertData();
        }

        getDataMedicine(selectAll);
        edtFind.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String strFind = edtFind.getText().toString().trim();
                if (strFind.equals(""))
                {
                    getDataMedicine(selectAll);
                }else {
                    String sqlFind = "SELECT * FROM Medicine_v1 WHERE TenM LIKE '%"+strFind+"%'";
                    c.getDataMedicine(sqlFind);
                    if (arrMedicines.isEmpty()){
                        Toast.makeText(c, "Không tìm thấy!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void getDataMedicine(String sql){
        Cursor dataMedicine = database.GetData(sql);
        arrMedicines.clear();
        while (dataMedicine.moveToNext()){
            String name = dataMedicine.getString(1);
            int id = dataMedicine.getInt(0);
            int status = dataMedicine.getInt(2);
            arrMedicines.add(new Medicine(id, name, status));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(add_medicine, menu);

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menuAdd){
            dialogAdd();
        }
        if (item.getItemId() == R.id.menuReset){
            resetData();
        }
        return super.onOptionsItemSelected(item);
    }
    private void resetData(){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(c);
        dialog.setMessage("Bạn có muốn khôi phục dữ liệu gốc không?");
        dialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                database.QueryData("DELETE FROM Medicine_v1");
                Toast.makeText(c, "Đã khôi phục thành công!", Toast.LENGTH_SHORT).show();
                InsertData();
                getDataMedicine(selectAll);
            }
        });
        dialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();
    }

    private void dialogAdd(){
        final Dialog dialog = new Dialog(c);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_medicine);

        final EditText editAdd = dialog.findViewById(R.id.editAddMedicine);
        final Button btnAdd = dialog.findViewById(R.id.btnAdd);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        final RadioGroup radioGroup = dialog.findViewById(R.id.radioGroup);
        final int[] check = {-1};
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.radioBHYT:
                        check[0] = 1;
                        break;
                    case R.id.radioService:
                        check[0] = 0;
                        break;
                }
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String editName = editAdd.getText().toString();
                if (editName.equals("")) {
                    Toast.makeText(c, "Vui lòng nhập tên thuốc!", Toast.LENGTH_SHORT).show();
                }else {
                    database.QueryData("INSERT INTO Medicine_v1 VALUES(null, '" + editName + "', '" + check[0] +"')");
                    Toast.makeText(c, "Đã thêm thành công!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    getDataMedicine(selectAll);
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    public void dialogUpdate(String name, final int id, final int status){
        final Dialog dialog = new Dialog(c);
        dialog.setContentView(R.layout.dialog_update_medicine);

        final EditText edtUpdate = dialog.findViewById(R.id.editUpdateMedicine);
        Button btnUpdate = dialog.findViewById(R.id.btnUpdate);
        Button btnCancel = dialog.findViewById(R.id.btnUpdateCancel);
        RadioGroup radioGroup = dialog.findViewById(R.id.radioUpdateGroup);
        RadioButton radioUpdateService = dialog.findViewById(R.id.radioUpdateService);
        RadioButton radioUpdateBHYT = dialog.findViewById(R.id.radioUpdateBHYT);
        final int[] check = {-1};
        if (status == 0){
            radioUpdateService.setChecked(true);
            check[0] = 0;
        } else {
            radioUpdateBHYT.setChecked(true);
            check[0] = 1;
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radioUpdateBHYT:
                        check[0] = 1;
                        break;
                    case R.id.radioUpdateService:
                        check[0] = 0;
                        break;
                }
            }
        });
        edtUpdate.setText(name);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newName = edtUpdate.getText().toString().trim();
                database.QueryData("UPDATE Medicine_v1 SET TenM = '"+ newName +"', Status = '" + check[0] + "' WHERE Id = '"+ id +"'");
                Toast.makeText(c, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                getDataMedicine(selectAll);
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    public void dialogDelete(final String name, final int id){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(c);
        dialog.setMessage("Bạn có muốn xóa ( "+ name+" ) này không?");
        dialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                database.QueryData("DELETE FROM Medicine_v1 WHERE Id = '"+ id +"'");
                Toast.makeText(c, "Đã xóa xong! (" + name + ")", Toast.LENGTH_SHORT).show();
                getDataMedicine(selectAll);
            }
        });
        dialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();
    }

    public void InsertData(){
        //BHYT
        database.QueryData("INSERT INTO Medicine_v1 VALUES(null, 'Ranitidin 300mg', 1)");
        database.QueryData("INSERT INTO Medicine_v1 VALUES(null, 'Zinmax-Domesco 500mg', 1)");
        database.QueryData("INSERT INTO Medicine_v1 VALUES(null, 'Humalog Mix 75/25 Kwikpen', 1)");
        database.QueryData("INSERT INTO Medicine_v1 VALUES(null, 'Viên nang Kupitral', 1)");
        database.QueryData("INSERT INTO Medicine_v1 VALUES(null, 'Vorifend Forte', 1)");
        database.QueryData("INSERT INTO Medicine_v1 VALUES(null, 'MASAK', 1)");
        database.QueryData("INSERT INTO Medicine_v1 VALUES(null, 'AGIRENYL', 1)");
        database.QueryData("INSERT INTO Medicine_v1 VALUES(null, 'Vitamin E 400 IU', 1)");
        database.QueryData("INSERT INTO Medicine_v1 VALUES(null, 'Mezapizin 10', 1)");
        database.QueryData("INSERT INTO Medicine_v1 VALUES(null, 'VASLOR 10', 1)");
        database.QueryData("INSERT INTO Medicine_v1 VALUES(null, 'BESALICYD', 1)");
        database.QueryData("INSERT INTO Medicine_v1 VALUES(null, 'MATERAZZI', 1)");
        //service
        database.QueryData("INSERT INTO Medicine_v1 VALUES(null, 'Depo- Medrol 40mg/ml', 0)");
        database.QueryData("INSERT INTO Medicine_v1 VALUES(null, 'Syseye 10ml', 0)");
        database.QueryData("INSERT INTO Medicine_v1 VALUES(null, 'Detriat 100mg', 0)");
        database.QueryData("INSERT INTO Medicine_v1 VALUES(null, 'No-spa 40mg tab', 0)");
        database.QueryData("INSERT INTO Medicine_v1 VALUES(null, 'Albendazol stada 400mg', 0)");
        database.QueryData("INSERT INTO Medicine_v1 VALUES(null, 'SEAWA 70ml', 0)");
        database.QueryData("INSERT INTO Medicine_v1 VALUES(null, 'Bộ Rinorin rửa mũi xoang', 0)");
        database.QueryData("INSERT INTO Medicine_v1 VALUES(null, 'Scanax 500', 0)");
        database.QueryData("INSERT INTO Medicine_v1 VALUES(null, 'Lefvox-500', 0)");
        database.QueryData("INSERT INTO Medicine_v1 VALUES(null, 'Omeprazol DHG', 0)");
        database.QueryData("INSERT INTO Medicine_v1 VALUES(null, 'Dudencer', 0)");
        database.QueryData("INSERT INTO Medicine_v1 VALUES(null, 'Stadnex 40 CAP', 0)");
        database.QueryData("INSERT INTO Medicine_v1 VALUES(null, 'Stadnex 20 CAP', 0)");
        database.QueryData("INSERT INTO Medicine_v1 VALUES(null, 'Capesto 20', 0)");
        database.QueryData("INSERT INTO Medicine_v1 VALUES(null, 'Capesto 40', 0)");
        database.QueryData("INSERT INTO Medicine_v1 VALUES(null, 'Spasmavérin Alverine', 0)");
        database.QueryData("INSERT INTO Medicine_v1 VALUES(null, 'Dismin 500', 0)");
        database.QueryData("INSERT INTO Medicine_v1 VALUES(null, 'Stadpizide 50', 0)");
        database.QueryData("INSERT INTO Medicine_v1 VALUES(null, 'Neuropyl 3g', 0)");
        database.QueryData("INSERT INTO Medicine_v1 VALUES(null, 'Hapacol Blue', 0)");
        database.QueryData("INSERT INTO Medicine_v1 VALUES(null, 'Hapacol 650', 0)");
        database.QueryData("INSERT INTO Medicine_v1 VALUES(null, 'Lostad T25', 0)");
        database.QueryData("INSERT INTO Medicine_v1 VALUES(null, 'Hafenthyl 200', 0)");
        database.QueryData("INSERT INTO Medicine_v1 VALUES(null, 'Metronidazol Stada 400mg', 0)");
        database.QueryData("INSERT INTO Medicine_v1 VALUES(null, 'Clarithromycin Stada 500mg', 0)");
        database.QueryData("INSERT INTO Medicine_v1 VALUES(null, 'Be-stedy 16', 0)");
        database.QueryData("INSERT INTO Medicine_v1 VALUES(null, 'Calci-D3', 0)");
        database.QueryData("INSERT INTO Medicine_v1 VALUES(null, 'Naxyfresh Tab. 100mg', 0)");

    }
}
