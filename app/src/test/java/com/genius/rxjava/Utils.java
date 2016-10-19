package com.genius.rxjava;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by geniusmart on 2016/10/19.
 */

public class Utils {

    public static void sleep(long sec) {
        try {
            Thread.sleep(sec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static Observable<Integer> o1 = Observable.create(new Observable.OnSubscribe<Integer>() {
        @Override
        public void call(Subscriber<? super Integer> subscriber) {
            System.out.println("o1--begin");
            subscriber.onNext(1);
            sleep(1000);
            subscriber.onNext(2);
            sleep(5000);
            subscriber.onNext(3);
            subscriber.onCompleted();
        }
    });

    public static Observable<String> o2 = Observable.create(new Observable.OnSubscribe<String>() {
        @Override
        public void call(Subscriber<? super String> subscriber) {
            System.out.println("o2--begin");
            subscriber.onNext("a");
            subscriber.onNext("b");
            subscriber.onNext("c");
            subscriber.onNext("d");
            subscriber.onCompleted();
        }
    });
}
