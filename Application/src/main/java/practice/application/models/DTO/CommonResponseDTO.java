package practice.application.models.DTO;

import lombok.Data;

@Data
public class CommonResponseDTO {
    private String value;

    public CommonResponseDTO(String value) {
        this.value = value;
    }

    public CommonResponseDTO() {
    }
}
