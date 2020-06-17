package com.dbls.api.dto;

import lombok.Data;

@Data
public class CurrentShiftsResponse {
    private String shiftOneStart;
    private String shiftOneEnd;
    private String shiftTwoStart;
    private String shiftTwoEnd;
    private String shiftThreeStart;
    private String shiftThreeEnd;
}
