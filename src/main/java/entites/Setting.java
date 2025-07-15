package entites;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Setting {
    private Integer settingId;
    private String settingName;
    private String settingType;
    private String settingValue;
}
