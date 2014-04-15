package org.testory.util.any;

import static java.util.Arrays.asList;
import static java.util.Collections.nCopies;
import static org.testory.common.Checks.checkArgument;
import static org.testory.common.Collections.flip;
import static org.testory.common.Collections.last;
import static org.testory.common.Objects.areEqualDeep;
import static org.testory.util.any.Anyvocation.anyvocation;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.testory.common.Nullable;

public class Repairs {
  public static Anyvocation repair(Anyvocation anyvocation) {
    boolean isVarargs = isVarargs(anyvocation);
    List<Class<?>> parameters = asList(anyvocation.method.getParameterTypes());
    List<Object> unfolded = isVarargs
        ? unfoldArguments(anyvocation.arguments)
        : anyvocation.arguments;
    List<Object> repairedUnfolded = repair(unfolded, anyvocation);
    List<Object> repaired = isVarargs
        ? foldArguments(last(parameters), parameters.size(), repairedUnfolded)
        : repairedUnfolded;
    return anyvocation(anyvocation.method, anyvocation.instance, repaired, anyvocation.anys);
  }

  private static boolean isVarargs(Anyvocation anyvocation) {
    return anyvocation.method.isVarArgs()
        && !mustBe(last(anyvocation.arguments), last(anyvocation.anys));
  }

  private static List<Object> repair(List<Object> arguments, Anyvocation anyvocation) {
    List<Boolean> solution = trySolveEager(anyvocation.anys, arguments);
    checkArgument(solution != null);
    checkArgument(areEqualDeep(flip(solution),
        trySolveEager(flip(anyvocation.anys), flip(arguments))));

    List<Any> anysQueue = new ArrayList<Any>(anyvocation.anys);
    List<Object> repaired = new ArrayList<Object>();
    for (int i = 0; i < solution.size(); i++) {
      repaired.add(solution.get(i)
          ? anysQueue.remove(0).token
          : arguments.get(i));
    }
    return repaired;
  }

  @Nullable
  private static List<Boolean> trySolveEager(List<Any> anys, List<Object> arguments) {
    List<Boolean> solution = new ArrayList<Boolean>(nCopies(arguments.size(), false));
    int nextIndex = 0;
    nextAny: for (Any any : anys) {
      for (int i = nextIndex; i < arguments.size(); i++) {
        if (mustBe(arguments.get(i), any)) {
          solution.set(i, true);
          nextIndex = i + 1;
          continue nextAny;
        }
      }
      for (int i = nextIndex; i < arguments.size(); i++) {
        if (couldBe(arguments.get(i), any)) {
          solution.set(i, true);
          nextIndex = i + 1;
          continue nextAny;
        }
      }
      return null;
    }
    return solution;
  }

  private static List<Object> unfoldArguments(List<?> packed) {
    ArrayList<Object> unpacked = new ArrayList<Object>();
    unpacked.addAll(packed.subList(0, packed.size() - 1));
    unpacked.addAll(asList((Object[]) last(packed)));
    return unpacked;
  }

  private static List<Object> foldArguments(Class<?> arrayType, int length, List<Object> arguments) {
    List<Object> packed = new ArrayList<Object>();
    packed.addAll(arguments.subList(0, length - 1));
    packed.add(asArray(arrayType, arguments.subList(length - 1, arguments.size())));
    return packed;
  }

  private static Object asArray(Class<?> arrayType, List<Object> elements) {
    Object array = Array.newInstance(arrayType.getComponentType(), 0);
    return elements.toArray((Object[]) array);
  }

  private static boolean mustBe(Object argument, Any any) {
    return any.token == argument;
  }

  private static boolean couldBe(Object argument, Any any) {
    return true;
  }
}
