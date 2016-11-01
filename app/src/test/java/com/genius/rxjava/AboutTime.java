package com.genius.rxjava;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.schedulers.TestScheduler;

/**
 * Created by geniusmart on 2016/11/1.
 */

public class AboutTime {

    private TestScheduler mTestScheduler;
    private List<Object> mList;

    @Before
    public void setUp(){
        mTestScheduler = new TestScheduler();
        mList = new ArrayList<>();
    }

    @Test
    public void timer(){
        //延迟一定时间后执行一次，创建型的操作符
        Observable.timer(5, TimeUnit.SECONDS,mTestScheduler)
                .subscribe(mList::add);

        mTestScheduler.advanceTimeBy(5, TimeUnit.SECONDS);
        System.out.println(mList);
    }

    @Test
    public void interval(){
        //每隔一段时间执行
        Observable.interval(5,TimeUnit.SECONDS,mTestScheduler)
                .take(10)
                .subscribe(mList::add);

        mTestScheduler.advanceTimeBy(50, TimeUnit.SECONDS);
        System.out.println(mList);
    }

    @Test
    public void delay(){
        //针对数据流进行处理，非创建型操作符
        Observable.just(1).delay(0,TimeUnit.SECONDS)
                .subscribe(System.out::println);
    }
}
