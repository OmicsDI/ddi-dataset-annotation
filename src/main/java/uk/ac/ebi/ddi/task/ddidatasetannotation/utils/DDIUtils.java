package uk.ac.ebi.ddi.task.ddidatasetannotation.utils;

public class DDIUtils {

    private DDIUtils() {
    }

    public static String getConcatenatedField(String[] fields) {

        String finalFields = "";
        if (fields != null && fields.length > 0) {
            int count = 0;
            for (String value : fields) {
                if (count == fields.length - 1) {
                    finalFields = finalFields + value;
                } else {
                    finalFields = finalFields + value + ",";
                }
                count++;
            }
        }
        return finalFields;
    }
}
