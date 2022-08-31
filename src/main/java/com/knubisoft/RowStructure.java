package com.knubisoft;

import com.knubisoft.anno.Data;
import com.knubisoft.anno.Lookup;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RowStructure {

    //^\w+(?= \d+[.,]\d+ UAH)
    @Lookup(regex = "(?<category>\\w+( \\w+)?)")
    private String category;

//    \d+[.,]\d+(?= UAH \d+[.,]\d+ UAH)
    @Lookup(regex = "(?<budget>\\d+,\\d+\\h[A-Z]{3})")
    private String budget;

//    \d+[.,]\d+(?= UAH$)
    @Lookup(regex = "(?<actual>\\d+,\\d+\\h[A-Z]{3})")
    private String actual;

}