package com.yejf.mockito;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.BDDMockito.given;
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

    @Spy
    MockitoTestI spy = new MockitoTest();

    /**
     *使用 when(spy.xx()).thenReturn() 将调用原生方法，再修改返回值
     * 而使用 doReturn().when(spy).xx() 将连原生方法都不调用，直接提供返回值，达到 mock一样的效果
     *
     * doReturn(Object)
     *
     * doThrow(Throwable...)
     *
     * doThrow(Class)
     *
     * doAnswer(Answer)
     *
     * doNothing()
     *
     * doCallRealMethod()
     */
    @Test
    public void doMethod() {
//        when(spy.someMethod("123")).thenReturn("666");
        doReturn("555").when(spy).someMethod("123");

        System.out.println(spy.someMethod("123"));
    }


    /**
     * 该例子 证明了上一例子的说法
     * 对 spy 对象进行 stub ，具体方法依然会被执行，只是返回值被 mock 了而已
     */
    @Test
    public void spyRealObject() {

        List list = new LinkedList();
        List spy = spy(list);

        //optionally, you can stub out some methods:
        when(spy.size()).thenReturn(100);

        //using the spy calls *real* methods
        spy.add("one");
        spy.add("two");

        //Impossible: real method is called so spy.get(0) throws IndexOutOfBoundsException (the list is yet empty)
        when(spy.get(0)).thenReturn("foo");

        //You have to use doReturn() for stubbing
        doReturn("foo").when(spy).get(0);

        //prints "one" - the first element of a list
        System.out.println(spy.get(0));

        //size() method was stubbed - 100 is printed
        System.out.println(spy.size());

        //optionally, you can verify
        verify(spy).add("one");
        verify(spy).add("two");
    }

    /**
     * 当 mock 对象方法没被 stub ，默认返回null，使用 RETURNS_SMART_NULLS 后
     * String 类型会返回空串
     * Boolean 类型会返回false
     * Integer 类型会返回 0
     */
    @Test
    public void changeDefaultReturn() {
        MockitoTestI mock = mock(MockitoTestI.class, Mockito.RETURNS_SMART_NULLS);
        System.out.println(mock.someMethod("123"));
    }

    /**
     * verify 可以使用 anyThat( ArgumentMatcher) 来校验参数
     * 也可以直接用 ArgumentCaptor
     *   ArgumentCaptor 适合如下场景:
     *  1、ArgumentMatcher 不好复用
     *  2、只需校验参数对象的某个 value
     */
    @Test
    public void capturing() {
        ArgumentCaptor<Person> argument = ArgumentCaptor.forClass(Person.class);
        mock.doSomething(new Person("John"));
        verify(mock).doSomething(argument.capture());
        assertEquals("John", argument.getValue().getName());
    }

    /**
     * mock 对象使用 thenCallRealMethod 可以
     * 但 mock 的class 必须是具体类，不能是抽象类
     */
    @Test
    public void realPartialMock() {
        //you can create partial mock with spy() method:
        List list = spy(new LinkedList());

        //you can enable partial mock capabilities selectively on mocks:
        MockitoTestI mock = mock(MockitoTest.class);
        //Be sure the real implementation is 'safe'.
        //If real implementation throws exceptions or depends on specific state of the object then you're in trouble.
        when(mock.someMethod("123")).thenCallRealMethod();

        System.out.println(mock.someMethod("123"));
    }

    /**
     * reset() 清除 mock 上的 stub
     */
    @Test
    public void resetingMock() {
        List mock = mock(List.class);
        when(mock.size()).thenReturn(10);
        mock.add(1);

        reset(mock);
        //at this point the mock forgot any interactions & stubbing
        System.out.println(mock.size());
//        verify(mock).add(1);
    }

    /**
     * 该例子展示了一种测试风格叫BDD behavior driven development
     * 三步测试：given when then
     */
    @Test
    public void bdd() {
        //given
//        given(seller.askForBread()).willReturn(new Bread());

        //when
//        Goods goods = shop.buyBread();

        //then
//        assertThat(goods, containBread());
    }

    /**
     * 使mock对象可以被序列化（感觉没啥用）
     */
    @Test
    public void serializableMock() {

        //example mock
        List serializableMock = mock(List.class, withSettings().serializable());


        //example spy
        List<Object> list = new ArrayList<Object>();
        List<Object> spy = mock(ArrayList.class, withSettings()
                .spiedInstance(list)
                .defaultAnswer(CALLS_REAL_METHODS)
                .serializable());
    }
}