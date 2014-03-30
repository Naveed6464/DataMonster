/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.data.monster;

import java.util.List;
import lombok.Data;

/**
 *
 * @author naveed
 */
@Data
public class ParserJson {

    boolean leafPage;
    String type;
    List<Product> products;
    String url;

    public ParserJson() {
    }

    public ParserJson(boolean leafPage, String type, List<Product> products, String url) {
        this.leafPage = leafPage;
        this.type = type;
        this.products = products;
        this.url = url;
    }

    public ParserJson(List<Product> products) {
        this(true, "product", products, "");
    }

}
