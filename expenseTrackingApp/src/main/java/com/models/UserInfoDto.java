package com.models;


import com.entities.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInfoDto extends User
{
    @NonNull
    private String firstName; // first_name

    @NonNull
    private String lastName;//last_name

    @NonNull
    private Long phoneNumber;

    @NonNull
    private String email; // email


}
