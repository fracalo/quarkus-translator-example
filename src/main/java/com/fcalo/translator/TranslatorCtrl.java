package com.fcalo.translator;

import io.quarkus.vertx.web.Route;
import io.quarkus.vertx.web.RoutingExchange;
import io.vertx.ext.web.RoutingContext;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TranslatorCtrl {

    @Route(path = "hello")
    public String hello() {
        return "hello";
    }

    @Route(methods = Route.HttpMethod.GET, path = "hello-through-ctx")
    public void hello(RoutingContext rc) {
        rc.response().end("hello twice");
    }

    @Route(methods = Route.HttpMethod.GET, path = "hello-through-exchange")
    public void hello(RoutingExchange ex) {
        var paramName = ex.getParam("name").orElse("Stranger");
        ex.ok("hello, this looks lots like express.js, paramName is " + paramName);
    }
}