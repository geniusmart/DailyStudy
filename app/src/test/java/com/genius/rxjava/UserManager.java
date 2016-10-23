package com.genius.rxjava;

import android.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import rx.Observable;

/**
 * Created by geniusmart on 16/10/23.
 */

public class UserManager {

    private Db mDb;
    public static final String[] EMPTY = new String[0];
    private UserApi mUserApi;

    public UserManager(Db db,UserApi userApi){
        mDb = db;
        mUserApi = userApi;
    }

    /**
     * TODO:defer/autoConnect是什么鬼,了解mokito的更多api，了解rule
     * @param ids
     * @return
     */
    public Observable<List<User>> getUser(String[] ids){

        Observable<Pair<List<User>,String[]>> cachePair = Observable.defer(() -> {
            List<User> cache = mDb.getCache(ids);
            if(cache.size() == ids.length){
                return Observable.just(Pair.create(cache,EMPTY));
            }

            String[] hits = extractIds(cache);
            String[] misses = new String[ids.length - cache.size()];

            Arrays.sort(hits);
            int pos = 0;
            for (String id : ids){
                if(Arrays.binarySearch(hits,id)<1){
                    misses[pos++] = id;
                }
            }
            return Observable.just(Pair.create(cache,misses));
        }).publish().autoConnect(2);

        //获取命中数据
        Observable<List<User>> hitUsers = cachePair.map(listPair -> listPair.first);
        //获取未命中数据，从网络获取
        Observable<List<User>> missUsers = cachePair.flatMap(listPair -> {
            String[] second = listPair.second;
            if (second == EMPTY) {
                return Observable.just(Collections.<User>emptyList());
            }

            return mUserApi.getUser(second);
        }).doOnNext(users -> {
            if (!users.isEmpty()){
                mDb.saveListOfUser(users);
            }
        });

        Observable<List<User>> zip = Observable.zip(hitUsers, missUsers, (local, remote) -> {

            List<User> allUser = new ArrayList<>(local.size() + remote.size());
            allUser.addAll(local);
            allUser.addAll(remote);
            return allUser;
        });

        return zip;
    }

    private String[] extractIds(List<User> cache) {
        String[] ids = new String[cache.size()];
        int pos = 0;
        for (User user : cache){
            ids[pos] = user.id;
            pos++;
        }
        return new String[0];
    }


    public static class User{
        public String id;
    }
    public static class Db{
        public List<User> getCache(String[] ids){
            List<User> users = new ArrayList<>();
            System.out.println("从本地获取缓存数据");
            return users;
        }

        public void saveListOfUser(List<User> users) {
            System.out.println("缓存用户数据到数据库中");
        }
    }

    public static class UserApi{

        public Observable<List<User>> getUser(String[] second) {
            System.out.println("从网络获取用户信息数据");
            return Observable.just(Collections.singletonList(new User()));
        }
    }
}
