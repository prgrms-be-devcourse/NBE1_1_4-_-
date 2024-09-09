package practice.application.models;

public enum OrderStatus {
    ORDER,      //주문 요청 완료(=배송 전)
    DELIVERY,   //배송 처리 완료
    CANCELED   // 주문 취소
}
