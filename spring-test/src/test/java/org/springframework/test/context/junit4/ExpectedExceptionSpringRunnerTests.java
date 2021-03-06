/*
 * Copyright 2002-2013 the original author or authors.
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

package org.springframework.test.context.junit4;

import java.util.ArrayList;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.JUnit4;

import org.springframework.test.context.TestExecutionListeners;

import static org.junit.Assert.*;

/**
 * Verifies proper handling of the following in conjunction with the
 * {@link SpringJUnit4ClassRunner}:
 * <ul>
 * <li>JUnit's {@link Test#expected() &#064;Test(expected=...)}</li>
 * </ul>
 *
 * @author Sam Brannen
 * @since 3.0
 */
@RunWith(JUnit4.class)
public class ExpectedExceptionSpringRunnerTests {

	@Test
	public void expectedExceptions() throws Exception {
		Class<?> testClass = ExpectedExceptionSpringRunnerTestCase.class;
		TrackingRunListener listener = new TrackingRunListener();
		RunNotifier notifier = new RunNotifier();
		notifier.addListener(listener);

		new SpringJUnit4ClassRunner(testClass).run(notifier);
		assertEquals("failures for test class [" + testClass + "].", 0, listener.getTestFailureCount());
		assertEquals("tests started for test class [" + testClass + "].", 1, listener.getTestStartedCount());
		assertEquals("tests finished for test class [" + testClass + "].", 1, listener.getTestFinishedCount());
	}


	@Ignore("TestCase classes are run manually by the enclosing test class")
	@TestExecutionListeners({})
	public static final class ExpectedExceptionSpringRunnerTestCase {

		// Should Pass.
		@Test(expected = IndexOutOfBoundsException.class)
		public void verifyJUnitExpectedException() {
			new ArrayList<Object>().get(1);
		}

	}

}
