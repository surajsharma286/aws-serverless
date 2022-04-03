package com.suraj.aws.lambda.dto;

public class CustomerCheckoutEvent {
    private String customerNo;
    private String firstName;
    private String lastName;

    public CustomerCheckoutEvent() {
    }

    public CustomerCheckoutEvent(String customerNo, String firstName, String middleName, String lastName) {
        this.customerNo = customerNo;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "CustomerCheckoutEvent{" +
                "customerNo='" + customerNo + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
