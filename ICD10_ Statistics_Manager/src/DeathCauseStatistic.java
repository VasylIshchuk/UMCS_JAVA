import java.util.Arrays;

public class DeathCauseStatistic {
    private String code;
    private int[] quantityOFDeaths;

    public String getCode() {
        return code;
    }

// The method returns a DeathCauseStatistic object (which contains the ICD-10 code for the disease and
// a table of the number of deaths in the subsequent age groups) based on a single row of the CSV file.
    public static DeathCauseStatistic fromCsvLine(String line) {
        DeathCauseStatistic deathCauseStatistic = new DeathCauseStatistic();
        String[] elemFromLine = line.split(",");

        //  The trim() method in Java is used to remove whitespace from the beginning and end of a string.
        deathCauseStatistic.code = elemFromLine[0].trim();
        int[] quantityOFDeaths;
        quantityOFDeaths = Arrays.stream(elemFromLine) // The Arrays.stream() method creates a stream of elements from an array.
                .skip(2)
                .mapToInt(str -> str.equals("-") ? 0 : Integer.parseInt(str)).toArray();
        deathCauseStatistic.quantityOFDeaths = quantityOFDeaths;
//        int quantityGroups = elemFromLine.length-2;//because we skip columns (code,together)
//        int [] quantityOFDeaths = new int[quantityGroups];
//        for(int i=0; i<quantityGroups; ++i){
//            if(elemFromLine[i+2].equals("-"))
//                quantityOFDeaths[i] = 0;
//            else
//                quantityOFDeaths[i] =
//                        Integer.parseInt(elemFromLine[i+2]);
//        }
//        deathCauseStatistic.quantityOFDeaths =quantityOFDeaths;
        return deathCauseStatistic;
    }

    public static class AgeBracketDeaths {
        public final int young;
        public final int old;
        public final int deathCount;

        public AgeBracketDeaths(int young, int old, int deathCount) {
            this.young = young;
            this.old = old;
            this.deathCount = deathCount;
        }

        @Override
        public String toString() {
            return "AgeBracketDeaths: { Young and old: " +
                    young + " - " + old +
                    " ; Quantity deaths: " +
                    deathCount + " }";
        }
    }

    public AgeBracketDeaths getAgeBracketDeaths(int age) {
        int young, old, deathCount;
        int index = age / 5;
        if (age >= 100) deathCount = quantityOFDeaths[quantityOFDeaths.length - 1];
        else deathCount = quantityOFDeaths[index];
        young = index * 5;
        old = young + 4;
        return new AgeBracketDeaths(young, old, deathCount);
    }

    @Override
    public String toString() {
        return "DeathCauseStatistic{" +
                "ICD10='" + getCode() + '\'' +
                ", deaths=" + Arrays.toString(quantityOFDeaths) +
                '}';
    }
}
