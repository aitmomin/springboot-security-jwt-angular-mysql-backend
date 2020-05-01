package com.snrt.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@AssociationOverrides({
        @AssociationOverride(name = "primaryKey.user_advrt",
                joinColumns = @JoinColumn(name = "idUser")),
        @AssociationOverride(name = "primaryKey.advertisement",
                joinColumns = @JoinColumn(name = "idAdvert")) })
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Advert_User implements Serializable {
    @EmbeddedId
    private AdvertID primaryKey = new AdvertID();
    private boolean isViewed;
}
