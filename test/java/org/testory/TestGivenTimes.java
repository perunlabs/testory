package org.testory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.testory.Testory.givenTimes;
import static org.testory.testing.Closures.returning;
import static org.testory.testing.Closures.voidReturning;

import java.util.concurrent.Callable;

import org.junit.Before;
import org.junit.Test;
import org.testory.common.Closure;
import org.testory.common.VoidClosure;

public class TestGivenTimes {
  private int times, counter, failTime;
  private Exception exception;

  @Before
  public void before() {
    times = 5;
    counter = 0;
    exception = new Exception("exception");
  }

  @Test
  public void closure_is_called_many_times() {
    givenTimes(times, new Closure() {
      public Void invoke() {
        counter++;
        return null;
      }
    });
    assertEquals(times, counter);
  }

  @Test
  public void closure_is_called_zero_times() {
    givenTimes(0, new Closure() {
      public Void invoke() {
        counter++;
        return null;
      }
    });
    assertEquals(0, counter);
  }

  @Test
  public void closure_can_throw_throwable() {
    times = 5;
    failTime = 3;
    try {
      givenTimes(times, new Closure() {
        public Void invoke() throws Throwable {
          counter++;
          if (counter == failTime) {
            throw exception;
          }
          return null;
        }
      });
    } catch (Throwable throwable) {
      assertTrue(throwable instanceof RuntimeException);
      assertSame(exception, throwable.getCause());
      assertEquals(failTime, counter);
      return;
    }
    fail();
  }

  @Test
  public void closure_cannot_be_called_negative_number_of_times() {
    try {
      givenTimes(-1, returning(null));
      fail();
    } catch (TestoryException e) {}
  }

  @Test
  public void closure_cannot_be_null() {
    try {
      givenTimes(times, (Closure) null);
      fail();
    } catch (TestoryException e) {}
  }

  @Test
  public void void_closure_is_called_many_times() {
    givenTimes(times, new VoidClosure() {
      public void invoke() {
        counter++;
      }
    });
    assertEquals(times, counter);
  }

  @Test
  public void void_closure_is_called_zero_times() {
    givenTimes(0, new VoidClosure() {
      public void invoke() {
        counter++;
      }
    });
    assertEquals(0, counter);
  }

  @Test
  public void void_closure_can_throw_throwable() {
    times = 5;
    failTime = 3;
    try {
      givenTimes(times, new VoidClosure() {
        public void invoke() throws Throwable {
          counter++;
          if (counter == failTime) {
            throw exception;
          }
        }
      });
    } catch (Throwable throwable) {
      assertTrue(throwable instanceof RuntimeException);
      assertSame(exception, throwable.getCause());
      assertEquals(failTime, counter);
      return;
    }
    fail();
  }

  @Test
  public void void_closure_cannot_be_called_negative_number_of_times() {
    try {
      givenTimes(-1, voidReturning());
      fail();
    } catch (TestoryException e) {}
  }

  @Test
  public void void_closure_cannot_be_null() {
    try {
      givenTimes(times, (VoidClosure) null);
      fail();
    } catch (TestoryException e) {}
  }

  @Test
  public void invocation_is_called_many_times() {
    givenTimes(times, new Runnable() {
      public void run() {
        counter++;
      }
    }).run();
    assertEquals(times, counter);
  }

  @Test
  public void invocation_is_called_zero_times() {
    times = 0;
    givenTimes(0, new Runnable() {
      public void run() {
        counter++;
      }
    }).run();
    assertEquals(times, counter);
  }

  @Test
  public void invocation_can_throw_throwable() {
    times = 5;
    failTime = 3;
    try {
      givenTimes(times, new Callable<Object>() {
        public Object call() throws Exception {
          counter++;
          if (counter == failTime) {
            throw exception;
          }
          return null;
        }
      }).call();
    } catch (Throwable throwable) {
      assertSame(exception, throwable);
      assertEquals(failTime, counter);
      return;
    }
    fail();
  }

  @Test
  public void invocation_cannot_be_called_negative_number_of_times() {
    try {
      givenTimes(-1, new Runnable() {
        public void run() {}
      });
      fail();
    } catch (TestoryException e) {}
  }

  @Test
  public void invocation_instance_cannot_be_null() {
    try {
      givenTimes(times, (Object) null);
      fail();
    } catch (TestoryException e) {}
  }
}
