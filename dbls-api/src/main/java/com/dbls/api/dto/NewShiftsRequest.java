package com.dbls.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class NewShiftsRequest {
    private String shiftOneStart;
    private String shiftOneEnd;
    private String shiftTwoStart;
    private String shiftTwoEnd;
    private String shiftThreeStart;
    private String shiftThreeEnd;
}
