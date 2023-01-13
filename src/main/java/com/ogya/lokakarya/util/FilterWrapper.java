package com.ogya.lokakarya.util;

import java.util.List;

public class FilterWrapper<T> {

private String name;
private List<T> value;

public String getName() {
return name;
}

public void setName(String name) {
this.name = name;
}

public List<T> getValue() {
return value;
}

public void setValue(List<T> value) {
this.value = value;
}

}