/*
 * Copyright 2002-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.test.context.junit4.statements;

import java.util.concurrent.TimeoutException;

import org.junit.runners.model.Statement;

import org.springframework.test.annotation.Timed;

/**
 * {@code SpringFailOnTimeout} is a custom JUnit {@link Statement} which adds
 * support for Spring's {@link Timed @Timed} annotation by throwing an exception
 * if the next statement in the execution chain takes more than the specified
 * number of milliseconds.
 *
 * <p>In contrast to JUnit's
 * {@link org.junit.internal.runners.statements.FailOnTimeout FailOnTimeout},
 * the next {@code statement} will be executed in the same thread as the
 * caller and will therefore not be aborted preemptively.
 *
 * @see #evaluate()
 * @author Sam Brannen
 * @since 3.0
 */
public class SpringFailOnTimeout extends Statement {

	private final Statement next;

	private final long timeout;


	/**
	 * Construct a new {@code SpringFailOnTimeout} statement.
	 *
	 * @param next the next {@code Statement} in the execution chain
	 * @param timeout the configured {@code timeout} for the current test
	 * @see Timed#millis()
	 */
	public SpringFailOnTimeout(Statement next, long timeout) {
		this.next = next;
		this.timeout = timeout;
	}

	/**
	 * Evaluate the next {@link Statement statement} in the execution chain
	 * (typically an instance of {@link SpringRepeat}) and throw a
	 * {@link TimeoutException} if the next {@code statement} executes longer
	 * than the specified {@code timeout}.
	 */
	@Override
	public void evaluate() throws Throwable {
		long startTime = System.currentTimeMillis();
		try {
			this.next.evaluate();
		}
		finally {
			long elapsed = System.currentTimeMillis() - startTime;
			if (elapsed > this.timeout) {
				throw new TimeoutException(String.format("Test took %s ms; limit was %s ms.", elapsed, this.timeout));
			}
		}
	}

}
