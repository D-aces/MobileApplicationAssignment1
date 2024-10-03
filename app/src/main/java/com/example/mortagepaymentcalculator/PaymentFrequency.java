package com.example.mortagepaymentcalculator;

public enum PaymentFrequency {
    WEEKLY("weekly", 52),
    RAPID_WEEKLY("rapid weekly", 52),
    BI_WEEKLY("bi-weekly", 26),
    RAPID_BI_WEEKLY("rapid bi-weekly", 26),
    MONTHLY("monthly", 12),
    SEMI_MONTHLY("semi-monthly", 24);

    private final String displayName;
    private final int paymentsPerYear;

    PaymentFrequency(String displayName, int paymentsPerYear) {
        this.displayName = displayName;
        this.paymentsPerYear = paymentsPerYear;
    }

    public String getDisplayName() {
        return displayName;
    }
    public int getPaymentsPerYear(){
        return paymentsPerYear;
    }
}
