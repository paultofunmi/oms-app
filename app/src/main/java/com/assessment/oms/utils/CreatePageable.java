package com.assessment.oms.utils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

public class CreatePageable {

    public static Sort createSort(String sort){
        String evalSort = sort.split(",")[0];
        String sortDirection = sort.split(",")[1];
        Sort.Direction evalDirection = handleSortDirection(sortDirection.trim());
        return Sort.by(new Sort.Order(evalDirection,evalSort.trim()).ignoreCase());

    }

    private static Sort.Direction handleSortDirection(String sortDirection) {
        if (sortDirection.equalsIgnoreCase("DESC")){
            return Sort.Direction.DESC;
        } else {
            return Sort.Direction.ASC;
        }
    }

}
