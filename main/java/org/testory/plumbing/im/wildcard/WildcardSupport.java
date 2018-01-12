package org.testory.plumbing.im.wildcard;

import static java.lang.String.format;
import static org.testory.common.Matchers.asMatcher;
import static org.testory.common.Matchers.equalDeep;
import static org.testory.common.Matchers.isMatcher;
import static org.testory.common.Matchers.same;
import static org.testory.plumbing.PlumbingException.check;
import static org.testory.plumbing.im.wildcard.Wildcard.wildcard;

import org.testory.common.DelegatingMatcher;
import org.testory.common.Formatter;
import org.testory.common.Matcher;
import org.testory.common.Matchers;
import org.testory.plumbing.history.History;
import org.testory.proxy.Invocation;
import org.testory.proxy.InvocationMatcher;

public class WildcardSupport {
  private final History history;
  private final Tokenizer tokenizer;
  private final WildcardMatcherizer matcherizer;
  private final Formatter formatter;

  private WildcardSupport(
      History history,
      Tokenizer tokenizer,
      WildcardMatcherizer matcherizer,
      Formatter formatter) {
    this.history = history;
    this.tokenizer = tokenizer;
    this.matcherizer = matcherizer;
    this.formatter = formatter;
  }

  public static WildcardSupport wildcardSupport(
      History history,
      Tokenizer tokenizer,
      WildcardMatcherizer matcherizer,
      Formatter formatter) {
    check(history != null);
    check(tokenizer != null);
    check(matcherizer != null);
    check(formatter != null);
    return new WildcardSupport(history, tokenizer, matcherizer, formatter);
  }

  public Object any(final Class<?> type) {
    check(type != null);
    DelegatingMatcher printableMatcher = new DelegatingMatcher(Matchers.anything) {
      public String toString() {
        return format("any(%s)", type.getName());
      }
    };
    return anyImpl(printableMatcher, type);
  }

  public Object any(final Class<?> type, Object matcher) {
    check(type != null);
    check(matcher != null);
    check(isMatcher(matcher));
    final Matcher asMatcher = asMatcher(matcher);
    DelegatingMatcher printableMatcher = new DelegatingMatcher(asMatcher) {
      public String toString() {
        return format("any(%s, %s)", type.getName(), asMatcher);
      }
    };
    return anyImpl(printableMatcher, type);
  }

  public Object anyInstanceOf(final Class<?> type) {
    check(type != null);
    Matcher matcher = new Matcher() {
      public boolean matches(Object item) {
        return type.isInstance(item);
      }

      public String toString() {
        return format("anyInstanceOf(%s)", type.getName());
      }
    };
    return anyImpl(matcher, type);
  }

  public Object a(final Object value) {
    check(value != null);
    DelegatingMatcher printableMatcher = new DelegatingMatcher(equalDeep(value)) {
      public String toString() {
        return format("a(%s)", formatter.format(value));
      }
    };
    return anyImpl(printableMatcher, value.getClass());
  }

  public Object the(final Object value) {
    check(value != null);
    DelegatingMatcher printableMatcher = new DelegatingMatcher(same(value)) {
      public String toString() {
        return format("the(%s)", formatter.format(value));
      }
    };
    return anyImpl(printableMatcher, value.getClass());
  }

  private Object anyImpl(Matcher matcher, Class<?> type) {
    Object token = tokenizer.token(type);
    history.add(wildcard(matcher, token));
    return token;
  }

  public InvocationMatcher matcherize(Invocation invocation) {
    check(invocation != null);
    return matcherizer.matcherize(invocation);
  }
}
