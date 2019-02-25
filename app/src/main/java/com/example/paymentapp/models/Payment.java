package com.example.paymentapp.models;

public class Payment {

    // Attributes
    private String amount;
    private Bank bank;
    private PayerCost payerCost;
    private PaymentMethod paymentMethod;
    private User user;

    // Getters && Setters
    public String getAmount() {
        return amount;
    }
    public void setAmount(String amount) {
        this.amount = amount;
    }
    public Bank getBank() {
        return bank;
    }
    public void setBank(Bank bank) {
        this.bank = bank;
    }
    public PayerCost getPayerCost() {
        return payerCost;
    }
    public void setPayerCost(PayerCost payerCost) {
        this.payerCost = payerCost;
    }
    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }
    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

}