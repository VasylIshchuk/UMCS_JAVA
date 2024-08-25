public class Main {
    public static void main(String[] args) {
        DeathCauseStatistic deathCauseStatistic =
                DeathCauseStatistic.fromCsvLine("A04.7          ,758,-,-,-,-,-,1,-,1,3,5,9,12,30,58,64,94,161,192,95,33");
        System.out.println(deathCauseStatistic.getAgeBracketDeaths(45));

//        DeathCauseStatisticList deathCauseStatisticList = new DeathCauseStatisticList();
//        deathCauseStatisticList.repopulate("mortality.csv");
//        System.out.println(deathCauseStatisticList.mostDeadlyDiseases(3,5));

//        ICDCodeTabular icdCodeTabular = new ICDCodeTabularOptimizedForMemory();
//        System.out.println(icdCodeTabular.getDescription("A01.0"));
    }
}
// "mortality.csv"
//      You have a CSV file (mortality.csv) containing mortality statistics in Poland for the year 2019,
// broken down by age groups and causes of death.
//      The causes of death are recorded in the first column using ICD-10 codes.
//      The second column contains the total number of deaths for each cause, and the subsequent columns
// show the number of deaths in various age brackets described in the header.