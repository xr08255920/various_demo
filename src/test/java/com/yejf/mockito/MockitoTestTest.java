package com.yejf.mockito;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

public class MockitoTestTest {
    @Test
    public void name() {
        //Let's import Mockito statically so that the code looks clearer


        //mock creation
        List mockedList = mock(List.class);

        //using mock object
        mockedList.add("one");
        mockedList.clear();

        //verification
        verify(mockedList).add("one");
        verify(mockedList).clear();

    }

    @Test
    public void name2() {
//You can mock concrete classes, not just interfaces
        LinkedList mockedList = mock(LinkedList.class);

        //stubbing
        when(mockedList.get(0)).thenReturn("first");
//        when(mockedList.get(1)).thenThrow(new RuntimeException());

        //following prints "first"
        System.out.println(mockedList.get(0));

        //following throws runtime exception
        System.out.println(mockedList.get(1));

        //following prints "null" because get(999) was not stubbed
        System.out.println(mockedList.get(999));

        //Although it is possible to verify a stubbed invocation, usually it's just redundant
        //If your code cares what get(0) returns, then something else breaks (often even before verify() gets executed).
        //If your code doesn't care what get(0) returns, then it should not be stubbed.
        verify(mockedList).get(0);
    }

    @Test
    public void ArgumentMatchers() {
        LinkedList mockedList = mock(LinkedList.class);
        //stubbing using built-in anyInt() argument matcher
        when(mockedList.get(anyInt())).thenReturn("element");

        //stubbing using custom matcher (let's say isValid() returns your own matcher implementation):
        when(mockedList.contains(argThat(isValid()))).thenReturn(true);

        //following prints "element"
        System.out.println(mockedList.get(999));

        mockedList.add("niubility");

        System.out.println("mockedlist contains abcdefg :"+mockedList.contains("abcdefg"));
        System.out.println("mockedlist contains abc :"+mockedList.contains("abc"));

        //you can also verify using an argument matcher
        verify(mockedList).get(anyInt());

        //argument matchers can also be written as Java 8 Lambdas
        verify(mockedList).add(argThat(someString -> someString.toString().length() > 5));
    }

    private ArgumentMatcher isValid() {
        return new ArgumentMatcher() {
            @Override
            public boolean matches(Object o) {
                if (o.toString().length() > 3)
                    return true;
                return false;
            }
        };
    }
    @Mock
    private MockitoTestI mock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void specialVerify() {
        mock.someMethod(12,"abc","third argument");
        verify(mock).someMethod(anyInt(), anyString(), eq("third argument"));
        //above is correct - eq() is also an argument matcher

        /**
         * fault example
         */
//        verify(mtt).someMethod(anyInt(), anyString(), "third argument");
        //above is incorrect - exception will be thrown because third argument is given without an argument matcher.
    }

    @Test
    public void timesTest() {
        LinkedList mockedList = mock(LinkedList.class);
        //using mock
        mockedList.add("once");

        mockedList.add("twice");
        mockedList.add("twice");

        mockedList.add("three times");
        mockedList.add("three times");
        mockedList.add("three times");

        //following two verifications work exactly the same - times(1) is used by default
        verify(mockedList).add("once");
        verify(mockedList, times(1)).add("once");

        //exact number of invocations verification
        verify(mockedList, times(2)).add("twice");
        verify(mockedList, times(3)).add("three times");

        //verification using never(). never() is an alias to times(0)
        verify(mockedList, never()).add("never happened");

        //verification using atLeast()/atMost()
        verify(mockedList, atLeastOnce()).add("three times");
        verify(mockedList, atLeast(2)).add("three times");
        verify(mockedList, atMost(5)).add("three times");
    }

    @Test
    public void stupException() {
        LinkedList mockedList = mock(LinkedList.class);
        doThrow(new RuntimeException()).when(mockedList).clear();

        //following throws RuntimeException:
//        mockedList.clear();

    }

    @Test
    public void verifyInOrder() {
        // A. Single mock whose methods must be invoked in a particular order
        List singleMock = mock(List.class);

        //using a single mock
        singleMock.add("was added first");
        singleMock.add("was added second");

        //create an inOrder verifier for a single mock
        InOrder inOrder = inOrder(singleMock);

        //following will make sure that add is first called with "was added first", then with "was added second"
        inOrder.verify(singleMock).add("was added first");
        inOrder.verify(singleMock).add("was added second");

        // B. Multiple mocks that must be used in a particular order
        List firstMock = mock(List.class);
        List secondMock = mock(List.class);

        //using mocks
        firstMock.add("was called first");
        secondMock.add("was called second");

        //create inOrder object passing any mocks that need to be verified in order
         inOrder = inOrder(firstMock, secondMock);

        //following will make sure that firstMock was called before secondMock
        inOrder.verify(firstMock).add("was called first");
        inOrder.verify(secondMock).add("was called second");

        // Oh, and A + B can be mixed together at will
    }

    @Test
    public void neverExec() {
        List mockOne = mock(List.class);
        List mockTwo = mock(List.class);
        List mockThree   = mock(List.class);
        //using mocks - only mockOne is interacted
        mockOne.add("one");

        //ordinary verification
        verify(mockOne).add("one");

        //verify that method was never called on a mock
        verify(mockOne, never()).add("two");

        //verify that other mocks were not interacted
        verifyZeroInteractions(mockTwo, mockThree);
    }

    @Test
    public void findRedundant() {
        LinkedList mockedList = mock(LinkedList.class);
        //using mocks
        mockedList.add("one");
//        mockedList.add("two");

        verify(mockedList).add("one");

        //following verification will fail
        verifyNoMoreInteractions(mockedList);

    }

    @Test
    public void stubConsecutiveCalls() {
        when(mock.someMethod("some arg"))
                .thenThrow(new RuntimeException())
                .thenReturn("foo");

        //First call: throws runtime exception:
        try {
            mock.someMethod("some arg");
        } catch (Exception e) {
            System.out.println("let it go~");
        }

        //Second call: prints "foo"
        System.out.println(mock.someMethod("some arg"));

        //Any consecutive call: prints "foo" as well (last stubbing wins).
        System.out.println(mock.someMethod("some arg"));

        when(mock.someMethod("some arg"))
                .thenReturn("one", "two", "three");
        System.out.println(mock.someMethod("some arg"));
        System.out.println(mock.someMethod("some arg"));
        System.out.println(mock.someMethod("some arg"));


        //All mock.someMethod("some arg") calls will return "two"
        when(mock.someMethod("some arg"))
                .thenReturn("one");
        when(mock.someMethod("some arg"))
                .thenReturn("two");
        System.out.println(mock.someMethod("some arg"));
        System.out.println(mock.someMethod("some arg"));
    }

    /**
     * 自定义相同返回类型的Answer，处理复杂的返回逻辑
     */
    @Test
    public void withCallback() {
        when(mock.someMethod(anyString())).thenAnswer(
                new Answer() {
                    public Object answer(InvocationOnMock invocation) {
                        Object[] args = invocation.getArguments();
                        Object mock = invocation.getMock();
                        return "called with arguments: " + Arrays.toString(args);
                    }
                });

        //Following prints "called with arguments: [foo]"
        System.out.println(mock.someMethod("foo"));
    }
}