package com.genius.rxjava;

import com.genius.binder.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Created by geniusmart on 16/10/23.
 */

@RunWith(RobolectricGradleTestRunner.class)
@Config(sdk = 21,constants = BuildConfig.class)
public class UserManagerTest {

    @Mock
    private UserManager.Db mDb;

    @Mock
    private UserManager.UserApi mUserApi;

    UserManager userManager;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        userManager = new UserManager(mDb,mUserApi);
    }

    @Test
    public void getUser(){

        String[] ids = new String[]{"1","2","3"};

        UserManager.User user = new UserManager.User();
        when(mDb.getCache(ids)).thenReturn(Collections.emptyList());
        when(mUserApi.getUser(ids)).thenReturn(Observable.just(Collections.singletonList(user)));

        TestSubscriber<List<UserManager.User>> testSubscriber = TestSubscriber.create();
        userManager.getUser(ids).subscribe(testSubscriber);

        testSubscriber.awaitTerminalEvent();

        testSubscriber.assertNoErrors();
        testSubscriber.assertCompleted();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertValue(Collections.singletonList(user));

        verify(mDb,times(1)).getCache(ids);
        verify(mUserApi,times(1)).getUser(ids);
        verify(mDb,times(1)).saveListOfUser(Collections.singletonList(user));


        verifyNoMoreInteractions(mDb);
        verifyNoMoreInteractions(mUserApi);

        //Mockito.verifyNoMoreInteractions();

        //Collections.singletonList()

    }

    @Test
    public void getUser2(){
        String[] ids = new String[]{"1","2","3"};
        List<UserManager.User> users = Arrays.asList(new UserManager.User(), new UserManager.User(), new UserManager.User());
        when(mDb.getCache(ids)).thenReturn(users);

        TestSubscriber<List<UserManager.User>> testSubscriber = TestSubscriber.create();
        userManager.getUser(ids).subscribe(testSubscriber);

        testSubscriber.awaitTerminalEvent();

        testSubscriber.assertNoErrors();
        testSubscriber.assertCompleted();
        testSubscriber.assertValueCount(1);
        testSubscriber.assertValue(users);
    }
}
