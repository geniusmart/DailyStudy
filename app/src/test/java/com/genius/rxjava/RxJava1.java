package com.genius.rxjava;

import org.junit.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.schedulers.TestScheduler;

import static com.genius.rxjava.Utils.o1;
import static com.genius.rxjava.Utils.o2;

/**
 * Created by geniusmart on 16/10/7.
 */

public class RxJava1 {

    @Test
    public void testSimple() {

        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Hello1");
                subscriber.onNext("Hello2");
                subscriber.onCompleted();
            }
        });

        Subscriber<String> subscriber = new Subscriber<String>() {

            @Override
            public void onStart() {
                System.out.println("onStart");
            }

            @Override
            public void onCompleted() {
                System.out.println("onCompleted");
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
    public void testFrom() {
        Observable<String> observable = Observable.from(new String[]{"Hello", "World"});
        observable.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.println(s);
            }
        });
    }

    @Test
    public void testMap() {
        Observable.just(1)
                .map(new Func1<Integer, String>() {
                    @Override
                    public String call(Integer integer) {
                        return integer + "->";
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
    public void testFlatMap() {
        Observable.just("string")
                .flatMap(new Func1<String, Observable<Integer>>() {
                    @Override
                    public Observable<Integer> call(String str) {
                        char[] chars = str.toCharArray();
                        Integer[] nums = new Integer[chars.length];
                        int i = 0;
                        for (char value : chars) {
                            nums[i++] = Integer.valueOf(value);
                        }
                        return Observable.from(nums);
                    }
                })
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer result) {
                        System.out.println(result);
                    }
                });
    }

    @Test
    public void testSubscribeOn() {
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                System.out.println(Thread.currentThread().getName());
                subscriber.onNext("1");
            }

        });

        observable.subscribeOn(Schedulers.computation()).subscribe();
        observable.subscribeOn(Schedulers.io()).subscribe();
        observable.subscribeOn(Schedulers.immediate()).subscribe();
        observable.subscribeOn(Schedulers.newThread()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.println(s);
            }
        });
    }


    @Test
    public void testObserveOn() {

        Action1 action1 = new Action1() {
            @Override
            public void call(Object o) {
                System.out.println(Thread.currentThread().getName());
            }
        };

        Observable.just(1)
                .observeOn(Schedulers.computation())
                .subscribe(action1);
    }

    //TODO 原理
    @Test
    public void testThreadLift() {
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                System.out.println("1" + Thread.currentThread().getName());
                subscriber.onNext(1);
            }
        })
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.io())
                .map(new Func1<Integer, String>() {
                    @Override
                    public String call(Integer integer) {
                        System.out.println("2" + Thread.currentThread().getName());
                        return integer + "-->";
                    }
                })
                .observeOn(Schedulers.newThread())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String s) {
                        System.out.println("3" + Thread.currentThread().getName());
                        return Observable.just(s + "<--");
                    }
                })
                .observeOn(Schedulers.io())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        System.out.println("4" + Thread.currentThread().getName());
                        System.out.println(s);
                    }
                });
    }


    @Test
    public void testMerge() {

        Observable.merge(o1, o2)
                .subscribe(new Action1<Serializable>() {
                    @Override
                    public void call(Serializable serializable) {
                        System.out.println(serializable.toString());
                    }
                });
    }

    @Test
    public void testZip(){
        Observable.zip(o1, o2, new Func2<Integer, String, Object>() {
            @Override
            public Object call(Integer integer, String s) {
                return integer + s;
            }
        })
                .subscribe(new Subscriber<Object>() {

                    @Override
                    public void onStart() {
                        System.out.println("begin" + System.currentTimeMillis());
                    }

                    @Override
                    public void onCompleted() {
                        System.out.println("end" + System.currentTimeMillis());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Object o) {
                        System.out.println(o);
                    }
                });
    }

    @Test
    public void concat(){
        Observable.concat(o1,o2).subscribe(new Action1<Serializable>() {
            @Override
            public void call(Serializable serializable) {
                System.out.println(serializable);
            }
        });
    }

    @Test
    public void doOnXxx(){
        Observable.just(1)
                .doOnNext(i->System.out.println("doOnNext"))
                .doOnCompleted(()->System.out.println("doOnCompleted"))
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println("onNext");
                    }
                });
    }

    @Test
    public void timer(){
        TestScheduler scheduler = new TestScheduler();
        List<Long> result = new ArrayList<>();
        //延迟一定时间后执行一次，创建型的操作符
        Observable.timer(1,TimeUnit.SECONDS,scheduler)
                .subscribe(result::add);

        scheduler.advanceTimeBy(1, TimeUnit.SECONDS);
        System.out.println(result);
    }

    @Test
    public void interval(){
        //每隔一段时间执行
        Observable.interval(10,TimeUnit.SECONDS)
                .subscribe(System.out::println);
    }

    @Test
    public void delay(){
        //针对数据流进行处理，非创建型操作符
        Observable.just(1).delay(0,TimeUnit.SECONDS)
                .subscribe(System.out::println);
    }


}
