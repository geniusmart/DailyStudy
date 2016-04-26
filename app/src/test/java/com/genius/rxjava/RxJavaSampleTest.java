package com.genius.rxjava;

import com.genius.dailystudy.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import rx.Observable;
import rx.functions.Action1;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class,sdk = 21)
public class RxJavaSampleTest {

    public void setup(){
        ShadowLog.stream = System.out;
    }

    @Test
    public void print(){
        System.out.print(1);

        String[] array = new String[]{"Hello","World"};
        Observable<String> observable = Observable.from(array);
        observable.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.println(s);
            }
        });
    }

    @Test
    public void test1(){}
}