package com.snrt.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AdvertID implements Serializable {
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    private User user_advrt;
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    private Advertisement advertisement;
}
