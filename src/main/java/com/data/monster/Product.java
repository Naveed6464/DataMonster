/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.data.monster;

import lombok.Data;

/**
 *
 * @author naveed
 */
@Data
public class Product {

    String title;
    String description;
    String offerPrize;

    public Product() {
    }

    public Product(String title, String description, String offerPrize) {
        this.title = title;
        this.description = description;
        this.offerPrize = offerPrize;
    }

}
