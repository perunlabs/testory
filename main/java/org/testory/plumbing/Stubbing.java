package org.testory.plumbing;

import static org.testory.plumbing.Cache.newCache;
import static org.testory.plumbing.PlumbingException.check;

import org.testory.common.Chain;
import org.testory.common.Optional;
import org.testory.proxy.Handler;
import org.testory.proxy.Invocation;
import org.testory.proxy.InvocationMatcher;

public class Stubbing {
  public final InvocationMatcher invocationMatcher;
  public final Handler handler;

  private Stubbing(InvocationMatcher invocationMatcher, Handler handler) {
    this.invocationMatcher = invocationMatcher;
    this.handler = handler;
  }

  public static Stubbing stubbing(InvocationMatcher invocationMatcher, Handler handler) {
    check(invocationMatcher != null);
    check(handler != null);
    return new Stubbing(invocationMatcher, handler);
  }

  public String toString() {
    return "stubbing(" + invocationMatcher + ", " + handler + ")";
  }

  public static Optional<Stubbing> findStubbing(Invocation invocation, Chain<Object> history) {
    Cache cache = localCache.get().update(history);
    localCache.set(cache);
    for (Object event : cache.untilLastTyped) {
      if (event instanceof Stubbing) {
        Stubbing stubbing = (Stubbing) event;
        if (stubbing.invocationMatcher.matches(invocation)) {
          return Optional.of(stubbing);
        }
      }
    }
    return Optional.empty();
  }

  public static boolean isStubbed(Invocation invocation, Chain<Object> history) {
    return findStubbing(invocation, history).isPresent();
  }

  private static ThreadLocal<Cache> localCache = new ThreadLocal<Cache>() {
    protected Cache initialValue() {
      return newCache(Stubbing.class);
    }
  };
}
