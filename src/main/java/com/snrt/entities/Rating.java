package com.snrt.entities;

import lombok.*;
import javax.persistence.EmbeddedId;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@AssociationOverrides({
        @AssociationOverride(name = "primaryKey.user",
                joinColumns = @JoinColumn(name = "idUser")),
        @AssociationOverride(name = "primaryKey.track",
                joinColumns = @JoinColumn(name = "idTrack")) })
@Data @NoArgsConstructor @AllArgsConstructor @ToString
public class Rating implements Serializable {
    @EmbeddedId
    private RatingID primaryKey = new RatingID();
    private boolean rating;
}
