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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.medicine.R.menu.add_medicine;

public class MainActivity extends AppCompatActivity {

    Database database;
    ListView lvMedicine;
    EditText edtFind;
    ArrayList<Medicine> arrMedicines;
    MedicineAdapter adapter;
    String selectAll = "SELECT * FROM Medicine ORDER BY TenM ASC";
//    String selectAll = "SELECT * FROM Medicine";

    MainActivity c = MainActivity.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtFind = findViewById(R.id.editTextFind);
        lvMedicine = findViewById(R.id.listMedicine);
        database = new Database(c, "Medicine.SQLite", null, 1);
        database.QueryData("CREATE TABLE IF NOT EXISTS Medicine(Id INTEGER PRIMARY KEY AUTOINCREMENT, TenM VARCHAR(200))");

        arrMedicines = new ArrayList<>();
        adapter = new MedicineAdapter(c, R.layout.item_medicine, arrMedicines);
//        Log.v("onCreate", arrMedicines.get(arrMedicines.size()-1).getNameMedicine());
        lvMedicine.setAdapter(adapter);

//        database.QueryData("INSERT INTO Medicine VALUES(null, 'ytt')");
//        database.QueryData("INSERT INTO Medicine VALUES(null, 'unnng')");


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
                    String sqlFind = "SELECT * FROM Medicine WHERE TenM LIKE '%"+strFind+"%'";
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

    //String selectAll = "SELECT * FROM Medicine";
    public void getDataMedicine(String sql){
        Cursor dataMedicine = database.GetData(sql);
        arrMedicines.clear();
        while (dataMedicine.moveToNext()){
            String name = dataMedicine.getString(1);
            int id = dataMedicine.getInt(0);
            arrMedicines.add(new Medicine(id, name));
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
        return super.onOptionsItemSelected(item);
    }

    private void dialogAdd(){
        final Dialog dialog = new Dialog(c);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_medicine);

        final EditText editAdd = dialog.findViewById(R.id.editAddMedicine);
        final Button btnAdd = dialog.findViewById(R.id.btnAdd);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String editName = editAdd.getText().toString();
                if (editName.equals("")) {
                    Toast.makeText(c, "Vui lòng nhập tên thuốc!", Toast.LENGTH_SHORT).show();
                }else {
                    database.QueryData("INSERT INTO Medicine VALUES(null, '" + editName + "')");
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
    public void dialogUpdate(String name, final int id){
        final Dialog dialog = new Dialog(c);
        dialog.setContentView(R.layout.dialog_update_medicine);

        final EditText edtUpdate = dialog.findViewById(R.id.editUpdateMedicine);
        Button btnUpdate = dialog.findViewById(R.id.btnUpdate);
        Button btnCancel = dialog.findViewById(R.id.btnUpdateCancel);

        edtUpdate.setText(name);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newName = edtUpdate.getText().toString().trim();
                database.QueryData("UPDATE Medicine SET TenM = '"+ newName +"' WHERE Id = '"+ id +"'");
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
        dialog.setMessage("Bạn có muốn xóa "+ name+" này không?");
        dialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                database.QueryData("DELETE FROM Medicine WHERE Id = '"+ id +"'");
                Toast.makeText(c, "Đã xóa xong! " + name, Toast.LENGTH_SHORT).show();
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

}
