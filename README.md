# missing.test.assertions

[![Clojars Project](https://img.shields.io/clojars/v/borkdude/missing.test.assertions.svg)](https://clojars.org/borkdude/missing.test.assertions)
[![cljdoc badge](https://cljdoc.org/badge/borkdude/missing.test.assertions)](https://cljdoc.org/d/borkdude/missing.test.assertions/CURRENT)

Library for checking absence of assertions in
[clojure.test](https://clojure.github.io/clojure/clojure.test-api.html) tests.

## Usage

Require `missing.test.assertions` in your test namespace(s).

Note that in `a-test` no assertions are made, but `another-test` has one
assertion. So we expect a warning only about `a-test`.

``` clojure
(ns foo.core-test
  (:require [clojure.test :refer [deftest testing is]]
            [missing.test.assertions]))

(deftest a-test
  (testing "..."
    1))

(deftest another-test
  (testing (is 1)))
```

When executing the tests, the following warning will get printed to `*err*`:

``` clojure
WARNING: no assertions made in test a-test
```

## Notes

- This library works with Clojure and ClojureScript.
- When registering your own `clojure.test/report` `:begin-test-var` and
  `:end-test-var` hooks, call `missing.test.assertions/register!` afterwards. This will
  redefine the `missing.test.assertions` hooks while your hooks keep working.
- The default behavior in absence of test assertions is printing a message. If you want an exception, call
  `(missing.test.assertions/register! {:throw? true})`.

## Test

### JVM

    lein test

or

    clj -A:test

### Node

    script/test/node

## Related projects

- [kaocha](https://github.com/lambdaisland/kaocha) has this as a built-in feature

## License

Copyright © 2019 Michiel Borkent

Distributed under the EPL License, same as Clojure. See LICENSE.
