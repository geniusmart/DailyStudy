package com.genius.rxjava;

import org.junit.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.genius.rxjava.Utils.o1;
import static com.genius.rxjava.Utils.o2;

/**
 * Created by geniusmart on 2016/10/19.
 */
public class RxJava2{

    @Test
    public void buffer(){
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                for (int i = 0;i<100;i++){
                    subscriber.onNext("-->"+i);
                }
            }
        }).subscribeOn(Schedulers.io()).buffer(3,TimeUnit.SECONDS)
        .subscribe(new Action1<List<String>>() {
            @Override
            public void call(List<String> strings) {
                for(String s : strings){
                    //无法打印出来
                    System.out.println(s);
                }
            }
        });
    }

    @Test
    public void interval(){
        Observable.interval(1000,TimeUnit.SECONDS).subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                System.out.println(aLong);
            }
        });
    }

    @Test
    public void repeat(){
        Observable.range(1,2).repeat(5)
                .subscribe(System.out::println);
    }

    /**
     * 使用第一个先发射数据的 Observable ，其他的 Observable 被丢弃。
        Read more: http://blog.chengyunfeng.com/?p=972#ixzz4NWRlVAgc
     */
    @Test
    public void amb(){
        Observable.amb(o1,o2)
                .subscribe(System.out::println);
    }


}
