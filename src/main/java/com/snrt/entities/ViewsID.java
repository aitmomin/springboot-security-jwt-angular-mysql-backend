package com.snrt.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import javax.persistence.*;
import java.io.Serializable;

@Embeddable
@Data @NoArgsConstructor @AllArgsConstructor @ToString
public class ViewsID  implements Serializable {
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    private User user;
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    private Track track;
}
