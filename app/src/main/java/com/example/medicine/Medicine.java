package com.example.medicine;

public class Medicine {
    private int Id;
    private int Status; //0: Dich vụ , 1: BHYT
    private String NameMedicine;

    public Medicine(int id, String nameMedicine, int status) {
        Id = id;
        Status = status;
        NameMedicine = nameMedicine;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getStatus(){
        return Status;
    }

    public void setStatus(int status)
    {
        Status = status;
    }

    public String getNameMedicine() {
        return NameMedicine;
    }

    public void setNameMedicine(String nameMedicine) {
        NameMedicine = nameMedicine;
    }
}
