package practice.application.models.DTO;

import lombok.Data;

@Data
public class ExResponseDTO {

    private String code;
    private String message;

    public ExResponseDTO(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public ExResponseDTO() {
    }
}
