/*
 * Copyright 2014-2022 Rudy De Busscher (https://www.atbash.be)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package be.atbash.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.util.Date;

class ConvertDateTest {

    @BeforeAll
    public static void setupTest() {
        System.setProperty("atbash.utils.locales", "nl-BE, en-US");
    }

    @ParameterizedTest()
    @ArgumentsSource(ConvertDateArgumentProvider.class)
    void convertToDate(ConvertDateArgument argument) {
        try {
            LocalDateTime result = convertToLocalDateTime(ConvertDate.convertToDate(argument.getTestValue()));
            Assertions.assertThat(argument.isErrorExpected()).isFalse();
            testResult(result, argument);
        } catch (Throwable e) {
            if (e instanceof AssertionError) {
                throw e;
            }
            e.printStackTrace();  // So that we can see the exception.
            Assertions.assertThat(argument.isErrorExpected()).isTrue();
            Assertions.assertThat(e).isInstanceOf(argument.getErrorClass());
            Assertions.assertThat(e.getMessage()).isEqualTo(argument.getMessage());
        }
    }

    private void testResult(LocalDateTime result, ConvertDateArgument argument) {
        Assertions.assertThat(result.get(ChronoField.YEAR)).isEqualTo(argument.getYear());
        Assertions.assertThat(result.get(ChronoField.MONTH_OF_YEAR)).isEqualTo(argument.getMonth());
        Assertions.assertThat(result.get(ChronoField.DAY_OF_MONTH)).isEqualTo(argument.getDay());

        Assertions.assertThat(result.get(ChronoField.HOUR_OF_DAY)).isEqualTo(argument.getHours());
        Assertions.assertThat(result.get(ChronoField.MINUTE_OF_HOUR)).isEqualTo(argument.getMinutes());
        Assertions.assertThat(result.get(ChronoField.SECOND_OF_MINUTE)).isEqualTo(argument.getSeconds());

    }

    private static LocalDateTime convertToLocalDateTime(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
}