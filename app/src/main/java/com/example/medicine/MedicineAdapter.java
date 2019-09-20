package com.example.medicine;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MedicineAdapter extends BaseAdapter {
    MainActivity context;
    private int layout;
    private List<Medicine> medicineList;

    public MedicineAdapter(MainActivity context, int layout, List<Medicine> medicineList) {
        this.context = context;
        this.layout = layout;
        this.medicineList = medicineList;
    }

    @Override
    public int getCount() {
        return medicineList.size();
    }

    @Override
    public Medicine getItem(int i) {
        return medicineList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private class ViewHolder{
        TextView txtMedicine;
        ImageView imgEdit;
        TextView txtStatus;
        TextView txtFullName;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);
            viewHolder.txtMedicine = view.findViewById(R.id.txtItemMedicine);
            viewHolder.imgEdit = view.findViewById(R.id.imageEdit);
            viewHolder.txtStatus = view.findViewById(R.id.txtStatus);
            viewHolder.txtFullName = view.findViewById(R.id.txtFullName);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final Medicine medicine = medicineList.get(i);

        viewHolder.txtMedicine.setText(medicine.getNameMedicine());
        viewHolder.txtFullName.setText(medicine.getNameMedicine());

        if (medicine.getStatus() == 0){
            viewHolder.txtStatus.setText("DV");
            viewHolder.txtStatus.setTextColor(Color.RED);
        } else {
            viewHolder.txtStatus.setText("BH");
            viewHolder.txtStatus.setTextColor(Color.BLUE);
        }


        viewHolder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.dialogUpdate(medicine.getNameMedicine(), medicine.getId(), medicine.getStatus());
            }
        });

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                context.dialogDelete(medicine.getNameMedicine(), medicine.getId());
                return false;
            }
        });
        return view;
    }
}
