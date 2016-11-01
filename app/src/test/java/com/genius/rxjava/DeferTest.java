package com.genius.rxjava;

import org.junit.Test;

import rx.Observable;
import rx.functions.Func0;

/**
 * Created by geniusmart on 2016/10/28.
 */

public class DeferTest {

    //TODO-所谓冷启动？
    @Test
    public void defer(){

        Person person = new Person();

        Observable<Person> defer = Observable.defer(new Func0<Observable<Person>>() {
            @Override
            public Observable<Person> call() {
                return Observable.just(person);
            }
        });

        person.name = "geniusmart";

        defer.subscribe(currentPerson -> {
            System.out.println(currentPerson.name);
        });
    }

    //TODO - 没有达到预期的效果？
    @Test
    public void withoutDefer(){
        Person person = new Person();

        Observable<Person> just = Observable.just(person);

        person.name = "geniusmart";

        just.subscribe( currentPerson -> {
            System.out.println(currentPerson.name);
        });
    }

    @Test
    public void deferWithPublish(){
        Observable<Object> observable = Observable.defer(new Func0<Observable<Object>>() {
            @Override
            public Observable<Object> call() {
                System.out.println("defer");
                return Observable.just(1);
            }
        }).publish().autoConnect(2);
        Observable<Object> map = observable.map(o -> "a");
        Observable<Object> flatMap = observable.flatMap(o -> Observable.just("abc"));
        map.zipWith(flatMap, (o, o2) -> null) .subscribe();
    }

    private static class Person {
        String name = "genius";
    }



}
