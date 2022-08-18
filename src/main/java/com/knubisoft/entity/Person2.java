package com.knubisoft.entity;

import com.knubisoft.anno.TableAnno;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.lang.annotation.Target;
import java.math.BigInteger;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@TableAnno(tableName = "person_short")
public class Person2 {

    private String name;
    private BigInteger age;

}
