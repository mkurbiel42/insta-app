package com.example.instaappfront.model.retrofit;

import com.example.instaappfront.model.abst.FilterData;

public class FilterCall {
    private FilterData filterData;
    private String filterType;
    private Long id;

    public FilterCall() {
    }

    public FilterCall(FilterData filterData, String filterType, Long id) {
        this.filterData = filterData;
        this.filterType = filterType;
        this.id = id;
    }

    public FilterData getFilterData() {
        return filterData;
    }

    public void setFilterData(FilterData filterData) {
        this.filterData = filterData;
    }

    public String getFilterType() {
        return filterType;
    }

    public void setFilterType(String filterType) {
        this.filterType = filterType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
