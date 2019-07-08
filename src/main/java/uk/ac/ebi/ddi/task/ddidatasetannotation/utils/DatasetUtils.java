package uk.ac.ebi.ddi.task.ddidatasetannotation.utils;

import uk.ac.ebi.ddi.ddidomaindb.dataset.Field;
import uk.ac.ebi.ddi.service.db.model.dataset.Dataset;

import java.util.Map;
import java.util.Set;

public class DatasetUtils {

    private DatasetUtils() {
    }

    public static boolean hasDateType(Dataset dataset, Field dateType) {
        Map<String, Set<String>> dates = dataset.getDates();

        if (dates != null) {
            return dates.containsKey(dateType.getName());
        }
        return false;
    }

    public static Set<String> getDateType(Dataset dataset, Field dateType) {
        Map<String, Set<String>> dates = dataset.getDates();
        return dates.get(dateType.getName());
    }
}
