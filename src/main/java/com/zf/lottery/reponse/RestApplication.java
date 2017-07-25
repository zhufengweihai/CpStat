package com.zf.lottery.reponse; 

import java.util.HashSet;
import java.util.Set;

public class RestApplication /*extends javax.ws.rs.core.Application*/{

    private Set<Object> singletons = new HashSet<Object>();

    public RestApplication () {
        singletons.add(new ResultService());
    }

//    @Override
//    public Set<Object> getSingletons() {
//        return singletons;
//    }
}