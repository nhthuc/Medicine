package com.example.medicine;

import android.content.Context;
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
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final Medicine medicine = medicineList.get(i);

        viewHolder.txtMedicine.setText(medicine.getNameMedicine());

        viewHolder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.dialogUpdate(medicine.getNameMedicine(), medicine.getId());
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
