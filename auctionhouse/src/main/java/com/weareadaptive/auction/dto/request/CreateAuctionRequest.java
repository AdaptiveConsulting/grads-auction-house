package com.weareadaptive.auction.dto.request;


import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public record CreateAuctionRequest(
        //        @Pattern(regexp = "^[A-Za-z0-9.]+$")
        @NotBlank
        @Size(max = 7)
        String symbol,

        @NotNull
        @Positive
        @DecimalMin(value = "0.1")
        float minPrice,

        @NotNull
        @Positive
        @Min(1)
        int quantity
) {
}
