package practice.application.models.enumType;

public enum UserType {
    CUSTOMER("ROLE_CUSTOMER"),
    ADMIN("ROLE_ADMIN");

    private String description;

    UserType(String description) {
        this.description = description;
    }

    public String getDescription() {return description;}

    // 문자열로부터 enum 상수를 찾는 메서드
    public static UserType fromRole(String role) {
        for (UserType userType : UserType.values()) {
            if (userType.getDescription().equalsIgnoreCase(role)) {
                return userType;
            }
        }
        throw new IllegalArgumentException("No enum constant with role " + role);
    }
}
