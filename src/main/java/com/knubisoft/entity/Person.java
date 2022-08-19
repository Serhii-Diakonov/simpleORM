package com.knubisoft.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.knubisoft.anno.TableAnno;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.csveed.annotations.CsvDate;
import org.csveed.annotations.CsvFile;

import java.math.BigInteger;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@TableAnno
@CsvFile(separator = ',')
public class Person {

    private String name;
    private BigInteger age;
    private BigInteger salary;
    private String position;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    @CsvDate
    private LocalDate dateOfBirth;
    private Float xxx;

}
