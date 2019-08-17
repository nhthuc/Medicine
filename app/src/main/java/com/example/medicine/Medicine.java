package com.example.medicine;

public class Medicine {
    private int Id;
    private String NameMedicine;

    public Medicine(int id, String nameMedicine) {
        Id = id;
        NameMedicine = nameMedicine;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getNameMedicine() {
        return NameMedicine;
    }

    public void setNameMedicine(String nameMedicine) {
        NameMedicine = nameMedicine;
    }
}
