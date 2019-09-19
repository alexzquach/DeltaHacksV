package com.kqsoft.expensetutorken;

public class SpinnerObject {
    private  int Id;
    private String Name;
    private  int Defaultflag;


    public SpinnerObject ( int Id , String Name, int Defaultflag ) {
        this.Id = Id;
        this.Name = Name;
        this.Defaultflag = Defaultflag;
    }

    public int getId () {
        return Id;
    }

    public String getName () {
        return Name;
    }

    public int getDefaultflag () {
        return Defaultflag;
    }


    //this display label on spinner
    @Override
    public String toString () {
        return Name;
    }

}
