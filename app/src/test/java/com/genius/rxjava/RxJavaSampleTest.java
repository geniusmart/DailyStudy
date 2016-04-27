package com.genius.rxjava;

import org.junit.Test;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class RxJavaSampleTest {

    @Test
    public void observable_subscribe() {
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Hello1");
                subscriber.onNext("Hello2");
            }
        });

        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                System.out.println(s);
            }
        };

        observable.subscribe(subscriber);
    }

    @Test
    public void observable_from() {
        String[] array = new String[]{"Hello", "World"};
        Observable<String> observable = Observable.from(array);
        observable.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.println(s);
            }
        });
    }

    @Test
    public void observable_just() {
        Observable<Integer> observable = Observable.just(1, 2, 3);
        observable.subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                System.out.println(integer);
            }
        });
    }

    @Test
    public void schedulers() {

        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                System.out.println("Observable--" + Thread.currentThread().getName());
                subscriber.onNext("");
            }
        });

        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                System.out.println("Subscriber--" + Thread.currentThread().getName());
            }
        };

        //observable.subscribe(subscriber);
        observable.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(subscriber);
    }

    @Test
    public void map() {
        Observable.just(1)
                .map(new Func1<Integer, String>() {
                    @Override
                    public String call(Integer integer) {
                        return String.valueOf(integer);
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        System.out.print(s);
                    }
                });
    }

    @Test
    public void flatMap() {
        Observable.just(1)
                .flatMap(new Func1<Integer, Observable<String>>() {
                    @Override
                    public Observable<String> call(Integer integer) {
                        return Observable.just(String.valueOf(integer));
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        System.out.println(s);
                    }
                });
    }

    @Test
    public void lift() {
        Observable.just(1)
                .lift(new Observable.Operator<Object, Integer>() {
                    @Override
                    public Subscriber<? super Integer> call(final Subscriber<? super Object> subscriber) {
                        return new Subscriber<Integer>() {
                            @Override
                            public void onCompleted() {
                                subscriber.onCompleted();
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(Integer integer) {
                                subscriber.onNext(String.valueOf(integer));
                            }
                        };
                    }
                })
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object s) {
                        System.out.println(s.hashCode());
                    }
                });
    }

    @Test
    public void subscriteOn_observeOn() {

        Observable<Object> observable = Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                System.out.println("observable--" + Thread.currentThread().getName());
                subscriber.onNext("1");
            }
        });

        Subscriber subscriber = new Subscriber() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Object o) {
                System.out.println("subscriber--" + Thread.currentThread().getName());
                System.out.println(o);
            }
        };

        observable.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map(new Func1<Object, String>() {
                    @Override
                    public String call(Object o) {
                        System.out.println("Func1--" + Thread.currentThread().getName());
                        return String.valueOf(o);
                    }
                })
                .observeOn(Schedulers.newThread())
                .map(new Func1<String, Integer>() {
                    @Override
                    public Integer call(String s) {
                        System.out.println("Func2--" + Thread.currentThread().getName());
                        return 1;
                    }
                })
                .observeOn(Schedulers.computation())
                .subscribe(subscriber);
    }
}