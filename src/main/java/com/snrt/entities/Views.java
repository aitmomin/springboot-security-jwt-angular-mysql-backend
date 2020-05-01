package com.snrt.entities;

import lombok.*;
import javax.persistence.EmbeddedId;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@AssociationOverrides({
        @AssociationOverride(name = "key.user",
                joinColumns = @JoinColumn(name = "idUser")),
        @AssociationOverride(name = "key.track",
                joinColumns = @JoinColumn(name = "idTrack")) })
@Data
@NoArgsConstructor @AllArgsConstructor @ToString
public class Views implements Serializable {
    @EmbeddedId
    private ViewsID key = new ViewsID();
    private long views = 0;
}
