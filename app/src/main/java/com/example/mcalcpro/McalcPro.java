package com.example.mcalcpro;

public class McalcPro {
    private double principle;
    private double amortization;
    private double irate;

    public McalcPro(String p, String a , String i){


        this.principle = Double.parseDouble(p);
        this.amortization = Double.parseDouble(a);
        this.irate = Double.parseDouble(i);


    }


}
