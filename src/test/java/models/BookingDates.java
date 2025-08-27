package models;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingDates {
    private String checkin;
    private String checkout;
}