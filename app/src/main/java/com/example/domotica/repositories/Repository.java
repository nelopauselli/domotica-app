package com.example.domotica.repositories;

class Repository {
    CharSequence toFileName(String value){
        return value.toLowerCase().replaceAll("[^a-z0-9]","");
    }
}
