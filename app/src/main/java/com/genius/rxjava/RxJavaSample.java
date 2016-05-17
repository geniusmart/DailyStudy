package com.genius.rxjava;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;

public class RxJavaSample {
    public void test() {

        Observer<String> observer = new Observer<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {

            }
        };

        Observable observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Hello");
                subscriber.onNext("Hi");
                subscriber.onNext("Aloha");
                subscriber.onCompleted();
            }
        });

        //TODO observable1和observable2与observable等价？
        Observable observable1 = Observable.just("");
        String[] words = {"Hello", "Hi", "Aloha"};
        Observable observable2 = Observable.from(words);

        Action0 action0 = new Action0() {
            @Override
            public void call() {

            }
        };

        Action1 action1 = new Action1() {
            @Override
            public void call(Object o) {

            }
        };


        Student[] students = null;
        Subscriber<Course> subscriber = new Subscriber<Course>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onNext(Course course) {

            }
        };

        Observable.from(students).flatMap(new Func1<Student, Observable<Course>>() {
            @Override
            public Observable<Course> call(Student student) {
                return Observable.just(student.mCourse);
            }
        }).subscribe(subscriber);

    }

    private static class Course {
        public String name;
    }

    private static class Student {
        Course mCourse;
    }
}
