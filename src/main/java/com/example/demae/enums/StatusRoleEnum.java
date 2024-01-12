package com.example.demae.enums;

public enum StatusRoleEnum {
    WAIT(OrderStatus.WAIT),
    COMPLETE(OrderStatus.COMPLETE),
    END(OrderStatus.END);

    private final String orderStatus;

    StatusRoleEnum(String orderStatus){
        this.orderStatus = orderStatus;
    }
    public String getOrderStatus(){
        return this.orderStatus;
    }
    public static class OrderStatus{
        public static final String WAIT ="WAIT";
        public static final String COMPLETE ="COMPLETE";
        public static final String END ="END";
    }
}
